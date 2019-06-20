package models;

public class Message {
    private SnEvent snEvent;
    private String url;

    public Message(SnEvent snEvent, String url) {
        this.snEvent = snEvent;
        this.url = url;
    }

    public SnEvent getSnEvent() {
        return snEvent;
    }

    public void setSnEvent(SnEvent snEvent) {
        this.snEvent = snEvent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
