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
// Debug.java
//
// Help with debugging!
//
// Copyright : 2004 CrossWire Bible Society http://crosswire.org
//
///////////////////////////////////////////////////////////////////////////

package org.crosswire.flashcards;

import java.io.PrintStream;

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

    // Singleton class
    private Debug() {}

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

        if( null != printStream && trace && enabled ) {

            Debug.printStream.print( " TRACE : " + identity + " : " +
                                     Thread.currentThread( ) + "\n" + message );

        }

    }

    // ---------------
    static void inform( String identity, String message ) {

        if( null != printStream && inform && enabled ) {

            Debug.printStream.print( "INFORM : " + identity + " : " +
                                     Thread.currentThread( ) + "\n" + message );

        }

    }

    // ---------------
    static void warn( String identity, String message ) {

        if( null != printStream && warn && enabled ) {

            Debug.printStream.print( "  WARN : " + identity + " : " +
                                     Thread.currentThread( ) + "\n" + message );

        }

    }

    // ---------------
    static void error( String identity, String message ) {

        if( null != printStream && error ) {

            Debug.printStream.print( " ERROR : " + identity + " : " +
                                     Thread.currentThread( ) + "\n" + message );

        }

    }

}
