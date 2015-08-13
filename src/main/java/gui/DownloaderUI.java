/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultCaret;
import reddit.Main;

/**
 *
 * @author Jacob
 */
public class DownloaderUI {
    
    public JProgressBar progressBar;
    public JTextArea outputArea;
    public JButton button;

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    public void createAndShowGUI() {

        //Setup native look and feel
        setNativeLookAndFeel();

        //Create and set up the window.
        JFrame frame = new JFrame("Reddit Downloader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(380, 475);
        
        /*
        frame.addComponentListener(new ComponentListener() {
            
            @Override
            public void componentResized(ComponentEvent e) {
                System.out.println(frame.getWidth() + " " + frame.getHeight());
            }
            
            @Override
            public void componentMoved(ComponentEvent e) {}
            
            @Override
            public void componentShown(ComponentEvent e) {}
            
            @Override
            public void componentHidden(ComponentEvent e) {}
        });
        */
        
        JPanel pane = new JPanel();
        JLabel label;
        
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        label = new JLabel("Subreddit", SwingConstants.CENTER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        pane.add(label, c);
        
        final JTextField textField = new JTextField("MySubreddit");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.ipady = 10;      //make this component tall
        c.gridx = 1;
        c.gridy = 0;
        pane.add(textField, c);
        
        label = new JLabel("Number of Posts", SwingConstants.CENTER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 10;      //make this component tall
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        pane.add(label, c);
        
        final JSpinner jSpinner = new JSpinner();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 10;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3;
        //c.weighty = 1.0;
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.gridx = 1;
        c.gridy = 1;
        pane.add(jSpinner, c);
        
        outputArea = new JTextArea(0, SwingConstants.CENTER);
        DefaultCaret caret = (DefaultCaret) outputArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        c.fill = GridBagConstraints.BOTH;
        //c.ipady = 10;      //make this component tall
        c.weightx = 0.5;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 2;
        pane.add(new JScrollPane(outputArea), c);
        
        button = new JButton("Download");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                button.setEnabled(false);
                                button.setText("In Progress");
                                Main.doDownload(textField.getText(), (int) jSpinner.getValue());
                                button.setEnabled(true);
                                button.setText("Download");
                            }
                        }).start();
                    }
                });
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 20;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.weighty = 0;
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.gridx = 0;
        c.gridy = 3;
        pane.add(button, c);
        
        progressBar = new JProgressBar();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.ipady = 20;
        c.gridwidth = 3;
        //c.insets = new Insets(10, 0, 0, 0);  //top padding
        c.gridx = 0;       //aligned with button 2
        //c.gridwidth = 2;   //2 columns wide
        c.gridy = 4;       //third row
        pane.add(progressBar, c);
        
        frame.getContentPane().add(pane);

        //Display the window.
        //frame.pack();
        frame.setVisible(true);
    }
    
    private void setNativeLookAndFeel() {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // handle exception
        }
        
    }
    
}
