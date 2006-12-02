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
import java.util.TreeSet;


/**
 * A Lesson is an ordered list of FlashCards.
 * The lesson also has a description which is useful for showing to a user.
 *
 * @author Troy A. Griffitts [scribe at crosswire dot org]
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
public class Lesson implements Comparable, Serializable {

    /**
      * The <code>filename</code> gives the relative location of the lesson.
      * Typically this is something like lesson/setname/lessonname.flash.
      */
     private String url;

     /**
      * A <code>description</code> of the lesson to be displayed to the user.
      */
     private String description;

     /**
      * A path to the <code>font</code> to be used by the lesson.
      */
     private String font;

     /**
      * An ordered list of <code>flashCards</code>
      */
     private Set flashCards = new TreeSet();

     private boolean modified = false;

     /**
      * Serialization ID
      */
     private static final long serialVersionUID = -4031174832238749375L;

     /**
      * Construct a new, empty lesson.
      */
     public Lesson() {
          this("NewLesson.flash", "New Lesson");
     }


     /**
      * Construct a lesson from URL.
      * @param url
      */
     public Lesson(String url) {
          this(url, null);
     }


     /**
      * Construct a lesson and assign a description
      * @param url
      * @param description
      */
     public Lesson(String url, String description) {
          this.url = url;
          load();

          if (description != null) {
               this.description = description;
          }
     }


     /**
      * Load this lesson from persistent store named by the lesson's <code>filename</code>.
      */
     public void load() {
          try {
               URL lessonURL = new URL(url);
               Properties lesson = new Properties();
               lesson.load(lessonURL.openConnection().getInputStream());
               int wordCount = Integer.parseInt(lesson.getProperty("wordCount"));
               description = lesson.getProperty("lessonTitle", url.substring(url.lastIndexOf('/') + 1));
               for (int i = 0; i < wordCount; i++) {
                    add(new FlashCard(lesson.getProperty("word" + i), lesson.getProperty("answers" + i)));
               }
               modified = false;
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
               lesson.setProperty("lessonTitle", description);
               Iterator iter = flashCards.iterator();
               int i = 0;
               while (iter.hasNext()) {
                    FlashCard flashCard = (FlashCard) iter.next();
                    lesson.setProperty("word" + i, flashCard.getFront());
                    lesson.setProperty("answers" + i, flashCard.getBack());
                    i++;
               }
               lesson.setProperty("wordCount", Integer.toString(i));

               // Save it as a "home" resource.
               URL filePath = new URL(url);
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
               modified = false;
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


     /**
      * Adds the specified <code>FlashCard</code> to this Lesson.
      *
      * @param flashCard to be added.
      */
     public void add(FlashCard flashCard) {
          flashCards.add(flashCard);
          modified = true;
     }


     /**
      * Removes the specified <code>FlashCard</code> from the lesson.
      *
      * @param flashCard to be removed.
      */
     public void remove(FlashCard flashCard) {
          flashCards.remove(flashCard);
          modified = true;
     }


     /**
      * @param flashCard
      * @return
      */
     public boolean contains(FlashCard flashCard) {
          return flashCards.contains(flashCard);
     }


     /**
      * @return Returns the filename.
      */
     public String getURL() {
          return url;
     }


     /**
      * @param filename The filename to set.
      */
     public void setURL(String url) {
          this.url = url;
          modified = true;
     }


     /**
      * @return Returns the description.
      */
     public String getDescription() {
          return description;
     }


     /**
      * @param newDescription The description to set.
      */
     public void setDescription(String newDescription) {
          description = newDescription;
          modified = true;
     }


     /**
      * @return Returns the font.
      */
     public String getFont() {
          return font;
     }


     /**
      * @param newFont The font to set.
      */
     public void setFont(String newFont) {
          font = newFont;
          modified = true;
     }


     /**
      * @return Returns the flashCards.
      */
     public Iterator iterator() {
          return flashCards.iterator();
     }


     /**
      * @return whether this lesson has been modified
      */
     public boolean isModified() {
          if (modified) {
               return true;
          }

          Iterator iter = iterator();
          while (iter.hasNext()) {
               FlashCard flashCard = (FlashCard) iter.next();
               if (flashCard.isModified()) {
                    return true;
               }
          }
          return false;
     }


     /* (non-Javadoc)
      * @see java.lang.Comparable#compareTo(java.lang.Object)
      */
     public int compareTo(Object obj) {
          Lesson lesson = (Lesson) obj;
          return url.compareTo(lesson.url);
     }


     /* (non-Javadoc)
      * @see java.lang.Object#toString()
      */
     public String toString() {
          return description;
     }

}
