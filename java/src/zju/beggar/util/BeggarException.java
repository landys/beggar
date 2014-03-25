/**
 * 
 */
package zju.beggar.util;

/**
 * @author Administrator
 *
 */
public class BeggarException extends Exception
{

    /**
     * 
     */
    private static final long serialVersionUID = -3411247445349310599L;

    public BeggarException()
    {
	super();
    }
    
    public BeggarException(String message)
    {
	super(message);
    }
    
    public BeggarException(String message, Throwable cause)
    {
	super(message, cause);
    }

    public BeggarException(Throwable cause)
    {
	super(cause);
    }
}
