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

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.crosswire.common.swing.FixedSplitPane;


public class SetupPane extends JPanel
{
    private LessonManager lessonManager;
    private JList lessonSetList = new JList(new DefaultListModel());
    private JList lessonList = new JList(new DefaultListModel());

    //Construct the frame
    public SetupPane(LessonManager lessonManager)
    {
        this.lessonManager = lessonManager;
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try
        {
            jbInit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Iterator iterator()
    {
        return new SelectedLessonIterator(lessonList);
    }

    private void jbInit() throws Exception
    {
        setLayout(new BorderLayout());
        JPanel lessonPanel = new JPanel(new BorderLayout());
        lessonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Lessons: "));
        lessonPanel.add(new JScrollPane(lessonList));
//        lessonList.setCellRenderer(new CheckBoxListCellRenderer());
        lessonSetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lessonSetList.addListSelectionListener(new LessonSetSelectionListener(lessonList));

        JPanel lessonSetPanel = new JPanel(new BorderLayout());
        lessonSetPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Lesson Sets: "));
        lessonSetPanel.add(new JScrollPane(lessonSetList), BorderLayout.CENTER);

        JSplitPane splitPane = new FixedSplitPane();
        splitPane.setResizeWeight(0.3D);
        splitPane.setDividerLocation(0.3D);
        splitPane.setRightComponent(lessonPanel);
        splitPane.setLeftComponent(lessonSetPanel);

        add(splitPane);

        loadLessonSets();
    }

    private void loadLessonSets()
    {
        Iterator lessonSetIterator = lessonManager.iterator();
        while (lessonSetIterator.hasNext())
        {
            LessonSet lessonSet = (LessonSet) lessonSetIterator.next();
            DefaultListModel model = (DefaultListModel) lessonSetList.getModel();
            model.addElement(lessonSet);
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
    /**
     * When a <code>LessonSet</code> is loaded this listener will populate the lessonList
     * with the <code>Lesson</code>s.
     */
    private static class LessonSetSelectionListener implements ListSelectionListener
    {

        /**
         * Create a listener that populates the lessonList with lessons.
         * @param lessonList the list to populate
         */
        public LessonSetSelectionListener(JList lessonList)
        {
            this.lessonList = lessonList;
        }

        /* (non-Javadoc)
         * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
         */
        public void valueChanged(ListSelectionEvent e)
        {
            if (e.getValueIsAdjusting())
            {
                return;
            }
            JList list = (JList) e.getSource();
            LessonSet lessonSet = (LessonSet) list.getSelectedValue();
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
        }
        private JList lessonList;
    }

    /**
     * This renderer shows selection with a check box instead of shading the row.
     */
    private static class CheckBoxListCellRenderer extends JCheckBox implements ListCellRenderer
    {
        public CheckBoxListCellRenderer()
        {
            // So it looks like it is a row in a list, we need to show the background of the list
            // and not the checkbox.
            setOpaque(false);
        }

        /* (non-Javadoc)
         * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
         */
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            setSelected(isSelected);
            setText(value.toString());
            setFocusPainted(cellHasFocus);
            return this;
        }

    }
}