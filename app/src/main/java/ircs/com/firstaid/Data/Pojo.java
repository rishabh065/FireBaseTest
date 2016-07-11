package ircs.com.firstaid.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by rishabh on 6/11/2016.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Pojo implements Parcelable {
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

    protected Pojo(Parcel in) {
        value = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value);
        dest.writeString(url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Pojo> CREATOR = new Parcelable.Creator<Pojo>() {
        @Override
        public Pojo createFromParcel(Parcel in) {
            return new Pojo(in);
        }

        @Override
        public Pojo[] newArray(int size) {
            return new Pojo[size];
        }
    };
}
