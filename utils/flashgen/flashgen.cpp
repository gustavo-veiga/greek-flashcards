#include <iostream>
#include <swmgr.h>
#include <swmodule.h>
#include <versekey.h>
#include <listkey.h>
#include <map>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>



using namespace sword;
using std::map;

void usage(const char *progName) {
	std::cerr << "usage: " << progName << " <modName> <rangeKey> <maxWordsPerLesson>\n";
}



void encode(SWBuf &text) {
	const unsigned char *from;
	char digit[10];
	unsigned long ch;
        signed short utf16;
	unsigned char from2[7];

	SWBuf orig = text;

	from = (const unsigned char *)orig.c_str();

	// -------------------------------
	for (text = ""; *from; from++) {
		ch = 0;
                //case: ANSI
		if ((*from & 128) != 128) {
			text += *from;
			continue;
		}
                //case: Invalid UTF-8 (illegal continuing byte in initial position)
		if ((*from & 128) && ((*from & 64) != 64)) {
			continue;
		}
                //case: 2+ byte codepoint
		from2[0] = *from;
		from2[0] <<= 1;
		int subsequent;
		for (subsequent = 1; (from2[0] & 128) && (subsequent < 7); subsequent++) {
			from2[0] <<= 1;
			from2[subsequent] = from[subsequent];
			from2[subsequent] &= 63;
			ch <<= 6;
			ch |= from2[subsequent];
		}
		subsequent--;
		from2[0] <<= 1;
		char significantFirstBits = 8 - (2+subsequent);
		
		ch |= (((short)from2[0]) << (((6*subsequent)+significantFirstBits)-8));
		from += subsequent;
                if (ch < 0x10000) {
				utf16 = (signed short)ch;
				text += '\\';
				text += 'u';
				sprintf(digit, "%.4x", utf16);
				for (char *c = digit; *c; c++) *c=toupper(*c);
				text += digit;
			 }
			else {
				utf16 = (signed short)((ch - 0x10000) / 0x400 + 0xD800);
				text += '\\';
				text += 'u';
				sprintf(digit, "%.4x", utf16);
				for (char *c = digit; *c; c++) *c=toupper(*c);
				text += digit;
				utf16 = (signed short)((ch - 0x10000) % 0x400 + 0xDC00);
				text += '\\';
				text += 'u';
				sprintf(digit, "%.4x", utf16);
				for (char *c = digit; *c; c++) *c=toupper(*c);
				text += digit;
			}
	}
}


SWBuf getGreekWord(const char *buf) {
	char *s = strstr(buf, "<hi type=\"b\">");
	SWBuf retVal = "";
	if (s) {
		s = strstr(s+1, "<hi type=\"b\">");
		if (s) {
			s += 13;
			char *e = strstr(s, "</hi>");
			if (e) {
				retVal.appendFormatted("%.*s", (int)((long)e-(long)s), s);
				encode(retVal);
			}
		}
	}
	return retVal;
}


SWBuf getGreekDef(const char *buf) {
	const char *s = buf + strlen(buf);
	while (--s > buf) {
		if (*s == '>') {
			s++;
			break;
		}
	}
	return s;
}

class Word;
class Word {
public:
	int freq;
	SWBuf strong;
	SWBuf greek;
	std::map<SWBuf, Word> defs;
	Word() { freq = 0; strong = ""; greek = ""; }
};

typedef std::map<SWBuf, Word> WordMap;
typedef std::multimap<int, Word> FreqWordMap;

