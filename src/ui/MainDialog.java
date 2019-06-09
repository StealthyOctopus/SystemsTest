package ui;

import javax.swing.*;
import java.awt.event.*;

/*
    MainDialog is our main window and base for other views to be attached
 */
public class MainDialog extends JDialog
{
    private JPanel contentPane;
    private JButton buttonQuit;
    private JButton buttonCancel;
    private JPanel subContentPane;

    public MainDialog()
    {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonQuit);

        //bind to quit
        buttonQuit.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onOK();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public JPanel GetMainPane()
    {
        return this.contentPane;
    }

    public void addPanel(JPanel panel)
    {
        this.subContentPane.add(panel);
    }

    private void onOK()
    {
        dispose();
    }

    private void onCancel()
    {
        dispose();
    }

    //TESTING UI Main entry
    public static void main(String[] args)
    {
        MainDialog dialog = new MainDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
