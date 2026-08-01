package lib.db;

import java.util.HashMap;
import java.util.Map;

public class StatusManager {
    private ConnectionManager connectionManager;

    public StatusManager() {
        this.connectionManager = ConnectionManager.getInstance();
    }

    public Context save(Context context) {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("uid", String.valueOf(context.getUid()));
        dataMap.put("status", context.getStatus().name());
        dataMap.put("count", String.valueOf(context.getCount()));
        dataMap.put("stage", context.getStage());
        dataMap.put("data", context.getData());
        String query = "INSERT INTO retry (uid, status, count, stage, data) VALUES ({uid}, '{status}', {count}, '{stage}', '{data}')";
        return connectionManager.execute(query, dataMap);
    }

    public void setStatusAs(Context retry,Status status) {
        retry.setStatus(status);
        update(retry);
    }

    public void update(Context retry) {
        Map<String, String> data = new HashMap<>();
        data.put("id", String.valueOf(retry.getId()));
        data.put("uid", String.valueOf(retry.getUid()));
        data.put("status", retry.getStatus().name());
        data.put("count", String.valueOf(retry.getCount()));
        data.put("stage", retry.getStage());
        data.put("data", retry.getData());
        String query = "UPDATE retry SET status='{status}', count={count}, data='{data}', stage='{stage}' WHERE id={id}";
        connectionManager.execute(query, data);
    }
}