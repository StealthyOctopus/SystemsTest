package main;

import components.PowerManager;
import components.controllers.LifeSupportController;
import components.controllers.ReactorController;
import components.controllers.ShieldController;
import components.models.LifeSupportModel;
import components.models.ReactorModel;
import components.models.ShieldModel;
import core.SystemsFactory;
import core.TickableGroup;
import ui.LifeSupportView;
import ui.MainDialog;
import ui.ReactorView;
import ui.ShieldView;
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

        core.Engine engine = new core.Engine(120);

        //Now we can create come components and add them to our engine to keep them updated
        logger.LogString("Creating reactor...");

        //View component
        ReactorView reactorView = new ReactorView();

        //add to main window
        mainWindow.addPanel(reactorView.getPanel());

        ReactorModel reactorModel = new ReactorModel();
        ReactorController reactorController = new ReactorController(reactorModel, reactorView);

        logger.LogString("creating power management system...");

        //Create the power manager, passing in the reactor
        PowerManager powerManager = new PowerManager(reactorModel);

        logger.LogString("creating systems factory...");

        SystemsFactory systemsFactory = new SystemsFactory(powerManager, engine, mainWindow);

        if(systemsFactory == null)
        {
            System.exit(1);
            return;
        }

        //add power manager to tickables
        systemsFactory.AddTickable(powerManager);

        //add reactor model to tickables
        systemsFactory.AddTickable(reactorModel);

        //----------------------CREATE POWERED SYSTEMS----------------------//

        //Add a life support system
        systemsFactory.AddNewPoweredSystem("LIFESUPPORT", 0);

        //Add a shield system
        systemsFactory.AddNewPoweredSystem("SHIELDS", 1);

        //----------------------FINALIZE----------------------//

        logger.LogString("Starting Engine...");

        //Start the engine, kicking off the engine thread and component updates
        engine.start();

        logger.LogString("Engine initialised");

        //finalize window
        mainWindow.pack();
        mainWindow.setVisible(true);

        //----------------------SHUTDOWN----------------------//

        System.exit(0);
    }

}
