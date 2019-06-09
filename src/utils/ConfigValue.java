package utils;

/*
    ***********UNUSED***********
    ConfigValue provides a wrapper for generic objects loaded from a config
 */
public class ConfigValue
{
    private enum Type {
        STRING,
        INTEGER,
        FLOAT;
    }

    private Type type;
    private Object value;

    private ConfigValue(Object value, Type type) {
        this.value = value;
        this.type = type;
    }

    public static ConfigValue fromString(String s)
    {
        return new ConfigValue(s, Type.STRING);
    }

    public static ConfigValue fromInteger(Integer i)
    {
        return new ConfigValue(i, Type.INTEGER);
    }

    public static ConfigValue fromFloat(Float f)
    {
        return new ConfigValue(f, Type.FLOAT);
    }

    public Type getType()
    {
        return this.type;
    }

    public String getStringValue()
    {
        return (String) value;
    }

    public Integer getIntegerValue()
    {
        return (Integer) value;
    }

    public Float getFloatValue()
    {
        return (Float) value;
    }
}
