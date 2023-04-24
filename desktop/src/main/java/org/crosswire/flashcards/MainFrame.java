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
 * Copyright: 2008 CrossWire Bible Society
 */
package org.crosswire.flashcards;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

/**
 * The main flash card program.
 * 
 * @author Troy A. Griffitts [scribe at crosswire dot org]
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
public class MainFrame extends JFrame {
    /**
     * Serialization ID
     */
    private static final long serialVersionUID = 8755537121252831657L;

    private SetupPane setupPane;
    private QuizPane testPane;
    private EditPane editPane;

    // Construct the frame
    public MainFrame() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        setJMenuBar(new MainMenu(this));
        setupPane = new SetupPane();
        testPane = new QuizPane(setupPane);
        editPane = new EditPane();
        JOptionPane.setRootFrame(this);
        jbInit();
    }

    // Component initialization
    private void jbInit() {
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        this.setSize(new Dimension(600, 480));
        this.setTitle("FlashCards - (c) 2008 CrossWire Bible Society http://crosswire.org");

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Setup", setupPane);
        tabs.addTab("Quiz", testPane);
        tabs.addTab("Edit", editPane);
        tabs.setSelectedComponent(setupPane);
        contentPane.add(tabs, BorderLayout.CENTER);
    }

    // Overridden so we can exit when window is closed
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            System.exit(0);
        }
    }

}
