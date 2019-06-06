package components;

public class Reactor implements core.Tickable {

	private float powerOutput;
	private float powerUpSpeed;
	private float powerDownSpeed;
	private float currentPowerOutput;
	private float maxPowerOutput;
	
	private ReactorState state;
	
	public final float GetPowerOutput() {
		return this.currentPowerOutput;
	}
	
	public Reactor(float initialOutput, float maxPowerOutput)
	{
		this.powerOutput = initialOutput;
		this.maxPowerOutput = maxPowerOutput;
		this.powerUpSpeed = 200.0f;
		this.powerDownSpeed = 400.0f;
		this.state = ReactorState.State_Off;
		this.currentPowerOutput = 0.0f;
	}
	
	public final ReactorState GetState() {
		return this.state;
	}
	
	public boolean initialise()
	{
		this.state = ReactorState.State_On;
		return true;
	}
	
	public void powerDown()
	{
		this.state = ReactorState.State_Off;
	}
	
	public boolean tryAdjustOutput(final float adjustment) {
		
		//save calculating twice 
		final float adjustedOutput = this.powerOutput + adjustment;
		
		//will this adjustment be within bounds
		boolean success = adjustedOutput >= 0.0f && adjustedOutput <= this.maxPowerOutput;
		
		//make sure we don't exceed max
		this.powerOutput = Math.min(adjustedOutput, this.maxPowerOutput);
		
		//cannot be less than 0
		this.powerOutput = Math.max(this.powerOutput, 0.0f);
		
		return success;
	}
	
	private void GeneratePower(float dt)
	{
		switch(this.state)
		{
		case State_Init:
			break;
		case State_Off:
			State_Off(dt);
			break;
		case State_On:
			State_On(dt);
			break;
		default:
			break;
		
		}
	}
	
	private void State_On(float dt) {
		if(this.currentPowerOutput != this.powerOutput)
		{
			this.currentPowerOutput += this.powerUpSpeed * dt;
			//clamp
			this.currentPowerOutput = Math.min(this.currentPowerOutput, this.powerOutput);
		}
		
		
	}
	
	private void State_Off(float dt)
	{
		if(this.currentPowerOutput > 0.0f)
		{
			this.currentPowerOutput -= this.powerDownSpeed * dt;
			//clamp
			this.currentPowerOutput = Math.max(this.currentPowerOutput, 0.0f);
		}
		
	}

	@Override
	public void Tick(float dt) {
		// TODO Auto-generated method stub
		//Our internal update of power values
		GeneratePower(dt);
	}
}
