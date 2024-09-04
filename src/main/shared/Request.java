package shared;

import java.io.Serializable;
import java.util.Map;

public class Request implements Serializable {
    public static final String LOAD_REQUEST = "LOAD_REQUEST";
    public static final String CALC_REQUEST = "CALC_REQUEST";
    public static final String SAVE_REQUEST = "SAVE_REQUEST";

    private String header;
    private Map<String, Object> data;

    public Request() {
    }

    public Request(String header, Map<String, Object> data) {
        this.header = header;
        this.data = data;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}