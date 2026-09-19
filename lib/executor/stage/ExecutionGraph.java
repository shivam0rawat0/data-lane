package lib.executor.stage;


import lib.db.Context;
import lib.db.Status;

public class ExecutionGraph {
    private static int retryCount;

    public static void setRetryCount(int retryCount) {
        ExecutionGraph.retryCount = retryCount;
    }

    /*
        State machine implementation.
        Execution is driven entirely by Context.status.
        Transitions:
        PENDING -> execute handler
        RETRY   -> retry current stage
        SUCCESS -> advance to next stage
        FAILED  -> terminate
     */
    public boolean step(Context context) {
        switch(context.getStatus()) {
            case RETRY:
                context.setCount(context.getCount() + 1);
                if (context.getCount() >= retryCount){
                    context.setStatus(Status.FAILED);
                    return true;
                }
            // Retry executes the current stage again.
            case PENDING:
                Handler handler = PipelineRegistry.getHandler(context);
                handler.handle(context);
                return true;
            case SUCCESS:
                if(PipelineRegistry.isLast(context)) {
                    return false;
                }
                PipelineRegistry.next(context);
                context.setStatus(Status.PENDING);
                return true;
            case FAILED:
                return false;
        }
        return false;
    }
}