package org.crosswire.flashcards;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;
import java.util.Properties;
import java.io.FileInputStream;
import java.util.Hashtable;
import java.io.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class MainFrame extends JFrame {
  JPanel contentPane;
  JLabel statusBar = new JLabel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel choicesPanel = new JPanel();
  JTabbedPane jTabbedPane1 = new JTabbedPane();
  JPanel jPanel4 = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JPanel setupPanel = new JPanel();
  JPanel jPanel6 = new JPanel();
  BorderLayout borderLayout4 = new BorderLayout();
  JLabel jLabel2 = new JLabel();
  JLabel lessonDirectory = new JLabel();
  JButton jButton2 = new JButton();
  BorderLayout borderLayout5 = new BorderLayout();
  JPanel lessonPanel = new JPanel();
  JButton jButton1 = new JButton();
  Vector words = new Vector();
  Vector notLearned = new Vector();
  WordEntry currentWord = null;
  int wrong = 0;
  int totalAsked = 0;
  int totalWrong = 0;
  JButton jButton3 = new JButton();
  BorderLayout borderLayout6 = new BorderLayout();
  boolean shownAnswer = false;
  JLabel wordText = new JLabel();
  Hashtable lessons = new Hashtable();
  GridLayout gridLayout1 = new GridLayout();
  GridLayout gridLayout2 = new GridLayout();
  JPanel jPanel3 = new JPanel();
  BorderLayout borderLayout7 = new BorderLayout();
  JLabel wCount = new JLabel();

  static class WordEntry {
    public WordEntry(String word) { this.word = word; }
    public WordEntry(String word, String answers) { this(word); this.answers = answers; }
    public String word;
    public String answers;
    public int attempts = 0;
    public String toString() {
      return word;
    }
  }


  //Construct the frame
  public MainFrame() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
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
    jPanel4.setLayout(borderLayout3);
    setupPanel.setLayout(borderLayout4);
    jLabel2.setMaximumSize(new Dimension(140, 14));
    jLabel2.setMinimumSize(new Dimension(140, 14));
    jLabel2.setPreferredSize(new Dimension(140, 14));
    jLabel2.setText("Lesson Directory");
    jButton2.setText("...");
    jButton2.addActionListener(new MainFrame_jButton2_actionAdapter(this));
    jPanel6.setLayout(borderLayout5);
    lessonDirectory.setText("");
    lessonPanel.setLayout(gridLayout2);
    choicesPanel.setLayout(gridLayout1);
    jButton1.setText("Start");
    jButton1.addActionListener(new MainFrame_jButton1_actionAdapter(this));
    jButton3.setFocusPainted(true);
    jButton3.setMnemonic('A');
    jButton3.setText("Show Answer");
    jButton3.addActionListener(new MainFrame_jButton3_actionAdapter(this));
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
    gridLayout2.setColumns(2);
    gridLayout2.setRows(0);
    jPanel3.setLayout(borderLayout7);
    wCount.setBorder(BorderFactory.createEtchedBorder());
    contentPane.add(jTabbedPane1,  BorderLayout.CENTER);
    jTabbedPane1.add(jPanel4,  "Test");
    jPanel4.add(jPanel2, BorderLayout.CENTER);
    jPanel4.add(jPanel1,  BorderLayout.NORTH);
    jPanel4.add(jPanel3,  BorderLayout.SOUTH);
    jPanel3.add(statusBar, BorderLayout.CENTER);
    jPanel3.add(wCount,  BorderLayout.EAST);
    jTabbedPane1.add(setupPanel,  "Setup");
    setupPanel.add(jPanel6, BorderLayout.NORTH);
    jPanel6.add(jLabel2, BorderLayout.WEST);
    jPanel6.add(lessonDirectory, BorderLayout.CENTER);
    jPanel6.add(jButton2, BorderLayout.EAST);
    setupPanel.add(lessonPanel, BorderLayout.CENTER);
    jPanel2.add(choicesPanel,  BorderLayout.CENTER);
    jPanel2.add(wordText,  BorderLayout.NORTH);
    jPanel1.add(jButton1, BorderLayout.WEST);
    jPanel1.add(jButton3,  BorderLayout.EAST);


    loadLessonDirectory("./");
    jTabbedPane1.setSelectedComponent(setupPanel);
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
  public void loadLessonDirectory(String directoryPath) {
    deleteChildren(lessonPanel);
    File d = new File(directoryPath);
    File[] children = d.listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return (name.endsWith(".flash"));
      }
    });
    java.util.Arrays.sort(children);
    lessons = new Hashtable();
    for (int i = 0; i < children.length; i++) {
      Properties p = new Properties();
      try {
        p.load(new FileInputStream(children[i].getAbsolutePath()));
      }
      catch (IOException ex) {
      }
      JCheckBox ck = new JCheckBox(p.getProperty("lessonTitle"), false);
      lessonPanel.add(ck, null);
      lessons.put(ck, children[i].getAbsolutePath());
    }
    lessonDirectory.setText(directoryPath);
    this.pack();
  }

  void jButton2_actionPerformed(ActionEvent e) {
    JFileChooser dialog = new JFileChooser();
    dialog.setCurrentDirectory(new File("./"));
    if (dialog.showOpenDialog(this) == dialog.APPROVE_OPTION) {
      lessonDirectory.setText(dialog.getSelectedFile().getParentFile().getAbsolutePath());
      loadLessonDirectory(lessonDirectory.getText());
    }
  }

  public void loadLessons() {
    boolean loadedFont = false;
    words = new Vector();
    for (int l = 0; l < lessonPanel.getComponentCount(); l++) {
      Properties lesson = new Properties();
      JCheckBox ck = (JCheckBox)lessonPanel.getComponent(l);
      if (!ck.isSelected())
        continue;
      try {
        String fullPath = (String)lessons.get(ck);
        lesson.load(new FileInputStream(fullPath));
      }
      catch (Exception e1) {
        e1.printStackTrace();
      }
      int wordCount = Integer.parseInt(lesson.getProperty("wordCount"));
      for (int i = 0; i < wordCount; i++) {
        words.add(new WordEntry(lesson.getProperty("word" + Integer.toString(i)),
                                lesson.getProperty("answers" +
            Integer.toString(i))));
      }
      if (!loadedFont) {
        String font = lesson.getProperty("font", "");
        if (font.length() > 1) {
          try {
            loadFont(new FileInputStream(font));
            loadedFont = true;
          }
          catch (FileNotFoundException ex) {
          }
        }
      }

    }
  }


  public void loadFont(InputStream is) {
    try {
        statusBar.setText("Loading font...");
        statusBar.paintImmediately(statusBar.getVisibleRect());
        Font font = Font.createFont(Font.TRUETYPE_FONT, is);
        Font newFont = font.deriveFont((float)18.0);
        wordText.setFont(newFont);
        is.close();
        statusBar.setText("New Font Loaded.");
    }
    catch (Exception ex) { ex.printStackTrace(); }
  }




  void jButton1_actionPerformed(ActionEvent e) {
    loadLessons();
    notLearned = (Vector)words.clone();
    totalAsked = 0;
    totalWrong = 0;
    showRandomWord(currentWord);
  }


  public void showRandomWord(WordEntry last) {
    while (currentWord == last) {
      int wordNum = (int) (Math.random() * notLearned.size());
      currentWord = (WordEntry) notLearned.get(wordNum);
    }
    showWord(currentWord);
  }


  public void showWord(WordEntry w) {
    currentWord = w;
    wordText.setText(w.word);
    Vector choices = (Vector)words.clone();
    choices.remove(w);
    deleteChildren(choicesPanel);
    int size = choices.size();
    for (int i = 0; ((i < 9) && (size > 0)); i++) {
      int c = (int)(Math.random() * size);
      WordEntry wc = (WordEntry)choices.get(c);
      JCheckBox ck = new JCheckBox(wc.answers, false);
      choicesPanel.add(ck, null);
      ck.addItemListener(new MainFrame_answer_itemAdapter(this));
      choices.remove(wc);
      size = choices.size();
    }
    int correct = (int)(Math.random() * choicesPanel.getComponentCount());
    JCheckBox ck = (JCheckBox)choicesPanel.getComponent(correct);
    ck.setText(w.answers);
    wrong = 0;
    shownAnswer = false;
    updateStats();
    this.pack();
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
      if (ck.getText().compareTo(currentWord.answers) != 0) {
        statusBar.setText(ck.getText() + " is not correct.  Please try again.");
        wrong++;
        totalWrong++;
        ck.setSelected(false);
      }
      else {
        if (notLearned.size() > 1) {
          statusBar.setText("Correct.  Try this next word");
          if (wrong > 0) {
            currentWord.attempts += wrong;
          }
          else currentWord.attempts--;
          if (currentWord.attempts < 0) {
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
      if (ck.getText() == currentWord.answers) {
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
    if (next >= notLearned.size())
      next = 0;
    showWord((WordEntry)notLearned.get(next));
    showAnswer();
  }
}




class MainFrame_jButton2_actionAdapter implements java.awt.event.ActionListener {
  MainFrame adaptee;

  MainFrame_jButton2_actionAdapter(MainFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jButton2_actionPerformed(e);
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
