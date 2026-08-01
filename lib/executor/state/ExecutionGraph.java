package lib.executor.state;


import lib.db.Context;
import lib.db.Status;
import lib.executor.Handler;

public class ExecutionGraph {

    private boolean isTerminal(Context context) {
        return context.getStatus() == Status.SUCCESS || context.getStatus() == Status.FAILED;
    }

    public boolean step(Context context){
        Handler handler = StageHandlerRegistry.getHandler(context);
        handler.handle(context);
        if(StageHandlerRegistry.isLast(context)) {
            return !isTerminal(context);
        }
        if(context.getStatus().equals(Status.SUCCESS)) {
            StageHandlerRegistry.setNextStage(context);
        }
        return true;
    }
}