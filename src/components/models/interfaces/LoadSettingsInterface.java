package components.models.interfaces;

public interface LoadSettingsInterface
{
    //Attempt to load settings from a configuration file
    boolean LoadSettingsFromConfig(final String xmlPath, final String schemaPath);
}
