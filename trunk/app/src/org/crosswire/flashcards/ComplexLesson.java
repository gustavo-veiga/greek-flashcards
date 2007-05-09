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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import javax.imageio.ImageIO;
import java.awt.geom.Rectangle2D;


/**
 * A Lesson is an ordered list of FlashCards.
 * The lesson also has a description which is useful for showing to a user.
 */
public class ComplexLesson
          extends Lesson {

     public ComplexLesson(String url) throws Exception {
          super(url);
     }


     public ComplexLesson(String url, String description) throws Exception {
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
               String lname = getURL().substring(baseOffset + 1);
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
               for (; i < getFlashcards().size(); i++) {
                    FlashCard flashCard = (FlashCard) getFlashcards().get(i);
                    lesson.setProperty("word" + i, flashCard.getFront());
                    lesson.setProperty("answers" + i, flashCard.getBack());
               }
               lesson.setProperty("wordCount", Integer.toString(i));

               // Save it as a "home" resource.
               URL filePath = new URL(getURL());
               File file = null;
               URLConnection connection = filePath.openConnection();
               if (connection instanceof JarURLConnection) {
                    file = new File(LessonManager.instance().getHomeProjectPath() + File.separator +
                                    ( (JarURLConnection) connection).getEntryName());
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
          }
          finally {
               if (outStream != null) {
                    try {
                         outStream.close();
                    }
                    catch (IOException e) {
                         Debug.error(this.getClass().getName(), e.getMessage());
                    }
               }
          }
     }


     /**
      * Save this lesson to persistent store named by the lesson's <code>filename</code>.
      */
     public void generateImages() {
          OutputStream outStream = null;
          try {
               // Create an image to save
               int baseOffset = getURL().lastIndexOf("/");
               if (baseOffset < 0) {
                    baseOffset = getURL().lastIndexOf( ("\\"));
               }
               String lname = getURL().substring(baseOffset + 1);
               lname = lname.substring(0, lname.indexOf(".flash"));
               String imagesPath = getURL().substring(0, baseOffset) + "/images";

               int i = 0;
               for (; i < getFlashcards().size(); i++) {
                    FlashCard f = (FlashCard)getFlashcards().elementAt(i);
                    String imageURLString = imagesPath + "/" + lname + "_" + Integer.toString(i) + ".png";
                    // Save it as a "home" resource.
                    URL filePath = new URL(imageURLString);
                    File file = null;
                    URLConnection connection = filePath.openConnection();
                    if (connection instanceof JarURLConnection) {
                         file = new File(LessonManager.instance().getHomeProjectPath() + File.separator +
                                         ( (JarURLConnection) connection).getEntryName());
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
                    final int width = 800;
                    final int height = 40;

                    // Create a buffered image in which to draw
                    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                    // Create a graphics contents on the buffered image
                    Graphics2D g2d = bufferedImage.createGraphics();

                    // Draw graphics
                    g2d.setColor(Color.white);
                    g2d.fillRect(0, 0, width, height);
                    g2d.setColor(Color.black);
                    Font font = new Font(g2d.getFont().getName(), Font.BOLD, (int)(height*.75));
                    g2d.setFont(font);
                    Rectangle2D rect = font.getStringBounds(f.getFront(), g2d.getFontRenderContext());
                    g2d.drawString(f.getFront(), 4, (int)(height*.70));
                    bufferedImage = bufferedImage.getSubimage(0, 0, (int)(rect.getWidth()+8), 40);

                    // Graphics context no longer needed so dispose it
                    g2d.dispose();

                    // Write generated image to a file
                    try {
                         // Save as PNG
                         ImageIO.write(bufferedImage, "png", outStream);

                    }
                    catch (IOException e) {
                    }

               }
          }
          catch (IOException ex) {
               Debug.error(this.getClass().getName(), ex.getMessage());
          }
          finally {
               if (outStream != null) {
                    try {
                         outStream.close();
                    }
                    catch (IOException e) {
                         Debug.error(this.getClass().getName(), e.getMessage());
                    }
               }
          }
     }
}
