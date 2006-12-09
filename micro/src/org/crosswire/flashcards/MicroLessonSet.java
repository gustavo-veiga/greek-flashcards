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
 * Copyright: 2006 CrossWire Bible Society
 */
package org.crosswire.flashcards;


/**
 * A <code>LessonSet</code> is an ordered list of <code>Lesson</code>s.
 * The lessons are sorted by filename.
 * The lesson set also has a description which is useful for showing to a user
 * and a directory name where its Lessons are stored. This directory name is expected to be a relative
 * path and will be stored either in a jar or in the user's FlashCard directory.
 *
 * @author Troy A. Griffitts [scribe at crosswire dot org]
 */
public class MicroLessonSet extends LessonSet {

     public MicroLessonSet(String url) {
          super(url);
     }


     /**
      * Load this lesson set from persistent store named by the lesson set's <code>dirname</code>.
      * This is the union of lessons in the Jar and in the user's flashcard home directory.
      */
     protected void load() {
		for (int i = 0; true; i++) {
			String path = getURL() + "/" + "lesson" + Integer.toString(i) + ".flash";
			try {
				add(new MicroLesson(path));
			}
			catch (Exception e) { break; }
		}
     }
}
