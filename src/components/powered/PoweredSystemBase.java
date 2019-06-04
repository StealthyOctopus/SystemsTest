package components.powered;

import core.Tickable;

public class PoweredSystemBase implements PowerDraw, Tickable {

	protected float requiredPowerDraw = 0.0f;
	protected float allowedPowerDraw = 0.0f;
	protected float percentageDraw = 0.0f;
	
	@Override
	public void Tick(float dt) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getRequestedPowerDraw() {
		// TODO Auto-generated method stub
		return this.allowedPowerDraw;
	}

	@Override
	public void setAllowedPowerDraw(float draw) {
		// TODO Auto-generated method stub
		this.allowedPowerDraw = draw;
		
		//also calculate percentage of max power
		this.percentageDraw = this.requiredPowerDraw != 0.0f ? (this.allowedPowerDraw / this.requiredPowerDraw) : 0.0f;
	}
	
	@Override
	public float getPowerDrawAsPercentage()
	{
		return this.percentageDraw;
	}

}
