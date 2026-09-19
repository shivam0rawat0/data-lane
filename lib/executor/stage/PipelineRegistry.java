package lib.executor.stage;

import java.util.concurrent.ConcurrentHashMap;

import lib.db.Context;

public class PipelineRegistry {
    static ConcurrentHashMap<String, Pipeline> pipelines = new ConcurrentHashMap<>();
    static PipelineRepository handlerRegistry;

    public static void setHandlerRepository(String classDir, String repoDir) {
        handlerRegistry = new PipelineRepository(classDir, repoDir);
    }

    public static void loadPipeline(String type) {
        pipelines.computeIfAbsent(type, key -> {
            String flow = handlerRegistry.getPipeline(key);
            System.out.println("[+f]:" + flow);
            return Pipeline.registerFlow(flow);
        });
        /*
        non atomic causes hidden exception, so we use computeIfAbsent to ensure atomicity
        if (!pipelines.containsKey(type)) {
            String flow = handlerRegistry.getPipeline(type);
            System.out.println("[+f]:"+flow);
            pipelines.put(type, Pipeline.registerFlow(flow));
            System.out.println("[+f:l]:"+flow);
        } */
    }
    
    public static String getFirst(String type) {
        return pipelines.get(type).initialStage;
    }

    public static boolean isLast(Context context) {
        Pipeline pipeline = pipelines.get(context.getPipeline());
        return context.getStage().equals(pipeline.terminalStage);
    }

    public static void next(Context context) {
        Pipeline pipeline = pipelines.get(context.getPipeline());
        if(pipeline.sequence.containsKey(context.getStage())) {
            context.setStage(pipeline.sequence.get(context.getStage()));
        } else {
            throw new IllegalArgumentException("Unknown stage: " + context.getStage());
        }
    }

    public static Handler getHandler(Context context) {
        return handlerRegistry.getHandler(context.getStage());    
    }
}