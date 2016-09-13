package model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Pavan on 1/17/16.
 *
 * This MsgGroup parcelable class can be passed from one activity to other activity
 */
public class MsgGroup implements Parcelable{

    private String groupId;
    private String groupName;
    private String username;
    private String collegeId;
    private String role;
    private String phoneNumber;
    private String updatedAt;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(groupId);
        parcel.writeString(groupName);
        parcel.writeString(username);
        parcel.writeString(collegeId);
        parcel.writeString(role);
        parcel.writeString(phoneNumber);
        parcel.writeString(updatedAt);
    }

    public static final Parcelable.Creator<MsgGroup> CREATOR = new Creator<MsgGroup>() {
        public MsgGroup createFromParcel(Parcel source) {
            MsgGroup globalInfo = new MsgGroup();

            globalInfo.groupId = source.readString();
            globalInfo.groupName = source.readString();
            globalInfo.username = source.readString();
            globalInfo.collegeId = source.readString();
            globalInfo.role = source.readString();
            globalInfo.phoneNumber = source.readString();
            globalInfo.updatedAt = source.readString();

            return globalInfo;
        }
        public MsgGroup[] newArray(int size) {
            return new MsgGroup[size];
        }
    };
}
