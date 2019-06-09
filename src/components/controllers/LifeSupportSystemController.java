package components.controllers;

import components.models.LifeSupportModel;
import components.models.interfaces.ModelListenerInterface;
import ui.LifeSupportView;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*
    LifeSupportSystemController bridges the gap between the model and view
 */
public class LifeSupportSystemController implements ModelListenerInterface, ChangeListener
{
    private LifeSupportModel model;
    private LifeSupportView view;

    public LifeSupportSystemController(LifeSupportModel model, LifeSupportView view)
    {
        this.model = model;
        this.view = view;

        //bind to slider updates
        if(this.view != null)
        {
            this.view.getPeopleSlider().addChangeListener(this::stateChanged);
        }

        if(this.model != null)
        {
            //bind to model updates
            this.model.bindListener(this::OnModelUpdated);

            //set initial people value on slider
            if(this.view != null)
            {
                this.view.setNumberOfPeople(this.model.getNumberOfPeople());
            }
        }
    }

    @Override
    public void OnModelUpdated()
    {
        //update the view with new model data
        if(this.view != null && this.model != null)
        {
            this.view.OnModelUpdated(this.model);
        }
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent)
    {
        //slider was updated, set the number of people in our life support model
        if(this.view != null && this.model != null)
        {
            int newValue = this.view.getPeopleSlider().getValue();
            this.model.setNumberOfPeople(newValue);
        }
    }
}
