package components.models;

import components.ReactorState;

public class ReactorModel {

	private float powerOutput;
	private float powerUpSpeed;
	private float powerDownSpeed;
	private float currentPowerOutput;
	private float maxPowerOutput;
	
	private ReactorState state;

	public ReactorModel(float initialOutput, float maxPowerOutput)
	{
		this.powerOutput = initialOutput;
		this.maxPowerOutput = maxPowerOutput;
		this.powerUpSpeed = 200.0f;
		this.powerDownSpeed = 400.0f;
		this.state = ReactorState.State_Off;
		this.currentPowerOutput = 0.0f;
	}

	public void setCurrentPowerOutput(final float currentPowerOutput) {
		this.currentPowerOutput = currentPowerOutput;
	}

	public final float getCurrentPowerOutput() {
		return this.currentPowerOutput;
	}

	public float getTargetOutput() {
		return this.powerOutput;
	}

	public void setTargetOutput(float powerOutput) {
		this.powerOutput = powerOutput;
	}

	public float getMaxPowerOutput() {
		return this.maxPowerOutput;
	}

	public float getPowerUpSpeed() {
		return this.powerUpSpeed;
	}

	public float getPowerDownSpeed() {
		return this.powerDownSpeed;
	}
	
	public final ReactorState GetState() {
		return this.state;
	}
	
	public void setState(ReactorState state)
	{
		this.state =state;
	}

	public final ReactorState getState()
	{
		return this.state;
	}

}
