package ui;

import components.models.ShieldModel;

import javax.swing.*;
import java.awt.*;

public class ShieldView
{
    private JLabel ShieldStrength;
    private JPanel RootPanel;

    public JPanel getPanel()
    {
        return this.RootPanel;
    }

    public void OnModelUpdated(ShieldModel model)
    {
        if(model == null)
            return;

        this.ShieldStrength.setText(String.format("%.2f%%", model.getCurrentShieldStrength()));
        this.ShieldStrength.setForeground(model.getCurrentShieldStrength() >= model.getShieldDangerLevel() ? Color.GREEN : Color.RED);
    }
}
