package components.powered;

public interface PowerDraw
{

    //How much power the object wants to draw
    public float getRequestedPowerDraw();

    //how much we allow the object to draw
    public void setAllowedPowerDraw(final float draw);

    //Percentage of our required power
    public float getPowerDrawAsPercentage();
}
