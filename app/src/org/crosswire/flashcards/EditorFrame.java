package org.crosswire.flashcards;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

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

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class EditorFrame extends JFrame {
    JPanel contentPane;
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
    Vector words = new Vector();
    UniTextEdit wordText = new UniTextEdit();

    static class WordEntry {
        public WordEntry(String word) { this.word = word; }
        public WordEntry(String word, String answers) { this(word); this.answers = answers; }
        public String word;
        public String answers;
        public String toString() {
            return word;
        }
    }

    //Construct the frame
    public EditorFrame() {
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
        image1 = new ImageIcon(EditorFrame.class.getResource("openFile.png"));
        image2 = new ImageIcon(EditorFrame.class.getResource("closeFile.png"));
        image3 = new ImageIcon(EditorFrame.class.getResource("saveDoc.png"));
        contentPane = (JPanel) this.getContentPane();
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
        jPanel8.add(jPanel9,  BorderLayout.CENTER);
        jPanel11.add(jLabel4, null);
        jPanel11.add(fileName, null);
        jPanel9.add(jPanel11, null);
        jPanel9.add(jPanel10, null);
        jPanel10.add(jLabel3, null);
        jPanel10.add(lessonTitle, null);
        jPanel8.add(jPanel3,  BorderLayout.NORTH);
        contentPane.add(jPanel5, BorderLayout.CENTER);
        jPanel5.add(jPanel6,  BorderLayout.SOUTH);
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
        jPanel1.add(jPanel2,  BorderLayout.NORTH);
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
        words = new Vector();
        words.add(new WordEntry(" "));
        showLesson();
    }


    ////////////////////////////////////////////
    //
    private void loadLesson( String lessonFileName ) {

        lesson = new Properties( );

        try {

            lesson.load( new FileInputStream( lessonFileName ) );

        } catch( Exception e ) {

            e.printStackTrace( );

        }

        words = new Vector( );
        int wordCount = Integer.parseInt( lesson.getProperty( "wordCount" ) );

        for( int i = 0; i < wordCount; ++ i ) {

            words.add( new WordEntry( lesson.getProperty( "word" + Integer.toString( i ) ),
                                      lesson.getProperty( "answers" + Integer.toString( i ) ) ) );

        }

        showLesson();

    }


    private void saveLesson() {
        try {
            lesson.setProperty("wordCount", Integer.toString(words.size()));
            for (int i = 0; i < words.size(); i++) {
                WordEntry we = (WordEntry) words.get(i);
                lesson.setProperty("word"+Integer.toString(i), we.word);
                lesson.setProperty("answers"+Integer.toString(i), we.answers);
            }
            lesson.store(new FileOutputStream(lesson.getProperty("fileName")),
                         "Flash Lesson");
        } catch (IOException ex) { ex.printStackTrace(); }
    }

    ////////////////////////////
    private void showLesson( ) {

        setTitle( "FlashCards Editor (c) CrossWire Bible Society http://crosswire.org - " +
                  lesson.getProperty( "fileName" ) );
        wordList.setListData( words );
        wordList.setSelectedIndex( 0 );
        wordList.addListSelectionListener( new EditorFrame_wordList_listSelectionAdapter( this ) );
        String fName = lesson.getProperty( "fileName" );
        fName = fName.substring( 0, fName.indexOf( ".flash" ) );
        fileName.setText( fName );
        lessonTitle.setText( lesson.getProperty( "lessonTitle" ) );

        // If a font was specified, load it.
        if( null != lesson.getProperty( "font" ) ) {

            fontPath.setText( lesson.getProperty( "font" ) );

            try {

                wordText.loadFont( new FileInputStream( fontPath.getText( ) ) );

            } catch( Exception exception ) {

                exception.printStackTrace( );

            }

        }

    }

    //File | Exit action performed
    public void jMenuFileExit_actionPerformed(ActionEvent e) {
        System.exit(0);
    }

    //Help | About action performed
    public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
        EditorFrame_AboutBox dlg = new EditorFrame_AboutBox(this);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = getSize();
        Point loc = getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.pack();
        dlg.show();
    }
    //Overridden so we can exit when window is closed
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            System.exit(0);
        }
    }

    void jButton1_actionPerformed(ActionEvent e) {
        JFileChooser dialog = new JFileChooser();
        dialog.setFileFilter(new FileFilter() {
                public boolean accept(File f) {
                    if (f.isDirectory()) return true;
                    else if (f.getName().endsWith(".flash")) return true;
                    return false;
                }
                public String getDescription() { return "Flash Card Lessons"; }
            });
        dialog.setCurrentDirectory(new File("./"));
        if (dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                loadLesson(dialog.getSelectedFile().getCanonicalPath());
            } catch( IOException ioe ) {
                ioe.printStackTrace( );
            }
        }
    }

    void jButton2_actionPerformed(ActionEvent e) {
        newLesson();
    }

    void jButton6_actionPerformed(ActionEvent e) {
        JFileChooser dialog = new JFileChooser();
        dialog.setFileFilter(new FileFilter() {
                public boolean accept(File f) {
                    if (f.isDirectory()) return true;
                    else if (f.getName().endsWith(".ttf")) return true;
                    return false;
                }
                public String getDescription() { return "TrueType Font"; }
            });
        dialog.setCurrentDirectory(new File("./"));
        if (dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            lesson.setProperty("font", dialog.getSelectedFile().getName());
            fontPath.setText(lesson.getProperty("font"));
            try {
                wordText.loadFont(new FileInputStream(fontPath.getText()));
            }
            catch (FileNotFoundException ex) { ex.printStackTrace(); }
        }

    }

    void addButton_actionPerformed(ActionEvent e) {
        words.add(new WordEntry(" "));
        wordList.setListData(words);
        wordList.setSelectedIndex(words.size()-1);
        wordText.requestFocus();
        wordText.setText("");
    }

    void updateWordList() {
        int currentWord = wordList.getSelectedIndex();
        wordList.setListData(words);
        wordList.setSelectedIndex(currentWord);

    }

    void wordText_caretUpdate(CaretEvent e) {
        int currentWord = wordList.getSelectedIndex();
        ((WordEntry)words.get(currentWord)).word = wordText.getText();
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
        if ((currentWord > -1)&&(currentWord != lastSelection)) {
            lastSelection = currentWord;
            WordEntry we = (WordEntry)words.get(currentWord);
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
        ((WordEntry)words.get(currentWord)).answers = answers.getText();
        //    updateWordList();
    }

    void fileName_caretUpdate(CaretEvent e) {
        lesson.setProperty("fileName", fileName.getText()+".flash");
        setTitle("FlashCard Editor (c) CrossWire Bible Society http://crosswire.org - " + lesson.getProperty("fileName"));
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
