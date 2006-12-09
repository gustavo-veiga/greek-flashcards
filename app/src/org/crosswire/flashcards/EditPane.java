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

import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

//import org.crosswire.common.swing.FixedSplitPane;


/**
 * An EditPane consists of Lesson Sets, Lessons, Flash Cards and a Flash Card editor.
 *
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
public class EditPane extends JPanel
{
    /**
     * Serialization ID
     */
    private static final long serialVersionUID = 8637424690635575114L;
     JButton imageGenButton = new JButton();

     //Construct the frame
    public EditPane()
    {
        jbInit();
    }

    private void jbInit()
    {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                        "Create and modify Lesson Sets, Lessons and Flash Cards "));

        final LessonSetPane lessonSetPanel = new LessonSetPane(true);
        final LessonPane lessonPanel = new LessonPane(true);
        final FlashCardPane flashCardPanel = new FlashCardPane();
        final FlashCardEditor flashCardEditor = new FlashCardEditor();
        final JButton saveButton = new JButton("Save");

        saveButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                LessonManager.instance().store();
            }
        });

        LessonChangeEventListener changeListener = new LessonChangeEventListener()
        {
            public void lessonChanged(LessonChangeEvent event)
            {
                boolean modified = LessonManager.instance().isModified();
                saveButton.setEnabled(modified);
            }

        };

        boolean modified = LessonManager.instance().isModified();
        saveButton.setEnabled(modified);

        // Hook up everything so that they see each other
        // When changes happen the save button is activated
        lessonSetPanel.addLessonChangeEventListener(changeListener);
        lessonPanel.addLessonChangeEventListener(changeListener);
        flashCardPanel.addLessonChangeEventListener(changeListener);

        // When flash cards are edited the FlashCard panel is updated
        flashCardEditor.addFlashCardEventListener(flashCardPanel);

        // When a lesson set is selected list the lessons in it.
        lessonSetPanel.addListSelectionListener(new ListSelectionListener()
        {
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
                lessonPanel.loadLessons((ComplexLessonSet) list.getSelectedValue());
            }
        });

        // When a lesson is selected list the flash cards in it
        lessonPanel.addListSelectionListener(new ListSelectionListener()
        {
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
                flashCardPanel.loadFlashCards((Lesson) list.getSelectedValue());
            }
        });

        // When a lesson is selected then FlashCards can be edited
        lessonPanel.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                if (e.getValueIsAdjusting())
                {
                    return;
                }
                JList list = (JList) e.getSource();
                flashCardEditor.setActive(list.getSelectedValue() != null);
            }
        });

        // When a flash card is selected then it can be edited
        flashCardPanel.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                if (e.getValueIsAdjusting())
                {
                    return;
                }
                DefaultListSelectionModel listSelectionModel = (DefaultListSelectionModel) e.getSource();
                int row = listSelectionModel.getMinSelectionIndex();
                FlashCard flashCard = null;
                if (row != -1)
                {
                    flashCard = flashCardPanel.getFlashCard(row);
                }
                flashCardEditor.setFlashCard(flashCard);
            }
        });

        JSplitPane lessonSplitPane = new JSplitPane();
        lessonSplitPane.setResizeWeight(0.5D);
        lessonSplitPane.setDividerLocation(0.5D);
        lessonSplitPane.setLeftComponent(lessonSetPanel);
        lessonSplitPane.setRightComponent(lessonPanel);

        JSplitPane flashCardSplitPane = new JSplitPane();
        flashCardSplitPane.setResizeWeight(0.5D);
        flashCardSplitPane.setDividerLocation(0.5D);
        flashCardSplitPane.setLeftComponent(flashCardPanel);
        flashCardSplitPane.setRightComponent(flashCardEditor);
        flashCardSplitPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Flash Cards: "));

        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        verticalSplitPane.setDividerLocation(0.35D);
        verticalSplitPane.setResizeWeight(0.35D);
        verticalSplitPane.setTopComponent(lessonSplitPane);
        verticalSplitPane.setBottomComponent(flashCardSplitPane);
          imageGenButton.setText("Prerender Word Images");
          imageGenButton.addActionListener(new EditPane_imageGenButton_actionAdapter(this));
          add(verticalSplitPane, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel();
        buttonPane.add(saveButton);
          buttonPane.add(imageGenButton);
          add(buttonPane, BorderLayout.SOUTH);
     }


     public void imageGenButton_actionPerformed(ActionEvent e) {
	      LessonManager.instance().genImages();
     }
}


class EditPane_imageGenButton_actionAdapter
          implements ActionListener {
     private EditPane adaptee;
     EditPane_imageGenButton_actionAdapter(EditPane adaptee) {
          this.adaptee = adaptee;
     }


     public void actionPerformed(ActionEvent e) {
          adaptee.imageGenButton_actionPerformed(e);
     }
}
