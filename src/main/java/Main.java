
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import org.json.simple.parser.ParseException;
import reddit.Reddit;
import sites.Sites;
/*
 * To change this license header, choose License Headers in Project Properties.
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
        /*
         long startTime = System.currentTimeMillis();
         Reddit reddit = new Reddit("My Super Cool App");
         queue.addAll(reddit.getURLsFromSubreddit("doge", 200));

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
        
        Sites.initialize();
    }
}
