package components.controllers;

import components.models.LifeSupportModel;
import components.models.ShieldModel;
import components.models.interfaces.ModelListenerInterface;
import ui.LifeSupportView;
import ui.ShieldView;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*
    ShieldController bridges the gap between the model and view
 */
public class ShieldController implements ModelListenerInterface
{
    private ShieldModel model;
    private ShieldView view;

    public ShieldController(ShieldModel model, ShieldView view)
    {
        this.model = model;
        this.view = view;

        if(this.model != null)
        {
            //bind to model updates
            this.model.bindListener(this::OnModelUpdated);
        }

        //set initial values on view
        OnModelUpdated();
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
}
