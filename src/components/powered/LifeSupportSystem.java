package components.powered;

public class LifeSupportSystem extends PoweredSystemBase {

	public LifeSupportSystem(String ID) {
		super(ID);		
		
		//default power draw //TODO:remove magic number
		requiredPowerDraw = 400.0f;
	}

	@Override
	public void Tick(float dt) {
		
	}
	
	public void manageLifesupport() {
		//TODO: Create some subsytems here
	}
}
