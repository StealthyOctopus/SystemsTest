package components.models;

import components.models.interfaces.LoadSettingsInterface;
import core.interfaces.Tickable;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ui.LifeSupportView;
import utils.ConfigurationLoader;

import javax.xml.validation.Schema;

/*
    LifeSupportModel extends PoweredSystemModel to hide away basic power changes
 */
public class LifeSupportModel extends PoweredSystemModel implements Tickable, LoadSettingsInterface
{

    //High level & arbitrary air quality values
    private float airQualityDangerLevel = 25.0f;
    private float currentAirQuality;

    //Values are per second
    private float reductionPerPerson = 5.0f;
    private float energyCostPerPerson = 20.0f;
    private float minPercentToMaintainQuality = 0.5f;//50%

    private int numberOfPeople;

    public LifeSupportModel()
    {
        //attempts to load settings from config by default
        this.LoadSettingsFromConfig("config/LifeSupportConfig.xml", "config/LifeSupportConfigSchema.xsd");

        //ensure our power draw is accurate
        this.setRequiredPowerDraw(this.energyCostPerPerson * this.numberOfPeople);
    }

    public LifeSupportModel(int numberOfPeople)
    {
        this.setNumberOfPeople(numberOfPeople);
    }

    public float getCurrentAirQuality()
    {
        return this.currentAirQuality;
    }

    private void setCurrentAirQualityClamped(float newAirQuality)
    {
        //ensure it is within bounds
        if(newAirQuality > 100.0f)
        {
            newAirQuality = 100.0f;
        }
        else if(newAirQuality < 0.0f)
        {
            newAirQuality = 0.0f;
        }

        //finally set
        this.currentAirQuality = newAirQuality;
    }

    public boolean isAirSafe()
    {
        return this.currentAirQuality > airQualityDangerLevel;
    }

    public void setNumberOfPeople(int numberOfPeople)
    {
        this.numberOfPeople = numberOfPeople;
        this.setRequiredPowerDraw(this.energyCostPerPerson * numberOfPeople);
    }

    public int getNumberOfPeople()
    {
        return numberOfPeople;
    }

    @Override
    public void Tick(float dt)
    {
        //filter air quality
        filterAir(dt);

        //notify listener if bound
        notifyListenerIfBound();
    }

    //update air quality based on current power levels & number of people
    private void filterAir(float dt)
    {
        //normalise percentage value before making calculations
        float normalisedPercentage = this.percentageDraw / 100.0f;

        //below a certain power level, quality will drop based on the number of people
        //above a certain percentage of power the quality will start to recover
        float changeInQuality = (normalisedPercentage - (this.minPercentToMaintainQuality / 100.0f)) * this.reductionPerPerson * this.numberOfPeople;

        //Get new air quality after the change
        float newAirQuality = this.currentAirQuality + changeInQuality * dt;

        //finally update our currentAirQuality var, ensuring it is within bounds
        this.setCurrentAirQualityClamped(newAirQuality);
    }

    //Load our configuration from file
    @Override
    public boolean LoadSettingsFromConfig(String path, final String schemaPath)
    {
        //Use the generic loader to get a list of nodes from our XML config file
        NodeList nodes = ConfigurationLoader.LoadNodesFromXML(path, schemaPath);

        if(nodes == null)
        {
            return false;
        }

        for(int i = 0; i < nodes.getLength(); ++i)
        {
            Node node = nodes.item(i);

            if (node != null && node.getNodeType() == Node.ELEMENT_NODE)
            {
                switch(node.getNodeName())
                {
                    case "numberOfPeople":
                        this.numberOfPeople = Integer.parseInt(node.getTextContent());
                        break;
                    case "reductionPerPerson":
                        this.reductionPerPerson = Float.parseFloat(node.getTextContent());
                        break;
                    case "airQualityDangerLevel":
                        this.airQualityDangerLevel = Float.parseFloat(node.getTextContent());
                        break;
                    case "energyCostPerPerson":
                        this.energyCostPerPerson = Float.parseFloat(node.getTextContent());
                        break;
                    case "minPercentToMaintainQuality":
                        this.minPercentToMaintainQuality = Float.parseFloat(node.getTextContent());
                        break;
                    default:
                        break;
                }
            }
        }

        //assume success
        return true;
    }
}
