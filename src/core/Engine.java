package core;

import java.util.*;

public class Engine implements Runnable {
	private Map<TickableGroup, ArrayList<Tickable>> TickableObjectGroups;
	
	public ArrayList<ui.ReactorWidget> Widgets = new ArrayList<ui.ReactorWidget>();
	
	private float targetDeltaTime = 1.0f / 60.0f; 
	private long lastMs = 0;
	private boolean running;
	private Thread t;
	private ui.Mainframe mainframe;
	
	public Engine(int fps) {
		//defaults to 60 fps
		this.targetDeltaTime = fps > 0 ? 1.0f / (float)fps : 1.0f / 60.0f;
		this.running = false;
		this.TickableObjectGroups = new HashMap<TickableGroup, ArrayList<Tickable>>();
		this.lastMs = System.currentTimeMillis();
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
	
	public void updateUI()
	{
		for(ui.ReactorWidget w : Widgets) {
			w.update();
		}
	}

	public void start (ui.Mainframe mainframe) {
		this.mainframe = mainframe;
		
		System.out.println("Starting engine thread");
		if (t == null) {
			t = new Thread (this, "Engine");
			t.start ();
		}
	}
	
	public void shutdown()
	{

	}

	@Override
	public void run() {
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
						if(tickable != null)
						{
							tickable.Tick(dt);
							
							if(tickable.getClass() == components.Reactor.class)
							{
								components.Reactor r = (components.Reactor)tickable;
								if(r != null) {
									this.mainframe.SetReactorText("Reactor Output at " + r.GetPowerOutput() + " gigawatts");
								}
							}
						}
					}
				}
				
				//Now update UI
				updateUI();
				
				this.lastMs = now;
			}
			
			try {
				Thread.sleep((long) this.targetDeltaTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
