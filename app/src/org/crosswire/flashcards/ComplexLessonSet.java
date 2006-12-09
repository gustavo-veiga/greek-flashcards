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
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Locale;
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
public class ComplexLessonSet extends LessonSet {

     public ComplexLessonSet(String url) {
          super(url);
     }


     /**
      * Load this lesson set from persistent store named by the lesson set's <code>dirname</code>.
      * This is the union of lessons in the Jar and in the user's flashcard home directory.
      */
     protected void load() {
          if (getURL() == null) { // assert we have an URL
               return;
          }

          URL lessonsURL = null;
          URLConnection connection = null;
          try {
               lessonsURL = new URL(getURL());
               connection = lessonsURL.openConnection();
          }
          catch (Exception e1) {
              Debug.error(this.getClass().getName(), e1.getMessage());
          }
          if (connection instanceof JarURLConnection) {
               JarURLConnection jarConnection = (JarURLConnection) connection;
               loadJarLessonSet(jarConnection);
          }
          else if (lessonsURL != null) {
              loadDirectoryLessonSet(new File(lessonsURL.getFile()));
          }
     }


     private void loadJarLessonSet(JarURLConnection jarConnection) {
          String dirName = jarConnection.getEntryName();
          JarFile jarFile = null;
          try {
               jarFile = jarConnection.getJarFile();
          }
          catch (Exception e2) {
              Debug.error(this.getClass().getName(), e2.getMessage());
          }
          if (jarFile == null) {
               return;
          }
          Enumeration entries = jarFile.entries();
          while (entries.hasMoreElements()) {
               JarEntry jarEntry = (JarEntry) entries.nextElement();
               String lessonPath = jarEntry.getName();
               if (lessonPath.startsWith(dirName) && !jarEntry.isDirectory() &&
                   lessonPath.toUpperCase(Locale.ENGLISH).endsWith(".FLASH")) {
                    add(new ComplexLesson("jar:" + jarConnection.getJarFileURL() + "!/" + lessonPath));
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
               File[] files = directory.listFiles(new FlashFileFilter());
               if (files == null) {
                    return;
               }
               Arrays.sort(files);
               for (int i = 0; i < files.length; i++) {
                    add(new ComplexLesson(files[i].getCanonicalFile().toURL().toString()));
               }
          }
          catch (IOException e) {
               // that's fine.  We just failed to load local files.
          }
     }


     public String getNextLessonFilename() {
          // This needs work: It should check for collisions
          String result = null;
          int next = getLessons().size();
          Object[] params = {
                    getURL(), new Integer(next)};
          MessageFormat format = new MessageFormat("{0}/lesson{1,number,00}.flash");
          result = format.format(params);
          return result;
     }


     static class FlashFileFilter implements FilenameFilter {
         public boolean accept(File dir, String name) {
              return name.toUpperCase(Locale.ENGLISH).endsWith(".FLASH");
         }
    }
}
