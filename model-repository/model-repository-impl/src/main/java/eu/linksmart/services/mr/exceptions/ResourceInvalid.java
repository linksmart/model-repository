package eu.linksmart.services.mr.exceptions;

public class ResourceInvalid extends Exception 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8498019496084759952L;

	public ResourceInvalid()
    {
    }
    
    public ResourceInvalid(String message)
    {
        super(message);
    }
    
    public ResourceInvalid(String message, Throwable cause)
    {
		super(message, cause);
	}

	public ResourceInvalid(Throwable cause)
	{
		super(cause);
	}

}
