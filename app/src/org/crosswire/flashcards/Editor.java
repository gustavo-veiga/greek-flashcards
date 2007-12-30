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
// Editor.java
//
// Editor for lessons used by Quiz (part of FlashCards).
//
// Copyright : (c) 2004 CrossWire Bible Society http://crosswire.org
//
///////////////////////////////////////////////////////////////////////////

package org.crosswire.flashcards;

import javax.swing.UIManager;

public class Editor {

    //
    // Attributes
    //

    boolean packFrame = false;

    //
    // Methods
    //

    // ---------------
    public Editor(boolean standAlone) {

        EditorFrame frame = new EditorFrame(standAlone);

        //Validate frames that have preset sizes
        //Pack frames that have useful preferred size info, e.g. from their layout

        if (packFrame)
        {
            frame.pack();
        }
        else
        {
            frame.validate( );
        }

        //Center the window

        //Dimension screenSize = Toolkit.getDefaultToolkit( ).getScreenSize( );
        //Dimension frameSize = frame.getSize( );

        //if( frameSize.height > screenSize.height ) {
        //
        //    frameSize.height = screenSize.height;
        //
        //}
        //
        //if( frameSize.width > screenSize.width ) {
        //
        //    frameSize.width = screenSize.width;
        //
        //}

        //frame.setLocation( ( screenSize.width - frameSize.width ) / 2,
        //                 ( screenSize.height - frameSize.height ) / 2 );
        frame.setVisible( true );

    }

    //Main method
    public static void main( String[ ] arguments ) {

        // Parse the command line arguments

        for (int index = 0; arguments.length > index; ++index)
        {
        }

        // Set the "Look And Feel"
        try 
        {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName( ) );
        }
        catch (Exception exception)
        {
            exception.printStackTrace(System.err);
        }

        LessonManager.instance();
        new Editor(true);
    }

}
