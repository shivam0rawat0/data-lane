package lib.executor.stage;

import java.util.HashMap;
import java.util.Map;

public class Pipeline {
    
    String initialStage;
    String terminalStage;
    Map<String, String> sequence;

    public static Pipeline registerFlow(String flow) {
        Pipeline pipeline = new Pipeline();
        pipeline.sequence = new HashMap<>();
        String[] stages = flow.split(",");
        pipeline.initialStage = stages[0].trim();
        pipeline.terminalStage = stages[stages.length - 1].trim();
        for(int i = 0;i < stages.length - 1;i++){
            String currentStage = stages[i].trim();
            String nextStage = stages[i + 1].trim();
            pipeline.sequence.put(currentStage, nextStage);
        }
        return pipeline;
    }
}
