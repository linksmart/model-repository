package eu.linksmart.services.mr.exceptions;

public class ResourceNotFound extends Exception 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7843878289132323380L;

	public ResourceNotFound()
    {
        super();
    }
    
    public ResourceNotFound(String message)
    {
        super(message);
    }
    
    public ResourceNotFound(String message, Throwable cause)
    {
		super(message, cause);
	}

	public ResourceNotFound(Throwable cause)
	{
		super(cause);
	}
}
