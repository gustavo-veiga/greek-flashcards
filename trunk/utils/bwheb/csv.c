#include <stdio.h>
#include <string.h>

void ProcessFile(char * pszIn, char * pszOut);
int ParseText(char * pszInput, char * pszOutput);
int ConvertBwHebb1(unsigned char ch, unsigned int *puiChar, unsigned int *puiAccent, unsigned int *puiAccent2);

int main(int argc, char *argv[])
{	
	char * pszInputPath, * pszOutputPath, * psz;
	char szUseage[] = "-i=[Path to input file]\n-o=[Path to output file]\n";	
	int nCnt;

	// Useage
	nCnt = 0;

	while (nCnt < argc)
	{
		psz = argv[nCnt];

		if (strstr(argv[nCnt], "?") == psz)
		{
			puts(szUseage);
			return 0;
		}

		nCnt ++;
	}

	// Input
	nCnt = 0;
	pszInputPath = NULL;

	while (nCnt < argc)
	{
		psz = argv[nCnt];

		if (strstr(argv[nCnt], "-i=") == psz)
		{
			pszInputPath = psz + 3;
			break;
		}

		nCnt ++;
	}

	if (!pszInputPath)
	{
		puts(szUseage);
		return 0;
	}

	// Output	

	nCnt = 0;
	pszOutputPath = NULL;

	while (nCnt < argc)
	{
		psz = argv[nCnt];

		if (strstr(argv[nCnt], "-o=") == psz)
		{
			pszOutputPath = psz + 3;
			break;
		}

		nCnt ++;
	}

	if (!pszOutputPath)
	{
		puts(szUseage);
		return 0;
	}

	ProcessFile(pszInputPath, pszOutputPath);
	return 0;
}

void ProcessFile(char * pszIn, char * pszOut)
{

	FILE * fsIn, * fsOut;
	fsIn = fopen(pszIn, "r");
	int nLen;
	char * pszCell;
	char szBuf[2048], szEncodeBuf[12288];

	if (!fsIn)
	{
		printf("Unable to read %s", pszIn);
		return;
	}

	fsOut = fopen(pszOut, "w+");
	
	fputs("lessonTitle=\n", fsOut);

	if (!fsOut)
	{
		printf("Unable to create/open %s", pszOut);
		fclose(fsIn);
		return;
	}
	
	int num = 0;
	char buf[255];
	while (fgets(szBuf, 2048, fsIn))
	{
	
		// Ignore any lines that do not begin with '"'
		// in case comments need to be stored in file.
		if (szBuf[0] != '\"')
			continue;

		for (nLen = strlen(szBuf); nLen && strchr(" \n\r\"", szBuf[nLen]); nLen--) {
			szBuf[nLen] = 0;
		}

/*
		// Clean the end of the string.
		{
			nLen --;

			if (szBuf[nLen] == '\n' || szBuf[nLen] == ' ' ||
				szBuf[nLen] == '\r' || szBuf[nLen] == '\"')
				szBuf[nLen] = '\0';
			else
				break;
		}
*/

		fprintf(stderr, "Parsing: %s\n", szBuf);
		// Find where text from second cell begins.
		pszCell = strstr(szBuf, "\",\"");

		if (!pszCell)
			continue;

		// Terminate text from first cell.
		pszCell[0] = '\0';

		// Skip past '"' at beginning of string.
		ParseText(&szBuf[1], szEncodeBuf);
		
		sprintf(buf, "word%d=", num);
		fputs(buf, fsOut);
		fputs(szEncodeBuf, fsOut);
		sprintf(buf, "\nanswers%d=", num);
		fputs(buf, fsOut);
		fputs(pszCell + 3, fsOut);
		fputs("\n", fsOut);
		num++;
	}

	sprintf(buf, "wordCount=%d\n", num);
	fputs(buf, fsOut);

	fclose(fsIn);
	fclose(fsOut);
}

