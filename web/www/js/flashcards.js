
var FlashCard = {
	front : null,
	back : null,
	getFront : function() { return this.front; },
	getBack : function() { return this.back; },
	setBack : function(val) { this.back = val; },
};

var Quizer = {
	WordEntry : {
		flashCard : null,
		attempts : 0,

		incrementFailures : function(failures) {
			this.attempts += failures;
		},

		getFailures : function() {
			return this.attempts;
		},

		getFlashCard() {
			return this.flashCard;
		}
	},

	words : [],
	notLearned : [],
	lastWord : null,
	wrong : 0,
	totalAsked : 0,
	totalWrong : 0,

	clear : function() {
		var self = this;
		self.words = [];
		self.reset();
	},
	reset : function() {
		var self = this;
		self.totalAsked = 0;
		self.totalWrong = 0;
		self.notLearned = [];
		for (var i = 0; i < self.words.length; ++i) {
			self.notLearned.push(
				$.extend(true, {}, self.WordEntry, { flashCard : self.words[i].getFlashCard() })
			);
		}
	},

	loadLesson : function(lesson) {
		var self = this;
console.log('loading lesson: ' + lesson.getDescription())
		var cards = lesson.getFlashCards();
		for (var i = 0; i < cards.length; ++i) {
			self.words.push(
				$.extend(true, {}, self.WordEntry, { flashCard : cards[i] })
			);
		}

		// lets combine duplicate words
		for (var i = 0; i < self.words.length - 1; ++i) {
			var w = self.words[i];
			for (var j = i + 1; j < self.words.length; ++j) {
				var xx = self.words[j];
				if (w.flashCard.getFront() === xx.flashCard.getFront()) {
					w.flashCard.setBack(w.flashCard.getBack() + ' or ' + xx.flashCard.getBack());
					self.words.splice(j, 1);
					j--;
				}
			}
		}
		self.reset();
	},
	// wrongCount: 0 = correct; > 0 = # wrong choices; < 0 = ignore
	getRandomWord : function(wrongCount) {
		var self = this;

		if (self.lastWord != null && wrongCount > -1) {
			self.totalAsked++;
			if (wrongCount > 0) {
				self.totalWrong += wrongCount;
				self.lastWord.incrementFailures(wrongCount);
			}
			else {
				self.lastWord.incrementFailures(-1);
			}
			// if we're the last word, don't ask again
			if (self.notLearned.length == 1 || self.lastWord.getFailures() < 0) {
				var iLastWord = self.notLearned.indexOf(self.lastWord);
				if (iLastWord > -1) self.notLearned.splice(iLastWord, 1);
			}
		}
		var numToLearn = self.notLearned.length;
		if (numToLearn == 0) {
			return null;
		}

		var currentWord = null;

		// if there are more than 1 words available be sure we don't get the same word
		if (numToLearn != 1) {
			currentWord = self.lastWord;

			// if we just want a new word and not report anything, find the NEXT word
			// because we're likely cycling through the words looking at answers and don't
			// want random answers which might include repeats
			if (wrongCount < 0 && currentWord != null) {
				var next = self.notLearned.indexOf(self.lastWord) + 1;
				if (next >= self.notLearned.length) {
					next = 0;
				}
				currentWord = self.notLearned[next];
			}
  
			// if we need to randomly find a new word, let's do it
			var attempt = 0;
			while (currentWord == self.lastWord) {
				var wordNum = self.getRandomInt(self.notLearned.length);
				currentWord = self.notLearned[wordNum];
				if (++attempt > 999) { console.log('INFINITE LOOP FORCE BREAK!!!!!'); break; }
				if (self.notLearned.length == 1) break;	// if we're the last word to learn
			}
		}
		else {
			currentWord = self.notLearned[0];
		}

		self.lastWord = currentWord;
		return currentWord.getFlashCard();
	},
	getRandomAnswers : function(count) {
		var self = this;
		var retVal = [];
		if (count > self.words.length) {
			count = self.words.length;
		}

		var attempt = 0;
		while (count > 0) {
			var wordNum = self.getRandomInt(self.words.length);
			var b = self.words[wordNum].flashCard.getBack();
			if (retVal.indexOf(b) < 0) {
				retVal.push(b);
				count--;
			}
			if (++attempt > 999) { console.log('INFINITE LOOP FORCE BREAK!!!!!'); break; }
		}
		if (self.lastWord) {
			// be sure the right answer is in there
			if (retVal.indexOf(self.lastWord.flashCard.getBack()) < 0) {
				var wordNum = self.getRandomInt(retVal.length);
				retVal.splice(wordNum, 1, self.lastWord.flashCard.getBack());
			}
		}
		return retVal;
	},
	getNotLearnedCount : function() {
		return this.notLearned.length;
	},
	getTotalAsked : function() {
		return this.totalAsked;
	},
	getTotalWrong : function() {
		return this.totalWrong;
	},
	getPercentage : function() {
		var self = this;
		var percent = 100;
		if (self.totalAsked > 0) {
			percent = ( (self.totalAsked - self.totalWrong) * 100) / self.totalAsked;
		}
		return percent;
	},
	getRandomInt : function(upperLimitExclusive) {
		return Math.floor(Math.random() * (upperLimitExclusive - 1));
	},
};

