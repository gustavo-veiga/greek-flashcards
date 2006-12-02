package org.crosswire.modedit;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class UniTextEdit extends JPanel {

  BorderLayout borderLayout1 = new BorderLayout();
  JPanel fontContentLoader = new JPanel();
  JPanel fontLoaderSizer = new JPanel();
  JPanel contentLoader = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JTextField jTextField1 = new JTextField();
  JTextField jTextField2 = new JTextField();
  JButton jButton1 = new JButton();
  JButton jButton2 = new JButton();
  JScrollPane textScrollPane = new JScrollPane();
  JPanel imSelect = new JPanel();
  JTextArea jTextArea1 = new JTextArea();
  JLabel statusBar = new JLabel();
  BorderLayout borderLayout4 = new BorderLayout();
  ButtonGroup buttonGroup1 = new ButtonGroup();
  JPanel jPanel5 = new JPanel();
  JSlider fontSizer = new JSlider();
  BorderLayout borderLayout5 = new BorderLayout();
  JLabel jLabel3 = new JLabel();
  JComboBox imComboBox = new JComboBox();
  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu jMenu1 = new JMenu();
  JMenuItem jMenuItem1 = new JMenuItem();
  JMenuItem jMenuItem2 = new JMenuItem();
  JMenuItem jMenuItem3 = new JMenuItem();
  JMenu jMenu2 = new JMenu();
  JMenuItem jMenuItem4 = new JMenuItem();
  JMenuItem jMenuItem5 = new JMenuItem();
  BorderLayout borderLayout6 = new BorderLayout();
  /**
   * Serialization ID
   */
  private static final long serialVersionUID = 8632340739785278662L;

  /**Construct the frame*/
  public UniTextEdit() {
      enableEvents(AWTEvent.WINDOW_EVENT_MASK);
      jbInit();
  }


  private void jbInit() {
    //setIconImage(Toolkit.getDefaultToolkit().createImage(Frame1.class.getResource("[Your Icon]")));
    this.setSize(new Dimension(549, 300));
    fontContentLoader.setLayout(borderLayout5);
    fontLoaderSizer.setLayout(borderLayout2);
    contentLoader.setLayout(borderLayout3);
    jLabel1.setMinimumSize(new Dimension(90, 13));
    jLabel1.setPreferredSize(new Dimension(90, 13));
    jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel1.setText("FontURL");
    jLabel2.setMinimumSize(new Dimension(90, 13));
    jLabel2.setPreferredSize(new Dimension(90, 13));
    jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel2.setText("contentURL");
    jTextField1.setToolTipText("");
    jTextField1.setText("http://www.crosswire.org/~scribe/ElEdit/1kg1.uni");
    jTextField2.setText("http://www.crosswire.org/~scribe/ElEdit/yoyo.ttf");
    jButton1.setText("Load");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton1_actionPerformed(e);
      }
    });
    jButton2.setText("Load");
    jButton2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton2_actionPerformed(e);
      }
    });
    jTextArea1.setText("");
    jTextArea1.setLineWrap(true);
    jTextArea1.setFont(new Font("Dialog", 0, 30));
    jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        jTextArea1_keyTyped(e);
      }
    });
