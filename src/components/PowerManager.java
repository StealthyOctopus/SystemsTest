package components;

import components.models.PoweredSystemModel;
import components.models.ReactorModel;
import core.interfaces.Tickable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//TODO: Change power delivery so that if we do not have enough power to satisfy all systems in a given priority, we can divide up the power
//      Optionally we could send a message to all systems of a given priority to see if any systems could run on less power (less than 100% capacity), volunteering to consume less power
//TODO: Connect up a power store to act as a buffer if the reactor produces too much power
//TODO: Allow power storage to be configured, giving the option to try and maintain a certain amount of backup power for example
//TODO: Potentially add a view component for the power manager to allow adjustments from GUI

/*
    Power manager is intended to act as an interface between interfaces systems and the reactors power output
    giving more control over how this power is distributed also allowing for adjustments to the reactors output to reduce waste power
 */
public class PowerManager implements Tickable
{
    private ReactorModel reactor;

    private float generatedPower = 0.0f;
    private float lastPowerOutput = -1.0f;

    private boolean newSystemAdded = false;

    //map our interfaces systems by priority, with the array list allowing us to have several systems at the same priority
    private Map<Integer, ArrayList<PoweredSystemModel>> poweredSystems;

    public PowerManager(ReactorModel reactor)
    {
        this.reactor = reactor;
        this.poweredSystems = new HashMap<Integer, ArrayList<PoweredSystemModel>>();
    }

    public void AddPoweredSystem(PoweredSystemModel system)
    {
        this.AddPoweredSystem(system, 0);
    }

    public void AddPoweredSystem(PoweredSystemModel system, Integer priority)
    {
        if (!this.poweredSystems.containsKey(priority)) {
            this.poweredSystems.put(priority, new ArrayList<PoweredSystemModel>());
        }

        ArrayList<PoweredSystemModel> systems = this.poweredSystems.get(priority);

        //Add to systems array list
        systems.add(system);

        //put back into Map
        this.poweredSystems.put(priority, systems);

        //ensure we update all systems in next tick as the power distribution will change
        this.newSystemAdded = true;
    }

    @Override
    public void Tick(float dt)
    {
        //check we have a valid ref
        if (this.reactor == null) {
            return;
        }

        //get the amount of power we have to share, this will be reduced as we transfer the power to systems
        this.generatedPower = this.reactor.getCurrentPowerOutput();

        //If the power is the same as the last frame, no need to adjust power (assuming power is distributed the same and we havent updated systems priorities, which currently is not allowed)
        if(this.generatedPower == this.lastPowerOutput && !this.newSystemAdded)
            return;

        //update the last output value
        this.lastPowerOutput = this.generatedPower;

        //if we added a new system since last tick we can set this to false now as it will be updated below
        if(this.newSystemAdded)
            this.newSystemAdded = false;

        //save this value to compare what we made use of verses the reactors current output
        final float powerDrawnFromReactor = this.generatedPower;

        //build up the total power requested by all systems, so that we can adjust the reactors output
        float totalPowerRequested = 0.0f;

        //loop through systems in priority order
        for (Integer priorityLevel : this.poweredSystems.keySet())
        {
            //get the array of systems at this priority level
            ArrayList<PoweredSystemModel> systems = this.poweredSystems.get(priorityLevel);

            //now loop through the systems
            for (PoweredSystemModel system : systems)
            {
                //only transfer power to system if we have any left
                if (this.generatedPower > 0.0f)
                    transferPowerToSystem(system);

                //Increment the total power requested by all systems this frame
                totalPowerRequested += getSystemsRequestedPower(system);
            }
        }

        //if we were short on power, or had too much attempt to adjust power output
        float powerDiff = powerDrawnFromReactor - totalPowerRequested;

        //attempt to make the adjustment (function returns a success value, but currently not used)
        //TODO: in future we could check how many times our adjustments have failed and look to turn off a system
        this.reactor.tryAdjustOutput(-powerDiff);

        //TODO: store remaining power
        //if we have too much power, consider storing it as a buffer in case the output is too low in future
        if(powerDiff > 0.0f)
        {
            ///store
        }

    }

    //Helper to include safety checks when requesting a systems current  power draw
    private final float getSystemsRequestedPower(PoweredSystemModel system)
    {
        return (system != null) ? system.getRequiredPowerDraw() : 0.0f;
    }

    //transfers power to a system, allowing it to function, using up power as a resource
    private void transferPowerToSystem(PoweredSystemModel system)
    {
        if (system != null)
        {
            //how much power does the system want to function at 100%
            final float powerRequested = this.getSystemsRequestedPower(system);

            //power allowed cannot be greater than the remaining power we transferred from the reactor at the start of this update
            final float powerAllowed = (powerRequested <= this.generatedPower) ? powerRequested : this.generatedPower;

            //set the systems allowed power (also recalculates the power percentage for convenience)
            system.setAllowedPowerDraw(powerAllowed);

            //the power has now been used
            this.generatedPower = Math.max(this.generatedPower - powerRequested, 0.0f);
        }
    }
}
