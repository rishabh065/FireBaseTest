package ircs.com.firstaid.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rishabh on 6/29/2016.
 */
public class Step_Image implements Parcelable{
    String url;
    String name;

    public Step_Image() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int mData;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    public static final Parcelable.Creator<Step_Image> CREATOR
            = new Parcelable.Creator<Step_Image>() {
        public Step_Image createFromParcel(Parcel in) {
            return new Step_Image(in);
        }

        public Step_Image[] newArray(int size) {
            return new Step_Image[size];
        }
    };

    private Step_Image(Parcel in) {
        mData = in.readInt();
    }

}
