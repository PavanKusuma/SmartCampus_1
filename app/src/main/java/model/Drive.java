package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pavan_Kusuma on 10/7/2016.
 */

public class Drive implements Parcelable {

    private String fileName;
    private String title;
    private String description;
    private String userObjectId;
    private String branches; // comma separated
    private int active;
    private int fileSize;


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public String getUserObjectId() {
        return userObjectId;
    }

    public void setUserObjectId(String userObjectId) {
        this.userObjectId = userObjectId;
    }

    public String getBranches() {
        return branches;
    }

    public void setBranches(String branches) {
        this.branches = branches;
    }

    public int isActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public static Creator<Drive> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(fileName);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(userObjectId);
        parcel.writeString(branches);
        parcel.writeInt(active);
        parcel.writeInt(fileSize);
    }

    public static final Parcelable.Creator<Drive> CREATOR = new Creator<Drive>() {
        public Drive createFromParcel(Parcel source) {
            Drive globalInfo = new Drive();

            globalInfo.fileName = source.readString();
            globalInfo.title = source.readString();
            globalInfo.description = source.readString();
            globalInfo.userObjectId = source.readString();
            globalInfo.branches = source.readString();
            globalInfo.active = source.readInt();
            globalInfo.fileSize = source.readInt();

            return globalInfo;
        }
        public Drive[] newArray(int size) {
            return new Drive[size];
        }
    };

}