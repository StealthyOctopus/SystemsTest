package utils;

public enum LogLevel
{
    NONE(0),
    VERBOSE(1);

    //Allow integer comparison to make log level checking simple
    private final int value;

    private LogLevel(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
