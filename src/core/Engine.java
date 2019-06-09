package core;

import core.interfaces.Tickable;

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

    public void AddTickable(Tickable tickable, TickableGroup group)
    {
        if (!this.TickableObjectGroups.containsKey(group)) {
            this.TickableObjectGroups.put(group, new ArrayList<Tickable>());
        }

        ArrayList<Tickable> tickablesInGroup = this.TickableObjectGroups.get(group);
        tickablesInGroup.add(tickable);
        this.TickableObjectGroups.put(group, tickablesInGroup);
    }

    public void updateUI()
    {
    }

    public void start()
    {
        System.out.println("Starting engine thread");
        if (t == null) {
            t = new Thread(this, "Engine");
            t.start();
        }
    }

    public void shutdown()
    {

    }

    @Override
    public void run()
    {
        this.running = true;

        while (this.running) {
            long now = System.currentTimeMillis();
            float dt = (now - this.lastMs) / 1000.0f;
            if (dt >= this.targetDeltaTime) {
                for (ArrayList<Tickable> Group : TickableObjectGroups.values()) {
                    for (Tickable tickable : Group) {
                        if (tickable != null) {
                            tickable.Tick(dt);
                        }
                    }
                }

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
