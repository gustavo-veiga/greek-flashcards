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
 * Copyright: 2006 CrossWire Bible Society
 */

package org.crosswire.flashcards;

import java.util.Vector;

/**
 * A Lesson is an ordered list of FlashCards.
 * The lesson also has a description which is useful for showing to a user.
 *
 * @author Troy A. Griffitts [scribe at crosswire dot org]
 */
public class MicroLesson
    extends Lesson {

  public MicroLesson(String url) throws Exception {
    super(url);
  }

  public MicroLesson(String url, String description) throws Exception {
    super(url, description);
  }

  /**
   * Load this lesson from persistent store named by the lesson's <code>filename</code>.
   */
  public void load() throws Exception {
    Properties lesson = new Properties();
    lesson.load(getURL());
    int wordCount = Integer.parseInt(lesson.getProperty("wordCount"));
    setDescription(lesson.getProperty("lessonTitle",
                                      getURL().substring(getURL().
        lastIndexOf('/') + 1)));

    int baseOffset = getURL().lastIndexOf('/');
    if (baseOffset < 0) {
      baseOffset = getURL().lastIndexOf( ('\\'));
    }
    String lname = getURL().substring(baseOffset + 1);
    lname = lname.substring(0, lname.indexOf(".flash"));
    String audioPath = getURL().substring(0, baseOffset) + "/audio";
    String imagePath = getURL().substring(0, baseOffset) + "/images";

    for (int i = 0; i < wordCount; i++) {
      FlashCard f = new FlashCard(lesson.getProperty("word" + i),
                                  lesson.getProperty("answers" + i));
      String audioURLString = audioPath + "/" + lname + "_" +
          Integer.toString(i) + ".wav";
      String imageURLString = imagePath + "/" + lname + "_" +
          Integer.toString(i) + ".png";
      f.setAudioURL(audioURLString);
      f.setImageURL(imageURLString);
      add(f);
    }
    setModified(false);
  }
}
