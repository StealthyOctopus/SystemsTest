package ui;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class reactorWidget extends Panel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private components.reactor reactorRef;
	
	private Button button;
	private Label label;
	private Label outputLabel;
	
	public reactorWidget(components.reactor reactorRef, ui.mainframe f) {
		
		this.reactorRef = reactorRef;
		this.label = new Label("Reactor");
		add(this.label);
		
		this.outputLabel = new Label("Power Output:");
		add(this.outputLabel);
		
		this.button = new Button("Startup Reactor");
		this.button.addActionListener(this);
		add(this.button);
		
		f.add(this);
	}
	
	//update our status label
	public void update() {
		if(this.reactorRef != null)
		{
			final float reactorOutput = this.reactorRef.GetPowerOutput();
			String text = "Power Output: " + reactorOutput + " gigawatts";
			this.outputLabel.setText(text);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		//Toggles reactor state
		if(this.reactorRef != null)
		{
			switch(this.reactorRef.GetState())
			{
			case State_Init:
				break;
			case State_Off:
				this.reactorRef.initialise();
				this.button.setLabel("Shutdown Reactor");
				break;
			case State_On:
				this.reactorRef.powerDown();
				this.button.setLabel("Startup Reactor");
				break;
			default:
				break;
			
			}
		}
	}
}
