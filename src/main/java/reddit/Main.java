package reddit;

import gui.DownloaderUI;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;
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

    private static DownloaderUI gui;

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        Sites.initialize();
        gui = new DownloaderUI();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.createAndShowGUI();
            }
        });
    }

    public static void updateProgress(WorkerData data) {
        gui.progressBar.setValue(data.downloadedCount.get());
    }

    public static void doDownload(String subreddit, int limit) {
        gui.progressBar.setMaximum(limit);
        gui.progressBar.setValue(0);
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
        output("Finished downloading from " + subreddit);
        output(String.format("Execution took: %s seconds", ((System.currentTimeMillis() - startTime) / 1000)));
    }

    public static void output(String text) {
        gui.outputArea.setText(gui.outputArea.getText() + text + "\n");
    }
}
