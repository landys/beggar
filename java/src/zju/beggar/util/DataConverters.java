/**
 * 
 */
package zju.beggar.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;

/**
 * @author Tony
 *
 */
public class DataConverters
{
    private static Hashtable<String, Class> primObjects;
    static
    {
	primObjects = new Hashtable<String, Class>();
	primObjects.put("int", Integer.class);
	primObjects.put("boolean", Boolean.class);
	primObjects.put("float", Float.class);
	primObjects.put("double", Double.class);
	primObjects.put("short", Short.class);
	primObjects.put("long", Long.class);
	primObjects.put("byte", Byte.class);
	primObjects.put("char", String.class);
	primObjects.put("void", String.class);
    }
    
    
    /**
     * Convert String to other type, like short, int, float...
     * @param strData -- value of String to be converted
     * @param dataClass -- the objective class
     * @return -- value of objective class being converted
     * @throws BeggarException
     */
    public static Object string2Other(final String strData, final Class dataClass) throws BeggarException
    {
	Class realClass = classPrim2Wrap(dataClass);
	Object re = null;
	try
	{
	    // invoke method valueOf of objective class
	    Method method = realClass.getMethod("valueOf", String.class);
	    re = method.invoke(null, strData);
	}
	catch (SecurityException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    throw new BeggarException(e.getMessage());
	}
	catch (NoSuchMethodException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    throw new BeggarException(e.getMessage());
	}
	catch (IllegalArgumentException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    throw new BeggarException(e.getMessage());
	}
	catch (IllegalAccessException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    throw new BeggarException(e.getMessage());
	}
	catch (InvocationTargetException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    throw new BeggarException(e.getMessage());
	}
	return re;
    }
    
    
    /**
     * Converte primitive type class to its wrap class.
     * I.e. int.class to Integer.class
     * @param prim -- primitive type class
     * @return -- its wrap class
     */
    private static Class classPrim2Wrap(Class prim)
    {
	if (prim.isPrimitive())
	{
	    return primObjects.get(prim.getName());
	}
	return prim;
    }
}
