package ui;

import java.awt.Frame;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Mainframe extends Frame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Label ReactorStatusLabel;
	
	public Mainframe() {
		setTitle("Mainframe");
		setSize(640,300);
		//
		//* An inner class is defined as the window Listener
		//* It is only interested in the windowClosing event,
		//* which will shut down the program
		//* *****************************************
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			} // Terminate the program
		});
		
	}
	
	public void SetReactorText(String text) {
		//this.ReactorStatusLabel.setText(text);
	}

}
