package main;

import core.TickableGroup;

public class main {

	public static void main(final String[] args) {
		
		ui.mainframe m = new ui.mainframe();
		m.show();
		
		// TODO Auto-generated method stub
		System.out.println("Creating Engine");
		core.engine e = new core.engine(30);
		
		System.out.println("Creating reactor...");
		
		components.reactor r = new components.reactor(1000.0f);
		
		System.out.println("Initialising reactor...");
		
		//r.initialise();
		ui.reactorWidget rw = new ui.reactorWidget(r, m);
		e.Widgets.add(rw);
		
		System.out.println("Adding reactor to tickable list");
		
		e.AddTickable(r, TickableGroup.Default);
		
		System.out.println("Starting Engine...");
		
		e.start(m);
		
		
		
		
	
		System.out.println("Shutdown - Mainloop complete");
	}

}
