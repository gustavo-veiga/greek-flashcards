///////////////////////////////////////////////////////////////////////////
//
// CGreekIM.java
//
// Input Method to match the Emacs cgreek package.
//
// Copyright : 2004 CrossWire Bible Society http://crosswire.org
//
///////////////////////////////////////////////////////////////////////////

package org.crosswire.modedit;

import java.util.*;

public class CGreekIM extends SWInputMethod {

    //
    // Attributes
    //

    private Hashtable characterMap = new Hashtable();

    //
    // Methods
    //

    // ---------------
    public CGreekIM( String name ) {

        super( name );
        init( );

    }

    // ---------------
    public String translate( char input ) {

        String returnValue = ( String ) characterMap.get( new Integer( input ) );

        if( returnValue == null ) {

            returnValue = new String() + input;

        }

        return returnValue;

    }

    // ---------------
    private void init( ) {

        // Lower case alphabet
        characterMap.put( new Integer( 'a' ), new String( new char [ ] { 0x03b1 } ) );
        characterMap.put( new Integer( 'b' ), new String( new char [ ] { 0x03b2 } ) );
        characterMap.put( new Integer( 'g' ), new String( new char [ ] { 0x03b3 } ) );
        characterMap.put( new Integer( 'd' ), new String( new char [ ] { 0x03b4 } ) );
        characterMap.put( new Integer( 'e' ), new String( new char [ ] { 0x03b5 } ) );
        characterMap.put( new Integer( 'z' ), new String( new char [ ] { 0x03b6 } ) );
        characterMap.put( new Integer( 'h' ), new String( new char [ ] { 0x03b7 } ) );
        characterMap.put( new Integer( 'q' ), new String( new char [ ] { 0x03b8 } ) );
        characterMap.put( new Integer( 'i' ), new String( new char [ ] { 0x03b9 } ) );
        characterMap.put( new Integer( 'k' ), new String( new char [ ] { 0x03ba } ) );
        characterMap.put( new Integer( 'l' ), new String( new char [ ] { 0x03bb } ) );
        characterMap.put( new Integer( 'm' ), new String( new char [ ] { 0x03bc } ) );
        characterMap.put( new Integer( 'n' ), new String( new char [ ] { 0x03bd } ) );
        characterMap.put( new Integer( 'x' ), new String( new char [ ] { 0x03be } ) );
        characterMap.put( new Integer( 'o' ), new String( new char [ ] { 0x03bf } ) );
        characterMap.put( new Integer( 'p' ), new String( new char [ ] { 0x03c0 } ) );
        characterMap.put( new Integer( 'r' ), new String( new char [ ] { 0x03c1 } ) );
        characterMap.put( new Integer( 's' ), new String( new char [ ] { 0x03c3 } ) );
        characterMap.put( new Integer( 't' ), new String( new char [ ] { 0x03c4 } ) );
        characterMap.put( new Integer( 'u' ), new String( new char [ ] { 0x03c5 } ) );
        characterMap.put( new Integer( 'f' ), new String( new char [ ] { 0x03c6 } ) );
        characterMap.put( new Integer( 'c' ), new String( new char [ ] { 0x03be } ) );
        characterMap.put( new Integer( 'y' ), new String( new char [ ] { 0x03c8 } ) );
        characterMap.put( new Integer( 'w' ), new String( new char [ ] { 0x03c9 } ) );

        // Upper case alphabet
        characterMap.put( new Integer( 'A' ), new String( new char [ ] { 0x0391 } ) );
        characterMap.put( new Integer( 'B' ), new String( new char [ ] { 0x0392 } ) );
        characterMap.put( new Integer( 'C' ), new String( new char [ ] { 0x0393 } ) );
        characterMap.put( new Integer( 'D' ), new String( new char [ ] { 0x0394 } ) );
        characterMap.put( new Integer( 'E' ), new String( new char [ ] { 0x0395 } ) );
        characterMap.put( new Integer( 'Z' ), new String( new char [ ] { 0x0396 } ) );
        characterMap.put( new Integer( 'H' ), new String( new char [ ] { 0x0397 } ) );
        characterMap.put( new Integer( 'Q' ), new String( new char [ ] { 0x0398 } ) );
        characterMap.put( new Integer( 'I' ), new String( new char [ ] { 0x0399 } ) );
        characterMap.put( new Integer( 'K' ), new String( new char [ ] { 0x039a } ) );
        characterMap.put( new Integer( 'L' ), new String( new char [ ] { 0x039b } ) );
        characterMap.put( new Integer( 'M' ), new String( new char [ ] { 0x039c } ) );
        characterMap.put( new Integer( 'N' ), new String( new char [ ] { 0x039d } ) );
        characterMap.put( new Integer( 'X' ), new String( new char [ ] { 0x039e } ) );
        characterMap.put( new Integer( 'O' ), new String( new char [ ] { 0x039f } ) );
        characterMap.put( new Integer( 'P' ), new String( new char [ ] { 0x03a0 } ) );
        characterMap.put( new Integer( 'R' ), new String( new char [ ] { 0x03a1 } ) );
        characterMap.put( new Integer( 'S' ), new String( new char [ ] { 0x03a3 } ) );
        characterMap.put( new Integer( 'T' ), new String( new char [ ] { 0x03a4 } ) );
        characterMap.put( new Integer( 'U' ), new String( new char [ ] { 0x03a5 } ) );
        characterMap.put( new Integer( 'F' ), new String( new char [ ] { 0x03a6 } ) );
        characterMap.put( new Integer( 'C' ), new String( new char [ ] { 0x039e } ) );
        characterMap.put( new Integer( 'Y' ), new String( new char [ ] { 0x03a8 } ) );
        characterMap.put( new Integer( 'W' ), new String( new char [ ] { 0x03a9 } ) );

        // Terminal sigma
        characterMap.put( new Integer( 'j' ), new String( new char [ ] { 0x03c2 } ) );

        // Iota subscript
        characterMap.put( new Integer( 'J' ), new String( new char [ ] { 0x0345 } ) );

        // smooth breathing
        characterMap.put( new Integer( '\'' ), new String( new char [ ] { 0x0313 } ) );
        characterMap.put( new Integer( 'v' ),  new String( new char [ ] { 0x0313 } ) );

        // smooth breathing
        characterMap.put( new Integer( '`' ), new String( new char [ ] { 0x0314 } ) );
        characterMap.put( new Integer( 'V' ), new String( new char [ ] { 0x0314 } ) );

        // acute
        characterMap.put( new Integer( '/' ), new String( new char [ ] { 0x0301 } ) );

        // grave
        characterMap.put( new Integer( '?' ), new String( new char [ ] { 0x0300 } ) );

        // circumflex
        characterMap.put( new Integer( '\\' ), new String( new char [ ] { 0x0311 } ) );
        characterMap.put( new Integer( '^' ),  new String( new char [ ] { 0x0311 } ) );

        // trema (diaeresis)
        characterMap.put( new Integer( '"' ), new String( new char [ ] { 0x0308 } ) );

        // comma
        characterMap.put( new Integer( ',' ), new String( new char [ ] { 0x002c } ) );

        // full stop
        characterMap.put( new Integer( '.' ), new String( new char [ ] { 0x0387 } ) );

        // question mark
        characterMap.put( new Integer( ';' ), new String( new char [ ] { 0x037e } ) );

        // colon
        characterMap.put( new Integer( ':' ), new String( new char [ ] { 0x003a } ) );

        // sampi (upper case 0x3e0, lower case 0x3e1)
        characterMap.put( new Integer( '!' ), new String( new char [ ] { 0x03e0 } ) );

        // diagamma (upper case 0x3dc, lower case 0x3dd)
        characterMap.put( new Integer( '#' ), new String( new char [ ] { 0x03dc } ) );

        // stigma (upper case 0x3da, lower case 0x3db)
        characterMap.put( new Integer( '$' ), new String( new char [ ] { 0x03da } ) );

        // qoppa (lower case)
        characterMap.put( new Integer( '%' ), new String( new char [ ] { 0x03df } ) );

        // qoppa (upper case)
        characterMap.put( new Integer( '&' ), new String( new char [ ] { 0x03de } ) );

    }

}
