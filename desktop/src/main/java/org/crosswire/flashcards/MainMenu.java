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
///////////////////////////////////////////////////////////////////////////
//
// MainMenu.java
//
// Menu bar for FlashCards
//
// Copyright : 2004 CrossWire Bible Society http://crosswire.org
//
///////////////////////////////////////////////////////////////////////////

package org.crosswire.flashcards;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

class MainMenu extends JMenuBar {

    //
    // Attributes
    //

    /**
     * Serialization ID
     */
    private static final long serialVersionUID = 8985550639988241474L;
    protected JFrame frame;
    protected JRadioButtonMenuItem trace, inform, warn, error;

    //
    // Methods
    //

    // ---------------
    MainMenu(JFrame frame) {
        this.frame = frame;

        JMenu menu1, menu2;
        JMenuItem item;

        // Application Menu

        menu1 = new JMenu("FlashCards");

        // Application Menu -> Editor

        // Application Menu -> Exit

        item = new JMenuItem("Exit");
        item.addActionListener(new ExitAction());
        menu1.add(item);
        add(menu1);

        // Help Menu

        menu1 = new JMenu("Help");

        // Help Menu -> About

        item = new JMenuItem("About");
        item.addActionListener(new AboutAction());
        menu1.add(item);

        // Add the menu

        add(menu1);

    }

    // ---------------
    static class ExitAction extends AbstractAction {

        /**
         * Serialization ID
         */
        private static final long serialVersionUID = -4617551302933053860L;

        public ExitAction() {
            super();
        }

        public void actionPerformed(ActionEvent event) {
            System.exit(0);
        }

    }

    // ---------------
    class AboutAction extends AbstractAction {

        /**
         * Serialization ID
         */
        private static final long serialVersionUID = 2317132982826962267L;

        public void actionPerformed(ActionEvent event) {

            String aboutString = "FlashCards\n" +
                    "A Vocabulary Training Tool by CrossWire\n" +
                    "(c) 2004-2007 CrossWire Bible Society\n" +
                    "http://crosswire.org";

            JOptionPane.showMessageDialog(frame,
                    aboutString,
                    "About FlashCards",
                    JOptionPane.INFORMATION_MESSAGE);
        }

    }

}