int ParseText(char * pszInput, char * pszOutput)
{
	// Input strings should be less than 2k.

	char ch;
	unsigned int uiA, uiB, uiC;
	int nPosDest = 0;
	int nChars;
	int nCodes = strlen(pszInput);
	int nCPos = 0;
	int nCCnt;
	int nType[2048];

	// Map character type 'simple, or combining'.
	for(;;)
	{
		uiA = 0;
		uiB = 0;
		uiC = 0;

		if (nCPos >= nCodes || nCPos >= 2048)
			break;

		ch = pszInput[nCPos];
		
		if (!ConvertBwHebb1((unsigned char)ch, &uiA, &uiB, &uiC))
		{
			// Character not handled.
			nType[nCPos] = 1;
			nCPos ++;
			continue;
		}

		if (uiA != 0)
		{
			nType[nCPos] = 1;
		}
		else
			nType[nCPos] = 0;
		
		nCPos ++;
	}
	
	nCCnt = nCodes;

	// Convert characters, reversing the string.
	for(;;)
	{
		uiA= 0;
		uiB= 0;
		uiC= 0;

		nCCnt --;

		if (nCCnt < 0)
			break;

		// Step backwards until we have found a non-combining character.
		if (nType[nCCnt] != 1)
			continue;
		
	
		// Convert, and place this character in the buffer.
		nChars = ConvertBwHebb1((unsigned char)pszInput[nCCnt], &uiA, &uiB, &uiC);
		
		if (uiA != 0)
		{
			sprintf(&pszOutput[nPosDest], "\\u%.4X", uiA);
			nPosDest += 6;
		}

		if (uiB != 0)
		{
			sprintf(&pszOutput[nPosDest], "\\u%.4X", uiB);
			nPosDest += 6;
		}

		if (uiC != 0)
		{
			sprintf(&pszOutput[nPosDest], "\\u%.4X", uiC);
			nPosDest += 6;
		}

		// Step forwards inserting all combining characters that go with the primary character
		// from the previous step.
		nCPos = 1;

		for(;;)
		{
			uiA= 0;
			uiB= 0;
			uiC= 0;

			if (nCCnt + nCPos >= nCodes)
				break;

			if (nType[nCCnt + nCPos] == 1)
				break;

			
			nChars = ConvertBwHebb1(pszInput[nCCnt + nCPos], &uiA, &uiB, &uiC);
			
			if (uiA != 0)
			{
				sprintf(&pszOutput[nPosDest], "\\u%.4X", uiA);
				nPosDest += 6;
			}

			if (uiB != 0)
			{
				sprintf(&pszOutput[nPosDest], "\\u%.4X", uiB);
				nPosDest += 6;
			}

			if (uiC != 0)
			{
				sprintf(&pszOutput[nPosDest], "\\u%.4X", uiA);
				nPosDest += 6;
			}

			nCPos ++;
		}
	}

//	fprintf(stderr, "Output: %s\n", pszOutput);
	return 1;
}

