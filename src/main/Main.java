package main;

import components.PowerManager;
import components.controllers.ReactorController;
import components.models.ReactorModel;
import components.powered.LifeSupportSystem;
import core.TickableGroup;
import ui.MainDialog;
import ui.ReactorView;
import utils.Logger;

import javax.swing.*;

public class Main {
	
	public static void main(final String[] args) {
		
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
		ReactorController r = new ReactorController(rm, rv);
		
		//add reactor to tickables
		e.AddTickable(r, TickableGroup.Default);
		
		logger.LogString("creating power management system...");
		
		PowerManager p = new PowerManager(r);
		
		LifeSupportSystem l = new LifeSupportSystem("Life_Support");
		
		//add life support to tickables 
		e.AddTickable(l, TickableGroup.Default);
		
		//add initial systems to power manager
		p.AddPoweredSystem(l);
		
		//add power manager to tickables
		e.AddTickable(p, TickableGroup.Default);
		
		//Create our basic UI widgets, passing our various components in where appropriate
		//ui.ReactorWidget rw = new ui.ReactorWidget(r, m);
		//e.Widgets.add(rw);
		
		logger.LogString("Starting Engine...");
		
		//Start the engine, kicking off the engine thread and component updates
		e.start(mainWindow);
	
		logger.LogString("Engine initialised");

		mainWindow.pack();
		mainWindow.setVisible(true);

		System.exit(0);
	}

}
