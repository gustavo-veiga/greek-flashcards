///////////////////////////////////////////////////////////////////////////
//
// Debug.java
//
// Help with debugging!
//
// Copyright : 2004 CrossWire Bible Society http://crosswire.org
//
///////////////////////////////////////////////////////////////////////////

package org.crosswire.flashcards;

import java.io.*;

class Debug {

    //
    // Attributes
    //

    private static boolean enabled = false;
    private static boolean trace = true;
    private static boolean inform = true;
    private static boolean warn = true;
    private static boolean error = true;
    private static PrintStream printStream = System.err;

    //
    // Methods
    //

    // ---------------
    static boolean getEnabled( ) { return enabled; }

    // ---------------
    static void setEnabled( boolean enabled ) { Debug.enabled = enabled; }

    // ---------------
    static boolean getTrace( ) { return trace; }

    // ---------------
    static void setTrace( boolean trace ) { Debug.trace = trace; }

    // ---------------
    static boolean getInform( ) { return inform; }

    // ---------------
    static void setInform( boolean inform ) { Debug.inform = inform; }

    // ---------------
    static boolean getWarn( ) { return warn; }

    // ---------------
    static void setWarn( boolean warn ) { Debug.warn = warn; }

    // ---------------
    static boolean getError( ) { return error; }

    // ---------------
    static void setError( boolean error ) { Debug.error = error; }

    // ---------------
    static PrintStream getPrintStream( ) { return printStream; }

    // ---------------
    static void setPrintStream( PrintStream printStream ) {

        Debug.printStream = printStream;

    }

    // ---------------
    static void trace( String identity, String message ) {

        if( null != printStream && false != trace && false != enabled ) {

            Debug.printStream.print( " TRACE : " + identity + " : " +
                                     Thread.currentThread( ) + "\n" + message );

        }

    }

    // ---------------
    static void inform( String identity, String message ) {

        if( null != printStream && false != inform && false != enabled ) {

            Debug.printStream.print( "INFORM : " + identity + " : " +
                                     Thread.currentThread( ) + "\n" + message );

        }

    }

    // ---------------
    static void warn( String identity, String message ) {

        if( null != printStream && false != warn && false != enabled ) {

            Debug.printStream.print( "  WARN : " + identity + " : " +
                                     Thread.currentThread( ) + "\n" + message );

        }

    }

    // ---------------
    static void error( String identity, String message ) {

        if( null != printStream && false != error ) {

            Debug.printStream.print( " ERROR : " + identity + " : " +
                                     Thread.currentThread( ) + "\n" + message );

        }

    }

}
