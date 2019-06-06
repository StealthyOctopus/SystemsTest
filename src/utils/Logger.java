package utils;

public class Logger {
	
	//verbose by default for now
	public LogLevel logLevel = LogLevel.VERBOSE;
	
	private static Logger instance = null; 
	  
    // protected constructor
	private Logger() 
    { 
    } 
  
    // static method to create instance of our singleton logger class 
    public static Logger getInstance() 
    { 
        if (instance == null) 
        	instance = new Logger(); 
  
        return instance; 
    } 
	
	public void LogString(String string, LogLevel logLevel) {
		if(logLevel.getValue() >= this.logLevel.getValue())
		{
			System.out.println(string);
		}
	}
	
	public void LogString(String string) {
		//default to VERBOSE
		this.LogString(string, LogLevel.VERBOSE);
	}
}
