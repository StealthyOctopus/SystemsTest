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
        if(this.poweredSystems == null)
        {
            //TODO:Error handling
            return;
        }

        if (!this.poweredSystems.containsKey(priority))
        {
            this.poweredSystems.put(priority, new ArrayList<PoweredSystemModel>());
        }

        ArrayList<PoweredSystemModel> systems = this.poweredSystems.get(priority);

        if(systems == null)
        {
            //TODO:Error handling
            return;
        }

        //Add to systems array list
        systems.add(system);

        //put back into Map
        this.poweredSystems.put(priority, systems);
    }

    @Override
    public void Tick(float dt)
    {
        //safety checks
        if (this.reactor == null || this.poweredSystems == null)
        {
            //TODO: Error handling
            return;
        }

        //get the amount of power we have to share, this will be reduced as we transfer the power to systems
        this.generatedPower = this.reactor.getCurrentPowerOutput();

        //save this value to compare what we made use of verses the reactors current output
        final float powerDrawnFromReactor = this.generatedPower;

        //build up the total power requested by all systems, so that we can adjust the reactors output
        float totalPowerRequested = 0.0f;

        //loop through systems in priority order
        for (Integer priorityLevel : this.poweredSystems.keySet())
        {
            //get the array of systems at this priority level
            ArrayList<PoweredSystemModel> systems = this.poweredSystems.get(priorityLevel);

            //safety check
            if(systems == null)
            {
                continue;
            }

            //Check how much power will be requested ahead of time, so we know if we'll need to divide up power
            final float totalPowerRequestedAtPriority = this.getTotalPowerRequirement(systems);
            final boolean enoughPower = totalPowerRequestedAtPriority <= this.generatedPower;
            //remaining power is used when enough power is set to false
            final float remainingPower = this.generatedPower;

            //now loop through the systems
            for (PoweredSystemModel system : systems)
            {
                //Safety check
                if(system == null)
                {
                    continue;
                }

                //only transfer power to system if we have any left
                if (this.generatedPower > 0.0f)
                {
                    float powerAmountRequest = this.getSystemsRequestedPower(system);
                    //If we do not have enough power to give all systems what they requested, reduce the power given to them ensuring all systems get a percentage of the remaining power
                    if(!enoughPower && totalPowerRequestedAtPriority > 0.0f)
                    {
                        //calculate percentage of total power this system requested
                        final float normalizedPercentage = powerAmountRequest / totalPowerRequestedAtPriority;
                        //give the system the same percentage, but of the remaining power value instead
                        powerAmountRequest = normalizedPercentage * remainingPower;
                    }
                    transferPowerToSystem(system, powerAmountRequest);
                }

                //Increment the total power requested by all systems this frame
                totalPowerRequested += getSystemsRequestedPower(system);
            }
        }

        //if we were short on power, or had too much attempt to adjust power output
        float powerDiff = powerDrawnFromReactor - totalPowerRequested;

        //attempt to make the adjustment (function returns a success value, but currently not used)
        //TODO: in future we could check how many times our adjustments have failed and look to turn off a system
        this.reactor.tryAdjustOutput(-powerDiff);

        //if we have too much power, consider storing it as a buffer in case the output is too low in future
        if(powerDiff > 0.0f)
        {
            //TODO: store remaining power
        }

    }

    private final float getTotalPowerRequirement(ArrayList<PoweredSystemModel> systems)
    {
        float total = 0.0f;

        for(PoweredSystemModel system : systems)
        {
            //getSystemsRequestedPower has a safety check for null systems, so no need to duplicate here
            total += this.getSystemsRequestedPower(system);
        }

        return total;
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

            //call transfer with fixed amount
            this.transferPowerToSystem(system, powerRequested);
        }
    }

    //transfers power to a system, providing a fixed  amount to be used
    private void transferPowerToSystem(PoweredSystemModel system, final float powerAmount)
    {
        if (system != null)
        {
            //power allowed cannot be greater than the remaining power we transferred from the reactor at the start of this update
            final float powerAllowed = (powerAmount <= this.generatedPower) ? powerAmount : this.generatedPower;

            //set the systems allowed power (also recalculates the power percentage for convenience)
            system.setAllowedPowerDraw(powerAllowed);

            //the power has now been used
            this.generatedPower = Math.max(this.generatedPower - powerAmount, 0.0f);
        }
    }
}
