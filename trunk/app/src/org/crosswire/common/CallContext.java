package org.crosswire.common;

/**
 * This singleton class provides a way for a method to determine
 * which class called it.
 * <p>
 * It has been tested to work in command line and WebStart environments.
 * 
 * <p><table border='1' cellPadding='3' cellSpacing='0'>
 * <tr><td bgColor='white' class='TableRowColor'><font size='-7'>
 *
 * Distribution Licence:<br />
 * JSword is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License,
 * version 2 as published by the Free Software Foundation.<br />
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.<br />
 * The License is available on the internet
 * <a href='http://www.gnu.org/copyleft/gpl.html'>here</a>, or by writing to:
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
 * MA 02111-1307, USA<br />
 * The copyright to this program is held by it's authors.
 * </font></td></tr></table>
 * @see gnu.gpl.Licence
 * @author DM Smith [ dmsmith555 at yahoo dot com]
 * @version $Id: CallContext.java,v 1.1 2004/04/20 21:16:06 joe Exp $
 */
public class CallContext extends SecurityManager
{
    /**
     * Prevent instansiation
     */
    private CallContext()
    {
    }

    /**
     * Singleton accessor
     */
    public static CallContext instance()
    {
        if (resolver == null)
        {
            resolver = new CallContext();
        }
        
        return resolver;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.SecurityManager#getClassContext()
     */
    protected Class[] getClassContext()
    {
        return super.getClassContext();
    }

    /**
     * When called from a method it will return the class
     * calling that method.
      */
    public Class getCallingClass()
    {
        return getCallingClass(1); // add 1 for this method
    }

    /**
     * When called from a method it will return the i-th class
     * calling that method, up the call chain.
     * If used with a -1 it will return the class making the call
     * -2 and -3 will return this class
     * @throws ArrayIndexOutOfBoundsException if the index is not valid
     */
    public Class getCallingClass(int i)
    {
        return resolver.getClassContext()[CALL_CONTEXT_OFFSET + i];
    }

    // may need to change if this class is redesigned
    /**
     * Offset needed to represent the caller of the method
     * that called this method.
     * 
     */
    private static final int CALL_CONTEXT_OFFSET = 3;

    private static CallContext resolver;
}
