package components;

import java.util.*;

import core.Tickable;
import components.powered.*;

public class PowerManager implements Tickable {
	
	private Reactor reactor;
	
	//map our powered systems by priority, with the array list allowing us to have several systems at the same priority
	private Map<Integer, ArrayList<PoweredSystemBase>> poweredSystems;
		
	public PowerManager(Reactor reactor)
	{
		this.reactor = reactor;
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
		if(reactor == null ) {
			return;
		}
		
		//Get power
	}
	
	public float getPowerOutput() {
		return (this.reactor != null) ? this.reactor.GetPowerOutput() : 0.0f;
	}
	
	public int updatePowerDraw(PoweredSystemBase system, float totalPower) {
		
		if(system != null)
		{
			final float powerRequested = system.getRequestedPowerDraw();
			final float powerAllowed = (powerRequested >= totalPower) ? powerRequested : totalPower;
			system.setAllowedPowerDraw(powerAllowed);
			
			totalPower = Math.max(totalPower - powerRequested, 0.0f);
		}
		
		return totalPower;
	}

}
