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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.crosswire.common.swing.RowTable;
import org.crosswire.common.swing.RowTableModel;

/**
 * A panel listing the Flash Cards in a Lesson.
 * 
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
public class FlashCardPane extends JPanel implements FlashCardEventListener
{
    private RowTable wordList = new RowTable(new ArrayList(), new FlashCardColumns());
    private Lesson lesson;

    /**
     * The listeners for handling ViewEvent Listeners
     */
    private EventListenerList listenerList = new EventListenerList();


    public FlashCardPane()
    {
        this(false);
    }

    /**
     * @param b
     */
    public FlashCardPane(boolean allowsEdits)
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
        wordList.setShowGrid(false);
        setLayout(new BorderLayout());

        wordList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(wordList), BorderLayout.CENTER);
    }

    public boolean contains(FlashCard flashCard)
    {
        return lesson.contains(flashCard);
    }

    public FlashCard getFlashCard(int i)
    {
        FlashCard flashCard = null;
        RowTableModel model = (RowTableModel) wordList.getModel();
        flashCard = (FlashCard) model.getRow(i);
        return flashCard;
    }

    public void add(FlashCard flashCard)
    {
        lesson.add(flashCard);
        RowTableModel model = (RowTableModel) wordList.getModel();
        model.addRow(flashCard);
        wordList.selectRow(model.getRow(flashCard));
        wordList.validate();
        wordList.repaint();
        fireLessonChanged(new LessonChangeEvent(this));
    }

    public void deleteSelected()
    {
        int row = wordList.getSelectedRow();
        RowTableModel model = (RowTableModel) wordList.getModel();
        FlashCard flashCard = (FlashCard) model.getRow(row);
        lesson.remove(flashCard);
        model.removeRow(flashCard);
        wordList.validate();
        wordList.repaint();
        fireLessonChanged(new LessonChangeEvent(this));
    }

    public void replaceSelected(FlashCard newFlashCard)
    {
        int row = wordList.getSelectedRow();
        RowTableModel model = (RowTableModel) wordList.getModel();
        FlashCard flashCard = (FlashCard) model.getRow(row);
        lesson.remove(flashCard);
        model.removeRow(flashCard);
        lesson.add(newFlashCard);
        model.addRow(newFlashCard);
        wordList.selectRow(model.getRow(newFlashCard));
        wordList.validate();
        wordList.repaint();
        fireLessonChanged(new LessonChangeEvent(this));
    }

    /**
     * @param lessonPanel
     */
    public void addListSelectionListener(ListSelectionListener listener)
    {
        wordList.addListSelectionListener(listener);
    }

    public void loadFlashCards(Lesson aLesson)
    {
        RowTableModel model = (RowTableModel) wordList.getModel();
        model.clear();
        lesson = aLesson;
        if (lesson != null)
        {
            Iterator flashCardIterator = lesson.iterator();
            while (flashCardIterator.hasNext())
            {
                FlashCard flashCard = (FlashCard) flashCardIterator.next();
                model.addRow(flashCard);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.crosswire.flashcards.FlashCardEventListener#flashCardChanged(org.crosswire.flashcards.FlashCardEvent)
     */
    public void flashCardChanged(FlashCardEvent event)
    {
        switch (event.getAction())
        {
        case FlashCardEvent.ADDED:
            add(event.getFlashCard());
            break;
        case FlashCardEvent.DELETED:
            deleteSelected();
            break;
        case FlashCardEvent.MODIFIED:
            replaceSelected(event.getFlashCard());
            break;
        default :
            break;
        }

    }

    /**
     * Adds a view event listener for notification of any changes to the view.
     *
     * @param listener the listener
     */
    public synchronized void addLessonChangeEventListener(LessonChangeEventListener listener)
    {
        listenerList.add(LessonChangeEventListener.class, listener);
    }

    /**
     * Removes a view event listener.
     *
     * @param listener the listener
     */
    public synchronized void removeLessonChangeEventListener(LessonChangeEventListener listener)
    {
        listenerList.remove(LessonChangeEventListener.class, listener);
    }

    /**
     * Notify the listeners that the view has been removed.
     *
     * @param e the event
     * @see EventListenerList
     */
    public void fireLessonChanged(LessonChangeEvent e)
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if (listeners[i] == LessonChangeEventListener.class)
            {
                ((LessonChangeEventListener) listeners[i + 1]).lessonChanged(e);
            }
        }
    }
}