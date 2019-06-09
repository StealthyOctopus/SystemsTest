package components.models;

import components.ReactorState;
import components.models.interfaces.ModelListenerInterface;
import core.interfaces.Tickable;


//TODO: Less manual checks to see if a listener should be notified of changes
//TODO: More reactor properties to be added, such as heat output, damage etc...
//TODO: Potentially split reactor model class into smaller classes when we have lots of different property types (such as damage or heat if they become involved enough)

public class ReactorModel implements Tickable
{
    private float powerOutput;
    private float powerUpSpeed;
    private float powerDownSpeed;
    private float currentPowerOutput;
    private float maxPowerOutput;
    private float latestPowerDelta;

    private ReactorState state;

    //Only allow one listener at present
    private ModelListenerInterface listener = null;

    public ReactorModel(float initialOutput, float maxPowerOutput)
    {
        this.powerOutput = initialOutput;
        this.maxPowerOutput = maxPowerOutput;
        this.powerUpSpeed = 200.0f;
        this.powerDownSpeed = 400.0f;
        this.state = ReactorState.State_Off;
        this.currentPowerOutput = 0.0f;
        this.latestPowerDelta = 0.0f;
    }

    public final float getLatestPowerDelta()
    {
        return this.latestPowerDelta;
    }

    public void setListener(ModelListenerInterface listener)
    {
        this.listener = listener;
    }

    public void setCurrentPowerOutput(float currentPowerOutput)
    {
        //ensure value is within bounds
        if(currentPowerOutput > this.maxPowerOutput)
            currentPowerOutput = this.maxPowerOutput;
        else if(currentPowerOutput < 0.0f)
            currentPowerOutput = 0.0f;

        this.currentPowerOutput = currentPowerOutput;
    }

    public final float getCurrentPowerOutput()
    {
        return this.currentPowerOutput;
    }

    public float getTargetOutput()
    {
        return this.powerOutput;
    }

    public void setTargetOutput(float powerOutput)
    {
        this.powerOutput = powerOutput;
    }

    public float getMaxPowerOutput()
    {
        return this.maxPowerOutput;
    }

    public float getPowerUpSpeed()
    {
        return this.powerUpSpeed;
    }

    public float getPowerDownSpeed()
    {
        return this.powerDownSpeed;
    }

    public final ReactorState GetState()
    {
        return this.state;
    }

    public void setMaxPowerOutput(float maxPowerOutput)
    {
        this.maxPowerOutput = maxPowerOutput;
    }

    public void setState(ReactorState state)
    {
        if(state != this.state)
        {
            NotifyListenerIfBound();
        }
        this.state = state;
    }

    public final ReactorState getState()
    {
        return this.state;
    }

    private void NotifyListenerIfBound()
    {
        if(this.listener != null)
        {
            this.listener.OnModelUpdated();
        }
    }

    @Override
    public void Tick(float dt)
    {
        //used to check if output has changed
        final float powerBefore = this.currentPowerOutput;

        //Our internal update of power values
        GeneratePower(dt);

        if(powerBefore != this.currentPowerOutput)
        {
            NotifyListenerIfBound();
        }
    }

    public boolean isPoweredOn()
    {
        return this.state == ReactorState.State_On;
    }

    public boolean tryAdjustOutput(final float adjustment)
    {
        //save calculating twice
        final float adjustedOutput = this.getTargetOutput() + adjustment;

        //will this adjustment be within bounds
        boolean success = adjustedOutput >= 0.0f && adjustedOutput <= this.getMaxPowerOutput();

        //make sure we don't exceed max
        this.setTargetOutput(Math.min(adjustedOutput, this.getMaxPowerOutput()));

        //cannot be less than 0
        this.setTargetOutput(Math.max(this.getTargetOutput(), 0.0f));

        //is the adjustment new?
        if(this.latestPowerDelta != adjustment)
        {
            NotifyListenerIfBound();
        }

        this.latestPowerDelta = adjustment;

        return success;
    }

    public String getStateString()
    {
        switch (this.state) {
            case State_Init:
                return "INITIALISING";
            case State_Off:
                return "POWERED OFF";
            case State_On:
                return "POWERED ON";
            default:
                return "ERROR";

        }
    }

    private void GeneratePower(float dt)
    {
        switch (this.state) {
            case State_Init:
                break;
            case State_Off:
                State_Off(dt);
                break;
            case State_On:
                State_On(dt);
                break;
            default:
                break;

        }
    }

    private void State_On(float dt)
    {
        final float currentOutput = this.getCurrentPowerOutput();
        if (currentOutput < this.getTargetOutput())
        {
            this.setCurrentPowerOutput(currentOutput + this.getPowerUpSpeed() * dt);
        }
        else if (currentOutput > this.getTargetOutput())
        {
            this.setCurrentPowerOutput(currentOutput - this.getPowerUpSpeed() * dt);
        }
    }

    private void State_Off(float dt)
    {
        final float currentOutput = this.getCurrentPowerOutput();
        if (currentOutput > 0.0f)
        {
            this.setCurrentPowerOutput(currentOutput - this.getPowerDownSpeed() * dt);
            //clamp
            this.setCurrentPowerOutput(Math.max(this.getCurrentPowerOutput(), 0.0f));
        }
    }
}
