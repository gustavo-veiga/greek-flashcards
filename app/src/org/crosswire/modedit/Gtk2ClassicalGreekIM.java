///////////////////////////////////////////////////////////////////////////
//
// Gtk2ClassicalGreekIM.java
//
// Input Method to match the Emacs cgreek package.
//
// Copyright : 2004 CrossWire Bible Society http://crosswire.org
//
///////////////////////////////////////////////////////////////////////////

package org.crosswire.modedit;

import java.util.*;

///////////////////////////////////////////////////////////////////////////
//
// Mimics 'im-classicalgreek - A GTK2 Input Method for Classicla Greek'.
// See http://im-classgreek.sourceforge.net
//
// N.B. Due to the way 'UniTextEdit' works, the breathing must be
//      entered AFTER the vowel!
//
//          alpha - lower: a    upper: A
//           beta - lower: b    upper: B
//          gamma - lower: g    upper: G
//          delta - lower: d    upper: D
//        epsilon - lower: e    upper: E
//           zeta - lower: z    upper: Z
//            eta - lower: q    upper: Q
//          theta - lower: th   upper: TH,Th
//           iota - lower: i    upper: I
//          kappa - lower: k    upper: K
//         lambda - lower: l    upper: L
//             mu - lower: m    upper: M
//             nu - lower: n,v  upper: N,V
//             xi - lower: x    upper: X
//        omicron - lower: o    upper: O
//             pi - lower: p    upper: P
//            rho - lower: r    upper: R
//          sigma - lower: s    upper: S
// terminal sigma - lower: c
//            tau - lower: t    upper: T
//        upsilon - lower: u    upper: U
//            phi - lower: f,ph upper: F,PH,Ph
//            chi - lower: ch   upper: CH,Ch
//            psi - lower: ps   upper: PS,Ps
//          omega - lower: w    upper: W
//
// The breathing is entered after the vowel.
//
//     j - smooth
//     h - rough
//
// Iota subscript is entered by typing 'y' after
// the vowel.
//
// After entering the breathing (if any),
// the letter and the iota subscript (if present),
// accents may be added as follows.
//
//     ' - acute
//     ` - grave
//     ~ - circumflex
//     _ - macron
//
///////////////////////////////////////////////////////////////////////////

public class Gtk2ClassicalGreekIM extends SWInputMethod {

    //
    // Attributes
    //

    private Hashtable characterMap = new Hashtable();

    //
    // Methods
    //