int ConvertBwHebb1(unsigned char ch, unsigned int *puiChar, unsigned int *puiAccent, unsigned int *puiAccent2)
{
	// In case statements codes for combining characters should always be stored in
	// puiAccent, or puiAccent2, otherwise invalid reversal of the characters may occur
	// in ParseText.

	int nChars = 0;
	*puiAccent2 = 0;
	switch (ch)
	{
		// HEBREW LETTER FINAL NUN
	case 33 :
		*puiChar   = 0X05DF;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW POINT QAMATS
	case 34 :
		*puiChar   = 0X0000;
		*puiAccent = 0X05B8;
		nChars = 1;
		break;
		
		// HEBREW LETTER FINAL TSADI
	case 35 :
		*puiChar   = 0X05E5;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW LETTER FINAL KAF WITH HEBREW POINT SHEVA
	case 37 :
		*puiChar   = 0X05DA;
		*puiAccent = 0X05B0;
		nChars = 2;
		break;
		
		// HEBREW POINT QAMATS
	case 39 :
		*puiChar   = 0X0000;
		*puiAccent = 0X05B8;
		nChars = 1;
		break;
		
		// HEBREW ACCENT ETNAHTA
	case 43 :
		*puiChar   = 0X0000;
		*puiAccent = 0X0591;
		nChars = 1;
		break;
		
		// HEBREW POINT SEGOL
	case 44 :
		*puiChar   = 0X0000;
		*puiAccent = 0X05B6;
		nChars = 1;
		break;
		
		// HEBREW POINT SHEVA '-'
	case 45 :
		*puiChar   = 0X05BE;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW POINT SHEVA
	case 46 :
		*puiChar   = 0X0000;
		*puiAccent = 0X05B0;
		nChars = 1;
		break;
		
		// HEBREW POINT HATAF SEGOL
	case 47 :
		*puiChar   = 0X0000;
		*puiAccent = 0X05B1;
		nChars = 1;
		break;
		
		// HEBREW POINT PATAH
	case 58 :
		*puiChar   = 0X0000;
		*puiAccent = 0X05B7;
		nChars = 1;
		break;
		
		// HEBREW POINT PATAH
	case 59 :
		*puiChar   = 0X0000;
		*puiAccent = 0X05B7;
		nChars = 1;
		break;
		
		// HEBREW POINT SEGOL
	case 60 :
		*puiChar   = 0X0000;
		*puiAccent = 0X05B6;
		nChars = 1;
		break;
		
		// HEBREW POINT SHEVA
	case 62 :
		*puiChar   = 0X0000;
		*puiAccent = 0X05B0;
		nChars = 1;
		break;
		
		// HEBREW LETTER VAV WITH HOLAM
	case 65 :
		*puiChar   = 0X05D5;
		*puiAccent = 0X05B9;
		nChars = 2;
		break;
		
		// HEBREW LETTER BET WITH DAGESH
	case 66 :
		*puiChar   = 0X05D1;
		*puiAccent = 0X05BC;
		nChars = 2;
		break;
		
		// HEBREW LETTER DALET WITH DAGESH
	case 68 :
		*puiChar   = 0X05D3;
		*puiAccent = 0X05BC;
		nChars = 2;
		break;
		
		// HEBREW POINT TSERE
	case 69 :
		*puiChar   = 0X0000;
		*puiAccent = 0X05B5;
		nChars = 1;
		break;
		
		// HEBREW LETTER GIMEL WITH DAGESH
	case 71 :
		*puiChar   = 0X05D2;
		*puiAccent = 0X05BC;
		nChars = 2;
		break;
		
		// HEBREW LETTER HE WITH MAPIQ
	case 72 :
		*puiChar   = 0X05D4;
		*puiAccent = 0X05BC;
		nChars = 2;
		break;
		
		// HEBREW POINT HIRIQ
	case 73 :
		*puiChar   = 0X0000;
		*puiAccent = 0X05B4;
		nChars = 1;
		break;
		
		// HEBREW LETTER TET WITH DAGESH
	case 74 :
		*puiChar   = 0X05D8;
		*puiAccent = 0X05BC;
		nChars = 2;
		break;
		
		// HEBREW LETTER KAF WITH DAGESH
	case 75 :
		*puiChar   = 0X05DB;
		*puiAccent = 0X05BC;
		nChars = 2;
		break;
		
		// HEBREW LETTER LAMED WITH DAGESH
	case 76 :
		*puiChar   = 0X05DC;
		*puiAccent = 0X05BC;
		nChars = 2;
		break;
		
		// HEBREW LETTER MEM WITH DAGESH
	case 77 :
		*puiChar   = 0X05DE;
		*puiAccent = 0X05BC;
		nChars = 2;
		break;
		
		// HEBREW LETTER NUN WITH DAGESH
	case 78 :
		*puiChar   = 0X05E0;
		*puiAccent = 0X05BC;
		nChars = 2;
		break;
		
		// HEBREW POINT HOLAM
	case 79 :
		*puiChar   = 0X0000;
		*puiAccent = 0X05B9;
		nChars = 1;
		break;
		
		// HEBREW LETTER PE WITH DAGESH
	case 80 :
		*puiChar   = 0X05E4;
		*puiAccent = 0X05BC;
		nChars = 2;
		break;
		
		// HEBREW LETTER TAV WITH DAGESH
	case 84 :
		*puiChar   = 0X05EA;
		*puiAccent = 0X05BC;
		nChars = 2;
		break;
		
		// HEBREW LETTER SHIN WITH SHIN DOT AND DAGESH
	case 86 :
		*puiChar   = 0X05E9;
		*puiAccent = 0X05C1;
		*puiAccent2= 0X05BC;
		nChars = 3;
		break;
		
		// HEBREW LETTER VAV WITH DAGESH
	case 87 :
		*puiChar   = 0X05D5;
		*puiAccent = 0X05BC;
		nChars = 2;
		break;
		
		// HEBREW LETTER AYIN
	case 91 :
		*puiChar   = 0X05E2;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW POINT HATAF QAMATS
	case 92 :
		*puiChar   = 0X0000;
		*puiAccent = 0X05B3;
		nChars = 1;
		break;
		
		// HEBREW POINT HATAF PATAH
	case 93 :
		*puiChar   = 0X0000;
		*puiAccent = 0X05B2;
		nChars = 1;
		break;
		
		// HEBREW ACCENT MUNAH
	case 95 :
		*puiChar   = 0X0000;
		*puiAccent = 0X05A3;
		nChars = 1;
		break;
		
		// HEBREW LETTER ALEF
	case 97 :
		*puiChar   = 0X05D0;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW LETTER BET
	case 98 :
		*puiChar   = 0X05D1;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW LETTER TSADI
	case 99 :
		*puiChar   = 0X05E6;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW LETTER DALET
	case 100 :
		*puiChar   = 0X05D3;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW POINT TSERE
	case 101 :
		*puiChar   = 0X0000;
		*puiAccent = 0X05B5;
		nChars = 1;
		break;
		
		// HEBREW LETTER SHIN WITH SIN DOT
	case 102 :
		*puiChar   = 0X05E9;
		*puiAccent = 0X05C2;
		nChars = 2;
		break;
		
		// HEBREW LETTER GIMEL
	case 103 :
		*puiChar   = 0X05D2;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW LETTER HE
	case 104 :
		*puiChar   = 0X05D4;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW POINT HIRIQ
	case 105 :
		*puiChar   = 0X0000;
		*puiAccent = 0X05B4;
		nChars = 1;
		break;
		
		// HEBREW LETTER TET
	case 106 :
		*puiChar   = 0X05D8;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW LETTER KAF
	case 107 :
		*puiChar   = 0X05DB;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW LETTER LAMED
	case 108 :
		*puiChar   = 0X05DC;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW LETTER MEM
	case 109 :
		*puiChar   = 0X05DE;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW LETTER NUN
	case 110 :
		*puiChar   = 0X05E0;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW POINT HOLAM
	case 111 :
		*puiChar   = 0X0000;
		*puiAccent = 0X05B9;
		nChars = 1;
		break;
		
		// HEBREW LETTER PE
	case 112 :
		*puiChar   = 0X05E4;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW LETTER QOF
	case 113 :
		*puiChar   = 0X05E7;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW LETTER RESH
	case 114 :
		*puiChar   = 0X05E8;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		// HEBREW LETTER SAMEKH
	case 115 :
		*puiChar   = 0X05E1;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW LETTER TAV
	case 116 :
		*puiChar   = 0X05EA;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW POINT QIBBUS
	case 117 :
		*puiChar   = 0X0000;
		*puiAccent = 0X05BB;
		nChars = 1;
		break;
		
		// HEBREW LETTER SHIN WITH SHIN DOT
	case 118 :
		*puiChar   = 0X05E9;
		*puiAccent = 0X05C1;
		nChars = 2;
		break;
		
		// HEBREW LETTER VAV
	case 119 :
		*puiChar   = 0X05D5;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW LETTER HET
	case 120 :
		*puiChar   = 0X05D7;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW LETTER YOD
	case 121 :
		*puiChar   = 0X05D9;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
		
		// HEBREW LETTER ZAYIN
	case 122 :
		*puiChar   = 0X05D6;
		*puiAccent = 0X0000;
		nChars = 1;
		break;

		// HEBREW POINT HOLAM
	case 123 :
		*puiChar   = 0X0000;
		*puiAccent = 0X05B9;
		nChars = 1;
		break;
		
		// HEBREW LETTER FINAL MEM
	case 126 :
		*puiChar   = 0X05DD;
		*puiAccent = 0X0000;
		nChars = 1;
		break;
	}
	
	if (nChars < 1)
		fprintf(stderr, "Unhandled character - %u\n", ch);
	
	return nChars;
}
