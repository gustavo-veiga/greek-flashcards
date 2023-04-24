package org.crosswire.flashcards;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.Connector;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class Properties {

  private Hashtable values = new Hashtable();

  public Properties() {
  }

  public void load(String filename) throws Exception {
    InputStream is = null;
    Class c = this.getClass();
    is = c.getResourceAsStream(filename);
    if (is == null) {
      throw new Exception("File Does Not Exist");
    }
    load(is);
  }

  public void loadURL(String url) throws Exception {
    StreamConnection c = null;
    InputStream is = null;
    try {
        c = (StreamConnection)Connector.open(url);
        is = c.openInputStream();
        load(is);
    }
    finally {
       if (is != null)
           is.close();
       if (c != null)
           c.close();
   }
  }

  public void load(InputStream is) {
    String content = getInputStreamContents(is);
    int[] pos = new int[1];
    pos[0] = 0;
    for (String line = getLine(content, pos); pos[0] > -1;
         line = getLine(content, pos)) {

      // skip comments
      if (line.startsWith("#")) {
        continue;
      }

      int divider = line.indexOf('=');

      // skip invalid lines
      if (divider < 0) {
        continue;
      }

      String key = line.substring(0, divider);
      String value = line.substring(divider + 1);
      setProperty(key, value);
    }
  }

/*
  public static String getInputStreamContents(InputStream is) {
    InputStreamReader isr = null;
    StringBuffer buffer = null;
    char buf[] = new char[50];
    try {
      isr = new InputStreamReader(is, "UTF8");

      buffer = new StringBuffer();
      int len;
      while ( (len = isr.read(buf, 0, 50)) > -1) {
        buffer.append( buf, 0, len);
      }
      if (isr != null) {
        isr.close();
      }
    }
    catch (Exception ex) {
      System.out.println(ex);
    }
    return buffer.toString();
  }
*/
public static String getInputStreamContents(InputStream is) {
  InputStreamReader isr = null;
  StringBuffer buffer = null;
  try {
    isr = new InputStreamReader(is);//, "UTF8");

    buffer = new StringBuffer();
    int ch;
    while ( (ch = isr.read()) > -1) {
      buffer.append( (char) ch);
    }
    if (isr != null) {
      isr.close();
    }
  }
  catch (Exception ex) {
    System.out.println(ex);
  }
  return buffer.toString();

}


  public void setProperty(String key, String value) {
    values.put(key, value);
  }

  public String getProperty(String key) {
    return getProperty(key, null);
  }

  public String getProperty(String key, String defaultValue) {
    String ret = (String) values.get(key);
    return (ret != null) ? ret : defaultValue;
  }

  private String getLine(String content, int[] pos) {
    int start = pos[0];
    int end = content.indexOf('\r', start);
    if (end < 0) {
      end = content.indexOf('\n', start);
    }
    pos[0] = (end > -1) ? end + 1 : end;
    String line = (end > -1) ? content.substring(start, end) :
        content.substring(start);
    return processUnicode(line.trim());
  }

  private String processUnicode(String in) {
    int offset = 0;
    while (true) {
      offset = in.indexOf("\\u");
      if ((offset < 0) || (offset > in.length() - 5)) {
        break;
      }
      String val = in.substring(offset+2,offset+6);
      short valueAsShort = Short.parseShort(val.trim(),16);
      in = in.substring(0,offset) + (char)valueAsShort + in.substring(offset+6);
    }
    return in;
  }

  private String processEscapes(String in) {
    int offset = 0;
    while (true) {
      offset = in.indexOf("\\", offset);
      if ((offset < 0) || (offset > in.length() - 1)) {
        break;
      }
      String val = in.substring(offset+2,offset+6);
      short valueAsShort = Short.parseShort(val.trim(),16);
      in = in.substring(0,offset) + in.substring(offset+2);
      if (in.charAt(offset) == '\\') {
        offset++;
      }
    }
    return in;
  }

}
