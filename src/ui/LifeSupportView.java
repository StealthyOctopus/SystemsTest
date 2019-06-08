package ui;

import components.models.LifeSupportModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class LifeSupportView implements ChangeListener
{
    private JSlider numberOfPeople;
    private JPanel rootPanel;
    private JLabel numberOfPeopleLabel;
    private JLabel powerConsumptionLabel;
    private JLabel airQualityLabel;

    public LifeSupportView()
    {
    }

    public void OnModelUpdated(LifeSupportModel model)
    {
        if(model == null)
            return;

        this.getPeopleSlider().addChangeListener(this::stateChanged);

        //Update the labels
        final String powerConsumptionString = String.format("%.2f", model.getAllowedPowerDraw());
        this.powerConsumptionLabel.setText(powerConsumptionString);
        //set colour to indicate low power mode
        this.powerConsumptionLabel.setForeground(model.getPercentageDraw() >= 100.0f ? Color.GREEN : Color.RED);

        //set air quality string
        final String airQualityString = String.format("%.2f%%", model.getCurrentAirQuality());
        this.airQualityLabel.setText(airQualityString);
        //update the colour to show danger levels if below danger levels, green if OK
        this.airQualityLabel.setForeground(model.isAirSafe() ? Color.GREEN : Color.RED);
    }

    public JSlider getPeopleSlider()
    {
        return this.numberOfPeople;
    }

    public JPanel getRootPanel()
    {
        return rootPanel;
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent)
    {
        this.setNumberOfPeople(this.numberOfPeople.getValue());
    }


    public void setNumberOfPeople(int num)
    {
        this.numberOfPeopleLabel.setText("" +  num);
    }
}
