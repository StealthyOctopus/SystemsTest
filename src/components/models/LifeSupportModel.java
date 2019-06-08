package components.models;

import core.Tickable;

public class LifeSupportModel extends PoweredSystemModel implements Tickable
{

    //High level & arbitrary air quality values
    private float airQualityDangerLevel = 25.0f;
    private float currentAirQuality;

    //Values are per second
    private float reductionPerPerson = 5.0f;
    private float energyCostPerPerson = 20.0f;

    private int numberOfPeople;

    public LifeSupportModel(int numberOfPeople)
    {
        this.setNumberOfPeople(numberOfPeople);
    }

    public boolean isAirSafe()
    {
        return currentAirQuality > airQualityDangerLevel;
    }

    public void setNumberOfPeople(int numberOfPeople)
    {
        this.numberOfPeople = numberOfPeople;
        this.requiredPowerDraw = this.energyCostPerPerson * numberOfPeople;
    }

    public int getNumberOfPeople()
    {
        return numberOfPeople;
    }

    @Override
    public void Tick(float dt)
    {
        //handle reduction coming from people existing
        reduceAirQuality(dt);

        //attempt to recover
        filterAir(dt);

        //notify listener if bound
        notifyListenerIfBound();
    }

    private void reduceAirQuality(float dt)
    {
        //decrease air quality based on number of people
        float decrease = (this.numberOfPeople * this.reductionPerPerson * dt);
        this.currentAirQuality = Math.max(this.currentAirQuality - decrease, 0.0f);
    }

    //improve air quality
    private void filterAir(float dt)
    {
        //at 100% of our required power draw we can completely negate the air quality reduction of each person
        float increase = (this.numberOfPeople * this.reductionPerPerson * dt) * this.percentageDraw;
        this.currentAirQuality = Math.min(this.currentAirQuality + increase, 100.0f);
    }
}
