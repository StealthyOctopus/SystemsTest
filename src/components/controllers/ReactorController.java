package components.controllers;

import components.ReactorState;
import components.models.interfaces.ModelListenerInterface;
import components.models.ReactorModel;
import ui.ReactorView;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
    ReactorController bridges the gap between our model and view
 */
public class ReactorController implements ActionListener, ModelListenerInterface, ChangeListener
{
    private ReactorModel reactorModel;
    private ReactorView reactorView;

    public ReactorController(ReactorModel reactorModel, ReactorView reactorView)
    {
        this.reactorModel = reactorModel;
        this.reactorView = reactorView;

        //bind listener so that we rare notified when the model updates
        if(this.reactorModel != null)
        {
            this.reactorModel.setListener(this::OnModelUpdated);
        }

        if (this.reactorView != null)
        {
            //set our initial slider value
            if(this.reactorModel != null)
            {
                this.reactorView.getMaxPowerSlider().setValue((int)this.reactorModel.getMaxPowerOutput());
            }

            //Bind to reactors on/off button
            this.reactorView.getToggleButton().addActionListener(this::actionPerformed);

            //bind to sliders value changed
            this.reactorView.getMaxPowerSlider().addChangeListener(this::stateChanged);
        }

        //set initial values
        OnModelUpdated();
    }

    //Our on/off button has been clicked
    public void actionPerformed(ActionEvent e)
    {
        //safety checks
        if (this.reactorModel == null || this.reactorView == null)
        {
            return;
        }

        //update our model
        this.reactorModel.setState(this.reactorModel.isPoweredOn() ? ReactorState.State_Off : ReactorState.State_On);

        //update our button text
        this.reactorView.setButtonText(this.reactorModel.isPoweredOn() ? "Power Off" : "Power On");
    }

    @Override
    public void OnModelUpdated()
    {
        //model has changed, update view
        if(this.reactorView != null && this.reactorModel != null)
        {
            this.reactorView.UpdateReactorModel(this.reactorModel);
        }
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent)
    {
        //the slider has been updated, update our models max power output
        if(this.reactorView != null && this.reactorModel != null)
        {
            int newValue = this.reactorView.getMaxPowerSlider().getValue();
            this.reactorModel.setMaxPowerOutput(newValue);
        }
    }
}
