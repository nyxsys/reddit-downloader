
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
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
        Thread.sleep(2000);
        long startTime = System.currentTimeMillis();
        String doc = Jsoup.connect("https://www.reddit.com/r/crappyoffbrands/.json?limit=100").userAgent("My Super Cool Application")
                .ignoreContentType(true).timeout(5000).get().body().text();
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = ((JSONObject) parser.parse(doc));
        JSONObject data = (JSONObject) jsonObject.get("data");
        JSONArray submissions = (JSONArray) data.get("children");
        for (Object submission : submissions) {
            JSONObject jsonSub = (JSONObject) submission;
            String url = (String) ((JSONObject) jsonSub.get("data")).get("url");
            queue.add(url);
        }
        ExecutorService executor = Executors.newFixedThreadPool(5);

        int cores = Runtime.getRuntime().availableProcessors();

        for (int i = 0; i <= cores; i++) {
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
