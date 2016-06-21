package bppc.com.firebasetest.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by rishabh on 6/11/2016.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Pojo {
    private String value;
    private String url;
    public Pojo() {

    }



    public Pojo(String value, String url) {
        this.value = value;
        this.url = url;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
