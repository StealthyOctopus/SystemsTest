package components.controllers;

import components.ReactorState;
import components.models.ReactorModel;
import ui.ReactorView;
import utils.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReactorController implements core.Tickable, ActionListener{

    private ReactorModel reactorModel = null;
    private ReactorView reactorView = null;

    private float latestPowerDelta = 0.0f;

    public ReactorController(ReactorModel reactorModel, ReactorView reactorView) {
        this.reactorModel = reactorModel;
        this.reactorView = reactorView;

        if(this.reactorView != null){
            this.reactorView.getToggleButton().addActionListener(this::actionPerformed);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if(this.reactorModel == null || this.reactorView == null) {
            return;
        }

        this.reactorModel.setState(this.isPoweredOn() ? ReactorState.State_Off : ReactorState.State_On);
        //update text
        this.reactorView.setButtonText(this.isPoweredOn() ? "Power Off" : "Power On");
    }

    public void powerOn()
    {
        if(this.reactorModel != null)
            this.reactorModel.setState(ReactorState.State_On);
    }

    public void powerOff()
    {
        if(this.reactorModel != null)
            this.reactorModel.setState(ReactorState.State_Off);
    }

    public boolean isPoweredOn() {
        return (this.reactorModel != null) ? this.reactorModel.getState() == ReactorState.State_On : false;
    }

    public float getCurrentPowerOutput() {
        return (this.reactorModel != null) ? this.reactorModel.getCurrentPowerOutput() : 0.0f;
    }

    public boolean tryAdjustOutput(final float adjustment) {
        if(this.reactorModel == null)
            return false;

        //save calculating twice
        final float adjustedOutput = this.reactorModel.getTargetOutput() + adjustment;

        //will this adjustment be within bounds
        boolean success = adjustedOutput >= 0.0f && adjustedOutput <= this.reactorModel.getMaxPowerOutput();

        //make sure we don't exceed max
        this.reactorModel.setTargetOutput(Math.min(adjustedOutput, this.reactorModel.getMaxPowerOutput()));

        //cannot be less than 0
        this.reactorModel.setTargetOutput(Math.max(this.reactorModel.getTargetOutput(), 0.0f));

        this.latestPowerDelta = adjustment;

        Logger.getInstance().LogString("" + this.latestPowerDelta);

        return success;
    }

    private void GeneratePower(float dt)
    {
        if(this.reactorModel == null)
            return;

        switch(this.reactorModel.getState())
        {
            case State_Init:
                break;
            case State_Off:
                State_Off(dt);
                break;
            case State_On:
                State_On(dt);
                break;
            default:
                break;

        }
    }

    private void State_On(float dt) {
        if(this.reactorModel == null)
            return;

        final float currentOutput = this.reactorModel.getCurrentPowerOutput();
        if(currentOutput != this.reactorModel.getTargetOutput())
        {
            this.reactorModel.setCurrentPowerOutput(currentOutput + this.reactorModel.getPowerUpSpeed() * dt);
            //clamp
            this.reactorModel.setCurrentPowerOutput(Math.min(this.reactorModel.getCurrentPowerOutput(), this.reactorModel.getTargetOutput()));
        }


    }

    private void State_Off(float dt) {
        if(this.reactorModel == null)
            return;
        final float currentOutput = this.reactorModel.getCurrentPowerOutput();
        if(currentOutput > 0.0f)
        {
            this.reactorModel.setCurrentPowerOutput(currentOutput - this.reactorModel.getPowerDownSpeed() * dt);
            //clamp
            this.reactorModel.setCurrentPowerOutput(Math.max(this.reactorModel.getCurrentPowerOutput(), 0.0f));
        }

    }

    @Override
    public void Tick(float dt) {
        // TODO Auto-generated method stub
        //Our internal update of power values
        GeneratePower(dt);

        //Update UI
        if(this.reactorView != null) {
            this.reactorView.UpdateReactorInformation(this.getStateAsString(), this.reactorModel.getCurrentPowerOutput(), this.latestPowerDelta);
        }
    }

    private String getStateAsString() {
        switch(this.reactorModel.getState())
        {
            case State_Init:
                return "INITIALISING";
            case State_Off:
                return "POWERED OFF";
            case State_On:
                return "POWERED ON";
            default:
                return "ERROR";

        }
    }
}
