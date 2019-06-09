package core;

import core.interfaces.Tickable;
import utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/*
    Engine class is intended to keep components ticking at a constant rate
 */
public class Engine implements Runnable
{
    private Map<TickableGroup, ArrayList<Tickable>> TickableObjectGroups;

    private float targetDeltaTime = 1.0f / 60.0f;
    private long lastMs = 0;
    private boolean running;
    private Thread t;

    public Engine(int fps)
    {
        //defaults to 60 fps
        this.targetDeltaTime = fps > 0 ? 1.0f / (float) fps : 1.0f / 60.0f;
        this.running = false;
        this.TickableObjectGroups = new HashMap<TickableGroup, ArrayList<Tickable>>();
        this.lastMs = System.currentTimeMillis();
    }

    //Adds an object that implements the Tickable interface, allowing the engine to update components each frame
    public void AddTickable(Tickable tickable, TickableGroup group)
    {
        if (!this.TickableObjectGroups.containsKey(group)) {
            this.TickableObjectGroups.put(group, new ArrayList<Tickable>());
        }

        ArrayList<Tickable> tickablesInGroup = this.TickableObjectGroups.get(group);
        tickablesInGroup.add(tickable);
        this.TickableObjectGroups.put(group, tickablesInGroup);
    }

    //Starts a thread to run our main game loop
    public void start()
    {
        Logger.getInstance().LogString("Starting engine thread");

        //can only start once
        if (t == null)
        {
            t = new Thread(this, "Engine");
            t.start();
        }
    }

    public void shutdown()
    {
        //TODO: Handle shutddown
    }

    @Override
    public void run()
    {
        this.running = true;

        //main engine loop
        while (this.running)
        {
            long now = System.currentTimeMillis();
            float dt = (now - this.lastMs) / 1000.0f;

            //Check enough time has passed since last tick to match target framerate
            if (dt >= this.targetDeltaTime)
            {
                //For all tickable groups
                for (ArrayList<Tickable> Group : TickableObjectGroups.values())
                {
                    //For all tickables in group
                    for (Tickable tickable : Group)
                    {
                        //TODO: Remove dead objects
                        if (tickable != null)
                        {
                            tickable.Tick(dt);
                        }
                    }
                }

                //Update last ms count
                this.lastMs = now;
            }

            //attempt to sleep thread until next tick is due
            try
            {
                Thread.sleep((long) this.targetDeltaTime);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
