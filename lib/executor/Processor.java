package lib.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lib.db.Retry;
import lib.db.Status;
import lib.db.StatusManager;
import lib.entity.Job;
import lib.executor.state.StateHandler;
import lib.executor.state.StateHandlerRegistry;

public class Processor {

    private ExecutorService executor;

    public Processor() {
        this.executor = new ThreadPoolExecutor(
        8,
        8,
        0,
        TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(1000),
        new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    public void submit(Job job){
        executor.submit(() -> process(job));
    }

    private static boolean isTerminal(Retry retry) {
        return retry.getStatus() == Status.SUCCESS || retry.getStatus() == Status.FAILED;
    }
    
    private void process(Job job){
        StatusManager manager = new StatusManager();
        Retry retry = manager.save(job.getUid());
        try {
            while(!isTerminal(retry)) {
                StateHandler handler = StateHandlerRegistry.getHandler(retry);
                retry.setStatus(null);
                retry.setStatus(handler.handle(retry));
                manager.setStatus(retry);
            }
        } catch (Exception e) {
            retry.setStatus(Status.FAILED);
            manager.setStatus(retry);
        }
    }
}