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
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.crosswire.common.swing.FixedSplitPane;


/**
 * An EditPane consists of Lesson Sets, Lessons and Flash Cards.
 * 
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
public class EditPane extends JPanel
{
    //Construct the frame
    public EditPane()
    {
        try
        {
            jbInit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception
    {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                        "Create and modify Lesson Sets, Lessons and Flash Cards "));

        LessonSetPane lessonSetPanel = new LessonSetPane(true);
        LessonPane lessonPanel = new LessonPane(true);
        FlashCardPane flashCardPanel = new FlashCardPane(true);
        final JButton saveButton = new JButton("Save");
        
        saveButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.err.println("do save");
                
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

        lessonSetPanel.addLessonChangeEvent(changeListener);
        lessonPanel.addLessonChangeEvent(changeListener);
        flashCardPanel.addLessonChangeEvent(changeListener);

        lessonSetPanel.addListSelectionListener(lessonPanel);
        lessonPanel.addListSelectionListener(flashCardPanel);

        JSplitPane horizontalSplitPane = new FixedSplitPane();
        horizontalSplitPane.setResizeWeight(0.3D);
        horizontalSplitPane.setDividerLocation(0.3D);
        horizontalSplitPane.setRightComponent(lessonPanel);
        horizontalSplitPane.setLeftComponent(lessonSetPanel);

        JSplitPane verticalSplitPane = new FixedSplitPane(JSplitPane.VERTICAL_SPLIT);
        verticalSplitPane.setDividerLocation(0.5D);
        verticalSplitPane.setResizeWeight(0.5D);
        verticalSplitPane.setTopComponent(horizontalSplitPane);
        verticalSplitPane.setBottomComponent(flashCardPanel);
        add(verticalSplitPane, BorderLayout.CENTER);
        
        JPanel buttonPane = new JPanel();
        buttonPane.add(saveButton);
        add(buttonPane, BorderLayout.SOUTH);
    }
}
