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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import org.crosswire.modedit.UniTextEdit;
import java.awt.*;

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
    private FlashCard flashCard;
    private JButton btnAdd = new JButton("Add");
    private JButton btnModify = new JButton("Modify");
    private JButton btnDelete = new JButton("Delete");
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

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
        setLayout(gridBagLayout1);
        setBorder(BorderFactory.createEtchedBorder());

        wordText.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Front"));
        wordText.setText("");
        wordText.showIMSelect(true);
        wordText.setComponentOrientation(ComponentOrientation.UNKNOWN);
        wordText.setFontSize(30);
        add(wordText,  new GridBagConstraints(0, 0, 1, 1, 0.7, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        answers.setSelectionStart(0);
        answers.setText("");
        answerPanel.setLayout(new BorderLayout());
        answerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Back"));
        answerPanel.add(answers);
        add(answerPanel,  new GridBagConstraints(0, 1, 1, 1, 0.3, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 5));
        btnAdd.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                createFlashCard();
            }

        });

        btnModify.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                modifyFlashCard();
            }

        });

        btnDelete.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                deleteFlashCard();
            }
        });

        JPanel pnlButtons = new JPanel();
        pnlButtons.add(btnAdd);
        pnlButtons.add(btnModify);
        pnlButtons.add(btnDelete);
        add(pnlButtons,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        setActive(false);
    }

    public void setFlashCard(FlashCard newFlashCard)
    {
        boolean selected = newFlashCard != null;
        if (selected)
        {
            try
            {
                flashCard = (FlashCard) newFlashCard.clone();
            }
            catch (CloneNotSupportedException e)
            {
                assert false;
            }
            wordText.setText(flashCard.getFront());
            answers.setText(flashCard.getBack());
        }
        else
        {
            wordText.setText("");
            answers.setText("");
        }
        btnDelete.setEnabled(selected);
        btnModify.setEnabled(selected);
    }

    public void setActive(boolean state)
    {
        btnAdd.setEnabled(state);
        btnModify.setEnabled(flashCard != null);
        btnDelete.setEnabled(state);
    }

    protected void createFlashCard()
    {
        flashCard = new FlashCard();
        flashCard.setFront(wordText.getText());
        flashCard.setBack(answers.getText());

        fireFlashCardChanged(new FlashCardEvent(this, flashCard, FlashCardEvent.ADDED));
    }

    protected void modifyFlashCard()
    {
        assert flashCard != null;

        flashCard.setFront(wordText.getText());
        flashCard.setBack(answers.getText());

        fireFlashCardChanged(new FlashCardEvent(this, flashCard, FlashCardEvent.MODIFIED));
    }

    protected void deleteFlashCard()
    {
        fireFlashCardChanged(new FlashCardEvent(this, flashCard, FlashCardEvent.DELETED));
    }

    /**
     * Adds a view event listener for notification of any changes to the view.
     *
     * @param listener the listener
     */
    public synchronized void addFlashCardEventListener(FlashCardEventListener listener)
    {
        listenerList.add(FlashCardEventListener.class, listener);
    }

    /**
     * Removes a view event listener.
     *
     * @param listener the listener
     */
    public synchronized void removeFlashCardEventListener(FlashCardEventListener listener)
    {
        listenerList.remove(FlashCardEventListener.class, listener);
    }

    /**
     * Notify the listeners that the view has been removed.
     *
     * @param e the event
     * @see EventListenerList
     */
    public void fireFlashCardChanged(FlashCardEvent e)
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if (listeners[i] == FlashCardEventListener.class)
            {
                ((FlashCardEventListener) listeners[i + 1]).flashCardChanged(e);
            }
        }
    }
}
