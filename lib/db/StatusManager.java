package lib.db;

import java.util.HashMap;
import java.util.Map;

public class StatusManager {
    private ConnectionManager connectionManager;
    private static int retryCount;

    public StatusManager() {
        this.connectionManager = ConnectionManager.getInstance();
    }

    public Retry save(long uid, String data) {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("uid", String.valueOf(uid));
        dataMap.put("status", Status.PENDING.name());
        dataMap.put("data", data);
        String query = "INSERT INTO retry (uid, status, data) VALUES ({uid}, '{status}', '{data}')";
        return connectionManager.execute(query, dataMap);
    }

    public void setStatus(Retry retry) {
        if (retry.getStatus().toString().contains("RETRY")) {
            retry.setCount(retry.getCount() + 1);
            if (retry.getCount() >= retryCount)
                retry.setStatus(Status.FAILED);
        }

        Map<String, String> data = new HashMap<>();
        data.put("id", String.valueOf(retry.getId()));
        data.put("uid", String.valueOf(retry.getUid()));
        data.put("status", retry.getStatus().name());
        data.put("count", String.valueOf(retry.getCount()));
        data.put("data", retry.getData());
        String query = "UPDATE retry SET status='{status}', count={count}, data='{data}' WHERE id={id}";
        connectionManager.execute(query, data);
    }

    public static void setRetryCount(int retryCount) {
        StatusManager.retryCount = retryCount;
    }
}