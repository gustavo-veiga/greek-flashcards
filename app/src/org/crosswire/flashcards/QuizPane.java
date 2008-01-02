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
 * The copyright to this program is held by it's authors
 * Copyright: 2004
 */
package org.crosswire.flashcards;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.Serializable;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.Hashtable;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;




/**
 * A panel that quizzes over a selection of lessons.
 *
 * @author Troy A. Griffitts [scribe at crosswire dot org]
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
public class QuizPane
          extends JPanel {
     private static final int NUM_COLUMNS = 2;
     // NUM_ANSWERS should be a multiple of NUM_COLUMNS.
     private static final int NUM_ANSWERS = 10;
     private static Hashtable fontCache = new Hashtable();

     float optimalFontSize = 30;
     List picks = new ArrayList();
     /**
      * Serialization ID
      */
     private static final long serialVersionUID = 8613458092624929167L;

     SetupPane setupPane;
     List words = new ArrayList();
     List notLearned = new ArrayList();
     WordEntry currentWord = null;
     int wrong = 0;
     int totalAsked = 0;
     int totalWrong = 0;
     boolean shownAnswer = false;
     JButton startLessonButton = new JButton();
     JButton playSoundButton = new JButton();
     JButton showAnswerButton = new JButton();
     JLabel wordText = new JLabel();
     JLabel statusBar = new JLabel();
     JLabel wCount = new JLabel();

     JPanel choicesPanel = new JPanel();
     GridLayout choicesPanelGridLayout = new GridLayout();
     JPanel statusPanel = new JPanel();
     BorderLayout statusPanelBorderLayout = new BorderLayout();
     JPanel jPanel1 = new JPanel();
     JPanel jPanel2 = new JPanel();
     BorderLayout borderLayout1 = new BorderLayout();
     BorderLayout borderLayout2 = new BorderLayout();
     JPanel jPanel3 = new JPanel();
     BorderLayout borderLayout3 = new BorderLayout();
     JPanel jPanel4 = new JPanel();
     JPanel jPanel5 = new JPanel();
     JPanel jPanel6 = new JPanel();
     BorderLayout borderLayout4 = new BorderLayout();

     static class WordEntry implements Serializable {

          protected String back;
          protected FlashCard flashCard;
          protected int attempts;
          protected String fontURL = null;
          /**
           * Serialization ID
           */
          private static final long serialVersionUID = -8148656461971656626L;


          public WordEntry(FlashCard flashCard) {
               this.flashCard = flashCard;
               back = flashCard.getBack();
          }


          public void incrementFailures(int failures) {
               attempts += failures;
          }


          public int getFailures() {
               return attempts;
          }


          public String getSide(boolean front) {
               return (front) ? flashCard.getFront() : back;
          }

          public String getAudioURL() {
               return flashCard.getAudioURL();
          }

          public void setFontURL(String fontURL) {
               this.fontURL = fontURL;
          }

          public String getFontURL() {
               return fontURL;
          }

          public String toString() {
               return flashCard.getFront();
          }
     }


     //Construct the frame
     public QuizPane(SetupPane setupPane) {
          this.setupPane = setupPane;
          jbInit();
     }


     //Component initialization
     private void jbInit() {
          startLessonButton.setText("Start");
          startLessonButton.addActionListener(new QuizPane_startLessonButton_actionAdapter(this));

          playSoundButton.setText("Listen");
          playSoundButton.addActionListener(new QuizPane_playSoundButton_actionAdapter(this));
          playSoundButton.setVisible(false);

          showAnswerButton.setFocusPainted(true);
          showAnswerButton.setMnemonic('A');
          showAnswerButton.setText("Show Answer");
          showAnswerButton.addActionListener(new QuizPane_showAnswerButton_actionAdapter(this));

          wordText.setBackground(SystemColor.text);
          wordText.setFont(new Font("Dialog", 0, 30));
          wordText.setMinimumSize(new Dimension(0, 50));
          wordText.setPreferredSize(new Dimension(0, 50));
          wordText.setHorizontalAlignment(SwingConstants.CENTER);
          wordText.setHorizontalTextPosition(SwingConstants.CENTER);

          statusBar.setBorder(BorderFactory.createEtchedBorder());
          statusBar.setText(" ");
          wCount.setBorder(BorderFactory.createEtchedBorder());

          choicesPanel.setLayout(choicesPanelGridLayout);
          choicesPanelGridLayout.setColumns(NUM_COLUMNS);
          choicesPanelGridLayout.setRows(0);

          statusPanel.setLayout(statusPanelBorderLayout);
          statusBar.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
          jPanel1.setLayout(borderLayout3);
          jPanel2.setLayout(borderLayout1);
          jPanel6.setLayout(borderLayout4);
          statusPanel.add(statusBar, BorderLayout.CENTER);
          statusPanel.add(wCount, BorderLayout.EAST);
          setLayout(borderLayout2);

          jPanel3.add(playSoundButton);
          jPanel4.add(startLessonButton);
          jPanel5.add(showAnswerButton);
          this.add(statusPanel, java.awt.BorderLayout.SOUTH);
          this.add(jPanel2, java.awt.BorderLayout.CENTER);
          this.add(jPanel1, java.awt.BorderLayout.NORTH);
          jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);
          jPanel1.add(jPanel4, java.awt.BorderLayout.WEST);
          jPanel1.add(jPanel5, java.awt.BorderLayout.EAST);
          jPanel2.add(jPanel6, java.awt.BorderLayout.SOUTH);
          jPanel2.add(wordText, java.awt.BorderLayout.CENTER);
          jPanel6.add(choicesPanel, java.awt.BorderLayout.CENTER);
          jPanel2.addComponentListener(new java.awt.event.ComponentAdapter() {
              public void componentResized(java.awt.event.ComponentEvent e) {
                  windowResized();
              }
          });
     }


     public void deleteChildren(JComponent c) {
          while (c.getComponentCount() > 0) {
               c.remove(c.getComponent(0));
          }
     }


     public void loadTest() {
          words = new ArrayList();
          Iterator lessonIter = setupPane.iterator();
          while (lessonIter.hasNext()) {
               Lesson lesson = (Lesson) lessonIter.next();
               Vector cards = lesson.getFlashcards();
               for (int i = 0; i < cards.size(); i++) {
                    WordEntry we = new WordEntry( (FlashCard) cards.get(i));
                    we.setFontURL(lesson.getFont());
                    words.add(we);
               }
          }
          // let's combine duplicate words
          for (int i = 0; i < words.size() - 1; i++) {
               WordEntry w = (WordEntry) words.get(i);
               for (int j = i + 1; j < words.size(); j++) {
                    WordEntry xx = (WordEntry) words.get(j);
                    if (w.flashCard.getFront().equals(xx.flashCard.getFront())) {
                         w.back += " or " + xx.back;
                         words.remove(j);
                         j--;
                    }
               }
          }
     }


    public Font loadFont(String url) {
        Font retVal = new Font("Dialog", 0, 30);
        if ((url != null) && (url.length() > 0)) {
            try {
                // see if our font is already loaded...
                retVal = (Font)fontCache.get(url);
                if (retVal == null) {
                    statusBar.setText("Loading font...");
                    statusBar.paintImmediately(statusBar.getVisibleRect());
                    URL fontURL = new URL(url);
                    URLConnection uc = fontURL.openConnection();
                    InputStream is = uc.getInputStream();
                    retVal = Font.createFont(Font.TRUETYPE_FONT, is);
                    is.close();
                    fontCache.put(url, retVal);
                    statusBar.setText("New Font Loaded.");
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retVal;
    }

     void startLessonButton_actionPerformed(ActionEvent e) {
          loadTest();
          notLearned = (List) ((ArrayList) words).clone();
          totalAsked = 0;
          totalWrong = 0;
          showRandomWord(currentWord);
     }


     void playSoundButton_actionPerformed(ActionEvent e1) {

               // assert we have an audioURL
               if (currentWord.getAudioURL() == null) {
                    return;
               }

               final int	EXTERNAL_BUFFER_SIZE = 128000;

               AudioInputStream	audioInputStream = null;
               try {
                    URL audioURL = new URL(currentWord.getAudioURL());
                    audioInputStream = AudioSystem.getAudioInputStream(audioURL);
               }
               catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
               }

               AudioFormat	audioFormat = audioInputStream.getFormat();

               SourceDataLine	line = null;
               DataLine.Info	info = new DataLine.Info(SourceDataLine.class,
                                                                  audioFormat);
               try {
                    line = (SourceDataLine) AudioSystem.getLine(info);
                    line.open(audioFormat);
               }
               catch (LineUnavailableException e) {
                    e.printStackTrace();
                    System.exit(1);
               }
               catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
               }

               line.start();

               int	nBytesRead = 0;
               byte[]	abData = new byte[EXTERNAL_BUFFER_SIZE];
               while (nBytesRead != -1) {
                    try {
                         nBytesRead = audioInputStream.read(abData, 0, abData.length);
                    }
                    catch (IOException e) {
                         e.printStackTrace();
                    }
                    if (nBytesRead >= 0) {
                         int	nBytesWritten = line.write(abData, 0, nBytesRead);
                    }
               }

               line.drain();
               line.close();
	}


     public void showRandomWord(WordEntry last) {
          deleteChildren(choicesPanel);
          int numToLearn = notLearned.size();
          if (numToLearn == 0) {
               return;
          }
          while (currentWord == last) {
               int wordNum = (int) (Math.random() * notLearned.size());
               currentWord = (WordEntry) notLearned.get(wordNum);
          }
          showWord(currentWord);
     }


     public void showWord(WordEntry w) {
          currentWord = w;

          playSoundButton.setVisible(currentWord.getAudioURL() != null);

          Font newFont = loadFont(currentWord.getFontURL()).deriveFont(optimalFontSize);
          wordText.setFont(newFont);

          wordText.setText(w.getSide(!setupPane.isFlipped()));
          if (setupPane.isNoMultipleChoice()) {
               choicesPanel.invalidate();
               choicesPanel.validate();
               choicesPanel.repaint();
          }
          else {
              List choices = (List) ((ArrayList) words).clone();
              choices.remove(w);

              // randomly pick answers
              boolean flipped = setupPane.isFlipped();
              picks = new ArrayList();
              picks.add(createAnswerEntry(w.getSide(flipped)));
              int size = words.size();
              while (picks.size() < Math.min(NUM_ANSWERS, size)) {
                  int c = (int) (Math.random() * choices.size());
                  WordEntry wc = (WordEntry) choices.get(c);
                  String answer = wc.getSide(flipped);

                  // some times two different word have the same answer
                  if (!picks.contains(answer)) {
                      picks.add(createAnswerEntry(answer));
                      choices.remove(wc);
                  }
              }
              // Now randomize these answers. To do this we swap the first one
              // with another.
              int c = (int) (Math.random() * picks.size());
              // If we have selected something other than ourselves.
              if (c > 0) {
                  picks.add(0, picks.remove(c));
                  picks.add(c, picks.remove(1));
              }
              Iterator iter = picks.iterator();
              while (iter.hasNext()) {
                  choicesPanel.add( (Component) iter.next());
              }
              wrong = 0;
              shownAnswer = false;
              setOptimalFontSizes();
              updateStats();
              choicesPanel.invalidate();
              choicesPanel.validate();
              choicesPanel.repaint();
          }
     }


     Component createAnswerEntry(String answer) {
          JCheckBox ck = new JCheckBox(answer, false);
          ck.setFont(new Font("Dialog", 0, 16));
          ck.addItemListener(new QuizPane_answer_itemAdapter(this));
          return ck;
     }


     void updateStats() {
          int percent = 100;
          if (totalAsked > 0) {
               percent = (int) ( ( ( (float) (totalAsked - totalWrong)) / (float) totalAsked) * 100);
          }
          wCount.setText(Integer.toString(notLearned.size()) + " | " + Integer.toString(totalAsked - totalWrong) + "/" +
                         Integer.toString(totalAsked) + " (" + Integer.toString(percent) + "%)");
     }


     void answer_itemStateChanged(ItemEvent e) {
		JCheckBox ck = null;
          try {
		     ck = (JCheckBox) e.getItem();
          }
          catch (Exception e1) { e1.printStackTrace(); }

          if (ck == null) return;

          if (ck.isSelected()) {
               totalAsked++;
               if (ck.getText().compareTo(currentWord.getSide(setupPane.isFlipped())) != 0) {
                    statusBar.setText("Please try again. " + ck.getText() + " is not correct.");
                    wrong++;
                    totalWrong++;
                    ck.setSelected(false);
               }
               else {
                    if (notLearned.size() > 1) {
                         statusBar.setText("Correct.  Try this next word");
                         if (wrong > 0) {
                              currentWord.incrementFailures(wrong);
                         }
                         else {
                              currentWord.incrementFailures( -1);
                         }
                         if (currentWord.getFailures() < 0) {
                              notLearned.remove(currentWord);
                         }
                         showRandomWord(currentWord);
                    }
                    else {
                         notLearned.remove(currentWord);
                         deleteChildren(choicesPanel);
                         wordText.setText("-=+* Great! *+=-");
                         statusBar.setText("Nice Job!  You've mastered all " + words.size() + " words!");
                    }
               }
               updateStats();
          }
     }


     public void showAnswer() {
          for (int i = 0; i < choicesPanel.getComponentCount(); i++) {
               JCheckBox ck = (JCheckBox) choicesPanel.getComponent(i);
               if (ck.getText().equals(currentWord.getSide(setupPane.isFlipped()))) {
                    ck.setFont(new Font(ck.getFont().getName(), Font.BOLD | Font.ITALIC, ck.getFont().getSize()));
                    break;
               }
          }
          shownAnswer = true;
     }


     void showAnswerButton_actionPerformed(ActionEvent e) {
          if (setupPane.isNoMultipleChoice()) {
               ++totalAsked; String dialogString = currentWord.getSide(!setupPane.isFlipped()) + "\n" +
                         currentWord.getSide(setupPane.isFlipped()) + "\n" + "Did You Get It Right?\n";
               int choice = JOptionPane.showConfirmDialog(this, dialogString, "Result", JOptionPane.YES_NO_OPTION);
               if (JOptionPane.YES_OPTION == choice) {
                    notLearned.remove(currentWord);
               }
               else {
                    ++totalWrong;
               }
               updateStats();
               if (notLearned.size() > 0) {
                    showRandomWord(currentWord);
               }
               else {
                    wordText.setText("-=+* Great! *+=-");
                    statusBar.setText("Nice Job!  You've mastered all " + words.size() + " words!");
               }
          }
          else {
               if (!shownAnswer) {
                    showAnswer();
                    return;
               }
               int next = notLearned.indexOf(currentWord) + 1;
               if (next == 0) {
                    return;
               }
               if (next >= notLearned.size()) {
                    next = 0;
               }
               deleteChildren(choicesPanel);
               showWord( (WordEntry) notLearned.get(next));
               showAnswer();
          }
     }
     
     
     public Rectangle getMaxBounds(float fontSize) {
         Graphics2D g2d = (Graphics2D)wordText.getGraphics();
         Rectangle biggest = new Rectangle(0, 0, 0, 0);
         FontRenderContext fc = g2d.getFontRenderContext();
         Iterator it = notLearned.iterator();
         while (it.hasNext()) {
             WordEntry we = (WordEntry)it.next();
             Font f = loadFont(we.getFontURL());
             if (f != null) {
                 f = f.deriveFont(fontSize);
                 TextLayout tlo = new TextLayout(we.getSide(!setupPane.isFlipped()), f, fc);
                 Rectangle2D rect = tlo.getBounds();
                 if (rect.getWidth() > biggest.width) biggest.width = (int)rect.getWidth();
                 if (rect.getHeight() > biggest.height) biggest.height = (int)rect.getHeight();
             }
         }
         return biggest;
     }

     
     public float getOptimalFontSize(Rectangle bounds) {
         float fontSize = 30;
         Rectangle referenceBounds = getMaxBounds(fontSize);
         float xmult = (float)bounds.width / (float)referenceBounds.width;
         float ymult = (float)bounds.height / (float)referenceBounds.height;
         fontSize *= (xmult < ymult) ? xmult : ymult;
         fontSize *= 0.75;
         return fontSize;
     }

     public void setOptimalFontSizes() {
         jPanel2.invalidate();
         jPanel2.validate();
         jPanel2.repaint();
         // make a first pass at the font size to approximate the choices font
         Rectangle bounds = jPanel2.getBounds();
         bounds.height /= 2;
         optimalFontSize = getOptimalFontSize(bounds);
         Font newFont = loadFont(currentWord.getFontURL());
         Font choiceFont = newFont.deriveFont((float)(optimalFontSize * 0.90) / (NUM_ANSWERS/NUM_COLUMNS));
         Iterator iter = picks.iterator();
         while (iter.hasNext()) {
             ((Component) iter.next()).setFont(choiceFont);
         }
         // Now that bottom layout is adjusted for new font size, computer real
         // font size for top
         optimalFontSize = getOptimalFontSize(bounds);
         newFont = loadFont(currentWord.getFontURL()).deriveFont(optimalFontSize);
         wordText.setFont(newFont);
     }
     
     
     protected void windowResized() {
         if (wordText != null) {
             if (wordText.getText() != null) {
                 if (wordText.getText().length() > 0) {
                     setOptimalFontSizes();
                 }
             }
         }
     }
}



