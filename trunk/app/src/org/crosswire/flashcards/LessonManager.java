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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.io.FilenameFilter;


/**
 * The <code>LessonManager</code> provides the management of <code>LessonSet</code>s.
 *
 * @author Troy A. Griffitts [scribe at crosswire dot org]
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
public class LessonManager {

     public static final String LESSON_ROOT = "lessons";
     private static final String DIR_PROJECT = ".flashcards";
     private static final LessonManager INSTANCE = new LessonManager();

     /**
      * An ordered list of <code>lessonSets</code>
      */
     private Set lessonSets = new TreeSet();
     private File homeLessonDir = null;
     private String homeProjectPath = null;

     public static LessonManager instance() {
          return INSTANCE;
     }


     private LessonManager() {
          try {
               homeProjectPath = System.getProperty("user.home") + File.separator + DIR_PROJECT;
               homeLessonDir = new File(homeProjectPath + File.separator + LESSON_ROOT);
          }
          catch (Exception e1) {
               e1.printStackTrace();
          }
          load();
     }


     /**
      * Appends the specified <code>Lesson</code> to the end of this list.
      *
      * @param flashCard to be appended to this list.
      */
     public void add(LessonSet lessonSet) {
          LessonSet exists = getLessonSet(lessonSet.getDescription());
          if (exists == null) {
               lessonSets.add(lessonSet);
          }
          else {
               exists.augment(lessonSet);
          }
     }


     public LessonSet getLessonSet(String description) {
          Iterator i = iterator();
          while (i.hasNext()) {
               LessonSet ls = (LessonSet) i.next();
               if (description.equals(ls.getDescription())) {
                    return ls;
               }
          }
          return null;
     }


     /**
      * Load this lesson from persistent store named by the lesson's <code>LESSON_ROOT</code>.
      */
     public void load() {
          loadLessonSetsFromJarDir("./");
          loadLessonSetsFromDir(homeLessonDir);
     }


     /**
      * Load lesson sets from the jar file
      */
     private void loadLessonSetsFromJarDir(String path) {
          File dir = new File(path);
          if (dir.isDirectory()) {
               File[] files = dir.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                         return name.toUpperCase().endsWith(".JAR");
                    }
               });
               if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                         loadJarLessonSets(files[i]);
                    }
               }
          }
     }


     /**
      * Load lesson sets from the jar file
      */
     private void loadJarLessonSets(File jarFile) {

          // Dig into the jar for lessonSets
          JarFile jjarFile = null;
          try {
               jjarFile = new JarFile(jarFile);
               Enumeration entries = jjarFile.entries();
               while (entries.hasMoreElements()) {
                    JarEntry jarEntry = (JarEntry) entries.nextElement();
                    if (jarEntry.isDirectory()) {
                         String entryName = jarEntry.getName();
                         // remove trailing '/'
                         entryName = entryName.substring(0, entryName.length() - 1);
                         if (entryName.startsWith(LESSON_ROOT) && !entryName.equals(LESSON_ROOT)) {
                              // let the description be just the directory name and not the path
                              add(new LessonSet("jar:" + jarFile.getCanonicalFile().toURL().toString() + "!/" +
                                                entryName));
                         }
                    }
               }
          }
          catch (Exception e2) {
               e2.printStackTrace();
          }
     }


     /**
      * Load lesson sets from the "home" directory
      */
     private void loadLessonSetsFromDir(File directory) {
          try {
               File[] files = directory.listFiles();
               if (files == null) {
                    return;
               }
               Arrays.sort(files);
               for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    if (file.isDirectory()) {
                         add(new LessonSet(files[i].toURL().toString()));
                    }
               }
          }
          catch (Exception e) {
               // that's fine.  We just failed to load local files.
          }
     }


     /**
      * See if any LessonSet has changes that need to be saved
      */
     public boolean isModified() {
          Iterator iter = lessonSets.iterator();
          while (iter.hasNext()) {
               LessonSet lessonSet = (LessonSet) iter.next();
               if (lessonSet.isModified()) {
                    return true;
               }
          }
          return false;
     }


     /**
      * Save all the modified lesson sets to persistent store named by the lesson's <code>LESSON_ROOT</code>.
      */
     public void store() {
          Iterator iter = lessonSets.iterator();
          while (iter.hasNext()) {
               LessonSet lessonSet = (LessonSet) iter.next();
               if (lessonSet.isModified()) {
                    lessonSet.store();
               }
          }
     }


     public Iterator iterator() {
          return lessonSets.iterator();
     }


     public String getHomeProjectPath() {
          return homeProjectPath;
     }
}
