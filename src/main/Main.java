package main;

import core.TickableGroup;
import utils.Logger;

public class Main {
	
	public static void main(final String[] args) {
		
		//create our main window frame to display some GUI
		ui.Mainframe m = new ui.Mainframe();
		m.show();
		
		//Get logger instance for convenience
		Logger logger = Logger.getInstance();
		
		//Create our "engine" to manage our tickable objects, updating them each frame
		logger.LogString("Creating Engine", utils.LogLevel.VERBOSE);
		core.Engine e = new core.Engine(30);
		
		//Now we can create come components and add them to our engine to keep them updated
		
		logger.LogString("Creating reactor...");
		
		components.Reactor r = new components.Reactor(1000.0f);
		
		logger.LogString("Initialising reactor...");
		
		
		//Create our basic UI widgets, passing our various components in where appropriate
		ui.ReactorWidget rw = new ui.ReactorWidget(r, m);
		e.Widgets.add(rw);
		
		logger.LogString("Adding reactor to tickable list");
		
		e.AddTickable(r, TickableGroup.Default);
		
		logger.LogString("Starting Engine...");
		
		//Start the engine, kicking off the engine thread and component updates
		e.start(m);
	
		logger.LogString("Engine initialised");
	}

}
