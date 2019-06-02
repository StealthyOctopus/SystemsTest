package core;

import java.util.*;

public class engine {
	private Map<TickableGroup, ArrayList<Tickable>> TickableObjectGroups;
	
	private float targetDeltaTime = 1.0f / 60.0f; 
	private long lastMs = 0;
	private boolean running;
	
	public engine(int fps) {
		//defaults to 60 fps
		this.targetDeltaTime = fps > 0 ? 1.0f / (float)fps : 1.0f / 60.0f;
		this.running = false;
		this.TickableObjectGroups = new HashMap<TickableGroup, ArrayList<Tickable>>();
		this.lastMs = 0;
	}
	
	public void AddTickable(Tickable tickable, TickableGroup group)
	{
		if(!this.TickableObjectGroups.containsKey(group))
		{
			this.TickableObjectGroups.put(group, new ArrayList<Tickable>());
		}
		
		ArrayList<Tickable> tickablesInGroup = this.TickableObjectGroups.get(group);
		tickablesInGroup.add(tickable);
		this.TickableObjectGroups.put(group, tickablesInGroup);
	}
	
	public void Run()
	{
		this.running = true;
		
		while(this.running)
		{
			long now = System.currentTimeMillis();
			float dt = (now - this.lastMs) / 1000.0f;
			if(dt >= this.targetDeltaTime)
			{
				for(ArrayList<Tickable> Group : TickableObjectGroups.values())
				{
					for(Tickable tickable : Group)
					{
						tickable.Tick(dt);
					}
				}
				this.lastMs = now;
			}
		}
	}
}
