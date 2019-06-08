package ui;

import javax.swing.*;

public class ReactorView
{
    private JPanel contentPane;
    private JLabel ReactorPowerDelta;
    private JLabel ReactorStatus;
    private JLabel ReactorOutput;
    private JButton ToggleStateButton;

    //Update UI with reactors state public
    public void UpdateReactorInformation(String stateStr, float output, float delta)
    {
        this.ReactorStatus.setText(stateStr);
        this.ReactorOutput.setText(String.format("%.2f", output));
        this.ReactorPowerDelta.setText(String.format("%.2f", delta));
    }

    public void setButtonText(String text)
    {
        this.ToggleStateButton.setText(text);
    }

    public JButton getToggleButton()
    {
        return ToggleStateButton;
    }

    public JPanel getPanel()
    {
        return contentPane;
    }
}
