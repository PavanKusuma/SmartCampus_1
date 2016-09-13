package svecw.smartcampus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapters.StudentWallAdapter;
import model.Wall;
import utils.ConnectionDetector;
import utils.Constants;

/**
 * Created by Pavan on 4/16/15.
 */
public class AlumniActivity extends Fragment {

    // adapter to populate list view
    StudentWallAdapter adapter;

    // list of college wall posts
    List<Wall> studentWallPostsList;

    // list view
    ListView listView;

    // toolbar
    Toolbar toolbar;

    //SmartDB smartDB = new SmartDB(this);
    byte[] b = Constants.null_indicator.getBytes();

    // instance for ConnectionDetector
    ConnectionDetector connectionDetector;

    // progress bar
    ProgressBar progressBar;

    // skip counter
    int skipCounter = 0;

    // layout inflater
    LayoutInflater layoutInflater;

    // listView footer button
    Button btnMore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.student_wall_activity, container, false);

        // get views from layout
        listView = (ListView) rootView.findViewById(R.id.studentWallListView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBarStudentWall);

        // create 'more' button for footerView

        // more button for listView
        btnMore = new Button(getActivity().getApplicationContext());
        btnMore.setText("More...");
        btnMore.setBackgroundResource(R.drawable.card_background);
        btnMore.setVisibility(View.GONE);

        // adding footerView to listView
        listView.addFooterView(btnMore);

        // adapter for the college wall posts
        adapter = new StudentWallAdapter(getActivity().getApplicationContext());
        //setListAdapter(adapter);
        listView.setAdapter(adapter);

        // list for storing college wall posts
        studentWallPostsList = new ArrayList<Wall>();

        // object for ConnectionDetector
        connectionDetector = new ConnectionDetector(getActivity().getApplicationContext());

        // check if internet is working
        // else show no network toast
        if(connectionDetector.isInternetWorking()) {

            // get the wall posts from database
            //new GetStudentWallPosts().execute(String.valueOf(skipCounter));


        } else {

            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity().getApplicationContext(), "No internet", Toast.LENGTH_SHORT).show();
        }

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // increment the skip counter
                skipCounter = skipCounter + 10;
                //new GetStudentWallPosts().execute(String.valueOf(skipCounter));
            }
        });

        return rootView;
    }

