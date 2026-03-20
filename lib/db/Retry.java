package lib.db;

public class Retry {
    private int id;
    private int uid;
    private int count;
    private Status status;
    
    public Retry(int id, int uid, Status status){
        this.id = id;
        this.uid = uid;
        this.status = status;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
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
}