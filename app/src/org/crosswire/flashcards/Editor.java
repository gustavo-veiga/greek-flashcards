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

import java.awt.Dimension;
import java.awt.Toolkit;

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
    public Editor( boolean standAlone ) {

        EditorFrame frame = new EditorFrame( standAlone );

        //Validate frames that have preset sizes
        //Pack frames that have useful preferred size info, e.g. from their layout

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

    //Main method
    public static void main( String[ ] arguments ) {

        // Parse the command line arguments

        for( int index = 0; arguments.length > index; ++ index ) {

            if( ( arguments [ index ] ).equals( "-debug" ) ) {

                Debug.setEnabled( true );

            }

        }

        // Set the "Look And Feel"

        try {

            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName( ) );

        } catch( Exception exception ) {

            exception.printStackTrace( );

        }

        new Editor( true );

    }

}