//    jTextArea1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    statusBar.setText("");
    imSelect.setLayout(borderLayout4);
    fontSizer.setMinimum(1);
    fontSizer.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        fontSizer_stateChanged(e);
      }
    });
    jLabel3.setText("Keyboard");
    imComboBox.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        imComboBox_itemStateChanged(e);
      }
    });
    jMenu1.setText("File");
    jMenuItem1.setText("Lookup Url");
    jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuItem1_actionPerformed(e);
      }
    });
    jMenuItem2.setText("Exit");
    jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuItem2_actionPerformed(e);
      }
    });
    jMenuItem3.setText("Save");
    jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuItem3_actionPerformed(e);
      }
    });
    jMenu2.setText("Edit");
    jMenuItem4.setText("Copy");
    jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuItem4_actionPerformed(e);
      }
    });
    jMenuItem5.setText("Paste");
    jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuItem5_actionPerformed(e);
      }
    });
    this.setLayout(borderLayout6);
    fontLoaderSizer.add(jLabel1, BorderLayout.WEST);
    fontLoaderSizer.add(jTextField2, BorderLayout.CENTER);
    fontLoaderSizer.add(jButton2, BorderLayout.EAST);
    fontLoaderSizer.add(fontSizer, BorderLayout.SOUTH);
    fontContentLoader.add(contentLoader, BorderLayout.CENTER);
    fontContentLoader.add(fontLoaderSizer, BorderLayout.NORTH);
    contentLoader.add(jLabel2, BorderLayout.WEST);
    contentLoader.add(jButton1, BorderLayout.EAST);
    contentLoader.add(jTextField1, BorderLayout.CENTER);
    this.add(textScrollPane, BorderLayout.CENTER);
    this.add(imSelect, BorderLayout.SOUTH);
    textScrollPane.getViewport().add(jTextArea1, null);
    imSelect.add(statusBar, BorderLayout.NORTH);
    imSelect.add(jPanel5, BorderLayout.EAST);
    jPanel5.add(jLabel3, null);
    jPanel5.add(imComboBox, null);
    this.add(fontContentLoader, BorderLayout.NORTH);
    jMenuBar1.add(jMenu1);
    jMenuBar1.add(jMenu2);
    jMenu1.add(jMenuItem1);
    jMenu1.add(jMenuItem3);
    jMenu1.add(jMenuItem2);
    jMenu2.add(jMenuItem4);
    jMenu2.add(jMenuItem5);
    fontSizer.setValue(jTextArea1.getFont().getSize());
    imComboBox.addItem(new GreekKeymanIM("Greek - Keyman"));
    imComboBox.addItem(new CGreekIM("Greek - CGreek"));
    imComboBox.addItem(new Ibycus4IM("Greek - Ibycus4"));
    imComboBox.addItem(new Gtk2ClassicalGreekIM("Greek - Gtk2 Classical"));
    imComboBox.addItem(new HebrewDurusauIM("Hebrew - Durusau"));
    imComboBox.addItem(new HebrewMCIM("Hebrew - Michigan-Claremont"));
    imComboBox.addItem(new NullIM("Latin"));
    imComboBox.setFocusable(false);
    jTextArea1.setFocusTraversalKeysEnabled(true);

    showLoaders(false);
    showIMSelect(false);
  }

  void jButton2_actionPerformed(ActionEvent e) {
    loadFont(jTextField2.getText());
  }

  void jButton1_actionPerformed(ActionEvent e) {
    load(jTextField1.getText());
  }


  void jTextArea1_keyTyped(KeyEvent e)
  {
      char typedChar = e.getKeyChar();
      String pushChar = null;
      statusBar.setText("");

      SWInputMethod inputMethod = (SWInputMethod) imComboBox.getSelectedItem();

      pushChar = inputMethod.translate(typedChar);
      if (inputMethod.getState() > 1)
      {
          statusBar.setText("Compound '"+typedChar+"'");
          e.consume();
      }
      else if (pushChar.length() > 1)
      {
          e.consume();
          jTextArea1.insert(pushChar, jTextArea1.getCaretPosition());
      }
      else
      {
          e.setKeyChar(pushChar.charAt(0));
      }
  }

  public void setFontSize(float size) {
    Font font = jTextArea1.getFont();
    Font newFont = font.deriveFont(size);
    jTextArea1.setFont(newFont);
  }

  void fontSizer_stateChanged(ChangeEvent e) {
          setFontSize(fontSizer.getValue());
  }

  void imComboBox_itemStateChanged(ItemEvent e) {
      // TODO: let's set focus back to text box after IM is changed
  }

  void jMenuItem1_actionPerformed(ActionEvent e) {
      String currentEntry = jTextField1.getText();
      javax.swing.JFileChooser fileChooser = new JFileChooser((currentEntry.startsWith("file://") ? currentEntry.substring(7):"."));
      if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
          jTextField1.setText("file://"+ fileChooser.getSelectedFile().getAbsolutePath());
      }
  }

  void jMenuItem3_actionPerformed(ActionEvent e) {
      String currentEntry = jTextField1.getText();
      javax.swing.JFileChooser fileChooser = new JFileChooser((currentEntry.startsWith("file://") ? currentEntry.substring(7):"."));
      if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
          store(fileChooser.getSelectedFile());
      }

  }

  void jMenuItem4_actionPerformed(ActionEvent e) {
      jTextArea1.copy();
  }

  void jMenuItem5_actionPerformed(ActionEvent e) {
      jTextArea1.paste();
  }

  void jMenuItem2_actionPerformed(ActionEvent e) {
      System.exit(0);
  }

  public void showIMSelect(boolean show) {
    imSelect.setVisible(show);
  }

  public void showLoaders(boolean show) {
    fontContentLoader.setVisible(show);
  }
  public String getText() {
    StringWriter out = new StringWriter();
    try {
      jTextArea1.write(out);
    } catch (IOException ex) {
        ex.printStackTrace(System.err);
    }
    return out.toString();
  }

  public void store(File outFile) {
    Writer writer = null;
    try {
        writer = new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8");
        jTextArea1.write(writer);
    }
    catch (Exception e1) {
        e1.printStackTrace(System.err);
    } finally {
        if (writer != null) {
            try
            {
                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace(System.err);
            }
        }
    }
    

  }

  public void requestFocus() {
    jTextArea1.requestFocus();
  }

  public void load(String url) {
    InputStream bis = null;
    try {
      statusBar.setText("Loading content...");
      statusBar.paintImmediately(statusBar.getVisibleRect());
      URLConnection connection = new URL(url).openConnection();
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      bis = new BufferedInputStream(connection.getInputStream());

      int len;
      byte inBuf[] = new byte[8192];
      do {
        len = bis.read(inBuf, 0, 8192);
        if (len != -1)
        {
          bos.write(inBuf, 0, len);
        }
      }
      while (len != -1);
      String newText = new String(bos.toByteArray(), "UTF-8");
      jTextArea1.setText(newText);
      statusBar.setText(Integer.toString(newText.length()) +
                        " characters of content loaded.");
    }
    catch (IOException ex) {
      ex.printStackTrace(System.err);
    } finally {
        if (bis != null) {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }
  }


  public void addCaretListener(CaretListener l) {
    jTextArea1.addCaretListener(l);
  }


  public void addPropertyChangeListener(PropertyChangeListener l) {
      //jTextArea1.addPropertyChangeListener(l);
  }


  public void addKeyListener(KeyListener l) {
    jTextArea1.addKeyListener(l);
  }


  public void setComponentOrientation(ComponentOrientation o) {
    jTextArea1.setComponentOrientation(o);
  }


  public void loadFont(String url) {
    if (url.length() < 3) {
        return;
    }

    InputStream is = null;
    try {
      statusBar.setText("Loading font...");
      statusBar.paintImmediately(statusBar.getVisibleRect());
      URLConnection connection = new URL(url).openConnection();
      is = connection.getInputStream();
      loadFont(is);
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


  public void loadFont(InputStream is) throws FontFormatException, IOException {
        statusBar.setText("Loading font...");
        statusBar.paintImmediately(statusBar.getVisibleRect());
        Font font = Font.createFont(Font.TRUETYPE_FONT, is);
        Font newFont = font.deriveFont((float)18.0);
        fontSizer.setValue(18);
        this.jTextArea1.setFont(newFont);
        statusBar.setText("New Font Loaded.");
  }

  public void setText(String text) {
    jTextArea1.setText(text);
    jTextArea1.repaint();
  }

}
