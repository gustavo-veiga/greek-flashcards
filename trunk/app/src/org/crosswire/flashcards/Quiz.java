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
 * The copyright to this program is held by it's authors.
 * Copyright: 2004
 */
///////////////////////////////////////////////////////////////////////////
//
// Quiz.java
//
// The start of it all
//
// Copyright : 2004 CrossWire Bible Society http://crosswire.org
//
///////////////////////////////////////////////////////////////////////////

package org.crosswire.flashcards;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.UIManager;

public class Quiz {

    //
    // Attributes
    //

    boolean packFrame = false;

    //
    // Methods
    //

    // ---------------
    public Quiz( ) {
        
        LessonManager lm = new LessonManager();

        MainFrame frame = new MainFrame(lm);

        // Validate frames that have preset sizes
        // Pack frames that have useful preferred size info,
        // e.g. from their layout
        if( packFrame ) { frame.pack( ); }
        else { frame.validate( ); }

        //Center the window

        Dimension screenSize = Toolkit.getDefaultToolkit( ).getScreenSize( );
        Dimension frameSize = frame.getSize( );

        if( frameSize.height > screenSize.height ) {

            frameSize.height = screenSize.height;

        }

        if( frameSize.width > screenSize.width ) {

            frameSize.width = screenSize.width;

        }

        frame.setLocation( ( screenSize.width - frameSize.width ) / 2,
                           ( screenSize.height - frameSize.height ) / 2 );
        frame.setVisible( true );

    }

    // ---------------
    public static void main( String [ ] arguments ) {

        // Parse the command line arguments

        for( int index = 0; arguments.length > index; ++ index ) {

            if( ( arguments [ index ] ).equals( "-debug" ) ) {

                Debug.setEnabled( true );

            }

        }

        // Set the "Look And Feel"

        try {

            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName( ) );

        } catch(Exception e ) {

            e.printStackTrace();

        }

        // Go...

        new Quiz( );

    }

}
