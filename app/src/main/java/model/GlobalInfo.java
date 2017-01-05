package model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Pavan on 12/29/15.
 */
public class GlobalInfo implements Parcelable{

    private String infoId;
    private String category;
    private String title;
    private String description;
    private String link;
    private String userObjectId;
    private String createdAt;
    private String updatedAt;

    private int mediaCount;
    private String media;
    private int hits;
    private int seen;

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUserObjectId() {
        return userObjectId;
    }

    public void setUserObjectId(String userObjectId) {
        this.userObjectId = userObjectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getMediaCount() {
        return mediaCount;
    }

    public void setMediaCount(int mediaCount) {
        this.mediaCount = mediaCount;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public static Creator<GlobalInfo> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(infoId);
        parcel.writeString(category);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(userObjectId);
        parcel.writeString(createdAt);
        parcel.writeString(updatedAt);
        parcel.writeInt(mediaCount);
        parcel.writeString(media);
        parcel.writeInt(hits);
        parcel.writeInt(seen);
    }

    public static final Parcelable.Creator<GlobalInfo> CREATOR = new Creator<GlobalInfo>() {
        public GlobalInfo createFromParcel(Parcel source) {
            GlobalInfo globalInfo = new GlobalInfo();

            globalInfo.infoId = source.readString();
            globalInfo.category = source.readString();
            globalInfo.title = source.readString();
            globalInfo.description = source.readString();
            globalInfo.userObjectId = source.readString();
            globalInfo.updatedAt = source.readString();
            globalInfo.createdAt = source.readString();
            globalInfo.mediaCount = source.readInt();
            globalInfo.media = source.readString();
            globalInfo.hits = source.readInt();
            globalInfo.seen = source.readInt();

            return globalInfo;
        }
        public GlobalInfo[] newArray(int size) {
            return new GlobalInfo[size];
        }
    };

}
