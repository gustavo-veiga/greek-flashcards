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
 * Copyright: 2004 CrossWire Bible Society
 */
package org.crosswire.flashcards;

import java.io.File;
import java.io.FilenameFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * A <code>LessonSet</code> is an ordered list of <code>Lesson</code>s.
 * The lessons are sorted by filename.
 * The lesson set also has a description which is useful for showing to a user
 * and a directory name where its Lessons are stored. This directory name is expected to be a relative
 * path and will be stored either in a jar or in the user's FlashCard directory.
 *
 * @author Troy A. Griffitts [scribe at crosswire dot org]
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
public class LessonSet
          implements Comparable {

     /**
      * The <code>url</code> of the lesson
      */
     private String url = null;

     /**
      * A <code>description</code> of the lesson to be displayed to the user.
      */
     private String description;

     /**
      * An ordered list of <code>lessons</code>
      */
     private Set lessons = new TreeSet();

     /**
      * Flag indicating whether this lesson set has been <code>modified</code>
      */
     private boolean modified;

     public LessonSet(String url) {
          this.url = url; // .toLowerCase();
          description = url.substring(url.lastIndexOf('/', url.length() - 2) + 1);
          if (description.endsWith("/")) {
               description = description.substring(0, description.length() - 1);
          }
          load();
     }


     /**
      * Get the relative path names of the lessons in this lesson set from
      * the jar file.
      * @param lessonSet
      */


     /**
      * Load this lesson set from persistent store named by the lesson set's <code>dirname</code>.
      * This is the union of lessons in the Jar and in the user's flashcard home directory.
      */
     private void load() {
          if (url == null) { // assert we have an URL
               return;
          }

          URL lessonsURL = null;
          URLConnection connection = null;
          try {
               lessonsURL = new URL(url);
               connection = lessonsURL.openConnection();
          }
          catch (Exception e1) {
               e1.printStackTrace();
          }
          if (connection instanceof JarURLConnection) {
               JarURLConnection jarConnection = (JarURLConnection) connection;
               loadJarLessonSet(jarConnection);
          }
          else {
              if (lessonsURL != null) {
                  loadDirectoryLessonSet(new File(lessonsURL.getFile()));
              }
          }
     }


     private void loadJarLessonSet(JarURLConnection jarConnection) {
          String dirName = jarConnection.getEntryName();
          JarFile jarFile = null;
          try {
               jarFile = jarConnection.getJarFile();
          }
          catch (Exception e2) {
               e2.printStackTrace();
          }
          if (jarFile == null) {
               return;
          }
          Enumeration entries = jarFile.entries();
          while (entries.hasMoreElements()) {
               JarEntry jarEntry = (JarEntry) entries.nextElement();
               String lessonPath = jarEntry.getName();
               if (lessonPath.startsWith(dirName) && !jarEntry.isDirectory() &&
                   lessonPath.toUpperCase().endsWith(".FLASH")) {
                    lessons.add(new Lesson("jar:" + jarConnection.getJarFileURL() + "!/" + lessonPath));
               }
          }
     }


     /**
      * Get the relative path names of the lessons in this lesson set from
      * the user's program home.
      * @param lessonSet
      */
     private void loadDirectoryLessonSet(File directory) {
          try {
               File[] files = directory.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                         return name.toUpperCase().endsWith(".FLASH");
                    }
               });
               if (files == null) {
                    return;
               }
               Arrays.sort(files);
               for (int i = 0; i < files.length; i++) {
                    lessons.add(new Lesson(files[i].getCanonicalFile().toURL().toString()));
               }
          }
          catch (Exception e) {
               // that's fine.  We just failed to load local files.
          }
     }


     /**
      * Save this lesson to persistent store named by the lesson's <code>dirname</code>.
      */
     public void store() {
          Iterator iter = lessons.iterator();
          while (iter.hasNext()) {
               Lesson lesson = (Lesson) iter.next();
               if (lesson.isModified()) {
                    lesson.store();
               }
          }
     }


     /**
      * @return whether the lesson set has been modified
      */
     public boolean isModified() {
          if (modified) {
               return true;
          }

          Iterator iter = lessons.iterator();
          while (iter.hasNext()) {
               Lesson lesson = (Lesson) iter.next();
               if (lesson.isModified()) {
                    return true;
               }
          }
          return false;
     }


     public Iterator iterator() {
          return lessons.iterator();
     }


     /**
      * Adds the specified <code>Lesson</code> to this lesson set.
      *
      * @param flashCard to be added.
      */
     public void add(Lesson lesson) {
          modified = true;
          lessons.add(lesson);
     }


     /**
      * @return Returns the description.
      */
     public String getDescription() {
          return description;
     }


     /**
      * @param description The description to set.
      */
     public void setDescription(String newDescription) {
          if (newDescription != null && !newDescription.equals(description)) {
               modified = true;
               description = newDescription;
          }
     }


     /**
      * @return Returns the dirname.
      */
     public String getURL() {
          return url;
     }


     public String getNextLessonFilename() {
          // This needs work: It should check for collisions
          String result = null;
          int next = lessons.size();
          Object[] params = {
                    url, new Integer(next)};
          MessageFormat format = new MessageFormat("{0}/lesson{1,number,00}.flash");
          result = format.format(params);
          return result;
     }


     /* (non-Javadoc)
      * @see java.lang.Comparable#compareTo(java.lang.Object)
      */
     public int compareTo(Object obj) {
          LessonSet lesson = (LessonSet) obj;
          return description.compareTo(lesson.description);
     }


     /* (non-Javadoc)
      * @see java.lang.Object#toString()
      */
     public String toString() {
          return description;
     }


     public void augment(LessonSet other) {
          Iterator i = other.iterator();
          while (i.hasNext()) {
               Lesson l = (Lesson) i.next();
               Lesson exists = getLesson(l.getDescription());
               if (exists != null) {
                    lessons.remove(exists);
               }
               lessons.add(l);
          }
     }


     public Lesson getLesson(String desc) {
          Iterator i = iterator();
          while (i.hasNext()) {
               Lesson ls = (Lesson) i.next();
               if (desc.equals(ls.getDescription())) {
                    return ls;
               }
          }
          return null;
     }
}
