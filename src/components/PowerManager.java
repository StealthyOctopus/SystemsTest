package components;

import java.util.*;

import core.Tickable;
import utils.Logger;
import components.powered.*;

public class PowerManager implements Tickable {
	
	private Reactor reactor = null;
	private float generatedPower = 0.0f;
	
	//map our powered systems by priority, with the array list allowing us to have several systems at the same priority
	private Map<Integer, ArrayList<PoweredSystemBase>> poweredSystems;
		
	public PowerManager(Reactor reactor)
	{
		this.reactor = reactor;
		this.poweredSystems = new HashMap<Integer, ArrayList<PoweredSystemBase>>();
	}
	
	public void AddPoweredSystem(PoweredSystemBase system) {
		this.AddPoweredSystem(system, 0);
	}
	
	public void AddPoweredSystem(PoweredSystemBase system, Integer priority) {
		if(!this.poweredSystems.containsKey(priority))
		{
			this.poweredSystems.put(priority, new ArrayList<PoweredSystemBase>());
		}
		
		ArrayList<PoweredSystemBase> systems = this.poweredSystems.get(priority);
		
		//Add to systems array list
		systems.add(system);
		
		//put back into Map
		this.poweredSystems.put(priority, systems);
	}

	@Override
	public void Tick(float dt) {
		//check we have a valid ref and that the reactor is on
		if(this.reactor == null || this.reactor.GetState() != ReactorState.State_On) {
			return;
		}
		
		//get the amount of power we have to share, this will be reduced as we transfer the power to systems
		this.generatedPower = this.reactor.GetPowerOutput();
		final float powerDrawnFromReactor = this.generatedPower;
		
		//build up the total power requested by all systems, so that we can adjust the reactors output
		float totalPowerRequested = 0.0f;
		
		//loop through systems in priority order
		//Note to self: potentially thread this to increase performance
		for(Integer priorityLevel : this.poweredSystems.keySet()) {
			ArrayList<PoweredSystemBase> systems =  this.poweredSystems.get(priorityLevel);
			for(PoweredSystemBase system : systems) {
				//only transfer power to system if we have any left
				if(this.generatedPower > 0.0f)
					transferPowerToSystem(system);
				//Increment
				totalPowerRequested += getSystemsRequestedPower(system);
			}
		}
		
		//if we were short on power, or had too much attempt to adjust power output
		float powerDiff = powerDrawnFromReactor - totalPowerRequested;
		
		//attempt to make the adjustment (function returns a success value, but currently not used)
		//TODO: in future we could check how many times our adjustments have failed and look to turn off a system
		this.reactor.tryAdjustOutput(-powerDiff);
		
		//if we have too much power, consider storing it as a buffer in case the output is too low in future
		if(powerDiff > 0.0f) {
			//TODO: store remaining power
		}
		
	}
	
	public float getPowerOutput() {
		return (this.reactor != null) ? this.reactor.GetPowerOutput() : 0.0f;
	}
	
	private final float getSystemsRequestedPower(PoweredSystemBase system) {
		return (system != null) ? system.getRequestedPowerDraw() : 0.0f;
	}
	
	//gives power to a system
	private void transferPowerToSystem(PoweredSystemBase system) {
		if(system != null)
		{
			final float powerRequested = getSystemsRequestedPower(system);
			final float powerAllowed = (powerRequested >= this.generatedPower) ? powerRequested : this.generatedPower;
			system.setAllowedPowerDraw(powerAllowed);
			
			//the power has now been used
			this.generatedPower = Math.max(this.generatedPower - powerRequested, 0.0f);
		}
	}

}
