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

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class MainFrame extends JFrame {
  LessonManager lessonManager;
  JPanel contentPane;
  JLabel statusBar = new JLabel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel choicesPanel = new JPanel();
  JTabbedPane jTabbedPane1 = new JTabbedPane();
  JPanel testPanel = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JButton startLessonButton = new JButton();
  Vector words = new Vector();
  Vector notLearned = new Vector();
  WordEntry currentWord = null;
  int wrong = 0;
  int totalAsked = 0;
  int totalWrong = 0;
  JButton showAnswerButton = new JButton();
  BorderLayout borderLayout6 = new BorderLayout();
  boolean shownAnswer = false;
  JLabel wordText = new JLabel();
  GridLayout gridLayout1 = new GridLayout();
  JPanel jPanel3 = new JPanel();
  BorderLayout borderLayout7 = new BorderLayout();
  JLabel wCount = new JLabel();
  private MainMenu mainMenu;
  private SetupPane setupPane;
  private JCheckBox flipSideCheckBox = new JCheckBox("Flip Flash Cards");

  	static class WordEntry
    {
        public WordEntry(FlashCard flashCard)
        {
            this.flashCard = flashCard;
        }

        public void incrementFailures(int failures)
        {
            attempts += failures;
        }

        public int getFailures()
        {
            return attempts;
        }

        public String getSide(boolean front)
        {
            return flashCard.getSide(front);
        }

        public String toString()
        {
            return flashCard.getFront();
        }
        private FlashCard flashCard;
        private int attempts;
    }


    //Construct the frame
    public MainFrame(LessonManager lessonManager)
    {
        this.lessonManager = lessonManager;
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        mainMenu = new MainMenu(lessonManager, this );
        setJMenuBar( mainMenu );
        setupPane = new SetupPane(lessonManager);
        try
        {
            jbInit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

  //Component initialization
  private void jbInit() throws Exception  {
    contentPane = (JPanel) this.getContentPane();
    contentPane.setLayout(borderLayout1);
    this.setSize(new Dimension(400, 300));
    this.setTitle("FlashCards  - (c) 2004 CrossWire Bible Society http://crosswire.org");
    statusBar.setBorder(BorderFactory.createEtchedBorder());
    statusBar.setText(" ");
    jPanel2.setLayout(borderLayout2);
    testPanel.setLayout(borderLayout3);
    choicesPanel.setLayout(gridLayout1);
    startLessonButton.setText("Start");
    startLessonButton.addActionListener(new MainFrame_jButton1_actionAdapter(this));
    showAnswerButton.setFocusPainted(true);
    showAnswerButton.setMnemonic('A');
    showAnswerButton.setText("Show Answer");
    showAnswerButton.addActionListener(new MainFrame_jButton3_actionAdapter(this));
    jPanel1.setLayout(borderLayout6);
    wordText.setBackground(SystemColor.text);
    wordText.setFont(new java.awt.Font("Dialog", 0, 30));
    wordText.setMaximumSize(new Dimension(106, 100));
    wordText.setMinimumSize(new Dimension(106, 100));
    wordText.setPreferredSize(new Dimension(106, 100));
    wordText.setHorizontalAlignment(SwingConstants.CENTER);
    wordText.setHorizontalTextPosition(SwingConstants.CENTER);
    gridLayout1.setColumns(3);
    gridLayout1.setRows(0);
    jPanel3.setLayout(borderLayout7);
    wCount.setBorder(BorderFactory.createEtchedBorder());
    contentPane.add(jTabbedPane1,  BorderLayout.CENTER);
    jTabbedPane1.addTab("Test", testPanel);
    testPanel.add(jPanel2, BorderLayout.CENTER);
    testPanel.add(jPanel1,  BorderLayout.NORTH);
    testPanel.add(jPanel3,  BorderLayout.SOUTH);
    jPanel3.add(statusBar, BorderLayout.CENTER);
    jPanel3.add(wCount,  BorderLayout.EAST);
    jTabbedPane1.addTab("Setup", setupPane);

    jPanel2.add(choicesPanel,  BorderLayout.CENTER);
    jPanel2.add(wordText,  BorderLayout.NORTH);
    jPanel1.add(startLessonButton, BorderLayout.WEST);
    jPanel1.add(flipSideCheckBox,  BorderLayout.CENTER);
    jPanel1.add(showAnswerButton,  BorderLayout.EAST);

    jTabbedPane1.setSelectedIndex(1);
  }

  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }

  public void deleteChildren(JComponent c) {
    while (c.getComponentCount() > 0)
      c.remove(c.getComponent(0));
  }

  public void loadTest() {
//    boolean loadedFont = false;
    words = new Vector();
    Iterator lessonIter = setupPane.iterator();
    while (lessonIter.hasNext())
    {
      Lesson lesson = (Lesson) lessonIter.next();
      Iterator cardIter = lesson.iterator();
      while (cardIter.hasNext())
      {
          words.add(new WordEntry((FlashCard)cardIter.next()));
      }
//      if (!loadedFont) {
//        String font = lesson.getFont();
//        if (font.length() > 1) {
//          try {
//            loadFont(new FileInputStream(font));
//            loadedFont = true;
//          }
//          catch (FileNotFoundException ex) {
//          }
//        }
//      }

    }
  }

//  public void loadFont(InputStream is) {
//    try {
//        statusBar.setText("Loading font...");
//        statusBar.paintImmediately(statusBar.getVisibleRect());
//        Font font = Font.createFont(Font.TRUETYPE_FONT, is);
//        Font newFont = font.deriveFont((float)18.0);
//        wordText.setFont(newFont);
//        is.close();
//        statusBar.setText("New Font Loaded.");
//    }
//    catch (Exception ex) { ex.printStackTrace(); }
//  }

  void jButton1_actionPerformed(ActionEvent e) {
    loadTest();
    notLearned = (Vector)words.clone();
    totalAsked = 0;
    totalWrong = 0;
    showRandomWord(currentWord);
  }

  public void showRandomWord(WordEntry last) {
    deleteChildren(choicesPanel);
    int numToLearn = notLearned.size();
    if (numToLearn == 0)
    {
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
    wordText.setText(w.getSide(!flipSideCheckBox.isSelected()));
    Vector choices = (Vector)words.clone();
    choices.remove(w);
//    deleteChildren(choicesPanel);
    int size = choices.size();
    for (int i = 0; ((i < 9) && (size > 0)); i++) {
      int c = (int)(Math.random() * size);
      WordEntry wc = (WordEntry)choices.get(c);
      JCheckBox ck = new JCheckBox(wc.getSide(flipSideCheckBox.isSelected()), false);
      choicesPanel.add(ck, null);
      ck.addItemListener(new MainFrame_answer_itemAdapter(this));
      choices.remove(wc);
      size = choices.size();
    }
    int correct = (int)(Math.random() * choicesPanel.getComponentCount());
    JCheckBox ck = (JCheckBox)choicesPanel.getComponent(correct);
    ck.setText(w.getSide(flipSideCheckBox.isSelected()));
    wrong = 0;
    shownAnswer = false;
    updateStats();
//    this.pack();
    choicesPanel.repaint();
  }


  void updateStats() {
    int percent = 100;
    if (totalAsked > 0) {
      percent = (int)((((float)(totalAsked - totalWrong)) / (float)totalAsked) * (float)100);
    }
    wCount.setText(Integer.toString(notLearned.size())+" | "+Integer.toString(totalAsked-totalWrong)+"/"+Integer.toString(totalAsked)+" ("+Integer.toString(percent)+"%)");
  }


  void answer_itemStateChanged(ItemEvent e) {
    JCheckBox ck = (JCheckBox)e.getItem();
    if (ck.isSelected()) {
      totalAsked++;
      if (ck.getText().compareTo(currentWord.getSide(flipSideCheckBox.isSelected())) != 0) {
        statusBar.setText(ck.getText() + " is not correct.  Please try again.");
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
          else currentWord.incrementFailures(-1);
          if (currentWord.getFailures() < 0) {
            notLearned.remove(currentWord);
          }
          showRandomWord(currentWord);
        }
        else {
          notLearned.remove(currentWord);
          deleteChildren(choicesPanel);
          wordText.setText("-=+* Great! *+=-");
          statusBar.setText("Nice Job!  You've mastered all " + words.size() +
                            " words!");
        }
      }
      updateStats();
    }
  }


  public void showAnswer() {
    for (int i = 0; i < choicesPanel.getComponentCount(); i++) {
      JCheckBox ck = (JCheckBox)choicesPanel.getComponent(i);
      if (ck.getText() == currentWord.getSide(flipSideCheckBox.isSelected())) {
        ck.setFont(new Font(ck.getFont().getName(), Font.BOLD|Font.ITALIC, ck.getFont().getSize()));
        break;
      }
    }
    shownAnswer = true;
  }

  void jButton3_actionPerformed(ActionEvent e) {
    if (!shownAnswer) {
      showAnswer();
      return;
    }
    int next = notLearned.indexOf(currentWord) + 1;
    if (next == 0)
    {
        return;
    }
    if (next >= notLearned.size())
      next = 0;
    deleteChildren(choicesPanel);
    showWord((WordEntry)notLearned.get(next));
    showAnswer();
  }

}

class MainFrame_jButton1_actionAdapter implements java.awt.event.ActionListener {
  MainFrame adaptee;

  MainFrame_jButton1_actionAdapter(MainFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jButton1_actionPerformed(e);
  }
}

class MainFrame_answer_itemAdapter implements java.awt.event.ItemListener {
  MainFrame adaptee;

  MainFrame_answer_itemAdapter(MainFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void itemStateChanged(ItemEvent e) {
    adaptee.answer_itemStateChanged(e);
  }
}

class MainFrame_jButton3_actionAdapter implements java.awt.event.ActionListener {
  MainFrame adaptee;

  MainFrame_jButton3_actionAdapter(MainFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jButton3_actionPerformed(e);
  }
}
