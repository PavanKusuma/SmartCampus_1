package svecw.smartcampus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import adapters.CommentAdapter;
import internaldb.SmartCampusDB;
import internaldb.SmartSessionManager;
import model.Comment;
import utils.Constants;
import utils.Routes;
import utils.Snippets;

/**
 * Created by Pavan on 6/10/15.
 */
public class NewCommentActivity extends Activity {

    // views of activity
    TextView emptyListText;
    EditText newComment;
    ImageView postComment;
    ListView commentsListView;
    ProgressBar commentsProgressBar;
    TextView likesView, disLikesView, commentsView;

    // list of current comments
    ArrayList<Comment> commentsList;

    // current post objectId
    String wallId, currentObjectId, username, commentId, commentText;
    int likes, dislikes, comments, position;

    // object for internal db
    SmartCampusDB smartCampusDB = new SmartCampusDB(this);
    SmartSessionManager session;

    // adapter for comments view
    CommentAdapter commentsAdapter;

    int status, status1;
    JSONObject jsonResponse, jsonResponse1;

    // new comment count
    // this value is returned back to wall activity to update the comment count
    int commentCount = 0;
    Intent commentIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_comment_layout);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // get views from activity
        emptyListText = (TextView) findViewById(R.id.emptyListText); emptyListText.setTypeface(sansFont);
        newComment = (EditText) findViewById(R.id.newComment1); newComment.setTypeface(sansFont);
        postComment = (ImageView) findViewById(R.id.postComment);
        commentsListView = (ListView) findViewById(R.id.commentsListView);
        commentsProgressBar = (ProgressBar) findViewById(R.id.commentsProgressBar);

        likesView = (TextView) findViewById(R.id.likeTextView); likesView.setTypeface(sansFont);
        //disLikesView = (TextView) findViewById(R.id.disLikeTextView);
        commentsView = (TextView) findViewById(R.id.commentTextView); commentsView.setTypeface(sansFont);

        // list of comments
        commentsList = new ArrayList<Comment>();
        session = new SmartSessionManager(NewCommentActivity.this);

        // fetch the intent
        commentIntent = getIntent();

        // current post object Id
        position = getIntent().getIntExtra(Constants.position, 0);
        username = smartCampusDB.getUser().get(Constants.userName).toString();
        wallId = getIntent().getStringExtra(Constants.wallId);
        currentObjectId = smartCampusDB.getUser().get(Constants.userObjectId).toString();
        likes = getIntent().getIntExtra(Constants.likes, 0);
        //dislikes = getIntent().getIntExtra(Constants.dislikes, 0);
        comments = getIntent().getIntExtra(Constants.comments, 0);

        // comments adapter
        commentsAdapter = new CommentAdapter(NewCommentActivity.this);
        commentsListView.setAdapter(commentsAdapter);
        commentsListView.setEmptyView(emptyListText);


        likesView.setText(String.valueOf(likes) + " likes");
        commentsView.setText(String.valueOf(comments) + " comments");

        commentCount = comments;

        // get comments
        // fetch comments for wallId
        String fetchURL = Routes.getWallComments + Constants.key + "/" + wallId;
        new FetchComments().execute(Routes.getWallComments, wallId);

        // post the comment to the db
        // and add it to current list of comments
        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //insert the record to local db and increase the count of the comments
                //smartCampusDB.insertUserComment(currentPostObjectId);

                // get the comment id
                commentId = Snippets.getCommentId();
                commentText = newComment.getText().toString();

                // check if comment is entered and frame the url
                if(commentText.length() > 0) {

                    String commentURL = Routes.createWallComment + Constants.key + "/" + commentId + "/" + Snippets.escapeURIPathParam(commentText) + "/" +
                            currentObjectId + "/" + Snippets.escapeURIPathParam(username) + "/" + wallId;

                    // post the wall comment
                    new PostWallComment().execute(Routes.createWallComment, commentId, Snippets.escapeURIPathParam(commentText), currentObjectId, Snippets.escapeURIPathParam(username));

                    // clear the comment
                    newComment.setText("");

                    // increase the comment count
                    commentCount++;
                    commentsView.setText(String.valueOf(commentCount) + " comments");
                    Log.v(Constants.appName, commentCount+" comment");

                }else
                {
                    // warning message
                    Toast.makeText(NewCommentActivity.this, "Write something..", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    @Override
    public void onBackPressed() {

        Log.v(Constants.appName, commentCount+" comment sending");

        // sent back the new comment count
        commentIntent.putExtra(Constants.commentCount, commentCount);
        // send position, so it can relate to which wall post the new comment belongs to
        commentIntent.putExtra(Constants.position, position);
        setResult(-1, commentIntent);
        finish();

    }

    /*
         * This method will resume the activity after returning from other activity
         * hence, adapter to the listview is set here
         *
         * (non-Javadoc)
         * @see android.app.Activity#onResume()
         */
    @Override
    protected void onResume() {
        super.onResume();

        commentsAdapter.updateItems(commentsList);
        // notify the adapter
        commentsAdapter.notifyDataSetChanged();
    }

    /**
     * This background task will post comment for the respective wallId
     */
    private class PostWallComment extends AsyncTask<String, Void, Void>{

        private String Content = "";
        private String Error = null;
        String data = "";

        @Override
        protected void onPreExecute() {

            /*progressDialog = new ProgressDialog(CollegeWallNewPostActivity.this);
            progressDialog.setMessage("Creating post ..");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
*/
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
        }

        @Override
        protected Void doInBackground(String... urls) {


            /************ Make Post Call To Web Server ***********/
            BufferedReader reader = null;

            // Send data
            try {

                // Set Request parameter
                data += "?&" + URLEncoder.encode(Constants.KEY, "UTF-8") + "=" + Constants.key
                        + "&" + URLEncoder.encode(Constants.commentId, "UTF-8") + "=" + (urls[1])
                        + "&" + URLEncoder.encode(Constants.comment, "UTF-8") + "=" + (urls[2])
                        + "&" + URLEncoder.encode(Constants.userObjectId, "UTF-8") + "=" + (urls[3])
                        + "&" + URLEncoder.encode(Constants.userName, "UTF-8") + "=" + (urls[4])
                        + "&" + URLEncoder.encode(Constants.wallId, "UTF-8") + "=" + wallId;

                Log.v(Constants.appName, urls[0]+data);

                // Defined URL  where to send data
                URL url = new URL(urls[0]+data);

                // Send POST data request
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                //conn.setDoInput(true);
                //OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                //wr.write(data);
                //wr.flush();

                // Get the server response
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + " ");
                }

                // Append Server Response To Content String
                Content = sb.toString();

                // close the reader
                //reader.close();

            } catch (Exception ex) {

                ex.printStackTrace();
                Error = ex.getMessage();


            } finally {

                try {

                    reader.close();

                } catch (Exception ex) {
                    Error = ex.getMessage();
                }
            }

            return null;
        }

        protected void onPostExecute(Void unused) {

            // clear the dialog
            //progressDialog.dismiss();

            if (Error != null) {

                Log.i("Connection", Error);

            } else {

                //Log.i("Connection", Content);
                /****************** Start Parse Response JSON Data *************/


                try {

                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(Content);


                    /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                    status = jsonResponse.getInt(Constants.status);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // check the status and proceed with the logic
                            switch (status){

                                // exception occurred
                                case -3:

                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    Log.v(Constants.appName, "error -3");
                                    break;

                                // key mismatch
                                case -2:

                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    Log.v(Constants.appName, "error -2");
                                    break;

                                // data found
                                case 1:

                                    Toast.makeText(getApplicationContext(), "Commented!", Toast.LENGTH_SHORT).show();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            // get the data and store in internal db
                                            Comment comment1 = new Comment();

                                            comment1.setComment(commentText);
                                            comment1.setUserObjectId(commentId);
                                            comment1.setUsername((String)smartCampusDB.getUser().get(Constants.userName));
                                            comment1.setMediaCount(0);
                                            comment1.setMedia(Constants.null_indicator);
                                            comment1.setCreatedAt("now");
                                            comment1.setUpdatedAt("now");
                                            comment1.setUserImage(session.getProfilePhoto());

                                            // add the object to list
                                            commentsList.add(comment1);

                                        // update the items
                                        //commentsAdapter.updateItems(commentsList);
                                        commentsAdapter.notifyDataSetChanged();

                                    }
                                    });
                                    break;


                            }
                        }
                    });





                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Oops! something went wrong, try again later", Toast.LENGTH_SHORT).show();
                    Log.v(Constants.appName, "error -e"+e.getMessage());
                    finish();

                }

            }
        }
    }


    /**
     * This background task will fetch the comments for the respective wallId
     */
    private class FetchComments extends AsyncTask<String, Void, Void>{

        private String Content = "";
        private String Error = null;
        String data = "";

        @Override
        protected void onPreExecute() {

            /*progressDialog = new ProgressDialog(CollegeWallNewPostActivity.this);
            progressDialog.setMessage("Creating post ..");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
*/
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            commentsProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... urls) {


            /************ Make Post Call To Web Server ***********/
            BufferedReader reader = null;

            // Send data
            try {

                // Set Request parameter
                data += "?&" + URLEncoder.encode(Constants.KEY, "UTF-8") + "=" + Constants.key
                        + "&" + URLEncoder.encode(Constants.wallId, "UTF-8") + "=" + (urls[1]);

                Log.v(Constants.appName, urls[0] + data);

                // Defined URL  where to send data
                URL url = new URL(urls[0] + data);

                // Send POST data request
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                //conn.setDoInput(true);
                //OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                //wr.write(data);
                //wr.flush();

                // Get the server response
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + " ");
                }

                // Append Server Response To Content String
                Content = sb.toString();

                // close the reader
                //reader.close();

            } catch (Exception ex) {

                ex.printStackTrace();
                Error = ex.getMessage();


            } finally {

                try {

                    reader.close();

                } catch (Exception ex) {
                    Error = ex.getMessage();
                }
            }

            return null;
        }

        protected void onPostExecute(Void unused) {

            // clear the dialog
            //progressDialog.dismiss();

            if (Error != null) {

                Log.i("Connection", Error);

            } else {

                //Log.i("Connection", Content);
                /****************** Start Parse Response JSON Data *************/


                try {

                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse1 = new JSONObject(Content);
                    Log.v(Constants.appName, "Check 000" + Content);

                    /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                    status1 = jsonResponse1.getInt(Constants.status);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // check the status and proceed with the logic
                            switch (status1){

                                // exception occurred
                                case -3:

                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    Log.v(Constants.appName, "error -31");
                                    break;

                                // key mismatch
                                case -2:

                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    Log.v(Constants.appName, "error -21");
                                    break;

                                // no data found
                                case 0:

                                    Log.v(Constants.appName, "Check 0");
                                    emptyListText.setText(R.string.noComments);
                                    Toast.makeText(getApplicationContext(), "No comments yet!", Toast.LENGTH_SHORT).show();
                                    break;

                                // data found
                                case 1:

                                    try {

                                        // get JSON Array of 'details'
                                        JSONArray jsonArray = jsonResponse1.getJSONArray(Constants.details);


                                        Log.v(Constants.appName, "length: "+jsonArray.length());
                                        Log.v(Constants.appName, "array: "+jsonArray);
                                        for(int i=0; i<jsonArray.length(); i++){

                                            // get the JSON object inside Array
                                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                                            // get the data and store in internal db
                                            Comment comment = new Comment();

                                            comment.setCommentId(jsonObject.getString(Constants.commentId));
                                            comment.setComment(jsonObject.getString(Constants.comment));
                                            comment.setUserObjectId(jsonObject.getString(Constants.userObjectId));
                                            comment.setUsername(jsonObject.getString(Constants.userName));
                                            comment.setWallId(jsonObject.getString(Constants.wallId));
                                            comment.setMediaCount(jsonObject.getInt(Constants.mediaCount));
                                            comment.setMedia(jsonObject.getString(Constants.media));
                                            comment.setCreatedAt(jsonObject.getString(Constants.createdAt));
                                            comment.setUpdatedAt(jsonObject.getString(Constants.updatedAt));
                                            //comment.setUserImage(jsonObject.getString(Constants.userImage));

                                            // get the user image
                                            JSONObject userObject = (JSONObject) jsonObject.get(Constants.user);
                                            int mediaCount = userObject.getInt(Constants.mediaCount);
                                            if(mediaCount > 0){
                                                //Log.v(Constants.appName, "userImage : "+userObject.getString(Constants.media));
                                                comment.setUserImage(userObject.getString(Constants.media));//.substring(0, (userObject.getString(Constants.media).length()-2)));
                                            }
                                            else{
                                                //Log.v(Constants.appName, "userImage : "+userObject.getString(Constants.media));
                                                comment.setUserImage(Constants.null_indicator);
                                            }

                                            // add the object to list
                                            commentsList.add(comment);


                                        }

                                        // update the items
                                        commentsAdapter.updateItems(commentsList);
                                        commentsAdapter.notifyDataSetChanged();
                                        commentsProgressBar.setVisibility(View.GONE);

                                        // update the count of comments
                                        commentsView.setText(commentsList.size() + " comments");

                                    }
                                    catch(Exception e){

                                        Log.e(Constants.error, e.getMessage());
                                        Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                        Log.v(Constants.appName, "error -e"+e.getMessage());
                                    }

                                    break;

                            }
                        }
                    });





                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Oops! something went wrong, try again later", Toast.LENGTH_SHORT).show();
                    Log.v(Constants.appName, "error -ee"+e.getMessage());
                }

            }

            // hide the progress bar
            commentsProgressBar.setVisibility(View.GONE);
        }
    }

}
