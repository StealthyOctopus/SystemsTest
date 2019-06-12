package core;

import components.PowerManager;
import components.controllers.LifeSupportController;
import components.controllers.ShieldController;
import components.models.LifeSupportModel;
import components.models.PoweredSystemModel;
import components.models.ShieldModel;
import core.interfaces.Tickable;
import ui.LifeSupportView;
import ui.MainDialog;
import ui.ShieldView;

//TODO: Ensure view and controller components can have their own common interfaces, so that view and controller creation can be much more generic

public class SystemsFactory
{

    private PowerManager powerManager;
    private Engine engine;
    private MainDialog mainWindow;

    public SystemsFactory(PowerManager powerManager, Engine engine, MainDialog mainWindow)
    {
        this.powerManager = powerManager;
        this.engine = engine;
        this.mainWindow = mainWindow;
    }
    //Helper to add tickable to group
    public <T extends Tickable> void AddTickable(T tickable)
    {
        if(tickable != null && this.engine != null)
        {
            this.engine.AddTickable(tickable, TickableGroup.Default);
        }
    }

    //helper to add a system top the power manager
    public void AddToPowerManager(PoweredSystemModel system, int priorityGroup)
    {
        if(system != null && this.powerManager != null)
        {
            this.powerManager.AddPoweredSystem(system, priorityGroup);
        }
    }

    //Creates a new instance of a system, along with any view and controller components
    public void AddNewPoweredSystem(final String type, int priorityGroup)
    {
        PoweredSystemModel poweredModel = null;

        //upper case component names before entering switch
        type.toUpperCase();

        switch(type)
        {
            case "LIFESUPPORT":
                poweredModel = new LifeSupportModel();
                LifeSupportView v = new LifeSupportView();
                LifeSupportController c = new LifeSupportController((LifeSupportModel) poweredModel, v);

                //add view to screen
                if(this.mainWindow != null && v != null)
                {
                    this.mainWindow.addPanel(v.getRootPanel());
                }

                break;
            case "SHIELDS":
                poweredModel = new ShieldModel();
                ShieldView v2 = new ShieldView();
                ShieldController c2 = new ShieldController((ShieldModel)poweredModel, v2);

                //add view to screen
                if(this.mainWindow != null && v2 != null)
                {
                    this.mainWindow.addPanel(v2.getPanel());
                }

                break;

            default:
                break;
        }

        //add to engines tick list
        AddTickable((Tickable)poweredModel);

        //add to power manager
        AddToPowerManager(poweredModel, priorityGroup);
    }
}
