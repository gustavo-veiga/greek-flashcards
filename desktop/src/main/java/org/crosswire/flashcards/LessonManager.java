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
     private static LessonManager instance = null;

     /**
      * An ordered list of <code>lessonSets</code>
      */
     private Vector lessonSets = new Vector();
     private File homeLessonDir = null;
     private String homeProjectPath = null;

     public static LessonManager instance() {
          if (instance == null) instance = new LessonManager();
          return instance;
     }

     public LessonManager(String jarsPath) {
          loadLessonSetsFromJarDir(jarsPath);
     }

     public LessonManager() {
         try {
           homeProjectPath = System.getProperty("user.home") + File.separator + DIR_PROJECT;
           homeLessonDir = new File(homeProjectPath + File.separator + LESSON_ROOT);
         }
         catch (Exception e) { e.printStackTrace(); }
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


     public Vector getLessonSets() {
          return lessonSets;
     }

     public LessonSet getLessonSet(String description) {
          for (int i = 0; i < lessonSets.size(); i++) {
               LessonSet ls = (LessonSet) lessonSets.elementAt(i);
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

          // see if there is a CWD/lessons folder with lesson sets
          loadLessonSetsFromDir(new File("./lessons/"));

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
              e1.printStackTrace();
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
              e1.printStackTrace();
          }
          if (connection instanceof JarURLConnection) {
               JarURLConnection jarConnection = (JarURLConnection) connection;
               try {

//                    String uri = jarConnection.getJarFileURL().toString();
                    String uri = new File(jarConnection.getJarFile().getName()).getCanonicalFile().toURL().toString();
System.out.println("uri = " + uri);
                    //stupid bug with webstart
                    if ((uri.startsWith("file:")) && (!uri.startsWith("file:/"))) {
                         uri = "file:/" + uri.substring(5);
                    }
                    uri = uri.replace(" ", "%20");

System.out.println("uri = " + uri);
                    loadJarLessonSets(new File(new java.net.URI(uri)));
/*
                    loadJarLessonSets(jarConnection.getJarFile());
*/
               }
               catch (Exception e) {
                   e.printStackTrace();
               }
          }
     }

     /**
      * Load lesson sets from the jar file
      */
     private void loadLessonSetsFromJarDir(String path) {
          try {
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
          catch (Exception e) { e.printStackTrace(); }
     }


     /**
      * Load lesson sets from the jar file
      */
/*
     private void loadJarLessonSets(File jarFile) {

          // Dig into the jar for lessonSets
          JarFile jjarFile = null;
          try {
               jjarFile = new JarFile(jarFile);
               loadJarLessonSets(jjarFile);
          }
          catch (IOException e2) {
              e2.printStackTrace();
          }
     }

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
              e2.printStackTrace();
          }
     }

     /**
      * Load lesson sets from the jar file
      */
     private void loadJarLessonSets(JarFile jjarFile) {

          try {
               Enumeration entries = jjarFile.entries();
               while (entries.hasMoreElements()) {
                    JarEntry jarEntry = (JarEntry) entries.nextElement();
                    if (jarEntry.isDirectory()) {
                         String entryName = jarEntry.getName();
                         // remove trailing '/'
                         entryName = entryName.substring(0, entryName.length() - 1);
                         if (entryName.startsWith(LESSON_ROOT) && !entryName.equals(LESSON_ROOT) && !entryName.endsWith("/audio")) {
                              // let the description be just the directory name and not the path
System.out.println("jar:" + new File(jjarFile.getName()).getCanonicalFile().toURL().toString() + "!/" + entryName);
                              add(new ComplexLessonSet("jar:" + new File(jjarFile.getName()).getCanonicalFile().toURL().toString() + "!/" + entryName));
                         }
                    }
               }
          }
          catch (IOException e2) {
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
                    if (file.isDirectory() && !file.getName().equals("audio")) {
                         add(new ComplexLessonSet(files[i].toURL().toString()));
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
          for (int i = 0; i < lessonSets.size(); i++) {
               LessonSet lessonSet = (LessonSet) lessonSets.elementAt(i);
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
          for (int i = 0; i < lessonSets.size(); i++) {
               LessonSet lessonSet = (LessonSet) lessonSets.elementAt(i);
               if (lessonSet.isModified()) {
                    lessonSet.store();
               }
          }
     }

     /**
      * Generate pre-rendered images for each card (useful on mobile phones)
      */
     public void genImages() {
          for (int i = 0; i < lessonSets.size(); i++) {
               ComplexLessonSet lessonSet = (ComplexLessonSet) lessonSets.elementAt(i);
               lessonSet.generateImages();
          }
     }


     public String getHomeProjectPath() {
          return homeProjectPath;
     }


     static class JarFileFilter implements FilenameFilter {
         public boolean accept(File dir, String name) {
              return name.toUpperCase(Locale.ENGLISH).endsWith(".JAR");
         }
     }


     public static void main(String argv[]) {
          // Parse the command line arguments
          String font = null;
          int action = 0;
          for (int i = 0; i < argv.length; i++) {
               if ("-genImages".equals(argv[i])) {
                    action = 1;
               }
          }
          switch (action) {
               case 0: System.out.println("usage: LessonManager [-genImages]"); break;
               case 1: LessonManager.instance().genImages(); break;
               default: break;
          }
     }
}
