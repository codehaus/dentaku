/*
 * Created on Mar 1, 2005
 *
 * Copyright STPenable Ltd. (c) 2004
 * 
 */
package org.codehaus.dentaku.summit.parameters;

import java.beans.IndexedPropertyDescriptor;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

import org.codehaus.plexus.summit.parameters.RequestParameters;

/**
 * @author David Wynter
 *
 * Add comment here
 */
public class DentakuSummitParameterParser {

    /**
     * Random access storage for parameter data.  The keys must always be
     * Strings.  The values will be arrays of Strings.
     */
    private RequestParameters parameters;
    
    public DentakuSummitParameterParser(RequestParameters parameters) {
    	this.parameters = parameters;
    }
    
    /**
     * Determine whether a given key has been inserted.  All keys are
     * stored in lowercase strings, so override method to account for
     * this.
     *
     * @param key An Object with the key to search for.
     * @return True if the object is found.
     */
    public boolean containsKey(Object key)
    {
        return parameters.containsKey(((String)key).toLowerCase());
    }

	/**
     * Uses bean introspection to set writable properties of bean from
     * the parameters, where a (case-insensitive) name match between
     * the bean property and the parameter is looked for.
     *
     * @param bean An Object.
     * @exception Exception a generic exception.
     */
    public void setProperties(Object bean) throws Exception
    {
        Class beanClass = bean.getClass();
        PropertyDescriptor[] props
                = Introspector.getBeanInfo(beanClass).getPropertyDescriptors();

        for (int i = 0; i < props.length; i++)
        {
            String propname = props[i].getName();
            Method setter = props[i].getWriteMethod();
            if (setter != null &&
                    (containsKey(propname)))
            {
                setProperty(bean, props[i]);
            }
        }
    }

    /**
     * Return an array of Strings for the given name.  If the name
     * does not exist, return null. Assumes lowercase folding
     *
     * @param name A String with the name.
     * @return A String[].
     */
    public String[] getStrings(String name)
    {
        return (String[]) parameters.getStrings(name.toLowerCase());
    }

    /**
     * Simple method that attempts to get a textual representation of
     * this object's name/value pairs.  String[] handling is currently
     * a bit rough.
     *
     * @return A textual representation of the parsed name/value pairs.
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        for (Iterator iter = ((RequestParameters) parameters).keys(); iter.hasNext();)
        {
            String name = (String) iter.next();
            try
            {
                sb.append('{');
                sb.append(name);
                sb.append('=');
                String[] params = this.getStrings(name);
                if (params.length <= 1)
                {
                    sb.append(params[0]);
                }
                else
                {
                    for (int i = 0; i < params.length; i++)
                    {
                        if (i != 0)
                        {
                            sb.append(", ");
                        }
                        sb.append('[')
                                .append(params[i])
                                .append(']');
                    }
                }
                sb.append("}\n");
            }
            catch (Exception ee)
            {
                try
                {
                    sb.append('{');
                    sb.append(name);
                    sb.append('=');
                    sb.append("ERROR?");
                    sb.append("}\n");
                }
                catch (Exception eee)
                {
                }
            }
        }
        return sb.toString();
    }

    /**
     * Set the property 'prop' in the bean to the value of the
     * corresponding parameters.  Supports all types supported by
     * getXXX methods plus a few more that come for free because
     * primitives have to be wrapped before being passed to invoke
     * anyway.
     *
     * @param bean An Object.
     * @param prop A PropertyDescriptor.
     * @exception Exception a generic exception.
     */
    protected void setProperty(Object bean,
                               PropertyDescriptor prop)
            throws Exception
    {
        if (prop instanceof IndexedPropertyDescriptor)
        {
            throw new Exception(prop.getName() +
                    " is an indexed property (not supported)");
        }

        Method setter = prop.getWriteMethod();
        if (setter == null)
        {
            throw new Exception(prop.getName() +
                    " is a read only property");
        }

        Class propclass = prop.getPropertyType();
        Object[] args = {null};

        if (propclass == String.class)
        {
            args[0] = ((RequestParameters) parameters).getString(prop.getName());
        }
        else if (propclass == Integer.class || propclass == Integer.TYPE)
        {
            args[0] = getIntObject(prop.getName());
        }
        else if (propclass == Long.class || propclass == Long.TYPE)
        {
            args[0] = new Long(((RequestParameters) parameters).getLong(prop.getName()));
        }
        else if (propclass == Boolean.class || propclass == Boolean.TYPE)
        {
            args[0] = getBooleanObject(prop.getName());
        }
        else if (propclass == Double.class || propclass == Double.TYPE)
        {
            args[0] = new Double(((RequestParameters) parameters).getDouble(prop.getName()));
        }
        else if (propclass == BigDecimal.class)
        {
            args[0] = ((RequestParameters) parameters).getBigDecimal(prop.getName());
        }
        else if (propclass == String[].class)
        {
            args[0] = getStrings(prop.getName());
        }
        else if (propclass == Object.class)
        {
            args[0] = ((RequestParameters) parameters).getObject(prop.getName());
        }
        else if (propclass == int[].class)
        {
            args[0] = ((RequestParameters) parameters).getInts(prop.getName());
        }
        else if (propclass == Integer[].class)
        {
            args[0] = getIntObjects(prop.getName());
        }
        else if (propclass == Date.class)
        {
            args[0] = ((RequestParameters) parameters).getDate(prop.getName());
        }
        else
        {
            throw new Exception("property "
                    + prop.getName()
                    + " is of unsupported type "
                    + propclass.toString());
        }

        setter.invoke(bean, args);
    }
    
