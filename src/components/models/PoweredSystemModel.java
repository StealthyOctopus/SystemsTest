package components.models;

public class PoweredSystemModel implements PoweredModelInterface
{
    protected float requiredPowerDraw = 0.0f;
    protected float allowedPowerDraw = 0.0f;
    protected float percentageDraw = 0.0f;

    //to allow notifying a controller of any changes in data (likely every frame)
    protected ModelListenerInterface listener;

    public PoweredSystemModel()
    {
    }

    public PoweredSystemModel(float requiredPowerDraw, float allowedPowerDraw, float percentageDraw)
    {
        this.setAllowedPowerDraw(allowedPowerDraw);
        this.setPercentageDraw(percentageDraw);
        this.setRequiredPowerDraw(requiredPowerDraw);
    }

    public void bindListener(ModelListenerInterface listener)
    {
        this.listener = listener;
    }

    public void unbindListener()
    {
        this.listener = null;
    }

    public void notifyListenerIfBound()
    {
        if(this.listener != null)
        {
            this.listener.OnModelUpdated();
        }
    }

    @Override
    public float getRequiredPowerDraw()
    {
        return this.requiredPowerDraw;
    }

    @Override
    public float getAllowedPowerDraw()
    {
        return allowedPowerDraw;
    }

    @Override
    public float getPercentageDraw()
    {
        return percentageDraw;
    }

    @Override
    public void setAllowedPowerDraw(float allowedPowerDraw)
    {
        this.allowedPowerDraw = allowedPowerDraw;
    }

    @Override
    public void setRequiredPowerDraw(float requiredPowerDraw)
    {
        this.requiredPowerDraw = requiredPowerDraw;
    }

    @Override
    public void setPercentageDraw(float percentageDraw)
    {
        this.percentageDraw = percentageDraw;
    }
}
