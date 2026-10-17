package lib.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lib.entity.Job;
import lib.executor.stage.ExecutionGraph;
import lib.executor.stage.PipelineRegistry;
import lib.db.Status;
import lib.db.Context;
import lib.db.StatusManager;

public class Processor {

    private ExecutorService executor;

    public Processor() {
        this.executor = new ThreadPoolExecutor(
        12,
        12,
        0,
        TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(1000),
        new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    public void submit(Job job){
        executor.submit(() -> process(job));
    }

    public void submitRetry(Context retry){
        executor.submit(() -> processRetry(retry));
    }

    private void process(Job job){
        StatusManager manager = new StatusManager();
        PipelineRegistry.loadPipeline(job.getType());
        System.out.println("[+f:e][" + job.getUid() + "]");
        System.out.printf("Processing Job: uid=%d, data=%s, type=%s%n", job.getUid(), job.getData(), job.getType());
        Context context = new Context(
            0,
            job.getUid(),
            0,
            Status.PENDING,
            PipelineRegistry.getFirst(job.getType()),
            job.getType(),
            job.getData()
        );
        context = manager.save(context);
        execute(context, manager);
    }

    private void processRetry(Context context){
        execute(context, new StatusManager());
    }    

    private void execute(Context context, StatusManager manager) {
        ExecutionGraph executionGraph = new ExecutionGraph();
        try {
            while(executionGraph.step(context)) {
                //manager.update(context);
            }
            context.setStatus(Status.SUCCESS);
            //manager.setStatusAs(context,Status.SUCCESS);
        } catch (Exception e) {
            //manager.setStatusAs(context,Status.RETRY);
            context.setStatus(Status.RETRY);
        }
    }
}