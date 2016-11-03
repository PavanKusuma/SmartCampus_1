package svecw.smartcampus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import internaldb.SmartCampusDB;
import internaldb.SmartSessionManager;
import model.GlobalInfo;
import utils.Constants;

/**
 * Created by Pavan on 7/7/15.
 */
public class WelcomeActivity extends Activity {

    // get the view from activity
    FloatingActionButton goBtn;
    public static ArrayList<GlobalInfo> knowledgeWallPosts = new ArrayList<GlobalInfo>();;

    RelativeLayout userImageLayout;
    TextView welcomeUsername;
    ImageView userImage;
    // global snackbar
    Snackbar snackbar;
    int counter = 0;

    SmartCampusDB smartCampusDB = new SmartCampusDB(this);

    // session manager
    SmartSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // display the welcome username from db
        welcomeUsername = (TextView) findViewById(R.id.welcomeUsername); welcomeUsername.setTypeface(sansFont);
        userImageLayout = (RelativeLayout) findViewById(R.id.userImageLayout);
        userImage = (ImageView) findViewById(R.id.userImage);

        welcomeUsername.setText("Hello " + smartCampusDB.getUser().get(Constants.userName).toString());
        // get views from activity
        goBtn = (FloatingActionButton) findViewById(R.id.goBtn);

        // navigate to home activity
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent homeIntent = new Intent(getApplicationContext(), GlobalHomeActivity.class);
                startActivity(homeIntent);
                finish();


                //new GetGlobalPosts().execute();


            }
        });


        // check if a profile photo exists in current session
        // if so display the photo
        // else hide the photo layout

        // get session manager object
        session = new SmartSessionManager(getApplicationContext());

        // get the profile pic from current shared preference
        String photoString = session.getProfilePhoto();
        if(!photoString.equals("-")){

            byte[] b = Base64.decode(photoString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

            RoundImage roundedImage = new RoundImage(bitmap, 220, 220);
            RoundImage roundedImage1 = new RoundImage(roundedImage.getBitmap(), bitmap.getWidth(), bitmap.getHeight());
            userImage.setImageDrawable(roundedImage1);

            userImageLayout.setVisibility(View.VISIBLE);
            userImage.setImageBitmap(bitmap);

        }
        else{

            // hide user image layout
            //userImageLayout.setVisibility(View.GONE);
        }
    }


/*
    *//**
     * This AsyncTask class will fetch globalInfo wall posts from
     * database and assign it to adapter for populating listview
     *//*
    class GetGlobalPosts extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... strings) {



            // create parse object for the "GlobalInfo"
            ParseQuery<ParseObject> globalInfoQuery = ParseQuery.getQuery(Constants.globalInfoTable);

            // get mapping for the list of objectIds
            globalInfoQuery.addDescendingOrder(Constants.createdAt);

            // get the list of shouts
            globalInfoQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> parseObjects, ParseException e) {

                    if (e == null) {

                        if (parseObjects.size() > 0) {

                            // navigate through the list of objects
                            for (int i = 0; i < parseObjects.size(); i++) {
                                counter = i;
                                // create object for collegeWallPost class
                                final GlobalInfo knowledgeWallPost = new GlobalInfo();


                                // date format for displaying created date
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm");

                                // set the values of the wall post into object
                                knowledgeWallPost.setTitle(parseObjects.get(i).getString(Constants.title));
                                knowledgeWallPost.setDescription(parseObjects.get(i).getString(Constants.description));
                                knowledgeWallPost.setBranch(parseObjects.get(i).getString(Constants.branch));
                                knowledgeWallPost.setRole(parseObjects.get(i).getString(Constants.role));
                                knowledgeWallPost.setCreatedAt(simpleDateFormat.format(parseObjects.get(i).getCreatedAt()));
                                knowledgeWallPost.setUpdatedAt(simpleDateFormat.format(parseObjects.get(i).getUpdatedAt()));
                                knowledgeWallPost.setObjectId(parseObjects.get(i).getObjectId());
                                knowledgeWallPost.setCategory(parseObjects.get(i).getString(Constants.category));

                                // get the mediaFile of the wall post
                                ParseFile mediaFile = (ParseFile) parseObjects.get(i).get(Constants.mediaFile);

                                mediaFile.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] bytes, ParseException e) {

                                        // check if the image is available
                                        if (e == null) {

                                            // check if the mediaTable file bytes are null indicator bytes
                                            // if so assign the mediaTable file string to null indicator
                                            // else fetch the mediaTable file bytes and assign
                                            if (Arrays.equals(bytes, Constants.null_indicator.getBytes())) {

                                                knowledgeWallPost.setMediaFile(Constants.null_indicator);
                                                //knowledgeWallPosts.add(knowledgeWallPost);

                                            } else {

                                                knowledgeWallPost.setMediaFile(Base64.encodeToString(bytes, Base64.DEFAULT).toString());
                                                Log.i(Constants.appName, "Image found");
                                                //knowledgeWallPosts.add(knowledgeWallPost);

                                                Log.i(Constants.appName, "Just Added");

                                            }

                                        } else {

                                            Log.i(Constants.appName, "Image not found");
                                        }

                                    }
                                });

                                knowledgeWallPosts.add(knowledgeWallPost);

                            }



                            Log.i(Constants.appName, "Size : "+knowledgeWallPosts.size());
                            Intent homeIntent = new Intent(getApplicationContext(), GlobalHomeActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.category, Constants.technology);
                            bundle.putParcelableArrayList(Constants.globalInfoTable, knowledgeWallPosts);
                            homeIntent.putExtras(bundle);

                            startActivity(homeIntent);


                        }
                    } else {

                        Log.i(Constants.appName, "Pass4");
                    }
                }
            });
*//*
            Log.i(Constants.appName, "Size : " + knowledgeWallPosts.size());
            Intent homeIntent = new Intent(getApplicationContext(), GlobalHomeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.category, Constants.technology);
            bundle.putParcelableArrayList(Constants.globalInfoTable, knowledgeWallPosts);
            homeIntent.putExtras(bundle);

            startActivity(homeIntent);*//*

            return null;
        }

    }*/
}
