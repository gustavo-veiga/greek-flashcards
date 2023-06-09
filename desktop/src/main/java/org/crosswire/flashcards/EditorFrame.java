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
///////////////////////////////////////////////////////////////////////////
//
// EditorFrame.java
//
// Editor for lessons used by Quiz (part of FlashCards).
//
// Copyright : (c) 2004 CrossWire Bible Society http://crosswire.org
//
///////////////////////////////////////////////////////////////////////////

package org.crosswire.flashcards;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.CaretEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileFilter;

import org.crosswire.modedit.UniTextEdit;

public class EditorFrame extends JFrame {

    //
    // Attributes
    //
    String cwdPath = "./";

    Container contentPane;
    JToolBar jToolBar = new JToolBar();
    JButton jButton1 = new JButton();
    JButton jButton2 = new JButton();
    JButton jButton3 = new JButton();
    Properties lesson = new Properties();
    ImageIcon image1;
    ImageIcon image2;
    ImageIcon image3;
    JLabel statusBar = new JLabel();
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanel1 = new JPanel();
    JPanel jPanel2 = new JPanel();
    BorderLayout borderLayout2 = new BorderLayout();
    JButton deleteButton = new JButton();
    JButton addButton = new JButton();
    JList wordList = new JList();
    JLabel fontPath = new JLabel();
    JButton jButton6 = new JButton();
    JPanel jPanel3 = new JPanel();
    BorderLayout borderLayout3 = new BorderLayout();
    JPanel jPanel4 = new JPanel();
    FlowLayout flowLayout1 = new FlowLayout();
    JLabel jLabel2 = new JLabel();
    JPanel jPanel5 = new JPanel();
    JPanel jPanel6 = new JPanel();
    BorderLayout borderLayout4 = new BorderLayout();
    JLabel jLabel1 = new JLabel();
    GridLayout gridLayout1 = new GridLayout();
    JTextField answers = new JTextField();
    JPanel jPanel7 = new JPanel();
    BorderLayout borderLayout6 = new BorderLayout();
    List words = new ArrayList();
    UniTextEdit wordText = new UniTextEdit();
    private boolean standAlone;

    /**
     * Serialization ID
     */
    private static final long serialVersionUID = -1544644199693526132L;

    static class WordEntry {
        public WordEntry(String word) {
            this.word = word;
        }

        public WordEntry(String word, String answers) {
            this(word);
            this.answers = answers;
        }

        public String word = "";
        public String answers = "";

        public String toString() {
            return word;
        }
    }

    //
    // Methods
    //

    // ---------------
    public EditorFrame(boolean standAlone) {
        this.standAlone = standAlone;
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);

