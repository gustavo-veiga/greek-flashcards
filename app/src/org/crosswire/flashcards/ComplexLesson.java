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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;


/**
 * A Lesson is an ordered list of FlashCards.
 * The lesson also has a description which is useful for showing to a user.
 *
 * @author Troy A. Griffitts [scribe at crosswire dot org]
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
public class ComplexLesson extends Lesson {

     public ComplexLesson(String url) {
          super(url);
     }

     public ComplexLesson(String url, String description) {
          super(url, description);
     }

     /**
      * Load this lesson from persistent store named by the lesson's <code>filename</code>.
      */
     public void load() {
          try {
               URL lessonURL = new URL(getURL());
               Properties lesson = new Properties();
               lesson.load(lessonURL.openConnection().getInputStream());
               int wordCount = Integer.parseInt(lesson.getProperty("wordCount"));
               setDescription(lesson.getProperty("lessonTitle", getURL().substring(getURL().lastIndexOf('/') + 1)));

               int baseOffset = getURL().lastIndexOf("/");
               if (baseOffset < 0) {
                    baseOffset = getURL().lastIndexOf( ("\\"));
               }
               String lname = getURL().substring(baseOffset+1);
               lname = lname.substring(0, lname.indexOf(".flash"));
               String audioPath = getURL().substring(0, baseOffset) + "/audio";

               for (int i = 0; i < wordCount; i++) {
                    FlashCard f = new FlashCard(lesson.getProperty("word" + i), lesson.getProperty("answers" + i));
                    String audioURLString = audioPath + "/" + lname + "_" + Integer.toString(i) + ".wav";
                    URL audioURL = new URL(audioURLString);
                    try {
                         audioURL.openConnection().getInputStream();
                         f.setAudioURL(audioURLString);
                    }
                    catch (Exception e) {}
                    add(f);
               }
               setModified(false);
          }
          catch (IOException e1) {
               /* ignore it */
          }
     }


     /**
      * Save this lesson to persistent store named by the lesson's <code>filename</code>.
      */
     public void store() {
          Properties lesson = new Properties();
          OutputStream outStream = null;
          try {
               lesson.setProperty("lessonTitle", getDescription());
               int i = 0;
               for (;i < getFlashcards().size(); i++) {
                    FlashCard flashCard = (FlashCard) getFlashcards().get(i);
                    lesson.setProperty("word" + i, flashCard.getFront());
                    lesson.setProperty("answers" + i, flashCard.getBack());
                    i++;
               }
               lesson.setProperty("wordCount", Integer.toString(i));

               // Save it as a "home" resource.
               URL filePath = new URL(getURL());
               File file = null;
               URLConnection connection = filePath.openConnection();
               if (connection instanceof JarURLConnection) {
                    file = new File(LessonManager.instance().getHomeProjectPath() + File.separator + ((JarURLConnection)connection).getEntryName());
               }
               else {
                    file = new File(filePath.getFile());
               }
               File dir = file.getParentFile();
               // Is it already a directory ?
               if (!dir.isDirectory()) {
                    dir.mkdirs();
               }
               outStream = new FileOutputStream(file);
               lesson.store(outStream, "Flash Lesson");
               setModified(false);
          }
          catch (IOException ex) {
              Debug.error(this.getClass().getName(), ex.getMessage());
          } finally {
              if (outStream != null) {
                  try
                  {
                      outStream.close();
                  }
                  catch (IOException e)
                  {
                      Debug.error(this.getClass().getName(), e.getMessage());
                  }
              }
          }
     }
}
