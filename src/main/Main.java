package main;

import components.PowerManager;
import components.controllers.LifeSupportSystemController;
import components.controllers.ReactorController;
import components.models.LifeSupportModel;
import components.models.ReactorModel;
import core.TickableGroup;
import ui.LifeSupportView;
import ui.MainDialog;
import ui.ReactorView;
import utils.Logger;

public class Main
{

    public static void main(final String[] args)
    {
        //create our main window frame to display some GUI
        MainDialog mainWindow = new MainDialog();

        //Get logger instance for convenience
        Logger logger = Logger.getInstance();

        //Create our "engine" to manage our tickable objects, updating them each frame
        logger.LogString("Creating Engine", utils.LogLevel.VERBOSE);
        core.Engine e = new core.Engine(30);

        //Now we can create come components and add them to our engine to keep them updated

        logger.LogString("Creating reactor...");

        ReactorView rv = new ReactorView();

        //add to main window
        mainWindow.addPanel(rv.getPanel());
        mainWindow.pack();

        ReactorModel rm = new ReactorModel(100.0f/*initial power output*/, 1000.0f/*max power output*/);
        ReactorController rc = new ReactorController(rm, rv);

        //add reactor model to tickables
        e.AddTickable(rm, TickableGroup.Default);

        logger.LogString("creating power management system...");

        PowerManager p = new PowerManager(rm);

        LifeSupportModel lifeSupportSystem = new LifeSupportModel(5);
        LifeSupportView lifeSupportView = new LifeSupportView();
        mainWindow.addPanel(lifeSupportView.getRootPanel());
        LifeSupportSystemController l = new LifeSupportSystemController(lifeSupportSystem, lifeSupportView);

        //add life support to tickables
        e.AddTickable(lifeSupportSystem, TickableGroup.Default);

        //add initial systems to power manager
        p.AddPoweredSystem(lifeSupportSystem);

        //add power manager to tickables
        e.AddTickable(p, TickableGroup.Default);

        logger.LogString("Starting Engine...");

        //Start the engine, kicking off the engine thread and component updates
        e.start();

        logger.LogString("Engine initialised");

        mainWindow.pack();
        mainWindow.setVisible(true);

        System.exit(0);
    }

}
