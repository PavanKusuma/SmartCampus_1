package model;

import java.util.Date;

/**
 * Created by Pavan_Kusuma on 4/17/2015.
 */
public class Messages {

    private String messageId;
    private String messageType;
    private String message;
    private String fromUserObjectId;
    private String toUserObjectId;
    private String groupId;
    private String branch;
    private int year;
    private int semester;
    private String department;
    private String createdAt;
    private String updatedAt;
    private int mediaCount;
    private String media;

    private String username;
    private String userimage;
    private String facultyBranch;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFromUserObjectId() {
        return fromUserObjectId;
    }

    public void setFromUserObjectId(String fromUserObjectId) {
        this.fromUserObjectId = fromUserObjectId;
    }

    public String getToUserObjectId() {
        return toUserObjectId;
    }

    public void setToUserObjectId(String toUserObjectId) {
        this.toUserObjectId = toUserObjectId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getFacultyBranch() {
        return facultyBranch;
    }

    public void setFacultyBranch(String facultyBranch) {
        this.facultyBranch = facultyBranch;
    }
}
