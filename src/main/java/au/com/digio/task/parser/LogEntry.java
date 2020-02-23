package au.com.digio.task.parser;

public class LogEntry {

    private String IP;
    private String URL;

    public LogEntry(String IP, String URL) {
        this.IP = IP;
        this.URL = URL;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
