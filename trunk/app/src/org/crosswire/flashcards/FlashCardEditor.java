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
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.crosswire.modedit.UniTextEdit;

/**
 * Editor for lessons used by Quiz (part of FlashCards).
 * 
 * @author Troy A. Griffitts [scribe at crosswire dot org]
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
public class FlashCardEditor extends JPanel
{

    //
    // Attributes
    //

    private JPanel answerPanel = new JPanel();
    private JTextField answers = new JTextField();
    private UniTextEdit wordText = new UniTextEdit();
    protected JDialog dlgMain;

    //
    // Methods
    //

    // ---------------
    public FlashCardEditor()
    {
        try
        {
            jbInit();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            Debug.error(this.toString(), exception.getMessage());
        }
    }

    //Component initialization
    private void jbInit() throws Exception
    {
        setLayout(new BorderLayout());

        wordText.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Front"));
        wordText.setText("");
        wordText.showIMSelect(true);
        wordText.setComponentOrientation(ComponentOrientation.UNKNOWN);
        wordText.setFontSize(30);
        add(wordText, BorderLayout.CENTER);

        answers.setSelectionStart(0);
        answers.setText("");
        answerPanel.setLayout(new BorderLayout());
        answerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Back"));
        answerPanel.add(answers);
        add(answerPanel, BorderLayout.SOUTH);
    }

    protected boolean createFlashCard(FlashCardPane flashCardPane)
    {
        
        String front = wordText.getText();
        String back = answers.getText();
        
        if (front == null || front.length() == 0)
        {
            JOptionPane.showMessageDialog(null, "Front is empty", "Unable to Create Flash Card", JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        if (back == null || back.length() == 0)
        {
            JOptionPane.showMessageDialog(null, "Back is empty", "Unable to Create Flash Card", JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        // Create a new flash card
        FlashCard flashCard = new FlashCard();
        flashCard.setFront(front);
        flashCard.setBack(back);
        if (flashCardPane.contains(flashCard))
        {
            JOptionPane.showMessageDialog(null, "FlashCard already exists", "Unable to Create Flash Card", JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        flashCardPane.add(flashCard);
        return true;
    }

    /**
     * Open this Panel in it's own dialog box.
     */
    public void showInDialog(final FlashCardPane flashCardPane)
    {
        dlgMain = new JDialog(JOptionPane.getFrameForComponent(flashCardPane), "Create a Flash Card", true);
        dlgMain.setSize(new Dimension(320, 240));
        dlgMain.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JComponent contentPane = (JComponent) dlgMain.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(this, BorderLayout.CENTER);

        JButton btnAdd = new JButton("Create");
        
        btnAdd.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (createFlashCard(flashCardPane))
                {
                    wordText.setText("");
                    answers.setText("");
                }
            }
        });

        JButton btnOK = new JButton("OK");
        btnOK.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (createFlashCard(flashCardPane))
                {
                    dlgMain.dispose();
                }
            }
        });


        JButton btnClose = new JButton("Close");
        
        btnClose.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                dlgMain.dispose();
            }
        });

        JPanel pnlButtons = new JPanel();
        pnlButtons.add(btnAdd);
        pnlButtons.add(btnOK);
        pnlButtons.add(btnClose);

        contentPane.add(pnlButtons, BorderLayout.SOUTH);
        dlgMain.setLocationRelativeTo(flashCardPane);
        dlgMain.show();
    }
}
