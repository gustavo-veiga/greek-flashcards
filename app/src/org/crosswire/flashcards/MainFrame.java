package org.crosswire.flashcards;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import org.crosswire.common.CWClassLoader;
import org.crosswire.common.ResourceUtil;

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
  Map lessonSets = new LinkedHashMap();
  private static final String LESSON_ROOT = "lessons";
  private static final String DIR_PROJECT = ".flashcards";
  private static final String FILE_PROTOCOL = "file";
  private MainMenu mainMenu;

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
    public MainFrame()
    {
        try
        {
            String path = System.getProperty("user.home") + File.separator + DIR_PROJECT; //$NON-NLS-1$
            URL home = new URL(FILE_PROTOCOL, null, path);
            CWClassLoader.setHome(home);
        }
        catch (MalformedURLException e1)
        {
            assert false;
        }

        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try
        {
            jbInit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        mainMenu = new MainMenu( this );
        setJMenuBar( mainMenu );
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


    loadLessons(LESSON_ROOT);
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

  public void loadLessons(String directoryPath) {
      deleteChildren(lessonPanel);
      lessonSets.clear();
      loadJarLessons(directoryPath);
      loadHomeLessons(directoryPath);
      loadWidgets();
  }
  
  private void loadJarLessons(String directoryPath)
  {

      // Dig into the jar for lessons
      URL lessonsURL = this.getClass().getResource('/' + directoryPath);
      URLConnection connection = null;
      try
      {
          connection = lessonsURL.openConnection();
      }
      catch (Exception e1)
      {
          assert false;
      }
      if (connection instanceof JarURLConnection)
      {
          JarURLConnection jarConnection = (JarURLConnection) connection;
          JarFile jarFile = null;
          try
          {
              jarFile = jarConnection.getJarFile();
          }
          catch (IOException e2)
          {
              assert false;
          }
          Enumeration entries = jarFile.entries();
          Set lessonSet = null;
          while (entries.hasMoreElements())
          {
              JarEntry jarEntry = (JarEntry) entries.nextElement();
              String entryName = jarEntry.getName();
              if (entryName.startsWith(directoryPath))
              {
                  if (jarEntry.isDirectory())
                  {
                      lessonSet = new TreeSet();
                      // remove trailing '/'
                      lessonSets.put(entryName.substring(0, entryName.length() - 1), lessonSet);
                  }
                  else
                  {
                      lessonSet.add(entryName);
                  }
              }
          }
      }
  }
  
  private void loadHomeLessons(String directoryPath)
  {
    try {
      List files = new ArrayList();
      File childFile = new File(directoryPath);
      directoryPath = childFile.getParent();
      getFileListing(new File(directoryPath), files);
      Collections.sort(files);
      Iterator iter = files.iterator();
      Set lessonSet = null;
      while (iter.hasNext())
      {
          File file = (File) iter.next();
          File parent = file.getParentFile();
          String parentPath = parent.getPath().replace('\\', '/');
          if (lessonSets.containsKey(parentPath))
          {
              lessonSet = (Set) lessonSets.get(parentPath);
          }
          else
          {
              lessonSet = new TreeSet();
          }
          String filePath = file.getPath().replace('\\', '/');
          lessonSet.add(filePath);
      }
    }
    catch (Exception e) {
	// that's fine.  We just failed to load local files.
    }
  }

  private void loadWidgets()
  {
      lessons = new Hashtable();
      Iterator iter = lessonSets.values().iterator();
      while (iter.hasNext())
      {
          Set lessonSet = (Set) iter.next();
          Iterator lessonIterator = lessonSet.iterator();
          while (lessonIterator.hasNext())
          {
            String lessonName = (String) lessonIterator.next();
            URL lessonURL = ResourceUtil.getResource(lessonName);
            Properties p = new Properties();
            try {
                p.load(lessonURL.openConnection().getInputStream());
            }
            catch (IOException ex) {
            }
          JCheckBox ck = new JCheckBox(p.getProperty("lessonTitle"), false);
          lessonPanel.add(ck, null);
          lessons.put(ck, lessonURL);
          }
      }
/*
 * This code (or something like it) is for the future and shows how to use ResourceBundles.
 * The extension on the files needs to be changed from .flash to .properties
 * and the name given to getBundle is not to contain .properties.
 */
//      Locale defaultLocale = Locale.getDefault();
//      while (iter.hasNext())
//      {
//          lessonSet = (Set) iter.next();
//          Iterator lessonIterator = lessonSet.iterator();
//          while (lessonIterator.hasNext())
//          {
//              String lessonName = (String) lessonIterator.next();
//              try
//              {
//                  ResourceBundle resources = ResourceBundle.getBundle(lessonName, defaultLocale, new CWClassLoader());
//                  JCheckBox ck = new JCheckBox(resources.getString("lessonTitle"), false);
//                  lessonPanel.add(ck, null);
//                  lessons.put(ck, resources);
//              }
//              catch (MissingResourceException ex)
//              {
//                  System.err.println("Cannot get resource " + lessonName + ":lessonTitle"); //$NON-NLS-1$ //$NON-NLS-2$
//              }
//          }
//      }
      this.pack();
  }
  /**
   * Recursively walk a directory tree and return a List of all
   * Files found; the List is sorted using File.compareTo.
   *
   * @param aStartingDir is a valid directory, which can be read.
   */
  public void getFileListing(File aStartingDir, List result) {
       // look for files that are either directories or end with .flash
      File[] filesAndDirs = aStartingDir.listFiles(new FilenameFilter() {
          public boolean accept(File dir, String name) {
              if (name.endsWith(".flash"))
              {
                  return true;
              }
              File testFile = new File(dir.getName() + File.separator + name);
              return testFile.isDirectory();
          }
      });
      if (filesAndDirs == null)
      {
          return;
      }
      for (int i = 0; i < filesAndDirs.length; i++)
      {
          File file = filesAndDirs[i];
          if (file.isDirectory()) {
              // dig deeper
              getFileListing(file, result);
          } else {
              // Add only files
              result.add(file);
          }
      }
  }

  void jButton2_actionPerformed(ActionEvent e) {
    JFileChooser dialog = new JFileChooser();
    dialog.setCurrentDirectory(new File("./"));
    if (dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      lessonDirectory.setText(dialog.getSelectedFile().getParentFile().getAbsolutePath());
      loadLessons(lessonDirectory.getText());
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
        URL lessonURL = (URL)lessons.get(ck);
        lesson.load(lessonURL.openConnection().getInputStream());
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
