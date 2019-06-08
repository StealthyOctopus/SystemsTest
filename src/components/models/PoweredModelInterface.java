package components.models;

public interface PoweredModelInterface
{
    float getRequiredPowerDraw();
    float getAllowedPowerDraw();
    float getPercentageDraw();

    void setAllowedPowerDraw(float allowedPowerDraw);
    void setRequiredPowerDraw(float requiredPowerDraw);
    void setPercentageDraw(float percentageDraw);
}
