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
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionListener;


/**
 * A panel consisting of all the lesson sets known to FlashCards.
 * 
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
public class LessonSetPane extends JPanel
{
    private JList lessonSetList = new JList(new DefaultListModel());
    private boolean editable;

    /**
     * The listeners for handling ViewEvent Listeners
     */
    private EventListenerList listenerList = new EventListenerList();

    //Construct the frame
    public LessonSetPane()
    {
        this(false);
    }

    /**
     * @param b
     */
    public LessonSetPane(boolean allowEdits)
    {
        editable = allowEdits;
        try
        {
            jbInit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @param lessonPanel
     */
    public void addListSelectionListener(ListSelectionListener listener)
    {
        lessonSetList.addListSelectionListener(listener);
    }

    private void jbInit() throws Exception
    {
        lessonSetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setLayout(new BorderLayout());

        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Lesson Sets: "));
        add(new JScrollPane(lessonSetList), BorderLayout.CENTER);

        JMenuBar lessonMenuBar = new JMenuBar();
        JMenu lessonSetEditMenu = new JMenu("Edit");
        JMenuItem newLessonSet = new JMenuItem("New Lesson Set");
        newLessonSet.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String answer = JOptionPane.showInputDialog(null, "<html>Lesson Set name:<br>(Single word, only letters)", "Create a New Lesson Set",
                                JOptionPane.PLAIN_MESSAGE);
                if (answer == null)
                {
                    return;
                }
                createLessonSet(answer);
            }
        });
        JMenuItem renameLessonSet = new JMenuItem("Rename Lesson Set");
        lessonMenuBar.add(lessonSetEditMenu);
        lessonSetEditMenu.add(newLessonSet);
        lessonSetEditMenu.add(renameLessonSet);

        if (editable)
        {
            add(lessonMenuBar, BorderLayout.NORTH);
        }

        loadLessonSets();
    }

    public void createLessonSet(String name)
    {
        LessonSet lessonSet = new LessonSet(LessonManager.LESSON_ROOT + '/' + name);
        DefaultListModel model = (DefaultListModel) lessonSetList.getModel();
        if (!model.contains(lessonSet))
        {
            model.addElement(lessonSet);
            LessonManager.instance().add(lessonSet);
            fireLessonChanged(new LessonChangeEvent(this));
        }
        lessonSetList.setSelectedValue(lessonSet, true);
    }

    private void loadLessonSets()
    {
        Iterator lessonSetIterator = LessonManager.instance().iterator();
        while (lessonSetIterator.hasNext())
        {
            LessonSet lessonSet = (LessonSet) lessonSetIterator.next();
            DefaultListModel model = (DefaultListModel) lessonSetList.getModel();
            model.addElement(lessonSet);
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
    public synchronized void removeLessonChangeEvent(LessonChangeEventListener listener)
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