package components.controllers;

import components.ReactorState;
import components.models.ModelListenerInterface;
import components.models.ReactorModel;
import ui.ReactorView;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReactorController implements ActionListener, ModelListenerInterface, ChangeListener
{
    private ReactorModel reactorModel;
    private ReactorView reactorView;

    public ReactorController(ReactorModel reactorModel, ReactorView reactorView)
    {
        this.reactorModel = reactorModel;
        this.reactorView = reactorView;

        if(this.reactorModel != null)
        {
            this.reactorModel.setListener(this::OnModelUpdated);
        }

        if (this.reactorView != null)
        {
            if(this.reactorModel != null)
            {
                this.reactorView.getMaxPowerSlider().setValue((int)this.reactorModel.getMaxPowerOutput());
            }
            this.reactorView.getToggleButton().addActionListener(this::actionPerformed);
            this.reactorView.getMaxPowerSlider().addChangeListener(this::stateChanged);
        }

        //set initial values
        OnModelUpdated();
    }

    public void actionPerformed(ActionEvent e)
    {
        if (this.reactorModel == null || this.reactorView == null) {
            return;
        }
        //update our model
        this.reactorModel.setState(this.reactorModel.isPoweredOn() ? ReactorState.State_Off : ReactorState.State_On);
        //update view
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
        if(this.reactorView != null && this.reactorModel != null)
        {
            int newValue = this.reactorView.getMaxPowerSlider().getValue();
            this.reactorModel.setMaxPowerOutput(newValue);
        }
    }
}
