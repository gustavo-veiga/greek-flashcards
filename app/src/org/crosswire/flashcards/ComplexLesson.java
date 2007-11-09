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
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
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
public class ComplexLesson extends Lesson {

     private static final String DIR_PROJECT = ".flashcards";

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
               String homeProjectPath = System.getProperty("user.home") + File.separator + DIR_PROJECT;
               URL lessonURL = new URL(getURL());
               Properties lesson = new Properties();
               lesson.load(lessonURL.openConnection().getInputStream());
               int wordCount = Integer.parseInt(lesson.getProperty("wordCount"));
               setDescription(lesson.getProperty("lessonTitle", getURL().substring(getURL().lastIndexOf('/') + 1)));
               String font = lesson.getProperty("lessonFont");
               if (font != null && font.length() > 0) {
                    String fontPath = homeProjectPath + File.separator + font+ ".ttf";
                    File fontFile = new File(fontPath);
                    if (fontFile.exists()) {
                         String url = fontFile.toURL().toString();
                         setFont(url);
                    }
               }
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

     public Font loadFont(String url) {
          Font font = null;
          if (url.length() > 2) {
               InputStream is = null;
               try {
                    URLConnection connection = new URL(url).openConnection();
                    is = connection.getInputStream();
                    font = loadFont(is);
               }
               catch (IOException ex) {
                    ex.printStackTrace(System.err);
               }
               catch (FontFormatException e) {
                    e.printStackTrace(System.err);
               } finally {
                    if (is != null) {
                         try {
                              is.close();
                         } catch (IOException e) {
                              e.printStackTrace(System.err);
                         }
                    }
               }
          }
          return font;
     }


     public Font loadFont(InputStream is) throws FontFormatException, IOException {
          Font font = Font.createFont(Font.TRUETYPE_FONT, is);
          return font;
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
               final int width = 800;
               final int height = 40;
               Font font = null;
               if (getFont() != null && getFont().length() > 0) {
                    Font newFont = loadFont(getFont());
                    font = newFont.deriveFont(Font.BOLD, (int)(height*.75));
               }
//               else font = new Font(g2d.getFont().getName(), Font.BOLD, (int)(height*.75));

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

                    // Create a buffered image in which to draw
                    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                    // Create a graphics contents on the buffered image
                    Graphics2D g2d = bufferedImage.createGraphics();

                    // Draw graphics
                    g2d.setColor(Color.white);
                    g2d.fillRect(0, 0, width, height);
                    g2d.setColor(Color.black);

                   // We need more intelligent font handling here.  Maybe in load(), when we
                   // grab the font name, we could also look in the lesson for the font file
                   // itself, otherwise look for it app-wide (whatever that means)
                    g2d.setFont((font != null) ? font : g2d.getFont().deriveFont(Font.BOLD, (int)(height*.74)));
//System.out.println("### Using Font: " + g2d.getFont().getName());

                    Rectangle2D rect = g2d.getFont().getStringBounds(f.getFront(), g2d.getFontRenderContext());
                    g2d.drawString(f.getFront(), 4, (int)(height*.72));
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
