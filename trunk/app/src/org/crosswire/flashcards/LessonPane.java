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
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * A panel listing the lessons in a lesson set.
 * 
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
public class LessonPane extends JPanel
{
    private JList lessonList = new JList(new DefaultListModel());
    private LessonSet lessonSet;

    private JMenuItem newItem;
    private boolean editable;

    /**
     * The listeners for handling ViewEvent Listeners
     */
    private EventListenerList listenerList = new EventListenerList();

    //Construct the frame
    public LessonPane()
    {
        this(false);
    }

    /**
     * @param b
     */
    public LessonPane(boolean allowEdits)
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

    public void setSelectionMode(int mode)
    {
        lessonList.setSelectionMode(mode);
    }

    public Iterator iterator()
    {
        return new SelectedLessonIterator(lessonList);
    }

    /**
     * @param flashCardPanel
     */
    public void addListSelectionListener(ListSelectionListener listener)
    {
        lessonList.addListSelectionListener(listener);
    }

    private void jbInit() throws Exception
    {
        setLayout(new BorderLayout());

        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Lessons: "));
        add(new JScrollPane(lessonList), BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu editMenu = new JMenu("Edit");
        newItem = new JMenuItem("New Lesson");
        menuBar.add(editMenu);
        editMenu.add(newItem);
        if (editable)
        {
            add(menuBar, BorderLayout.NORTH);
        }
        enableControls();
        newItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String answer = JOptionPane.showInputDialog(null, "Lesson Description: ", "Create a New Lesson", JOptionPane.PLAIN_MESSAGE);
                if (answer == null)
                {
                    return;
                }
                createLesson(answer);
            }
        });

//        JMenuItem editLesson = new JMenuItem("Edit Lesson");
//        JMenuItem renameLesson = new JMenuItem("Rename Lesson");
//        lessonEditMenu.add(editLesson);
//        lessonEditMenu.add(renameLesson);
        
        enableControls();
    }

    public void createLesson(String description)
    {
        Lesson lesson = new Lesson(lessonSet.getNextLessonFilename(), description);
        DefaultListModel model = (DefaultListModel) lessonList.getModel();
        if (!model.contains(lesson))
        {
            model.addElement(lesson);
            lessonSet.add(lesson);
            fireLessonChanged(new LessonChangeEvent(this));
        }
        lessonList.setSelectedValue(lesson, true);
        
    }

    public void loadLessons(LessonSet aLessonSet)
    {
        lessonSet = aLessonSet;
        DefaultListModel model = (DefaultListModel) lessonList.getModel();
        model.clear();
        if (lessonSet != null)
        {
            Iterator lessonIterator = lessonSet.iterator();
            while (lessonIterator.hasNext())
            {
                Lesson lesson = (Lesson) lessonIterator.next();
                model.addElement(lesson);
            }
        }
        enableControls();        
    }

    private void enableControls()
    {
        newItem.setEnabled(lessonSet != null);
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

    /**
     * Iterator over the selections in a JList
     */
    private static class SelectedLessonIterator implements Iterator
    {

        public SelectedLessonIterator(JList list)
        {
            model = (DefaultListModel) list.getModel();
            selectedIndexes = list.getSelectedIndices();
        }

        /* (non-Javadoc)
         * @see java.util.Iterator#remove()
         */
        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        /* (non-Javadoc)
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext()
        {
            return currentIndex < selectedIndexes.length;
        }

        /* (non-Javadoc)
         * @see java.util.Iterator#next()
         */
        public Object next()
        {
            return model.get(selectedIndexes[currentIndex++]);
        }

        private int[] selectedIndexes;
        private DefaultListModel model;
        private int currentIndex;
    }

}