        jbInit();
    }

    // Component initialization
    private void jbInit() {
        image1 = new ImageIcon(EditorFrame.class.getResource("openFile.png"));
        image2 = new ImageIcon(EditorFrame.class.getResource("closeFile.png"));
        image3 = new ImageIcon(EditorFrame.class.getResource("saveDoc.png"));
        contentPane = this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setSize(new Dimension(512, 300));
        this.setTitle("Editor");
        statusBar.setText(" ");
        jButton1.setIcon(image1);
        jButton1.addActionListener(new EditorFrame_jButton1_actionAdapter(this));
        jButton1.setToolTipText("Open Lesson");
        jButton2.setIcon(image2);
        jButton2.addActionListener(new EditorFrame_jButton2_actionAdapter(this));
        jButton2.setToolTipText("New Lesson");
        jButton3.setIcon(image3);
        jButton3.addActionListener(new EditorFrame_jButton3_actionAdapter(this));
        jButton3.setToolTipText("Save Lesson");
        jPanel1.setLayout(borderLayout2);
        deleteButton.setToolTipText("");
        deleteButton.setText("Delete");
        deleteButton.addActionListener(new EditorFrame_deleteButton_actionAdapter(this));
        addButton.setMnemonic('A');
        addButton.setSelected(false);
        addButton.setText("Add");
        addButton.addActionListener(new EditorFrame_addButton_actionAdapter(this));
        fontPath.setText("[default]");
        jButton6.setText("...");
        jButton6.addActionListener(new EditorFrame_jButton6_actionAdapter(this));
        jPanel3.setLayout(borderLayout3);
        jPanel4.setLayout(flowLayout1);
        jLabel2.setPreferredSize(new Dimension(31, 15));
        jLabel2.setText("Font:");
        jPanel5.setLayout(borderLayout4);
        jLabel1.setText("Answer");
        jPanel6.setLayout(gridLayout1);
        gridLayout1.setRows(2);
        gridLayout1.setVgap(0);
        answers.setSelectionStart(0);
        answers.setText("");
        answers.addCaretListener(new EditorFrame_answers_caretAdapter(this));
        answers.addActionListener(new EditorFrame_answers_actionAdapter(this));
        wordText.setText("");
        jPanel7.setLayout(borderLayout6);
        borderLayout1.setHgap(1);
        borderLayout1.setVgap(1);
        borderLayout2.setHgap(1);
        borderLayout2.setVgap(1);
        borderLayout3.setHgap(1);
        borderLayout3.setVgap(1);
        borderLayout4.setHgap(1);
        borderLayout4.setVgap(1);
        borderLayout6.setHgap(1);
        borderLayout6.setVgap(1);
        wordText.setText("");
        wordText.addCaretListener(new EditorFrame_wordText_caretAdapter(this));
        jPanel8.setLayout(borderLayout5);
        jLabel3.setText("Lesson Title");
        jLabel4.setText("File Name");
        jPanel9.setLayout(flowLayout2);
        flowLayout2.setAlignment(FlowLayout.LEFT);
        flowLayout2.setHgap(5);
        lessonTitle.setMaximumSize(new Dimension(2147483647, 2147483647));
        lessonTitle.setMinimumSize(new Dimension(150, 19));
        lessonTitle.setPreferredSize(new Dimension(150, 19));
        lessonTitle.setText("");
        lessonTitle.addCaretListener(new EditorFrame_lessonTitle_caretAdapter(this));
        fileName.setMinimumSize(new Dimension(150, 19));
        fileName.setPreferredSize(new Dimension(150, 19));
        fileName.setText("");
        fileName.addCaretListener(new EditorFrame_fileName_caretAdapter(this));
        jScrollPane1.setPreferredSize(new Dimension(200, 131));
        jToolBar.add(jButton2);
        jToolBar.add(jButton1);
        jToolBar.add(jButton3);
        contentPane.add(jPanel8, BorderLayout.NORTH);
        jPanel8.add(jPanel9, BorderLayout.CENTER);
        jPanel11.add(jLabel4, null);
        jPanel11.add(fileName, null);
        jPanel9.add(jPanel11, null);
        jPanel9.add(jPanel10, null);
        jPanel10.add(jLabel3, null);
        jPanel10.add(lessonTitle, null);
        jPanel8.add(jPanel3, BorderLayout.NORTH);
        contentPane.add(jPanel5, BorderLayout.CENTER);
        jPanel5.add(jPanel6, BorderLayout.SOUTH);
        jPanel6.add(jLabel1, null);
        jPanel6.add(answers, null);
        jPanel5.add(jPanel7, BorderLayout.CENTER);
        jPanel7.add(wordText, BorderLayout.CENTER);
        jPanel4.add(jLabel2, null);
        jPanel4.add(fontPath, null);
        jPanel4.add(jButton6, null);
        jPanel3.add(jToolBar, BorderLayout.WEST);
        contentPane.add(statusBar, BorderLayout.SOUTH);
        contentPane.add(jPanel1, BorderLayout.WEST);
        jPanel1.add(jPanel2, BorderLayout.NORTH);
        jPanel2.add(addButton, null);
        jPanel2.add(deleteButton, null);
        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
        jScrollPane1.getViewport().add(wordList, null);
        jPanel3.add(jPanel4, BorderLayout.EAST);
        newLesson();
        wordText.showIMSelect(true);
        wordText.setComponentOrientation(ComponentOrientation.UNKNOWN);
        wordText.setFontSize(30);
    }

    private void newLesson() {
        lesson = new Properties();
        lesson.setProperty("fileName", "NewLesson.flash");
        lesson.setProperty("lessonTitle", "New Lesson");
        lesson.setProperty("wordCount", "0");
        words = new ArrayList();
        words.add(new WordEntry(" "));
        showLesson();
    }

    ////////////////////////////////////////////
    //
    private void loadLesson(String lessonFileName) {

        lesson = new Properties();

        InputStream inStream = null;
        try {
            inStream = new FileInputStream(lessonFileName);
            lesson.load(inStream);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        words = new ArrayList();
        int wordCount = Integer.parseInt(lesson.getProperty("wordCount"));

        for (int i = 0; i < wordCount; ++i) {

            words.add(new WordEntry(lesson.getProperty("word" + Integer.toString(i)),
                    lesson.getProperty("answers" + Integer.toString(i))));

        }

        showLesson();

    }

    private void saveLesson() {
        OutputStream outStream = null;
        try {
            lesson.setProperty("wordCount", Integer.toString(words.size()));
            for (int i = 0; i < words.size(); i++) {
                WordEntry we = (WordEntry) words.get(i);
                lesson.setProperty("word" + Integer.toString(i), we.word);
                lesson.setProperty("answers" + Integer.toString(i), we.answers);
            }
            outStream = new FileOutputStream(cwdPath + "/" + lesson.getProperty("fileName"));
            lesson.store(outStream, "Flash Lesson");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    ////////////////////////////
    private void showLesson() {

        setTitle("FlashCards Editor (c) CrossWire Bible Society http://crosswire.org - " +
                lesson.getProperty("fileName"));
        wordList.setListData(words.toArray());
        wordList.setSelectedIndex(0);
        wordList.addListSelectionListener(new EditorFrame_wordList_listSelectionAdapter(this));
        String fName = lesson.getProperty("fileName");
        fName = fName.substring(0, fName.indexOf(".flash"));
        fileName.setText(fName);
        lessonTitle.setText(lesson.getProperty("lessonTitle"));

        // If a font was specified, load it.
        if (null != lesson.getProperty("font")) {

            fontPath.setText(lesson.getProperty("font"));

            try {

                wordText.loadFont(new FileInputStream(fontPath.getText()));

            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }

    }

    // File | Exit action performed

    public void jMenuFileExit_actionPerformed(ActionEvent event) {

        if (standAlone) {
            System.exit(0);
        } else {
            dispose();
        }

    }

    // Help | About action performed
    public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
        EditorFrame_AboutBox dlg = new EditorFrame_AboutBox(this);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = getSize();
        Point loc = getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
                (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.pack();
        dlg.setVisible(true);
    }

    // Overridden so we can exit when window is closed

    protected void processWindowEvent(WindowEvent event) {

        super.processWindowEvent(event);

        if (event.getID() == WindowEvent.WINDOW_CLOSING) {

            if (standAlone) {
                System.exit(0);
            } else {
                dispose();
            }

        }

    }

    void jButton1_actionPerformed(ActionEvent e) {
        JFileChooser dialog = new JFileChooser();
        dialog.setFileFilter(new FlashFileFilter());
        dialog.setCurrentDirectory(new File(cwdPath));
        if (dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                loadLesson(dialog.getSelectedFile().getCanonicalPath());
                cwdPath = dialog.getCurrentDirectory().getCanonicalPath();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    void jButton2_actionPerformed(ActionEvent e) {
        newLesson();
    }

    void jButton6_actionPerformed(ActionEvent e) {
        JFileChooser dialog = new JFileChooser();
        dialog.setFileFilter(new TTFFileFilter());
        dialog.setCurrentDirectory(new File("./"));
        if (dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            lesson.setProperty("font", dialog.getSelectedFile().getName());
            fontPath.setText(lesson.getProperty("font"));
            InputStream inStream = null;
            try {
                inStream = new FileInputStream(fontPath.getText());
                wordText.loadFont(inStream);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }

    }

    void addButton_actionPerformed(ActionEvent e) {
        words.add(new WordEntry(" "));
        wordList.setListData(words.toArray());
        wordList.setSelectedIndex(words.size() - 1);
        wordText.requestFocus();
        wordText.setText("");
    }

    void updateWordList() {
        int currentWord = wordList.getSelectedIndex();
        wordList.setListData(words.toArray());
        wordList.setSelectedIndex(currentWord);

    }

    void wordText_caretUpdate(CaretEvent e) {
        int currentWord = wordList.getSelectedIndex();
        ((WordEntry) words.get(currentWord)).word = wordText.getText();
        updateWordList();
    }

    int lastSelection = -1;
    JPanel jPanel8 = new JPanel();
    JPanel jPanel9 = new JPanel();
    BorderLayout borderLayout5 = new BorderLayout();
    JPanel jPanel10 = new JPanel();
    JLabel jLabel3 = new JLabel();
    JTextField lessonTitle = new JTextField();
    JPanel jPanel11 = new JPanel();
    JTextField fileName = new JTextField();
    JLabel jLabel4 = new JLabel();
    FlowLayout flowLayout2 = new FlowLayout();
    JScrollPane jScrollPane1 = new JScrollPane();

    void wordList_valueChanged(ListSelectionEvent e) {
        int currentWord = wordList.getSelectedIndex();
        if ((currentWord > -1) && (currentWord != lastSelection)) {
            lastSelection = currentWord;
            WordEntry we = (WordEntry) words.get(currentWord);
            wordText.setText(we.word);
            answers.setText(we.answers);
        }
    }

    void answers_actionPerformed(ActionEvent e) {

    }

    void jButton3_actionPerformed(ActionEvent e) {
        saveLesson();
    }

    void answers_caretUpdate(CaretEvent e) {
        int currentWord = wordList.getSelectedIndex();
        ((WordEntry) words.get(currentWord)).answers = answers.getText();
        // updateWordList();
    }

    void fileName_caretUpdate(CaretEvent e) {
        lesson.setProperty("fileName", fileName.getText() + ".flash");
        setTitle("FlashCard Editor (c) CrossWire Bible Society http://crosswire.org - " +
                lesson.getProperty("fileName"));
    }

    void lessonTitle_caretUpdate(CaretEvent e) {
        lesson.setProperty("lessonTitle", lessonTitle.getText());
    }

    void deleteButton_actionPerformed(ActionEvent e) {
        int item = wordList.getSelectedIndex();
        if (item > -1) {
            words.remove(item);
            updateWordList();
        }
    }

    static class FlashFileFilter extends FileFilter {
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().endsWith(".flash");
        }

        public String getDescription() {
            return "Flash Card Lessons";
        }
    }

    static class TTFFileFilter extends FileFilter {
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().endsWith(".ttf");
        }

        public String getDescription() {
            return "TrueType Font";
        }
    }
}

class EditorFrame_jButton1_actionAdapter implements java.awt.event.ActionListener {
    EditorFrame adaptee;

    EditorFrame_jButton1_actionAdapter(EditorFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jButton1_actionPerformed(e);
    }
}

class EditorFrame_jButton2_actionAdapter implements java.awt.event.ActionListener {
    EditorFrame adaptee;

    EditorFrame_jButton2_actionAdapter(EditorFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jButton2_actionPerformed(e);
    }
}

class EditorFrame_jButton6_actionAdapter implements java.awt.event.ActionListener {
    EditorFrame adaptee;

    EditorFrame_jButton6_actionAdapter(EditorFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jButton6_actionPerformed(e);
    }
}

class EditorFrame_addButton_actionAdapter implements java.awt.event.ActionListener {
    EditorFrame adaptee;

    EditorFrame_addButton_actionAdapter(EditorFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.addButton_actionPerformed(e);
    }
}

class EditorFrame_wordText_caretAdapter implements javax.swing.event.CaretListener {
    EditorFrame adaptee;

    EditorFrame_wordText_caretAdapter(EditorFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void caretUpdate(CaretEvent e) {
        adaptee.wordText_caretUpdate(e);
    }
}

class EditorFrame_wordList_listSelectionAdapter implements javax.swing.event.ListSelectionListener {
    EditorFrame adaptee;

    EditorFrame_wordList_listSelectionAdapter(EditorFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void valueChanged(ListSelectionEvent e) {
        adaptee.wordList_valueChanged(e);
    }
}

class EditorFrame_answers_actionAdapter implements java.awt.event.ActionListener {
    EditorFrame adaptee;

    EditorFrame_answers_actionAdapter(EditorFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.answers_actionPerformed(e);
    }
}

class EditorFrame_jButton3_actionAdapter implements java.awt.event.ActionListener {
    EditorFrame adaptee;

    EditorFrame_jButton3_actionAdapter(EditorFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jButton3_actionPerformed(e);
    }
}

class EditorFrame_answers_caretAdapter implements javax.swing.event.CaretListener {
    EditorFrame adaptee;

    EditorFrame_answers_caretAdapter(EditorFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void caretUpdate(CaretEvent e) {
        adaptee.answers_caretUpdate(e);
    }
}

class EditorFrame_fileName_caretAdapter implements javax.swing.event.CaretListener {
    EditorFrame adaptee;

    EditorFrame_fileName_caretAdapter(EditorFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void caretUpdate(CaretEvent e) {
        adaptee.fileName_caretUpdate(e);
    }
}

class EditorFrame_lessonTitle_caretAdapter implements javax.swing.event.CaretListener {
    EditorFrame adaptee;

    EditorFrame_lessonTitle_caretAdapter(EditorFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void caretUpdate(CaretEvent e) {
        adaptee.lessonTitle_caretUpdate(e);
    }
}

class EditorFrame_deleteButton_actionAdapter implements java.awt.event.ActionListener {
    EditorFrame adaptee;

    EditorFrame_deleteButton_actionAdapter(EditorFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.deleteButton_actionPerformed(e);
    }
}
