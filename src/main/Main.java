package main;

import components.PowerManager;
import components.controllers.LifeSupportController;
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

        core.Engine e = new core.Engine(120);

        //Now we can create come components and add them to our engine to keep them updated
        logger.LogString("Creating reactor...");

        //View component
        ReactorView reactorView = new ReactorView();

        //add to main window
        mainWindow.addPanel(reactorView.getPanel());

        ReactorModel reactorModel = new ReactorModel();
        ReactorController reactorController = new ReactorController(reactorModel, reactorView);

        //add reactor model to tickables
        e.AddTickable(reactorModel, TickableGroup.Default);

        logger.LogString("creating power management system...");

        //Create the power manager, passing in the reactor
        PowerManager p = new PowerManager(reactorModel);

        //Add test system
        LifeSupportModel lifeSupportSystem = new LifeSupportModel();

        //View
        LifeSupportView lifeSupportView = new LifeSupportView();

        //Add view to main window
        mainWindow.addPanel(lifeSupportView.getRootPanel());

        //Create life support controller to manage view interactions
        LifeSupportController l = new LifeSupportController(lifeSupportSystem, lifeSupportView);

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

        //finalize window
        mainWindow.pack();
        mainWindow.setVisible(true);

        System.exit(0);
    }

}
