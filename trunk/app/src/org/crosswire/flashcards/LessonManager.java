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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


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
     private Vector lessonSets = new Vector();
     private File homeLessonDir = null;
     private String homeProjectPath = null;

     public static LessonManager instance() {
          return INSTANCE;
     }


     private LessonManager() {
         homeProjectPath = System.getProperty("user.home") + File.separator + DIR_PROJECT;
         homeLessonDir = new File(homeProjectPath + File.separator + LESSON_ROOT);
         load();
         jbInit();
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
          // see if there are any jars in our CWD with lesson sets
          loadLessonSetsFromJarDir("./");

          // see if there are any lesson sets in our home project dir
          loadLessonSetsFromDir(homeLessonDir);

          // find the directory containing this
          // search all jars in that directory for lesson sets
          String thisName = LessonManager.class.getName();
          String thisRes = "/" + thisName.replace('.', '/') + ".class";
          URL thisURL = LessonManager.class.getResource(thisRes);
          if (thisURL == null) {
              return;
          }
          URLConnection thisCon = null;
          try {
              thisCon = thisURL.openConnection();
          }
          catch (Exception e1) {
              assert false;
          }
          if (thisCon instanceof JarURLConnection) {
               JarURLConnection jarConnection = (JarURLConnection) thisCon;
               loadLessonSetsFromJarDir(new File(jarConnection.getJarFileURL().getFile()).getParent());
          }

          // see if there are any lessons on our path
          // dig into the jar for lessonSets
          URL lessonsURL = LessonManager.class.getResource('/' + LESSON_ROOT);
          if (lessonsURL == null) {
              return;
          }
          URLConnection connection = null;
          try {
              connection = lessonsURL.openConnection();
          }
          catch (Exception e1) {
              assert false;
          }
          if (connection instanceof JarURLConnection) {
               JarURLConnection jarConnection = (JarURLConnection) connection;
               try {

                    String uri = jarConnection.getJarFileURL().toString();
                    //stupid bug with webstart
                    if ((uri.startsWith("file:")) && (!uri.startsWith("file:/"))) {
                         uri = "file:/" + uri.substring(5);
                    }
//                    int value =  JOptionPane.showConfirmDialog(null, uri,
//                                     "Text Edit", JOptionPane.OK_OPTION) ;

                    loadJarLessonSets(new File(new java.net.URI(uri)));
               }
               catch (Exception e) {
                   Debug.error(this.getClass().getName(), e.getMessage());
               }
          }
     }

     /**
      * Load lesson sets from the jar file
      */
     private void loadLessonSetsFromJarDir(String path) {
          File lessonDir = new File(path);
          if (lessonDir.isDirectory()) {
               File[] files = lessonDir.listFiles(new JarFileFilter());
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
                         if (entryName.startsWith(LESSON_ROOT) && !entryName.equals(LESSON_ROOT) && !entryName.endsWith("/audio")) {
                              // let the description be just the directory name and not the path
                              add(new ComplexLessonSet("jar:" + jarFile.getCanonicalFile().toURL().toString() + "!/" + entryName));
                         }
                    }
               }
          }
          catch (IOException e2) {
              Debug.error(this.getClass().getName(), e2.getMessage());
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
                    if (file.isDirectory() && !file.getName().equals("audio")) {
                         add(new ComplexLessonSet(files[i].toURL().toString()));
                    }
               }
          }
          catch (IOException e) {
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

     /**
      * Save all the modified lesson sets to persistent store named by the lesson's <code>LESSON_ROOT</code>.
      */
     public void genImages() {
          Iterator iter = lessonSets.iterator();
          while (iter.hasNext()) {
               ComplexLessonSet lessonSet = (ComplexLessonSet) iter.next();
               lessonSet.generateImages();
          }
     }


     public Iterator iterator() {
          return lessonSets.iterator();
     }


     public String getHomeProjectPath() {
          return homeProjectPath;
     }


     private void jbInit() {
     }

     static class JarFileFilter implements FilenameFilter {
         public boolean accept(File dir, String name) {
              return name.toUpperCase(Locale.ENGLISH).endsWith(".JAR");
         }
    }
}
