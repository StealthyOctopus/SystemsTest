package ui;

import components.models.LifeSupportModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/*
    LifeSupportView
 */
public class LifeSupportView implements ChangeListener
{
    private JSlider numberOfPeople;
    private JPanel rootPanel;
    private JLabel numberOfPeopleLabel;
    private JLabel powerConsumptionLabel;
    private JLabel airQualityLabel;

    public LifeSupportView()
    {
        //bind to slider updates
        this.getPeopleSlider().addChangeListener(this::stateChanged);
    }

    public void OnModelUpdated(LifeSupportModel model)
    {
        if(model == null)
        {
            return;
        }

        //check sliders max value is not smaller than the models current value
        if(this.getPeopleSlider().getMaximum() < model.getNumberOfPeople())
        {
            this.getPeopleSlider().setMaximum(model.getNumberOfPeople());
        }

        //update slider value if different
        if(this.getPeopleSlider().getValue() != model.getNumberOfPeople())
        {
            this.getPeopleSlider().setValue(model.getNumberOfPeople());
        }

        //Update the labels

        //Power draw as a percentage
        final String powerConsumptionString = String.format("%.2f%%", model.getPercentageDraw());
        this.powerConsumptionLabel.setText(powerConsumptionString);
        //set colour to indicate low power mode
        this.powerConsumptionLabel.setForeground(model.getPercentageDraw() >= model.getMinPercentToMaintainQuality() ? Color.GREEN : Color.RED);

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
        //slider updated
        this.setNumberOfPeople(this.numberOfPeople.getValue());
    }


    public void setNumberOfPeople(int num)
    {
        String postFix = (num == 1) ? "Person" : "People";
        this.numberOfPeopleLabel.setText(String.format("%d %s", num, postFix));
    }
}
