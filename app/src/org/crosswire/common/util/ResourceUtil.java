package org.crosswire.common.util;

import java.net.URL;
import java.util.MissingResourceException;

/**
 * Better implemenetations of the getResource methods with less ambiguity and
 * that are less dependent on the specific classloader situation.
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
 * @author Joe Walker [joe at eireneh dot com]
 * @author DM Smith [ dmsmith555 at yahoo dot com ]
 * @version $Id: ResourceUtil.java,v 1.5 2004/08/16 22:07:35 joe Exp $
 */
public class ResourceUtil
{
    /**
     * Prevent Instansiation
     */
    private ResourceUtil()
    {
    }

    /**
     * Generic resource URL fetcher. One way or the other we'll find it!
     * Either as a relative or an absolute reference.
     * @param search The name of the resource (without a leading /) to find
     * @return The requested resource
     * @throws MissingResourceException if the resource can not be found
     */
    public static URL getResource(String search) throws MissingResourceException
    {
        return getResource(CallContext.instance().getCallingClass(), search);
    }

    /**
     * Generic resource URL fetcher. One way or the other we'll find it!
     * Either as a relative or an absolute reference.
     * @param clazz The resource to find
     * @return The requested resource
     * @throws MissingResourceException if the resource can not be found
     */
    public static URL getResource(Class clazz, String resourceName) throws MissingResourceException
    {
        URL resource = new CWClassLoader(clazz).findResource(resourceName);

        if (resource == null)
        {
            throw new MissingResourceException("Can't find resource", clazz.getName(), resourceName);
        }

        return resource;
    }
}
