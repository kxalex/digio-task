package au.com.digio.task.parser.exeptions;

public class ParseException extends Exception {

    private String logLine;

    public ParseException(String message, String logLine) {
        super(message);
        this.logLine = logLine;
    }

    public String getLogLine() {
        return logLine;
    }
}
