package lib.executor.state;


import lib.db.Context;
import lib.db.Status;
import lib.executor.Handler;

public class ExecutionGraph {
    private static int retryCount;

    public static void setRetryCount(int retryCount) {
        ExecutionGraph.retryCount = retryCount;
    }

    public static void setupFlow(String flow){
        StageHandlerRegistry.registerFlow(flow);
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
                Handler handler = StageHandlerRegistry.getHandler(context);
                handler.handle(context);
                return true;
            case SUCCESS:
                if(StageHandlerRegistry.isLast(context)) {
                    return false;
                }
                StageHandlerRegistry.next(context);
                context.setStatus(Status.PENDING);
                return true;
            case FAILED:
                return false;
        }
        return false;
    }
}