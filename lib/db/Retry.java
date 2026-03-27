package lib.db;

public class Retry {
    private long id;
    private long uid;
    private int count;
    private Status status;
    private String data;
    
    public Retry(){        
    }
    public Retry(long id, long uid,int count, Status status, String data) {
        this.id = id;
        this.uid = uid;
        this.count = count;
        this.status = status;
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
}