package reddit;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import org.json.simple.parser.ParseException;
import reddit.Reddit;
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

    }
}
