package lib.db;

import java.time.Instant;

public class Context {
    private long id;
    private long uid;
    private int count;
    private Status status;
    private String stage;
    private String pipeline;
    private String data;
    private Instant createdAt;
    private Instant updatedAt;
    
    public Context(){        
    }

    public Context(long id, long uid,int count, Status status, String stage, String pipeline, String data) {
        this.id = id;
        this.uid = uid;
        this.count = count;
        this.status = status;
        this.stage = stage;
        this.pipeline = pipeline;
        this.data = data;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getUid() {
        return uid;
    }
    public void setUid(long uid) {
        this.uid = uid;
    }
    
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getStage() {
        return stage;
    }
    public void setStage(String stage) {
        this.stage = stage;
    }
    public String getPipeline() {
        return pipeline;
    }
    public void setPipeline(String pipeline) {
        this.pipeline = pipeline;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}