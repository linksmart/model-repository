package eu.linksmart.services.mr.exceptions;

public class ResourceTypeUnknown extends Exception 
{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 5350817863726247574L;

	public ResourceTypeUnknown() 
    {
        super();
    }

    public ResourceTypeUnknown(String message)
    {
        super(message);
    }
    
    public ResourceTypeUnknown(String message, Throwable cause)
    {
		super(message, cause);
	}

	public ResourceTypeUnknown(Throwable cause)
	{
		super(cause);
	}

}
