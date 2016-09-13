package model;

import java.util.Date;

/**
 * Created by Pavan on 6/18/15.
 */
public class StudentWall {

    private String objectId;
    private String postDescription;
    private Date createdAt;
    private Date updatedAt;
    private int likes;
    private int dislikes;
    private int comments;
    private String userObjectId;
    private String userName;
    private byte[] userImage;
    private byte[] mediaFile;
    private Boolean alumniPost;
    private int studentYear;

    public int getStudentYear() {
        return studentYear;
    }

    public void setStudentYear(int studentYear) {
        this.studentYear = studentYear;
    }

    public Boolean getAlumniPost() {
        return alumniPost;
    }

    public void setAlumniPost(Boolean alumniPost) {
        this.alumniPost = alumniPost;
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getUserObjectId() {
        return userObjectId;
    }

    public void setUserObjectId(String userObjectId) {
        this.userObjectId = userObjectId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public byte[] getUserImage() {
        return userImage;
    }

    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }

    public byte[] getMediaFile() {
        return mediaFile;
    }

    public void setMediaFile(byte[] mediaFile) {
        this.mediaFile = mediaFile;
    }
}
