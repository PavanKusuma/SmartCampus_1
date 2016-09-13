package internaldb;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import model.StudentWallPost;
import utils.Constants;

/**
 * Created by Pavan_Kusuma on 4/19/2015.
 */
public class SmartSessionManager {

    // shared preference instance
    public SharedPreferences sharedPreferences;

    // shared preference editor
    SharedPreferences.Editor editor;

    // context
    Context context;

    // shared preference name
    private static final String Shared_Preference_name = Constants.app_SharedPreferences;

    // Global count of wall posts
    int global_Count = 0;

    public SmartSessionManager(Context context){

        this.context = context;

        sharedPreferences = context.getSharedPreferences(Shared_Preference_name, 0);
        editor = sharedPreferences.edit();
    }

    // save profile photo
    public void saveProfilePhoto(String base64String){

        editor.putString(Constants.userImage, base64String);
        editor.commit();
    }

    // remove profile photo
    public void removeProfilePhoto(){

        editor.putString(Constants.userImage, Constants.null_indicator);
        editor.commit();
    }

    // get the saved profile photo
    public String getProfilePhoto(){

        return sharedPreferences.getString(Constants.userImage, Constants.null_indicator);
    }

    // save the wall posts
    public void saveWallPosts(ArrayList<StudentWallPost> studentWallPostArrayList){

        // date format for displaying created date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy");

        // navigate through list and save the wall post details
        for(int i=global_Count; i < (global_Count + studentWallPostArrayList.size()); i++){

            // store the details with numbering
            editor.putString(Constants.userName + i, studentWallPostArrayList.get(i).getUserName());
            editor.putString(Constants.userImage + i, studentWallPostArrayList.get(i).getUserImage());
            editor.putString(Constants.postDescription + i, studentWallPostArrayList.get(i).getPostDescription());
            editor.putString(Constants.createdAt + i, simpleDateFormat.format(studentWallPostArrayList.get(i).getCreatedAt()));
            editor.putString(Constants.updatedAt + i, simpleDateFormat.format(studentWallPostArrayList.get(i).getUpdatedAt()));
            editor.putString(Constants.mediaFile + i, studentWallPostArrayList.get(i).getMediaFile());
            editor.putString(Constants.objectId + i, studentWallPostArrayList.get(i).getObjectId());

            // commit the details
            editor.commit();
        }

        // add the list size to global wall posts size
        global_Count = global_Count + studentWallPostArrayList.size();

        Log.v(Constants.appName + "Saved", "Saved completely");

    }

    // get the student wall posts
    public ArrayList<StudentWallPost> getWallPosts(){

        // date format for displaying created date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy");

        // list of studentWallPosts
        ArrayList<StudentWallPost> studentWallPostArrayList = new ArrayList<StudentWallPost>();

        try {

            for (int i = 0; i < global_Count; i++) {

                // object for student wall post model
                StudentWallPost studentWallPost = new StudentWallPost();

                studentWallPost.setUserName(sharedPreferences.getString(Constants.userName + i, Constants.null_indicator));
                studentWallPost.setUserImage(sharedPreferences.getString(Constants.userImage + i, Constants.null_indicator));
                studentWallPost.setPostDescription(sharedPreferences.getString(Constants.postDescription + i, Constants.null_indicator));
                studentWallPost.setCreatedAt(simpleDateFormat.parse(sharedPreferences.getString(Constants.createdAt + i, Constants.null_indicator)));
                studentWallPost.setUpdatedAt(simpleDateFormat.parse(sharedPreferences.getString(Constants.updatedAt + i, Constants.null_indicator)));
                studentWallPost.setMediaFile(sharedPreferences.getString(Constants.mediaFile + i, Constants.null_indicator));
                studentWallPost.setObjectId(sharedPreferences.getString(Constants.objectId + i, Constants.null_indicator));

                // add the object to list
                studentWallPostArrayList.add(studentWallPost);

            }

        }
        catch (Exception e){
            Log.v(Constants.appName, e.getMessage());
        }
        // return the list
        return studentWallPostArrayList;
    }

}