/*

    class GetStudentWallPosts extends AsyncTask<String, Float, String>{
        @Override
        protected void onProgressUpdate(Float... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                // create parse object for the "CollegeWall"
                ParseQuery<ParseObject> studentPostsQuery = ParseQuery.getQuery(Constants.studentWallTable);

                // filter non-alumni posts and order by createdAt
                studentPostsQuery.whereEqualTo(Constants.alumniPost, true);
                studentPostsQuery.addDescendingOrder(Constants.createdAt);

                // check if skipCounter is not '0'
                // this indicates, request is received from 'load more' button click
                // to fetch more posts from db
                if(Integer.valueOf(params[0]) != 0){

                    studentPostsQuery.setSkip(skipCounter);
                }

                // limit results to 10
                studentPostsQuery.setLimit(10);

                // get the list of shouts
                studentPostsQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(final List<ParseObject> parseObjects, ParseException e) {

                        if (e == null) {

                            if (parseObjects.size() > 0) {

                                // navigate through the list of objects
                                for (int i = 0; i < parseObjects.size(); i++) {

                                    try {

                                        // create object for collegeWallPost class
                                        final Wall studentWall = new Wall();

                                        final ParseObject parseObject = parseObjects.get(i);

                                        // set the values of the wall post into object
                                        studentWall.setPostDescription(parseObjects.get(i).getString(Constants.postDescription));
                                        studentWall.setCreatedAt(parseObjects.get(i).getCreatedAt());
                                        studentWall.setUpdatedAt(parseObjects.get(i).getUpdatedAt());
                                        studentWall.setObjectId(parseObjects.get(i).getObjectId());
                                        studentWall.setLikes(parseObjects.get(i).getInt(Constants.likes));
                                        studentWall.setDislikes(parseObjects.get(i).getInt(Constants.dislikes));
                                        studentWall.setComments(parseObjects.get(i).getInt(Constants.comments));
                                        studentWall.setUserName(parseObjects.get(i).getString(Constants.userName));
                                        studentWall.setUserObjectId(parseObjects.get(i).getString(Constants.userObjectId));
                                        studentWall.setAlumniPost(parseObjects.get(i).getBoolean(Constants.alumniPost));
                                        studentWall.setStudentYear(parseObjects.get(i).getInt(Constants.studentYear));

                                        // check if the mediaId is not '0'
                                        // if so get the mediaFile from 'Media' table
                                        // else do nothing
                                        if(!parseObjects.get(i).getString(Constants.mediaId).contentEquals("0")) {

                                            ParseQuery<ParseObject> userDataObject = ParseQuery.getQuery(Constants.mediaTable);

                                            // check if the objectId exists as 'relativeId' in mediaTable table
                                            userDataObject.whereEqualTo(Constants.relativeId, parseObjects.get(i).getString(Constants.mediaId));

                                            userDataObject.getFirstInBackground(new GetCallback<ParseObject>() {
                                                @Override
                                                public void done(ParseObject parseObject, ParseException e) {

                                                    if(e==null) {
                                                        // get the mediaFile of the wall post
                                                        ParseFile mediaFile = (ParseFile) parseObject.get(Constants.mediaFile);

                                                        if (mediaFile != null)
                                                            mediaFile.getDataInBackground(new GetDataCallback() {
                                                                @Override
                                                                public void done(byte[] bytes, ParseException e) {

                                                                    // check if the image is available
                                                                    if (e == null) {

                                                                        // check if the mediaTable file bytes are null indicator bytes
                                                                        // if so assign the mediaTable file string to null indicator
                                                                        // else fetch the mediaTable file bytes and assign
                                                                        if (bytes == Constants.null_indicator.getBytes()) {

                                                                            // set the mediaTable file string to null indicator
                                                                            studentWall.setMediaFile(Constants.null_indicator.getBytes());
                                                                            //adapter.notifyDataSetChanged();
                                                                        } else {

                                                                            // set the mediaTable file string to image base 64 string
                                                                            //studentWall.setMediaFile(Base64.encodeToString(bytes, Base64.DEFAULT).toString());
                                                                            studentWall.setMediaFile(bytes);

                                                                        }

                                                                    }
                                                                }
                                                            });
                                                    }
                                                }
                                            });

                                        }
                                        else {

                                            // set the mediaTable file string to null indicator
                                            studentWall.setMediaFile(Constants.null_indicator.getBytes());
                                        }


                                        ParseQuery<ParseObject> userDataObject = ParseQuery.getQuery(Constants.mediaTable);

                                        // check if the objectId exists as 'relativeId' in mediaTable table
                                        userDataObject.whereEqualTo(Constants.relativeId, parseObjects.get(i).getString(Constants.userObjectId));

                                        userDataObject.getFirstInBackground(new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject parseObject, ParseException e) {

                                                if (e == null) {

                                                    // get the user image file
                                                    ParseFile userImageFile = (ParseFile) parseObject.get(Constants.mediaFile);

                                                    if (userImageFile != null)
                                                        // check if the data is available
                                                        userImageFile.getDataInBackground(new GetDataCallback() {
                                                            @Override
                                                            public void done(byte[] bytes, ParseException e) {

                                                                // check if the image is available
                                                                if (e == null) {

                                                                    if (bytes == Constants.null_indicator.getBytes()) {

                                                                        // set the user image file string to null indicator
                                                                        studentWall.setUserImage(Constants.null_indicator.getBytes());
                                                                        studentWallPostsList.add(studentWall);

                                                                        btnMore.setVisibility(View.VISIBLE);
                                                                        adapter.notifyDataSetChanged();
                                                                    } else {

                                                                        // set the user image file string to image base 64 string
                                                                        //studentWallPost.setUserImage(Base64.encodeToString(bytes, Base64.DEFAULT).toString());
                                                                        studentWall.setUserImage(bytes);
                                                                        studentWallPostsList.add(studentWall);

                                                                        btnMore.setVisibility(View.VISIBLE);
                                                                        adapter.notifyDataSetChanged();
                                                                    }


                                                                }
                                                            }
                                                        });

                                                }

                                                // if there is no record found for the given user id
                                                // show the default image
                                                else {
                                                    // set the user image file string to null indicator
                                                    studentWall.setUserImage(Constants.null_indicator.getBytes());
                                                    studentWallPostsList.add(studentWall);
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }
                                        });

                                        adapter.updateItems(studentWallPostsList);
                                        adapter.notifyDataSetChanged();

                                        progressBar.setVisibility(View.GONE);


                                    } catch (Exception ex) {

                                        Log.v(Constants.appName, ex.getMessage());
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }

                            }

                            // if there are no objects to display
                            else {

                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity().getApplicationContext(), "No more to display", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

            }

            // all parse data not found exceptions are caught
            catch(Exception e){
                getActivity().finish();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);

            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                    */
/**
                     * Updating parsed JSON data into ListView
                     * *//*


                    adapter.updateItems(studentWallPostsList);
                    // notify the adapter
                    adapter.notifyDataSetChanged();

                    // save the data to shared preferences
                    //sharedPreferences.saveWallPosts(studentWallPostsList);

                }
            });
        }
    }
*/

    @Override
    public void onResume() {
        super.onResume();

        adapter.updateItems(studentWallPostsList);
        adapter.notifyDataSetChanged();
    }
}
