package com.github.terefang.jmelange.commons.match;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SimpleFilter implements Filter
{
    public static final int AND = 1;
    public static final int OR = 2;
    public static final int NOT = 3;
    public static final int EQ = 4;
    public static final int LTE = 5;
    public static final int GTE = 6;
    public static final int SUBSTRING = 7;
    public static final int PRESENT = 8;
    public static final int APPROX = 9;
    public static final int REGEXP = 10;

    private final String m_name;
    private final Object m_value;
    private final int m_op;

    public SimpleFilter(String attr, Object value, int op)
    {
        m_name = attr;
        m_value = value;
        m_op = op;
    }

    public String getName()
    {
        return m_name;
    }

    public Object getValue()
    {
        return m_value;
    }

    public int getOperation()
    {
        return m_op;
    }

    public String toString()
    {
        String s = null;
        switch (m_op)
        {
            case AND:
                s = "(&" + toString((List) m_value) + ")";
                break;
            case OR:
                s = "(|" + toString((List) m_value) + ")";
                break;
            case NOT:
                s = "(!" + toString((List) m_value) + ")";
                break;
            case EQ:
                s = "(" + m_name + "=" + toEncodedString(m_value) + ")";
                break;
            case LTE:
                s = "(" + m_name + "<=" + toEncodedString(m_value) + ")";
                break;
            case GTE:
                s = "(" + m_name + ">=" + toEncodedString(m_value) + ")";
                break;
            case SUBSTRING:
                s = "(" + m_name + "=" + unparseSubstring((List<String>) m_value) + ")";
                break;
            case PRESENT:
                s = "(" + m_name + "=*)";
                break;
            case APPROX:
                s = "(" + m_name + "~=" + toEncodedString(m_value) + ")";
                break;
            case REGEXP:
                s = "(" + m_name + "~~" + toEncodedString(m_value) + ")";
                break;
        }
        return s;
    }

    private static String toString(List list)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++)
        {
            sb.append(list.get(i).toString());
        }
        return sb.toString();
    }

    private static String toDecodedString(String s, int startIdx, int endIdx)
    {
        StringBuffer sb = new StringBuffer(endIdx - startIdx);
        boolean escaped = false;
        for (int i = 0; i < (endIdx - startIdx); i++)
        {
            char c = s.charAt(startIdx + i);
            if (!escaped && (c == '\\'))
            {
                escaped = true;
            }
            else
            {
                escaped = false;
                sb.append(c);
            }
        }

        return sb.toString();
    }

    private static String toEncodedString(Object o)
    {
        if (o instanceof String)
        {
            String s = (String) o;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < s.length(); i++)
            {
                char c = s.charAt(i);
                if ((c == '\\') || (c == '(') || (c == ')') || (c == '*'))
                {
                    sb.append('\\');
                }
                sb.append(c);
            }

            o = sb.toString();
        }

        return o.toString();
    }

    public static SimpleFilter parse(String filter)
    {
        int idx = skipWhitespace(filter, 0);

        if ((filter == null) || (filter.length() == 0) || (idx >= filter.length()))
        {
            throw new IllegalArgumentException("Null or empty filter.");
        }
        else if (filter.charAt(idx) != '(')
        {
            throw new IllegalArgumentException("Missing opening parenthesis: " + filter);
        }

        SimpleFilter sf = null;
        List stack = new ArrayList();
        boolean isEscaped = false;
        while (idx < filter.length())
        {
            if (sf != null)
            {
                throw new IllegalArgumentException(
                    "Only one top-level operation allowed: " + filter);
            }

            if (!isEscaped && (filter.charAt(idx) == '('))
            {
                // Skip paren and following whitespace.
                idx = skipWhitespace(filter, idx + 1);

                if (filter.charAt(idx) == '&')
                {
                    int peek = skipWhitespace(filter, idx + 1);
                    if (filter.charAt(peek) == '(')
                    {
                        idx = peek - 1;
                        stack.add(0, new SimpleFilter(null, new ArrayList(), SimpleFilter.AND));
                    }
                    else
                    {
                        stack.add(0, new Integer(idx));
                    }
                }
                else if (filter.charAt(idx) == '|')
                {
                    int peek = skipWhitespace(filter, idx + 1);
                    if (filter.charAt(peek) == '(')
                    {
                        idx = peek - 1;
                        stack.add(0, new SimpleFilter(null, new ArrayList(), SimpleFilter.OR));
                    }
                    else
                    {
                        stack.add(0, new Integer(idx));
                    }
                }
                else if (filter.charAt(idx) == '!')
                {
                    int peek = skipWhitespace(filter, idx + 1);
                    if (filter.charAt(peek) == '(')
                    {
                        idx = peek - 1;
                        stack.add(0, new SimpleFilter(null, new ArrayList(), SimpleFilter.NOT));
                    }
                    else
                    {
                        stack.add(0, new Integer(idx));
                    }
                }
                else
                {
                    stack.add(0, new Integer(idx));
                }
            }
            else if (!isEscaped && (filter.charAt(idx) == ')'))
            {
                Object top = stack.remove(0);
                if (top instanceof SimpleFilter)
                {
                    if (!stack.isEmpty() && (stack.get(0) instanceof SimpleFilter))
                    {
                        ((List) ((SimpleFilter) stack.get(0)).m_value).add(top);
                    }
                    else
                    {
                        sf = (SimpleFilter) top;
                    }
                }
                else if (!stack.isEmpty() && (stack.get(0) instanceof SimpleFilter))
                {
                    ((List) ((SimpleFilter) stack.get(0)).m_value).add(
                        SimpleFilter.subfilter(filter, ((Integer) top).intValue(), idx));
                }
                else
                {
                    sf = SimpleFilter.subfilter(filter, ((Integer) top).intValue(), idx);
                }
            }
            else if (!isEscaped && (filter.charAt(idx) == '\\'))
            {
                isEscaped = true;
            }
            else
            {
                isEscaped = false;
            }

            idx = skipWhitespace(filter, idx + 1);
        }

        if (sf == null)
        {
            throw new IllegalArgumentException("Missing closing parenthesis: " + filter);
        }

        return sf;
    }

    private static SimpleFilter subfilter(String filter, int startIdx, int endIdx)
    {
        final String opChars = "=<>~";

        // Determine the ending index of the attribute name.
        int attrEndIdx = startIdx;
        for (int i = 0; i < (endIdx - startIdx); i++)
        {
            char c = filter.charAt(startIdx + i);
            if (opChars.indexOf(c) >= 0)
            {
                break;
            }
            else if (!Character.isWhitespace(c))
            {
                attrEndIdx = startIdx + i + 1;
            }
        }
        if (attrEndIdx == startIdx)
        {
            throw new IllegalArgumentException(
                "Missing attribute name: " + filter.substring(startIdx, endIdx));
        }
        String attr = filter.substring(startIdx, attrEndIdx);

        // Skip the attribute name and any following whitespace.
        startIdx = skipWhitespace(filter, attrEndIdx);

        // Determine the operator type.
        int op = -1;
        switch (filter.charAt(startIdx))
        {
            case '=':
                op = EQ;
                startIdx++;
                break;
            case '<':
                if (filter.charAt(startIdx + 1) != '=')
                {
                    throw new IllegalArgumentException(
                        "Unknown operator: " + filter.substring(startIdx, endIdx));
                }
                op = LTE;
                startIdx += 2;
                break;
            case '>':
                if (filter.charAt(startIdx + 1) != '=')
                {
                    throw new IllegalArgumentException(
                        "Unknown operator: " + filter.substring(startIdx, endIdx));
                }
                op = GTE;
                startIdx += 2;
                break;
            case '~':
                if (filter.charAt(startIdx + 1) == '=')
                {
                    op = APPROX;
                }
                else if (filter.charAt(startIdx + 1) == '~')
                {
                    op = REGEXP;
                }
                else
                {
                    throw new IllegalArgumentException(
                        "Unknown operator: " + filter.substring(startIdx, endIdx));
                }
                startIdx += 2;
                break;
            default:
                throw new IllegalArgumentException(
                    "Unknown operator: " + filter.substring(startIdx, endIdx));
        }

        // Parse value.
        Object value = toDecodedString(filter, startIdx, endIdx);

        // Check if the equality comparison is actually a substring
        // or present operation.
        if (op == EQ)
        {
            String valueStr = filter.substring(startIdx, endIdx);
            List<String> values = parseSubstring(valueStr);
            if ((values.size() == 2)
                && (values.get(0).length() == 0)
                && (values.get(1).length() == 0))
            {
                op = PRESENT;
            }
            else if (values.size() > 1)
            {
                op = SUBSTRING;
                value = values;
            }
        }

        return new SimpleFilter(attr, value, op);
    }

    public static List<String> parseSubstring(String value)
    {
        List<String> pieces = new ArrayList();
        StringBuffer ss = new StringBuffer();
        // int kind = SIMPLE; // assume until proven otherwise
        boolean wasStar = false; // indicates last piece was a star
        boolean leftstar = false; // track if the initial piece is a star
        boolean rightstar = false; // track if the final piece is a star

        int idx = 0;

        // We assume (sub)strings can contain leading and trailing blanks
        boolean escaped = false;
        for (;;)
        {
            if (idx >= value.length())
            {
                if (wasStar)
                {
                    // insert last piece as "" to handle trailing star
                    rightstar = true;
                }
                else
                {
                    pieces.add(ss.toString());
                    // accumulate the last piece
                    // note that in the case of
                    // (cn=); this might be
                    // the string "" (!=null)
                }
                ss.setLength(0);
                break;
            }

            // Read the next character and account for escapes.
            char c = value.charAt(idx++);
            if (!escaped && ((c == '(') || (c == ')')))
            {
                throw new IllegalArgumentException(
                    "Illegal value: " + value);
            }
            else if (!escaped && (c == '*'))
            {
                if (wasStar)
                {
                    // encountered two successive stars;
                    // I assume this is illegal
                    throw new IllegalArgumentException("Invalid filter string: " + value);
                }
                if (ss.length() > 0)
                {
                    pieces.add(ss.toString()); // accumulate the pieces
                    // between '*' occurrences
                }
                ss.setLength(0);
                // if this is a leading star, then track it
                if (pieces.size() == 0)
                {
                    leftstar = true;
                }
                wasStar = true;
            }
            else if (!escaped && (c == '\\'))
            {
                escaped = true;
            }
            else
            {
                escaped = false;
                wasStar = false;
                ss.append(c);
            }
        }
        if (leftstar || rightstar || pieces.size() > 1)
        {
            // insert leading and/or trailing "" to anchor ends
            if (rightstar)
            {
                pieces.add("");
            }
            if (leftstar)
            {
                pieces.add(0, "");
            }
        }
        return pieces;
    }

    public static String unparseSubstring(List<String> pieces)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < pieces.size(); i++)
        {
            if (i > 0)
            {
                sb.append("*");
            }
            sb.append(toEncodedString(pieces.get(i)));
        }
        return sb.toString();
    }

    public static boolean compareSubstring(List<String> pieces, String s)
    {
        // Walk the pieces to match the string
        // There are implicit stars between each piece,
        // and the first and last pieces might be "" to anchor the match.
        // assert (pieces.length > 1)
        // minimal case is <string>*<string>

        boolean result = true;
        int len = pieces.size();

        int index = 0;

        for (int i = 0; i < len; i++)
        {
            String piece = pieces.get(i);

            // If this is the first piece, then make sure the
            // string starts with it.
            if (i == 0)
            {
                if (!s.startsWith(piece))
                {
                    result = false;
                    break;
                }
            }

            // If this is the last piece, then make sure the
            // string ends with it.
            if (i == len - 1)
            {
                if (s.endsWith(piece))
                {
                    result = true;
                }
                else
                {
                    result = false;
                }
                break;
            }

            // If this is neither the first or last piece, then
            // make sure the string contains it.
            if ((i > 0) && (i < (len - 1)))
            {
                index = s.indexOf(piece, index);
                if (index < 0)
                {
                    result = false;
                    break;
                }
            }

            // Move string index beyond the matching piece.
            index += piece.length();
        }

        return result;
    }

    private static int skipWhitespace(String s, int startIdx)
    {
        int len = s.length();
        while ((startIdx < len) && Character.isWhitespace(s.charAt(startIdx)))
        {
            startIdx++;
        }
        return startIdx;
    }

	public boolean match(Object object) 
	{
		return matchInternal(object, this);
	}

	private static final boolean matchInternal(Object cap, SimpleFilter sf) 
	{
		boolean matched = true;

        if (sf.getOperation() == SimpleFilter.AND)
        {
            // Evaluate each subfilter against the remaining capabilities.
            // For AND we calculate the intersection of each subfilter.
            // We can short-circuit the AND operation if there are no
            // remaining capabilities.
            List<SimpleFilter> sfs = (List<SimpleFilter>) sf.getValue();
            for (int i = 0; matched && (i < sfs.size()); i++)
            {
                matched = matchInternal(cap, sfs.get(i));
            }
        }
        else if (sf.getOperation() == SimpleFilter.OR)
        {
            // Evaluate each subfilter against the remaining capabilities.
            // For OR we calculate the union of each subfilter.
            matched = false;
            List<SimpleFilter> sfs = (List<SimpleFilter>) sf.getValue();
            for (int i = 0; !matched && (i < sfs.size()); i++)
            {
                matched = matchInternal(cap, sfs.get(i));
            }
        }
        else if (sf.getOperation() == SimpleFilter.NOT)
        {
            // Evaluate each subfilter against the remaining capabilities.
            // For OR we calculate the union of each subfilter.
            List<SimpleFilter> sfs = (List<SimpleFilter>) sf.getValue();
            for (int i = 0; i < sfs.size(); i++)
            {
                matched = !(matchInternal(cap, sfs.get(i)));
            }
        }
        else
        {
            matched = false;
            if(cap instanceof Collection)
            {
            	for(Object lhs : ((Collection)cap).toArray())
            	{
            		if((matched = compareInternal(lhs, sf.getValue(), sf.getOperation())) == true)
            			break;
            	}
            }
            else if(cap instanceof Map)
            {
            	Object lhs = ((Map)cap).get(sf.getName());
            	if(lhs != null)
            	{
            		matched = compareInternal(lhs, sf.getValue(), sf.getOperation());
            	}
            }
            else if(cap instanceof String)
            {
           		matched = compareInternal(cap.toString(), sf.getValue(), sf.getOperation());
            }
            else
            {
                /*
            	Object lhs = null;
    			try 
    			{
    				// check bean world
    				lhs = PropertyUtils.getProperty(cap, sf.getName().toLowerCase());
    			} 
    			catch (Exception e) 
    			{
    				// no bean so check reflection
    				for(Field f : cap.getClass().getDeclaredFields())
    				{
    					if(f.getName().equalsIgnoreCase(sf.getName()))
    					{
    						try { lhs=f.get(cap); } catch (Exception e1) { }
    						break;
    					}
    				}
    			}
    			if(lhs!=null)
    			{
    				matched = compareInternal(lhs, sf.getValue(), sf.getOperation());
    			}
                */
            }
        }

        return matched;
	}
	
    private static boolean compareInternal(Object lhs, Object rhsUnknown, int op)
    {
        // If this is a PRESENT operation, then just return true immediately
        // since we wouldn't be here if the attribute wasn't present.
    	if (op == SimpleFilter.PRESENT)
        {
            return true;
        }

        // Regular Expressions compare either List/Collection or String
    	if (op == SimpleFilter.REGEXP && lhs instanceof Collection)
    	{
    		for(Object item : ((Collection)lhs).toArray())
    		{
    			if(item.toString().matches(rhsUnknown.toString()))
    			{
    				return true;
    			}
    		}
    		return false;
    	}
    	else if(op == SimpleFilter.REGEXP)
    	{
    		if(lhs.toString().matches(rhsUnknown.toString()))
			{
				return true;
			}
    		return false;
    	}
        
        // If the type is comparable, then we can just return the
        // result immediately.
        if (lhs instanceof Comparable)
        {
            // Spec says SUBSTRING is false for all types other than string.
            if ((op == SimpleFilter.SUBSTRING) && !(lhs instanceof String))
            {
                //return false; // ignore
            	return compareInternal(lhs.toString(), rhsUnknown, op);
            }

            Object rhs;
            if (op == SimpleFilter.SUBSTRING)
            {
                rhs = rhsUnknown;
            }
            else
            {
                try
                {
                    rhs = coerceType(lhs, (String) rhsUnknown);
                }
                catch (Exception ex)
                {
                    return false;
                }
            }

            switch (op)
            {
                case SimpleFilter.EQ :
                    return (((Comparable) lhs).compareTo(rhs) == 0);
                case SimpleFilter.GTE :
                    return (((Comparable) lhs).compareTo(rhs) >= 0);
                case SimpleFilter.LTE :
                    return (((Comparable) lhs).compareTo(rhs) <= 0);
                case SimpleFilter.APPROX :
                    return compareApproximate(((Comparable) lhs), rhs);
                case SimpleFilter.SUBSTRING :
                    return SimpleFilter.compareSubstring((List<String>) rhs, (String) lhs);
                default:
                    throw new RuntimeException(
                        "Unknown comparison operator: " + op);
            }
        }
        // Booleans do not implement comparable, so special case them.
        else if (lhs instanceof Boolean)
        {
            Object rhs;
            try
            {
                rhs = coerceType(lhs, (String) rhsUnknown);
            }
            catch (Exception ex)
            {
                return false;
            }

            switch (op)
            {
                case SimpleFilter.EQ :
                case SimpleFilter.GTE :
                case SimpleFilter.LTE :
                case SimpleFilter.APPROX :
                    return (lhs.equals(rhs));
                default:
                    throw new RuntimeException(
                        "Unknown comparison operator: " + op);
            }
        }

        // If the LHS is not a comparable or boolean, check if it is an
        // array. If so, convert it to a list so we can treat it as a
        // collection.
        if (lhs.getClass().isArray())
        {
            lhs = convertArrayToList(lhs);
        }

        // If LHS is a collection, then call compare() on each element
        // of the collection until a match is found.
        if (lhs instanceof Collection)
        {
            for (Iterator iter = ((Collection) lhs).iterator(); iter.hasNext(); )
            {
                if (compareInternal(iter.next(), rhsUnknown, op))
                {
                    return true;
                }
            }

            return false;
        }

        // Spec says SUBSTRING is false for all types other than string.
        if ((op == SimpleFilter.SUBSTRING) && !(lhs instanceof String))
        {
            return false;
        }

        // Since we cannot identify the LHS type, then we can only perform
        // equality comparison.
// TODO: COMPLIANCE - This should be changed to return false in case
//       of an exception, but the R4.2 CT has a mistake in it, so for
//       now we'll throw exceptions from equals().
//        try
//        {
//            return lhs.equals(coerceType(lhs, (String) rhsUnknown));
//        }
//        catch (Exception ex)
//        {
//            return false;
//        }
        Object rhsObj = null;
        try
        {
            rhsObj = coerceType(lhs, (String) rhsUnknown);
        }
        catch (Exception ex)
        {
            return false;
        }
        return lhs.equals(rhsObj);
    }
    
    private static Object coerceType(Object lhs, String rhsString) throws Exception
    {
        // If the LHS expects a string, then we can just return
        // the RHS since it is a string.
        if (lhs.getClass() == rhsString.getClass())
        {
            return rhsString;
        }

        // Try to convert the RHS type to the LHS type by using
        // the string constructor of the LHS class, if it has one.
        Object rhs = null;
        try
        {
            // The Character class is a special case, since its constructor
            // does not take a string, so handle it separately.
            if (lhs instanceof Character)
            {
                rhs = new Character(rhsString.charAt(0));
            }
            else if (lhs instanceof Boolean)
            {
                rhs = new Boolean(rhsString);
            }
            else if (lhs instanceof Byte)
            {
                rhs = new Byte(rhsString);
            }
            else if (lhs instanceof Short)
            {
                rhs = new Short(rhsString);
            }
            else if (lhs instanceof Integer)
            {
                rhs = new Integer(rhsString);
            }
            else if (lhs instanceof Long)
            {
                rhs = new Long(rhsString);
            }
            else
            {
                rhs = rhsString;
            }
        }
        catch (Exception ex)
        {
            throw new Exception(
                "Could not instantiate class "
                    + lhs.getClass().getName()
                    + " from string constructor with argument '"
                    + rhsString + "' because " + ex);
        }

        return rhs;
    }
    
    private static boolean compareApproximate(Object lhs, Object rhs)
    {
        if (rhs instanceof String)
        {
            return removeWhitespace((String) lhs)
                .equalsIgnoreCase(removeWhitespace((String) rhs));
        }
        else if (rhs instanceof Character)
        {
            return Character.toLowerCase(((Character) lhs))
                == Character.toLowerCase(((Character) rhs));
        }
        return lhs.equals(rhs);
    }

    private static String removeWhitespace(String s)
    {
        StringBuffer sb = new StringBuffer(s.length());
        for (int i = 0; i < s.length(); i++)
        {
            if (!Character.isWhitespace(s.charAt(i)))
            {
                sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }
    
    /**
     * This is an ugly utility method to convert an array of primitives
     * to an array of primitive wrapper objects. This method simplifies
     * processing LDAP filters since the special case of primitive arrays
     * can be ignored.
     * @param array An array of primitive types.
     * @return An corresponding array using pritive wrapper objects.
    **/
    private static List convertArrayToList(Object array)
    {
        int len = Array.getLength(array);
        List list = new ArrayList(len);
        for (int i = 0; i < len; i++)
        {
            list.add(Array.get(array, i));
        }
        return list;
    }
    
    
}