class QuizPane_startLessonButton_actionAdapter
          implements ActionListener {
     QuizPane adaptee;

     QuizPane_startLessonButton_actionAdapter(QuizPane adaptee) {
          this.adaptee = adaptee;
     }


     public void actionPerformed(ActionEvent e) {
          adaptee.startLessonButton_actionPerformed(e);
     }
}

class QuizPane_playSoundButton_actionAdapter
          implements ActionListener {
     QuizPane adaptee;

     QuizPane_playSoundButton_actionAdapter(QuizPane adaptee) {
          this.adaptee = adaptee;
     }


     public void actionPerformed(ActionEvent e) {
          adaptee.playSoundButton_actionPerformed(e);
     }
}


class QuizPane_answer_itemAdapter
          implements ItemListener {
     QuizPane adaptee;

     QuizPane_answer_itemAdapter(QuizPane adaptee) {
          this.adaptee = adaptee;
     }


     public void itemStateChanged(ItemEvent e) {
          adaptee.answer_itemStateChanged(e);
     }
}


class QuizPane_showAnswerButton_actionAdapter
          implements ActionListener {
     QuizPane adaptee;

     QuizPane_showAnswerButton_actionAdapter(QuizPane adaptee) {
          this.adaptee = adaptee;
     }


     public void actionPerformed(ActionEvent e) {
          adaptee.showAnswerButton_actionPerformed(e);
     }
}
