package reddit;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import sites.manager.Sites;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jacob
 */
public class WorkerThread implements Runnable {

    private final BlockingQueue<String> queue;

    public WorkerThread(BlockingQueue<String> queue) {

        this.queue = queue;

    }

    @Override
    public void run() {
        while (!queue.isEmpty()) {
            String url = "";
            try {
                url = queue.take();
                Sites.downloadURL(url);
            } catch (InterruptedException ex) {

            }
            System.out.println(Thread.currentThread().getName() + " Done downloading " + url);
        }

    }

}
