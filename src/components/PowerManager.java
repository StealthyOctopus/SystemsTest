package components;

import core.Tickable;

public class PowerManager implements Tickable {
	
	private Reactor reactor;
		
	public PowerManager(Reactor reactor)
	{
		this.reactor = reactor;
	}

	@Override
	public void Tick(float dt) {
		if(reactor == null ) {
			return;
		}
	}

}