int main(int argc, char **argv) {
	if (argc < 3) {
		usage(*argv);
		return -1;
	}
	const char *modName = argv[1];
	const char *rangeText = argv[2];
	int maxWords = atoi(argv[3]);

	SWMgr mgr;
	SWModule *module = mgr.getModule(modName);
	SWModule *lex = mgr.getModule("NasbGreek");
	if (!module) {
		std::cerr << "Could not find module [" << modName << "].  Available modules:\n";
		for (ModMap::iterator it = mgr.Modules.begin(); it != mgr.Modules.end(); it++) {
			std::cerr << "[" << (*it).second->Name() << "]\t - " << (*it).second->Description() << "\n";
		}
		return -2;
	}
	if (!module) {
		std::cerr << "Could not find lex module [NasbGreek].\n";
		return -3;
	}
	VerseKey vkey;
	ListKey rangeKey = vkey.ParseVerseList(rangeText, vkey, true);
	rangeKey.Persist(true);
	module->setKey(&rangeKey);


	WordMap words;

	for ((*module) = TOP; !module->Error(); (*module)++) {
		SWBuf text = (const char *)(*module);
//		std::cout << (const char *)text << "\n";
//		encode(text);
//		std::cout << (const char *)text << "\n";
		AttributeTypeList::iterator i1 = module->getEntryAttributes().find("Word");
		AttributeList::iterator i2;
		AttributeValue::iterator i3;
		if (i1 != module->getEntryAttributes().end()) {
//			std::cout << "[ " << i1->first << " ]\n";
			for (i2 = i1->second.begin(); i2 != i1->second.end(); i2++) {
//				std::cout << "\t[ " << i2->first << " ]\n";
				i3 = i2->second.find("Strongs");
				if (i3 != i2->second.end()) {
					SWBuf strong = i3->second;
					if (strchr("GH", strong[0]))
						strong << 1;
					words[strong].freq += 1;
					words[strong].strong = strong;
					i3 = i2->second.find("Text");
					if (i3 != i2->second.end()) {
						SWBuf text = i3->second;
						words[strong].defs[text].freq += 1;
						// store text in strong place; silly, but works
						words[strong].defs[text].strong = text;
					}
				}
			}
		}
	}

	FreqWordMap freqWords;
	for (WordMap::iterator it = words.begin(); it != words.end(); it++) {
		Word &word = it->second;
		freqWords.insert(std::make_pair(word.freq, word));
	}
	int freqHi = 0;
	int i = 0;
	int fd = 0;
	int f = 0;
	Word word;
	for (FreqWordMap::reverse_iterator it = freqWords.rbegin(); it != freqWords.rend(); it++) {
		word = it->second;
		if (!i) {
			SWBuf fileName;
			fileName.appendFormatted("set%d.flash", f++);
			fd = open(fileName.c_str(), O_CREAT|O_RDWR, S_IRWXU|S_IRWXG|S_IRWXO);
			freqHi = word.freq;
		}
		lex->setKey(word.strong.c_str());
		SWBuf out;
		out.appendFormatted("word%d=%s\n", i, getGreekWord(lex->getRawEntryBuf()).c_str());
		out.appendFormatted("freq%d=%d\n", i, word.freq);
		out.appendFormatted("answers%d=", i++);
		FreqWordMap freqDefs;
		for (WordMap::iterator it2 = word.defs.begin(); it2 != word.defs.end(); it2++) {
			Word &word = it2->second;
			freqDefs.insert(std::make_pair(word.freq, word));
		}
		for (FreqWordMap::reverse_iterator it2 = freqDefs.rbegin(); it2 != freqDefs.rend(); it2++) {
			Word &def = it2->second;
			out.appendFormatted("%s (%d); ", def.strong.c_str(), def.freq);
		}
		out += "\n";
		write(fd, out.c_str(), out.length());
		if (i >= maxWords) {
			out = "wordCount=";
			out.appendFormatted("%d\nlessonTitle=Set %d freq(%d-%d)\n", i, f, freqHi, word.freq);
			write(fd, out.c_str(), out.length());
			close(fd);
			i = 0;
		}
	}
	if (i) {
		SWBuf out = "wordCount=";
		out.appendFormatted("%d\nlessonTitle=Set %d freq(%d-%d)\n", i, f, freqHi, word.freq);
		write(fd, out.c_str(), out.length());
		close(fd);
		i = 0;
	}
	close(fd);

	return 0;
}
