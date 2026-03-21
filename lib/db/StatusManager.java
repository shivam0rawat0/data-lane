package lib.db;

import java.util.HashMap;
import java.util.Map;

public class StatusManager {
    private ConnectionManager connectionManager;
    private static int retryCount;

    public StatusManager() {
        this.connectionManager = new ConnectionManager();
    }

    public StatusManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public Retry save(long uid) {
        Map<String, String> data = new HashMap<>();
        data.put("uid", String.valueOf(uid));
        data.put("status", Status.PENDING.name());
        String query = "INSERT INTO retry (uid, status) VALUES ({uid}, '{status}')";
        return connectionManager.execute(query, data);
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
        String query = "UPDATE retry SET status='{status}' WHERE id={id}";
        connectionManager.execute(query, data);
    }

    public static void setRetryCount(int retryCount) {
        StatusManager.retryCount = retryCount;
    }
}