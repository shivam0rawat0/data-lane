package lib.executor.state;


import java.util.HashMap;
import java.util.Map;

import lib.db.Context;
import lib.executor.Handler;

public class StageHandlerRegistry {
    static String initialStage;
    static String terminalStage;

    static Map<String, String> sequence;
    static Map<String, Handler> handlerMap;
    
    public static void registerFlow(String flow) {
        sequence = new HashMap<>();
        String[] stages = flow.split(",");
        initialStage = stages[0].trim();
        terminalStage = stages[stages.length - 1].trim();
        for(int i = 0;i < stages.length - 1;i++){
            String currentStage = stages[i].trim();
            String nextStage = stages[i + 1].trim();
            sequence.put(currentStage, nextStage);
        }
    }
    
    public static String getFirst() {
        return initialStage;
    }

    public static boolean isLast(Context context) {
        return context.getStage().equals(terminalStage);
    }

    public static Handler getHandler(Context context) {
        return instanceHandler(context);
    }

    public static void setNextStage(Context context) {
        if(sequence.containsKey(context.getStage())) {
            context.setStage(sequence.get(context.getStage()));
        } else {
            throw new IllegalArgumentException("Unknown stage: " + context.getStage());
        }
    }

    private static Handler instanceHandler(Context context) {
        String handlerName = context.getStage();
        switch (handlerName) {
            case "FETCH":
                    return new FetchHandler();
            case "FORMAT":
                return new FormatHandler();
            case "CLASSIFY":
                return new ClassifyHandler();
            default:
                throw new IllegalArgumentException("Unknown stage: " + context.getStage());
        }
    }
}