var LessonManager = {
	flashData : null,
	lessonLoader : function(resourceName, callback) {
		mgr.flashData.files[resourceName].async("string").then(function(d) {
			if (callback) return callback(d);
		});
	},
	LessonSet : {
		Lesson : {
			resourceName : null,
			flashCards : [],
			font : null,
			audioPath : null,
			getDescription : function() {
				var des = messageResource.get('lessonTitle', this.resourceName);
				if (!des) des = this.resourceName.substring(this.resourceName.lastIndexOf('/') + 1);
				return des;
			},
			getFlashCards : function() {
				return this.flashCards;
			},
			loadFlashCards : function() {
				var self = this;
				var wordCount = parseInt(messageResource.get('wordCount', self.resourceName));

				var fontName = messageResource.get('lessonFont', self.resourceName);
				if (fontName != null && fontName.length > 0 && fontName != 'auto') {
					self.font = fontName;
				}

				var baseOffset = self.resourceName.lastIndexOf('/');
				if (baseOffset < 0) {
					baseOffset = self.resourceName.lastIndexOf('\\');
				}
				var lname = self.resourceName.substring(baseOffset + 1);
				lname = lname.substring(0, lname.indexOf('.flash'));
				var audioPath = self.resourceName.substring(0, baseOffset) + "/audio";

				for (var i = 0; i < wordCount; ++i) {
					var front = messageResource.get('word' + i, self.resourceName);
					var back  = messageResource.get('answers' + i, self.resourceName);
					var f = $.extend(true, {}, FlashCard, { front : front, back : back });
					var audioPath = audioPath + '/' + lname + "_" + i + ".wav";
					if (mgr.flashData.audioPath) self.audioPath = audioPath;
					self.flashCards.push(f);
				}
			},
		},
		dataPathBase : null,
		name : null,
		lessons : null,
		lessonsMap : null,
		loadLessons : function(llCallback) {
			var self = this;
			self.lessons = [];
			var lessonFiles = [];
			$.each(mgr.flashData.files, function(entryName, entryData) {
				if (entryName.startsWith(self.dataPathBase) && entryName.endsWith('.flash')) {
					lessonFiles.push(entryName);
				}
			});

			var loadLesson = function(i) {
				if (!i) i = 0;
				if (i >= lessonFiles.length) {
					self.lessonsMap = {};
					$(self.lessons).each(function() { self.lessonsMap[this.getDescription()] = this });
					return llCallback ? llCallback() : null;
				}
				var entryName = lessonFiles[i];
				var lessonName = entryName.substring(self.dataPathBase.length, entryName.length - '.flash'.length);
				var resourceName = entryName.substring(0, entryName.length - '.flash'.length);
				messageResource.load(resourceName, function() {
					var l = $.extend(true, {}, self.Lesson, { resourceName : resourceName });
					l.loadFlashCards();
					self.lessons.push(l);
					return loadLesson(++i);
				});
			};
			loadLesson();
		},
		getDescription : function() { return this.name; },
		getLessons : function(callback) { var self = this; if (self.lessons) return callback(self.lessons); self.loadLessons(function() { return callback(self.lessons); }); },
		getLesson : function(lessonName) {
			return this.lessonsMap[lessonName];
		},
	},
	lessonSets : [],
	lessonSetsMap : {},
	loadLessonSets : function(lessonDir, callback) {
		var promise = new JSZip.external.Promise(function (resolve, reject) {
			JSZipUtils.getBinaryContent(lessonDir + 'lessons.jar', function(err, data) {
				if (err) {
					reject(err);
				} else {
					resolve(data);
				}
			});
		});

		promise
			.then(JSZip.loadAsync)                     // 2) chain with the zip promise
			.then(function(zip) {
				mgr.flashData = zip;
				messageResource.init({
					fileExtension : '.flash',
					ajaxFunction : mgr.lessonLoader,
					filePath : '',
				});
				mgr.lessonSets = [];
				$.each(mgr.flashData.files, function(entryName, entryData) {
					if (entryName.startsWith('lessons/')) {
						var lessonName = entryName.substring(8).trim();
						if (lessonName.length) {
							if (lessonName.endsWith('/')) lessonName = lessonName.substring(0, lessonName.length - 1);
							if (lessonName.split('/').length == 1) {
								mgr.lessonSets.push($.extend(true, {}, mgr.LessonSet, { name : lessonName, dataPathBase : entryName }));
							}
						}
					}
				});
				mgr.lessonSetsMap = {};
				$(mgr.lessonSets).each(function() { mgr.lessonSetsMap[this.getDescription()] = this });
				if (callback) return callback();
			});

	},
	getLessonSets : function() {
		return mgr.lessonSets;
	},
	getLessonSet : function(lessonName) {
		return mgr.lessonSetsMap[lessonName];
	}
};
