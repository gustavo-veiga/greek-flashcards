/*
 * Distribution Licence:
 * FlashCard is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License,
 * version 2 as published by the Free Software Foundation.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU General Public License for more details.
 * The License is available on the internet at:
 *     http://www.gnu.org/copyleft/gpl.html,
 * or by writing to:
 *     Free Software Foundation, Inc.
 *     59 Temple Place - Suite 330
 *     Boston, MA 02111-1307, USA
 *
 *
 * Copyright: 2004 CrossWire Bible Society
 */
package org.crosswire.flashcards;

import java.util.Random;
import java.util.Vector;

/**
 * A panel that quizzes over a selection of lessons.
 *
 * @author Troy A. Griffitts [scribe at crosswire dot org]
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
public class Quizer {
  Random rand = new Random();
  Vector words = new Vector();
  Vector notLearned = new Vector();
  WordEntry lastWord = null;
  int wrong = 0;
  int totalAsked = 0;
  int totalWrong = 0;

  static class WordEntry {

    protected FlashCard flashCard;
    protected int attempts;

    public WordEntry(FlashCard flashCard) {
      this.flashCard = flashCard;
    }

    public void incrementFailures(int failures) {
      attempts += failures;
    }

    public int getFailures() {
      return attempts;
    }

    public FlashCard getFlashCard() {
      return flashCard;
    }

  }

  //Construct the frame
  public Quizer() {
    clear();
  }

  public void clear() {
    words = new Vector();
    reset();
  }

  public void loadLesson(Lesson lesson) {
    Vector cards = lesson.getFlashcards();
    for (int i = 0; i < cards.size(); i++) {
      FlashCard f = (FlashCard) cards.elementAt(i);
      words.addElement(new WordEntry(f));
    }
    // let's combine duplicate words
    for (int i = 0; i < words.size() - 1; i++) {
      WordEntry w = (WordEntry) words.elementAt(i);
      for (int j = i + 1; j < words.size(); j++) {
        WordEntry xx = (WordEntry) words.elementAt(j);
        if (w.flashCard.getFront().equals(xx.flashCard.getFront())) {
          w.flashCard.setBack(w.flashCard.getBack() + " or " +
                              xx.flashCard.getBack());
          words.removeElementAt(j);
          j--;
        }
      }
    }
    reset();
  }

  void reset() {
    totalAsked = 0;
    totalWrong = 0;
    words.trimToSize();
    notLearned = new Vector();
    for (int i = 0; i < words.size(); i++) {
      notLearned.addElement(new WordEntry( ( (WordEntry) words.elementAt(i)).
                                          getFlashCard()));
    }
  }

  // wrongCount: 0 = correct; > 0 = # wrong choices; < 0 = ignore
  public FlashCard getRandomWord(int wrongCount) {
    if ( (lastWord != null) && (wrongCount > -1)) {
      totalAsked++;
      if (wrongCount > 0) {
        totalWrong += wrongCount;
        lastWord.incrementFailures(wrongCount);
      }
      else {
        lastWord.incrementFailures(-1);
      }
      // if we're the last word, don't ask again
      if ((notLearned.size() == 1) || (lastWord.getFailures() < 0)) {
	notLearned.removeElement(lastWord);
	notLearned.trimToSize();
      }
    }


    int numToLearn = notLearned.size();
    if (numToLearn == 0) {
      return null;
    }

    WordEntry currentWord = null;

    // if there are more than 1 words available be sure we don't get the same word
    if (numToLearn != 1) {
      currentWord = lastWord;

      // if we just want a new word and not report anything, find the NEXT word
      // because we're likely cycling throw the words looking at answers and don't
      // want random answers which might include repeats
      if ( (wrongCount < 0) && (currentWord != null)) {
        int next = notLearned.indexOf(lastWord) + 1;
        if (next >= notLearned.size()) {
          next = 0;
        }
        currentWord = (WordEntry) notLearned.elementAt(next);
      }
  
      // if we need to randomly find a new word, let's do it
      while (currentWord == lastWord) {
        int wordNum = getRandomInt(notLearned.size());
        currentWord = (WordEntry) notLearned.elementAt(wordNum);
      }
    }
    else {
      currentWord = (WordEntry) notLearned.elementAt(0);
    }

    lastWord = currentWord;
    return currentWord.getFlashCard();
  }

  public int getPercentage() {
    int percent = 100;
    if (totalAsked > 0) {
      percent = ( (totalAsked - totalWrong) * 100) /
                          totalAsked;
    }
    return percent;
  }

  public Vector getRandomAnswers(int count) {
    Vector ret = new Vector();
    if (count > words.size()) {
      count = words.size();
    }

    while (count > 0) {
      int wordNum = getRandomInt(words.size());
      String b = ( (WordEntry) words.elementAt(wordNum)).flashCard.getBack();
      if (ret.indexOf(b) < 0) {
        ret.addElement(b);
        count--;
      }
    }
    // be sure the right answer is in there
    if (ret.indexOf(lastWord.flashCard.getBack()) < 0) {
      int wordNum = getRandomInt(ret.size());
      ret.setElementAt(lastWord.flashCard.getBack(), wordNum);
    }
System.out.println("Answer is offset: "+ret.indexOf(lastWord.flashCard.getBack()));
    return ret;
  }


  public int getTotalAsked() {
    return totalAsked;
  }

  public int getTotalWrong() {
    return totalWrong;
  }

  public int getNotLearnedCount() {
    return notLearned.size();
  }

  public int getRandomInt(int upperLimit) {
    int ret = rand.nextInt()%upperLimit;
    if (ret < 0) {
      ret *= -1;
    }
    return ret;
  }
}
