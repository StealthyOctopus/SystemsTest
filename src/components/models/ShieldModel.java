package components.models;

import core.interfaces.Tickable;

public class ShieldModel extends PoweredSystemModel implements Tickable
{
    private float currentShieldStrength = 0.0f;
    private float minPercentToMaintain = 50.0f;
    private float shieldDangerLevel = 25.0f;
    private float shieldChangeSpeed = 15.0f;

    public ShieldModel()
    {
        this.setRequiredPowerDraw(200.0f);
    }

    public float getCurrentShieldStrength()
    {
        return currentShieldStrength;
    }

    public float getShieldDangerLevel()
    {
        return shieldDangerLevel;
    }

    public void GenerateShields(float dt)
    {
        //normalise percentage value before making calculations
        float normalisedPercentage = this.percentageDraw / 100.0f;

        //below a certain power level, shield level will begin to drop
        //above a certain percentage of power the shield strength weill begin to recover
        float change = (normalisedPercentage - (this.minPercentToMaintain / 100.0f)) * this.shieldChangeSpeed;

        //Get value after the change
        float newShieldStrength = this.currentShieldStrength + change * dt;

        //finally update our currentAirQuality var, ensuring it is within bounds
        this.setShieldLevelClamped(newShieldStrength);
    }

    private void setShieldLevelClamped(float newShieldLevel)
    {
        //cannot be less than 0
        this.currentShieldStrength = Math.max(newShieldLevel, 0.0f);
    }

    @Override
    public void Tick(float dt)
    {
        //handle shield generation
        GenerateShields(dt);

        //Update listener
        notifyListenerIfBound();
    }
}
