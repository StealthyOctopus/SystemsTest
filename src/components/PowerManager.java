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
		
	}

	@Override
	public void Tick(float dt) {
		if(reactor == null ) {
			return;
		}
	}

}
