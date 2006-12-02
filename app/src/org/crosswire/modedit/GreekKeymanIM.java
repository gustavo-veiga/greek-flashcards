package org.crosswire.modedit;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001 CrossWire Bible Society under the terms of the GNU GPL
 * Company:
 * @author Troy A. Griffitts
 * @version 1.0
 */

import java.util.Hashtable;

public class GreekKeymanIM
    extends SWInputMethod {

  private Hashtable charMap = new Hashtable();

  public GreekKeymanIM(String name) {
    super(name);
    init();
  }

  public String translate(char in) {
    String retVal = (String) charMap.get(new Integer(in));
    if (retVal == null) {
      retVal = "" + in;

    }
    return retVal;
  }

  private void init() {
    charMap.put(new Integer('a'), new String(new char[] {0x03b1}));
    charMap.put(new Integer('b'), new String(new char[] {0x03b2}));
    charMap.put(new Integer('g'), new String(new char[] {0x03b3}));
    charMap.put(new Integer('d'), new String(new char[] {0x03b4}));
    charMap.put(new Integer('e'), new String(new char[] {0x03b5}));
    charMap.put(new Integer('z'), new String(new char[] {0x03b6}));
    charMap.put(new Integer('h'), new String(new char[] {0x03b7}));
    charMap.put(new Integer('q'), new String(new char[] {0x03b8}));
    charMap.put(new Integer('i'), new String(new char[] {0x03b9}));
    charMap.put(new Integer('k'), new String(new char[] {0x03ba}));
    charMap.put(new Integer('l'), new String(new char[] {0x03bb}));
    charMap.put(new Integer('m'), new String(new char[] {0x03bc}));
    charMap.put(new Integer('n'), new String(new char[] {0x03bd}));
    charMap.put(new Integer('c'), new String(new char[] {0x03be}));
    charMap.put(new Integer('o'), new String(new char[] {0x03bf}));
    charMap.put(new Integer('p'), new String(new char[] {0x03c0}));
    charMap.put(new Integer('r'), new String(new char[] {0x03c1}));
    charMap.put(new Integer('s'), new String(new char[] {0x03c3}));
    charMap.put(new Integer('"'), new String(new char[] {0x03c2}));
    charMap.put(new Integer('t'), new String(new char[] {0x03c4}));
    charMap.put(new Integer('u'), new String(new char[] {0x03c5}));
    charMap.put(new Integer('f'), new String(new char[] {0x03c6}));
    charMap.put(new Integer('x'), new String(new char[] {0x03c7}));
    charMap.put(new Integer('y'), new String(new char[] {0x03c8}));
    charMap.put(new Integer('w'), new String(new char[] {0x03c9}));
    charMap.put(new Integer('/'), new String(new char[] {0x0301}));
    charMap.put(new Integer('\\'), new String(new char[] {0x0300}));
    charMap.put(new Integer('='), new String(new char[] {0x0342}));
    charMap.put(new Integer(']'), new String(new char[] {0x0313}));
    charMap.put(new Integer('['), new String(new char[] {0x0314}));
    charMap.put(new Integer('|'), new String(new char[] {0x0345}));
    charMap.put(new Integer('+'), new String(new char[] {0x0308}));
    charMap.put(new Integer('^'), new String(new char[] {0x0311}));
    charMap.put(new Integer('-'), new String(new char[] {0x0306}));
    charMap.put(new Integer('_'), new String(new char[] {0x0304}));
    charMap.put(new Integer(':'), new String(new char[] {0x0387}));
    charMap.put(new Integer(';'), new String(new char[] {0x037e}));
    charMap.put(new Integer('A'), new String(new char[] {0x0391}));
    charMap.put(new Integer('B'), new String(new char[] {0x0392}));
    charMap.put(new Integer('G'), new String(new char[] {0x0393}));
    charMap.put(new Integer('D'), new String(new char[] {0x0394}));
    charMap.put(new Integer('E'), new String(new char[] {0x0395}));
    charMap.put(new Integer('Z'), new String(new char[] {0x0396}));
    charMap.put(new Integer('H'), new String(new char[] {0x0397}));
    charMap.put(new Integer('Q'), new String(new char[] {0x0398}));
    charMap.put(new Integer('I'), new String(new char[] {0x0399}));
    charMap.put(new Integer('K'), new String(new char[] {0x039a}));
    charMap.put(new Integer('L'), new String(new char[] {0x039b}));
    charMap.put(new Integer('M'), new String(new char[] {0x039c}));
    charMap.put(new Integer('N'), new String(new char[] {0x039d}));
    charMap.put(new Integer('C'), new String(new char[] {0x039e}));
    charMap.put(new Integer('O'), new String(new char[] {0x039f}));
    charMap.put(new Integer('P'), new String(new char[] {0x03a0}));
    charMap.put(new Integer('R'), new String(new char[] {0x03a1}));
    charMap.put(new Integer('S'), new String(new char[] {0x03a3}));
    charMap.put(new Integer('T'), new String(new char[] {0x03a4}));
    charMap.put(new Integer('U'), new String(new char[] {0x03a5}));
    charMap.put(new Integer('F'), new String(new char[] {0x03a6}));
    charMap.put(new Integer('X'), new String(new char[] {0x03a7}));
    charMap.put(new Integer('Y'), new String(new char[] {0x03a8}));
    charMap.put(new Integer('W'), new String(new char[] {0x03a9}));
  }
}
