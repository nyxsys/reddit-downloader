/*
 * Shared data structure that all workers have access to
 */
package reddit;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Jacob
 */
public class WorkerData {

    AtomicInteger downloadedCount = new AtomicInteger(0);
    public final BlockingQueue<String> queue = new LinkedBlockingQueue<>();

}
