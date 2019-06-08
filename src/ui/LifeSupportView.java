package ui;

import components.models.LifeSupportModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LifeSupportView implements ChangeListener
{
    private JSlider numberOfPeople;
    private JPanel rootPanel;
    private JLabel numberOfPeopleLabel;

    public LifeSupportView()
    {
    }

    public void OnModelUpdated(LifeSupportModel model)
    {
        this.getPeopleSlider().addChangeListener(this::stateChanged);
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
