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

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;

class MainMenu extends JMenuBar {

    //
    // Attributes
    //

    private JFrame frame;
    private JRadioButtonMenuItem trace, inform, warn, error;

    //
    // Methods
    //

    // ---------------
    MainMenu( JFrame frame ) {

        this.frame = frame;

        JMenu menu1, menu2;
        JMenuItem item;
        ButtonGroup buttonGroup;

        // Application Menu

        menu1 = new JMenu( "FlashCards" );

        // Application Menu -> Editor

        item = new JMenuItem( "Edit" );
        item.addActionListener( new EditAction( ) );
        menu1.add( item );

        // Application Menu -> Debugging

        if( Debug.getEnabled( ) ) {

            menu2 = new JMenu( "Debugging" );
            DebugAction debugAction = new DebugAction( );
            trace = new JRadioButtonMenuItem( "Trace" );
            if( Debug.getTrace( ) ) { trace.setSelected( true ); }
            trace.addActionListener( debugAction );
            menu2.add( trace );
            inform = new JRadioButtonMenuItem( "Inform" );
            if( Debug.getInform( ) ) { inform.setSelected( true ); }
            inform.addActionListener( debugAction );
            menu2.add( inform );
            warn = new JRadioButtonMenuItem( "Warn" );
            if( Debug.getWarn( ) ) { warn.setSelected( true ); }
            warn.addActionListener( debugAction );
            menu2.add( warn );
            error = new JRadioButtonMenuItem( "Error" );
            if( Debug.getError( ) ) { error.setSelected( true ); }
            error.addActionListener( debugAction );
            menu2.add( error );
            menu1.add( menu2 );

        }

        // Application Menu -> Exit

        item = new JMenuItem( "Exit" );
        item.addActionListener( new ExitAction( ) );
        menu1.add( item );
        add( menu1 );

        // Help Menu

        menu1 = new JMenu( "Help" );

        // Help Menu -> About

        item = new JMenuItem( "About" );
        item.addActionListener( new AboutAction( ) );
        menu1.add( item );

        // Add the menu

        add( menu1 );

    }

    //
    // Classes
    //

    // ---------------
    class EditAction extends AbstractAction {

        public void actionPerformed( ActionEvent event ) {

            Debug.trace( this.toString( ), "Beginning\n" );
            new Editor( false );
            Debug.trace( this.toString( ), "Ending\n" );

        }

    }

    // ---------------
    class DebugAction extends AbstractAction {

        public void actionPerformed( ActionEvent event ) {

            Debug.trace( this.toString( ), "Beginning\n" );
            Debug.inform( this.toString( ),
                          "event.getActionCommand( ) = " + event.getActionCommand( ) + "\n" );

            if( event.getActionCommand( ).equals( "Trace" ) ) {

                if( trace.isSelected( ) ) { Debug.setTrace( true ); }
                else { Debug.setTrace( false ); }

            } else if( event.getActionCommand( ).equals( "Inform" ) ) {

                if( inform.isSelected( ) ) { Debug.setInform( true ); }
                else { Debug.setInform( false ); }

            } else if( event.getActionCommand( ).equals( "Warn" ) ) {

                if( warn.isSelected( ) ) { Debug.setWarn( true ); }
                else { Debug.setWarn( false ); }

            } else if( event.getActionCommand( ).equals( "Error" ) ) {

                if( error.isSelected( ) ) { Debug.setError( true ); }
                else { Debug.setError( false ); }

            }

            Debug.trace( this.toString( ), "Ending\n" );

        }

    }

    // ---------------
    class ExitAction extends AbstractAction {

        public ExitAction( ) { super( ); }

        public void actionPerformed( ActionEvent event ) {

            Debug.trace( this.toString( ), "Beginning\n" );
            System.exit( 0 );
            Debug.trace( this.toString( ), "Ending\n" );

        }

    }

    // ---------------
    class AboutAction extends AbstractAction {

        public void actionPerformed( ActionEvent event ) {

            String aboutString = new String( "FlashCards\n" +
                                             "A Vocabulary Training Tool by CrossWire\n" +
                                             "(c) 2004 CrossWire Bible Society\n" +
                                             "http://crosswire.org" );

            Debug.trace( this.toString( ), "Beginning\n" );
            JOptionPane.showMessageDialog( frame,
                                           aboutString,
                                           "About FlashCards",
                                           JOptionPane.INFORMATION_MESSAGE );
            Debug.trace( this.toString( ), "Ending\n" );

        }

    }

}