    /**
     * Return an Integer for the given name.  If the name does not exist,
     * return null.
     *
     * @param name A String with the name.
     * @return An Integer.
     */
    public Integer getIntObject(String name) throws Exception
    {
        Integer result = null;
        String value = ((RequestParameters) parameters).getString(name);     
        return getIntValue(name, value);
    }
    
    private Integer getIntValue(String name, String value) throws Exception {
    	Integer result = null;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(value))
        {
            try
            {
                result = new Integer(value);
            }
            catch(NumberFormatException e)
            {
            	throw new Exception("Parameter (" + name
                        + ") with value of ("
		                + value + ") could not be converted to a Integer");
            }
        }
        return result;
    }

    /**
     * Return an array of Integers for the given name.  If the name
     * does not exist, return null.
     *
     * @param name A String with the name.
     * @return An Integer[].
     * @throws Exception
     */
    public Integer[] getIntObjects(String name) throws Exception
    {
        Integer[] result = null;
        String value[] = getStrings(name.toLowerCase());
        if (value != null)
        {
            result = new Integer[value.length];
            for (int i = 0; i < value.length; i++) {
            	result[i] = getIntValue(name, value[i]);
            }
        }
        return result;
    }
    /**
     * Returns a Boolean object for the given name.  If the parameter
     * does not exist or can not be parsed as a boolean, null is returned.
     * <p>
     * Valid values for true: true, on, 1, yes<br>
     * Valid values for false: false, off, 0, no<br>
     * <p>
     * The string is compared without reguard to case.
     *
     * @param name A String with the name.
     * @return A Boolean.
     */
    public Boolean getBooleanObject(String name) throws Exception 
    {
        Boolean result = null;
        String value = ((RequestParameters) parameters).getString(name);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(value))
        {
            if (value.equals("1") ||
                    value.equalsIgnoreCase("true") ||
                    value.equalsIgnoreCase("yes") ||
                    value.equalsIgnoreCase("on"))
            {
                result = Boolean.TRUE;
            }
            else if (value.equals("0") ||
                    value.equalsIgnoreCase("false") ||
                    value.equalsIgnoreCase("no") ||
                    value.equalsIgnoreCase("off"))
            {
                result = Boolean.FALSE;
            }
            else
            {
                throw new Exception("Parameter (" + name
                        + ") with value of ("
		                + value + ") could not be converted to a Boolean");
            }
        }
        return result;
    }

}
