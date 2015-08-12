package reddit;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.json.simple.parser.ParseException;
import sites.manager.Sites;
/*
 * To change this license header, choosroject Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jacob
 */
public class Main {

    private static final BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        Sites.initialize();
        /*
         long startTime = System.currentTimeMillis();
         Reddit reddit = new Reddit("My Super Cool App");
         queue.addAll(reddit.getURLsFromSubreddit("gifs", 200));

         int cores = Runtime.getRuntime().availableProcessors();

         ExecutorService executor = Executors.newFixedThreadPool(cores);

         for (int i = 0; i < cores; i++) {
         Runnable worker = new WorkerThread(queue);
         executor.execute(worker);
         }
         executor.shutdown();
         while (!executor.isTerminated()) {
         }
         System.out.println("Finished all threads");
         System.out.printf("Execution took: %s seconds", ((System.currentTimeMillis() - startTime) / 1000));
         */
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setNativeLookAndFeel();
                createAndShowGUI();
            }
        });
    }

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JPanel pane = new JPanel();
        JButton button;
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        button = new JButton("Button 1");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        pane.add(button, c);

        button = new JButton("Button 2");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        pane.add(button, c);

        button = new JButton("Button 3");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 2;
        c.gridy = 0;
        pane.add(button, c);

        button = new JButton("Long-Named Button 4");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        pane.add(button, c);

        button = new JButton("5");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10, 0, 0, 0);  //top padding
        c.gridx = 1;       //aligned with button 2
        c.gridwidth = 2;   //2 columns wide
        c.gridy = 2;       //third row
        pane.add(button, c);

        frame.getContentPane().add(pane);

        //Display the window.
        //frame.pack();
        frame.setVisible(true);
    }

    public static void setNativeLookAndFeel() {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // handle exception
        }

    }
}
