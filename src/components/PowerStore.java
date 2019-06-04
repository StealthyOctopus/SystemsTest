package components;

public class PowerStore implements core.Tickable{
	private float currentPowerStore;
	private float powerStorage;
	private float timeToDecay;
	private float decayRate;
	private float decayTimer;
	
	public PowerStore(float maxStorage, float timeToDecay, float decayRate)
	{
		this.powerStorage = maxStorage;
		this.timeToDecay = timeToDecay;
		this.decayRate = decayRate;
		this.decayTimer = 0.0f;
	}
	
	public void AddPower(float power)
	{
		if(power < 0.0f)
			return;
		
		this.currentPowerStore += power;
		this.currentPowerStore = Math.min(this.currentPowerStore, powerStorage);
		
		//reset decay timer
		this.decayTimer = 0.0f;
	}
	
	public float ConsumePower(float requestedAmount)
	{
		if(requestedAmount > 0.0f)
		{
			if(this.currentPowerStore >= requestedAmount)
			{
				this.currentPowerStore -= requestedAmount;
				return requestedAmount;
			}
			else
			{
				//not enough, return what we have
				float amount = this.currentPowerStore;
				this.currentPowerStore = 0.0f;
				return amount;
			}
			
		}
		else 
		{
			return 0.0f;
		}
	}
	
	private void HandleDecay(float dt)
	{
		if(this.currentPowerStore <= 0.0f)
		{
			this.decayTimer = 0.0f;
			return;
		}
		
		this.decayTimer += dt;
		
		if(this.decayTimer >= this.timeToDecay)
		{
			this.currentPowerStore -= this.decayRate * dt;
			this.currentPowerStore = Math.max(this.currentPowerStore, 0.0f);
		}
	}
	
	@Override
	public void Tick(float dt) {
		if(this.decayTimer > 0.0f)
			HandleDecay(dt);
	}

}
