package lib.entity;

public class Job {
    private long uid;
    private String url;
    private String type;
    
    public Job() {
    }

    public Job(long uid, String url, String type) {
        this.uid = uid;
        this.url = url;
        this.type = type;
    }

    public long getUid() {
        return uid;
    }
    public void setUid(long uid) {
        this.uid = uid;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
