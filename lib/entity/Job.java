package lib.entity;

public class Job {
    private long uid;
    private String data;
    private String type;
    
    public Job() {
    }

    public Job(long uid, String data, String type) {
        this.uid = uid;
        this.data = data;
        this.type = type;
    }

    public long getUid() {
        return uid;
    }
    public void setUid(long uid) {
        this.uid = uid;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
