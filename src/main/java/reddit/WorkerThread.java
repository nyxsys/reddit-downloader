package reddit;

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

    private final WorkerData data;

    public WorkerThread(WorkerData data) {
        this.data = data;
    }

    @Override
    public void run() {
        while (!data.queue.isEmpty()) {
            String url = "";
            try {
                url = data.queue.take();
                Sites.downloadURL(url);
                data.downloadedCount.addAndGet(1);
                Main.updateProgress(data);
            } catch (InterruptedException ex) {
            }
            //System.out.println(Thread.currentThread().getName() + " Done downloading " + url);
        }

    }

}
