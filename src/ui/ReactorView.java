package ui;

import components.models.ReactorModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/*
    ReactorView
 */
public class ReactorView implements ChangeListener
{
    private JPanel contentPane;
    private JLabel ReactorPowerAdjustment;
    private JLabel ReactorStatus;
    private JLabel ReactorOutput;
    private JButton ToggleStateButton;
    private JSlider maxPowerSlider;
    private JLabel ReactorMaxPowerLabel;
    private JPanel rootPanel;
    private JLabel ReactorTargetOutput;

    public ReactorView()
    {
        //bind to slider updates
        this.maxPowerSlider.addChangeListener(this::stateChanged);
    }

    //Update UI with reactors info
    public void UpdateReactorModel(ReactorModel model)
    {
        if(model == null)
        {
            return;
        }

        this.ReactorStatus.setText(model.getStateString());
        this.ReactorOutput.setText(String.format("%.2f", model.getCurrentPowerOutput()));
        this.ReactorPowerAdjustment.setText(String.format("%.2f", model.getLatestPowerDelta()));
        this.ReactorMaxPowerLabel.setText(String.format("%.2f", model.getMaxPowerOutput()));
        this.ReactorTargetOutput.setText(String.format("%.2f", model.getTargetOutput()));
    }

    public void setButtonText(String text)
    {
        this.ToggleStateButton.setText(text);
    }

    public void setStatusTextColour(Color color)
    {
        this.ReactorStatus.setForeground(color);
    }

    public JButton getToggleButton()
    {
        return this.ToggleStateButton;
    }

    public JPanel getPanel()
    {
        return this.rootPanel;
    }

    public JSlider getMaxPowerSlider()
    {
        return this.maxPowerSlider;
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent)
    {
        //Slider updated, update label
        int newValue = this.maxPowerSlider.getValue();
        this.ReactorMaxPowerLabel.setText(String.format("%.2f", (float)newValue));
    }
}
