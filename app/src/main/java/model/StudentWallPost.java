package model;

import java.util.Date;

/**
 * Created by Pavan on 4/16/15.
 */
public class StudentWallPost {

    private String UserObjectId;
    private String postDescription;
    private Date createdAt;
    private Date updatedAt;
    private String userName;
    private String userImage;
    private String mediaFile;
    private String objectId;

    private int likes;
    private int disLikes;
    private int comments;

    private boolean alumniPost;

    public boolean isAlumniPost() {
        return alumniPost;
    }

    public void setAlumniPost(boolean alumniPost) {
        this.alumniPost = alumniPost;
    }

    public String getUserObjectId() {
        return UserObjectId;
    }

    public void setUserObjectId(String userObjectId) {
        this.UserObjectId = userObjectId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getUserImage() { return userImage; }

    public void setUserImage(String userImage){
        this.userImage = userImage;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMediaFile() {
        return mediaFile;
    }

    public void setMediaFile(String mediaFile) {
        this.mediaFile = mediaFile;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setDisLikes(int disLikes) {
        this.disLikes = disLikes;
    }

    public int getDisLikes() {
        return disLikes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

}