    // ---------------
    public Gtk2ClassicalGreekIM( String name ) {

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

        // single input alphabet
        characterMap.put( new Integer( 'a' ), new String( new char [ ] { 0x03b1 } ) );
        characterMap.put( new Integer( 'A' ), new String( new char [ ] { 0x0391 } ) );
        characterMap.put( new Integer( 'b' ), new String( new char [ ] { 0x03b2 } ) );
        characterMap.put( new Integer( 'B' ), new String( new char [ ] { 0x0392 } ) );
        characterMap.put( new Integer( 'g' ), new String( new char [ ] { 0x03b3 } ) );
        characterMap.put( new Integer( 'G' ), new String( new char [ ] { 0x0393 } ) );
        characterMap.put( new Integer( 'd' ), new String( new char [ ] { 0x03b4 } ) );
        characterMap.put( new Integer( 'D' ), new String( new char [ ] { 0x0394 } ) );
        characterMap.put( new Integer( 'e' ), new String( new char [ ] { 0x03b5 } ) );
        characterMap.put( new Integer( 'E' ), new String( new char [ ] { 0x0395 } ) );
        characterMap.put( new Integer( 'z' ), new String( new char [ ] { 0x03b6 } ) );
        characterMap.put( new Integer( 'Z' ), new String( new char [ ] { 0x0396 } ) );
        characterMap.put( new Integer( 'q' ), new String( new char [ ] { 0x03b7 } ) );
        characterMap.put( new Integer( 'Q' ), new String( new char [ ] { 0x0397 } ) );
        characterMap.put( new Integer( 'i' ), new String( new char [ ] { 0x03b9 } ) );
        characterMap.put( new Integer( 'I' ), new String( new char [ ] { 0x0399 } ) );
        characterMap.put( new Integer( 'k' ), new String( new char [ ] { 0x03ba } ) );
        characterMap.put( new Integer( 'K' ), new String( new char [ ] { 0x039a } ) );
        characterMap.put( new Integer( 'l' ), new String( new char [ ] { 0x03bb } ) );
        characterMap.put( new Integer( 'L' ), new String( new char [ ] { 0x039b } ) );
        characterMap.put( new Integer( 'm' ), new String( new char [ ] { 0x03bc } ) );
        characterMap.put( new Integer( 'M' ), new String( new char [ ] { 0x039c } ) );
        characterMap.put( new Integer( 'n' ), new String( new char [ ] { 0x03bd } ) );
        characterMap.put( new Integer( 'v' ), new String( new char [ ] { 0x03bd } ) );
        characterMap.put( new Integer( 'N' ), new String( new char [ ] { 0x039d } ) );
        characterMap.put( new Integer( 'V' ), new String( new char [ ] { 0x039d } ) );
        characterMap.put( new Integer( 'x' ), new String( new char [ ] { 0x03be } ) ); 
        characterMap.put( new Integer( 'X' ), new String( new char [ ] { 0x039e } ) );
        characterMap.put( new Integer( 'o' ), new String( new char [ ] { 0x03bf } ) );
        characterMap.put( new Integer( 'O' ), new String( new char [ ] { 0x039f } ) );
        characterMap.put( new Integer( 'p' ), new String( new char [ ] { 0x03c0 } ) );
        characterMap.put( new Integer( 'P' ), new String( new char [ ] { 0x03a0 } ) );
        characterMap.put( new Integer( 'r' ), new String( new char [ ] { 0x03c1 } ) );
        characterMap.put( new Integer( 'R' ), new String( new char [ ] { 0x03a1 } ) );
        characterMap.put( new Integer( 's' ), new String( new char [ ] { 0x03c3 } ) );
        characterMap.put( new Integer( 'S' ), new String( new char [ ] { 0x03a3 } ) );
        characterMap.put( new Integer( 't' ), new String( new char [ ] { 0x03c4 } ) );
        characterMap.put( new Integer( 'T' ), new String( new char [ ] { 0x03a4 } ) );
        characterMap.put( new Integer( 'u' ), new String( new char [ ] { 0x03c5 } ) );
        characterMap.put( new Integer( 'U' ), new String( new char [ ] { 0x03a5 } ) );
        characterMap.put( new Integer( 'f' ), new String( new char [ ] { 0x03c6 } ) );
        characterMap.put( new Integer( 'F' ), new String( new char [ ] { 0x03a6 } ) );
        characterMap.put( new Integer( 'w' ), new String( new char [ ] { 0x03c9 } ) );
        characterMap.put( new Integer( 'W' ), new String( new char [ ] { 0x03a9 } ) );

        // Terminal sigma
        characterMap.put( new Integer( 'c' ), new String( new char [ ] { 0x03c2 } ) );

        // theta
        // phi
        // chi
        // psi
        characterMap.put( new Integer( 't' ), new String( new char [ ] { 0xffff } ) );
        characterMap.put( new Integer( 'p' ), new String( new char [ ] { 0xffff } ) );
        characterMap.put( new Integer( 'c' ), new String( new char [ ] { 0xffff } ) );

        // iota subscript
        characterMap.put( new Integer( 'y' ), new String( new char [ ] { 0x0345 } ) );

        // smooth breathing
        characterMap.put( new Integer( 'j' ), new String( new char [ ] { 0x0313 } ) );

        // rough breathing
        characterMap.put( new Integer( 'h' ), new String( new char [ ] { 0x0314 } ) );

        // acute
        characterMap.put( new Integer( '\'' ), new String( new char [ ] { 0x0301 } ) );

        // grave
        characterMap.put( new Integer( '`' ), new String( new char [ ] { 0x0300 } ) );

        // circumflex
        characterMap.put( new Integer( '~' ),  new String( new char [ ] { 0x0311 } ) );

        // trema (diaeresis)
        characterMap.put( new Integer( '"' ), new String( new char [ ] { 0x0308 } ) );

        // full stop
        characterMap.put( new Integer( '.' ), new String( new char [ ] { 0x0387 } ) );

        // question mark
        characterMap.put( new Integer( ';' ), new String( new char [ ] { 0x037e } ) );

    }

}
