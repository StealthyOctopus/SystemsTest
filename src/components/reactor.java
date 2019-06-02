package components;

public class reactor implements core.Tickable {

	private float powerOutput;
	private float powerUpSpeed;
	private float powerDownSpeed;
	private float currentPowerOutput;
	
	private ReactorState state;
	
	public final float GetPowerOutput() {
		return this.currentPowerOutput;
	}
	
	public reactor(float output)
	{
		this.powerOutput = output;
		this.powerUpSpeed = 200.0f;
		this.state = ReactorState.State_Off;
		this.currentPowerOutput = 0.0f;
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
		if(this.currentPowerOutput < this.powerOutput)
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
		
		//Do a print for now
		System.out.println("Reactor power output: " + GetPowerOutput());
	}
}
