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
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

//import org.crosswire.common.swing.FixedSplitPane;


/**
 * A panel used for setting up a quiz.
 *
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
public class SetupPane extends JPanel
{
    /**
     * Serialization ID
     */
    private static final long serialVersionUID = 1904221667403637140L;
    protected LessonPane lessonPanel = new LessonPane();
    private LessonSetPane lessonSetPanel = new LessonSetPane();
    private JCheckBox flipped = new JCheckBox("Flip the Flash Cards");
    private JCheckBox noMultipleChoice = new JCheckBox("No Multiple Choice");

    //Construct the frame
    public SetupPane()
    {
        jbInit();
    }

    public boolean isFlipped()
    {
        return flipped.isSelected();
    }

    public boolean isNoMultipleChoice()
    {
    	return noMultipleChoice.isSelected();
    }

    public Iterator iterator()
    {
        return lessonPanel.iterator();
    }

    private void jbInit()
    {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                        "Select a Lesson Set, then one or more Lessons: "));

        FlashCardPane flashCardPanel = new FlashCardPane();

        lessonSetPanel.addListSelectionListener(new LessonSetSelectionListener(lessonPanel));

        lessonPanel.addListSelectionListener(new LessonSelectionListener(flashCardPanel));

        JSplitPane horizontalSplitPane = new JSplitPane();
        horizontalSplitPane.setResizeWeight(0.3D);
        horizontalSplitPane.setDividerLocation(0.3D);
        horizontalSplitPane.setRightComponent(lessonPanel);
        horizontalSplitPane.setLeftComponent(lessonSetPanel);

//        JSplitPane verticalSplitPane = new FixedSplitPane(JSplitPane.VERTICAL_SPLIT);
//        verticalSplitPane.setOneTouchExpandable(true);
//        verticalSplitPane.setDividerSize(6);
//        verticalSplitPane.setDividerLocation(0.5D);
//        verticalSplitPane.setResizeWeight(0.5D);
//        verticalSplitPane.setTopComponent(horizontalSplitPane);
//        verticalSplitPane.setBottomComponent(flashCardPanel);
        add(horizontalSplitPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
        "Quiz Options: "));
        panel.add(flipped);
        panel.add(noMultipleChoice);
        add(panel, BorderLayout.SOUTH);
    }
 
    static class LessonSetSelectionListener implements ListSelectionListener
    {
        private LessonPane lessonPanel;

        public LessonSetSelectionListener(LessonPane lessonPanel)
        {
            this.lessonPanel = lessonPanel;
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
            lessonPanel.loadLessons((LessonSet) list.getSelectedValue());
        }
    }

    static class LessonSelectionListener implements ListSelectionListener
    {
        private FlashCardPane flashCardPanel;

        public LessonSelectionListener(FlashCardPane flashCardPanel)
        {
            this.flashCardPanel = flashCardPanel;
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
            Object[] selections = list.getSelectedValues();
            if (selections != null && selections.length == 1)
            {
                flashCardPanel.loadFlashCards((Lesson) selections[0]);
            }
        }
}
}
