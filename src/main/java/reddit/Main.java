package reddit;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.BoundedRangeModel;
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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultCaret;
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

    private static JProgressBar progressBar;
    private static JTextArea outputArea;
    private static boolean running = false;
    private static JButton button;

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        Sites.initialize();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
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
        JFrame frame = new JFrame("Reddit Downloader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

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
                                doDownload(textField.getText(), (int) jSpinner.getValue());
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

    public static void updateProgress(WorkerData data) {
        progressBar.setValue(data.downloadedCount.get());
    }

    private static void doDownload(String subreddit, int limit) {
        progressBar.setMaximum(limit);
        progressBar.setValue(0);
        output("------------------------");
        output(subreddit);
        output("------------------------");
        long startTime = System.currentTimeMillis();
        Reddit reddit = new Reddit("My Super Cool App");
        WorkerData data = new WorkerData();
        data.queue.addAll(reddit.getURLsFromSubreddit(subreddit, limit));

        int cores = Runtime.getRuntime().availableProcessors();

        ExecutorService executor = Executors.newFixedThreadPool(cores);

        for (int i = 0; i < cores; i++) {
            Runnable worker = new WorkerThread(data);
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
        System.out.printf("Execution took: %s seconds", ((System.currentTimeMillis() - startTime) / 1000));
    }

    public static void output(String text) {
        outputArea.setText(outputArea.getText() + text + "\n");
    }

    private static void setNativeLookAndFeel() {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // handle exception
        }

    }
}
