package eu.linksmart.services.mr.exceptions;

/**
 * @author hrasheed
 * 
 */
public class RepositoryException extends Exception
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 4995218152246238722L;

	public RepositoryException() 
    {	
    	
    }

    public RepositoryException(String message)
    {
        super(message);
    }
    
    public RepositoryException(String message, Throwable cause)
    {
		super(message, cause);
	}

	public RepositoryException(Throwable cause)
	{
		super(cause);
	}
}
