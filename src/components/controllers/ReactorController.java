package components.controllers;

import components.ReactorState;
import components.models.ModelListenerInterface;
import components.models.ReactorModel;
import ui.ReactorView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReactorController implements ActionListener, ModelListenerInterface
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
            this.reactorView.getToggleButton().addActionListener(this::actionPerformed);
        }
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
        if(this.reactorView != null)
        {
            this.reactorView.UpdateReactorInformation(this.reactorModel.getStateString(), this.reactorModel.getCurrentPowerOutput(), this.reactorModel.getLatestPowerDelta());
        }
    }
}
