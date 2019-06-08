package components.controllers;

import components.models.LifeSupportModel;
import components.models.ModelListenerInterface;
import components.models.PoweredModelInterface;
import ui.LifeSupportView;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LifeSupportSystemController implements ModelListenerInterface, ChangeListener
{
    private LifeSupportModel model;
    private LifeSupportView view;

    public LifeSupportSystemController(LifeSupportModel model, LifeSupportView view)
    {
        this.model = model;
        this.view = view;

        if(this.view != null)
        {
            this.view.getPeopleSlider().addChangeListener(this::stateChanged);
        }

        //bind to model updates
        if(this.model != null)
        {
            this.model.bindListener(this::OnModelUpdated);

            //set initial people value
            if(this.view != null)
            {
                this.view.setNumberOfPeople(this.model.getNumberOfPeople());
            }
        }
    }

    @Override
    public void OnModelUpdated()
    {
        if(this.view != null && this.model != null)
        {
            this.view.OnModelUpdated(this.model);
        }
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent)
    {
        if(this.view != null && this.model != null)
        {
            int newValue = this.view.getPeopleSlider().getValue();
            this.model.setNumberOfPeople(newValue);
        }
    }
}
