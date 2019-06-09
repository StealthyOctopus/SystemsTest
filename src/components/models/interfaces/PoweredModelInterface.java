package components.models.interfaces;
/*
    Interface to a system that requires power, providing getter and setter functions for the power requirements
 */
public interface PoweredModelInterface
{
    //Get power this system requires to function at 100%
    float getRequiredPowerDraw();

    //Get the power we were allowed to draw by the power manager
    float getAllowedPowerDraw();

    //Get the percentage power
    float getPercentageDraw();

    //Set power the system is allowed to use
    void setAllowedPowerDraw(float allowedPowerDraw);

    //Sets the power this system requires
    void setRequiredPowerDraw(float requiredPowerDraw);

    //Set the percentage of power requirement
    void setPercentageDraw(float percentageDraw);
}
