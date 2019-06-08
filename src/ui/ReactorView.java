package ui;

import components.models.ReactorModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ReactorView implements ChangeListener
{
    private JPanel contentPane;
    private JLabel ReactorPowerDelta;
    private JLabel ReactorStatus;
    private JLabel ReactorOutput;
    private JButton ToggleStateButton;
    private JSlider maxPowerSlider;
    private JLabel maxPowerLabel;
    private JPanel rootPanel;

    public ReactorView()
    {
        this.maxPowerSlider.addChangeListener(this::stateChanged);
    }

    //Update UI with reactors state public
    public void UpdateReactorModel(ReactorModel model)
    {
        this.ReactorStatus.setText(model.getStateString());
        this.ReactorOutput.setText(String.format("%.2f", model.getCurrentPowerOutput()));
        this.ReactorPowerDelta.setText(String.format("%.2f", model.getLatestPowerDelta()));
        this.maxPowerLabel.setText(String.format("%f", model.getMaxPowerOutput()));
    }

    public void setButtonText(String text)
    {
        this.ToggleStateButton.setText(text);
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
         int newValue = this.maxPowerSlider.getValue();
         this.maxPowerLabel.setText(String.format("%.2f", (float)newValue));
    }
}
