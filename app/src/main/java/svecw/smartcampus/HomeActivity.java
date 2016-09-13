package svecw.smartcampus;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import internaldb.SmartCampusDB;
import model.KnowledgeInfo;

/**
 * Created by Pavan on 6/8/15.
 */
public class HomeActivity extends AppCompatActivity {

    // get views from activity
    LinearLayout cseKnowledgeView, eceKnowledgeView, eeeKnowledgeView, mechKnowledgeView, civilKnowledgeView,
        basicScienceKnowledgeView, mbaKnowledgeView, mcaKnowledgeView;

    // progress bar
    ProgressBar knowledgeWallProgressBar;

    // bottom navigation views
    LinearLayout collegeWallView, studentWallView, alumniWallView, moreView;

    // internal db object
    SmartCampusDB smartCampusDB = new SmartCampusDB(this);

    // list for storing global wall posts
    ArrayList<KnowledgeInfo> knowledgeWallPosts;

    // date format for displaying created date
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy");

    // layout inflator
    LayoutInflater layoutInflater;
    RelativeLayout l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.app_name));

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        // object for LayoutInflater
        layoutInflater = LayoutInflater.from(this);

        // get views from activity
        cseKnowledgeView = (LinearLayout) findViewById(R.id.cseKnowledgeView);
        eceKnowledgeView = (LinearLayout) findViewById(R.id.eceKnowledgeView);
        eeeKnowledgeView = (LinearLayout) findViewById(R.id.eeeKnowledgeView);
        mechKnowledgeView = (LinearLayout) findViewById(R.id.mechKnowledgeView);
        civilKnowledgeView = (LinearLayout) findViewById(R.id.civilKnowledgeView);
        basicScienceKnowledgeView = (LinearLayout) findViewById(R.id.basicScienceKnowledgeView);
        mbaKnowledgeView = (LinearLayout) findViewById(R.id.mbaKnowledgeView);
        mcaKnowledgeView = (LinearLayout) findViewById(R.id.mcaKnowledgeView);

        // progress bar
        knowledgeWallProgressBar = (ProgressBar) findViewById(R.id.knowledgeWallProgressBar);

        // bottom bar views
        collegeWallView = (LinearLayout) findViewById(R.id.collegeWallView);
        studentWallView = (LinearLayout) findViewById(R.id.studentWallView);
        alumniWallView = (LinearLayout) findViewById(R.id.alumniWallView);
        moreView = (LinearLayout) findViewById(R.id.moreView);

        // wall post list
        knowledgeWallPosts = new ArrayList<KnowledgeInfo>();

        // show activities on click of corresponding views from home activity
        collegeWallView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // navigate to college wall activity
                Intent collegeWallIntent = new Intent(getApplicationContext(), CollegeWallActivity.class);
                startActivity(collegeWallIntent);

            }
        });


        studentWallView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // navigate to student wall activity
                Intent studentWallIntent = new Intent(getApplicationContext(), StudentWallActivity.class);
                startActivity(studentWallIntent);
            }
        });

        alumniWallView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if the user is student, then he will be see messages from faculty
                Intent alumniWallIntent = new Intent(getApplicationContext(), AlumniWallActivity.class);
                startActivity(alumniWallIntent);

            }
        });

        moreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent moreIntent = new Intent(getApplicationContext(), MoreActivity.class);
                startActivity(moreIntent);

            }
        });

        // get the posts
        //new GetGlobalWallPosts().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.createWallPost) {

            // navigate to selectNewPostActivity to show posting options based on privileges
            Intent newPostIntent = new Intent(getApplicationContext(), SelectNewPostActivity.class);
            startActivity(newPostIntent);

            return true;
        }/*
        else if (id == R.id.userProfile) {

            // navigate to selectNewPostActivity to show posting options based on privileges
            Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(profileIntent);

            return true;
        }*/


        return super.onOptionsItemSelected(item);
    }


    /**
     * This AsyncTask class will fetch globalInfo wall posts from
     * database and assign it to adapter for populating listview
     */
/*
    class GetGlobalWallPosts extends AsyncTask<String, Float, String> {

        @Override
        protected String doInBackground(String... params) {

            try {

                // create parse object for the "GlobalInfo"
                ParseQuery<ParseObject> globalInfoQuery = ParseQuery.getQuery(Constants.globalInfoTable);

                // get mapping for the list of objectIds
                globalInfoQuery.addDescendingOrder(Constants.createdAt);

                // get the list of shouts
                globalInfoQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {

                        if (e == null) {

                            if (parseObjects.size() > 0) {

                                // navigate through the list of objects
                                for (int i = 0; i < parseObjects.size(); i++) {

                                    // create object for collegeWallPost class
                                    final KnowledgeInfo knowledgeWallPost = new KnowledgeInfo();

                                    // set the values of the wall post into object
                                    knowledgeWallPost.setTitle(parseObjects.get(i).getString(Constants.title));
                                    knowledgeWallPost.setDescription(parseObjects.get(i).getString(Constants.description));
                                    knowledgeWallPost.setBranch(parseObjects.get(i).getString(Constants.branch));
                                    knowledgeWallPost.setRole(parseObjects.get(i).getString(Constants.role));
                                    knowledgeWallPost.setCreatedAt(parseObjects.get(i).getCreatedAt());
                                    knowledgeWallPost.setUpdatedAt(parseObjects.get(i).getUpdatedAt());
                                    knowledgeWallPost.setObjectId(parseObjects.get(i).getObjectId());

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

                                                    // set the mediaTable file string to null indicator
                                                    knowledgeWallPost.setMediaFile(Constants.null_indicator);
                                                    knowledgeWallPosts.add(knowledgeWallPost);

                                                    // assign the views to horizantal view based on branches
                                                    if (knowledgeWallPost.getBranch().contentEquals(Constants.CSE)) {

                                                        l = (RelativeLayout) layoutInflater.inflate(R.layout.home_single_listitem, null); // new RelativeLayout(NewHomeActivity.this);

                                                        TextView globalWallPostTitle = (TextView) l.findViewById(R.id.globalPostTitle);
                                                        TextView globalWallPostCreatedAt = (TextView) l.findViewById(R.id.globalPostCreatedAt);
                                                        final TextView globalWallPostDescription = (TextView) l.findViewById(R.id.globalPostDescription);
                                                        ImageView globalPostImage = (ImageView) l.findViewById(R.id.globalPostImage);

                                                        // set title and created At
                                                        globalWallPostTitle.setText(knowledgeWallPost.getTitle());
                                                        globalWallPostCreatedAt.setText(simpleDateFormat.format(knowledgeWallPost.getCreatedAt()));
                                                        globalWallPostDescription.setText(knowledgeWallPost.getDescription());
                                                        // assign default image
                                                        //globalPostImage.setImageResource(R.drawable.moon);

                                                        l.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                // pass the bundle for the full screen activity
                                                                Intent webViewIntent = new Intent(getApplicationContext(), KnowledgeWallWebView.class);
                                                                webViewIntent.putExtra(Constants.description, globalWallPostDescription.getText().toString());
                                                                startActivity(webViewIntent);
                                                            }
                                                        });

                                                        cseKnowledgeView.addView(l);

                                                    }

                                                    else if (knowledgeWallPost.getBranch().contentEquals(Constants.ECE)) {

                                                         l = (RelativeLayout) layoutInflater.inflate(R.layout.home_single_listitem, null); // new RelativeLayout(NewHomeActivity.this);

                                                        TextView globalWallPostTitle = (TextView) l.findViewById(R.id.globalPostTitle);
                                                        TextView globalWallPostCreatedAt = (TextView) l.findViewById(R.id.globalPostCreatedAt);
                                                        final TextView globalWallPostDescription = (TextView) l.findViewById(R.id.globalPostDescription);
                                                        ImageView globalPostImage = (ImageView) l.findViewById(R.id.globalPostImage);

                                                        // set title and created At
                                                        globalWallPostTitle.setText(knowledgeWallPost.getTitle());
                                                        globalWallPostCreatedAt.setText(simpleDateFormat.format(knowledgeWallPost.getCreatedAt()));
                                                        globalWallPostDescription.setText(knowledgeWallPost.getDescription());
                                                        // assign default image
                                                        //globalPostImage.setImageResource(R.drawable.moon);

                                                        l.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                // pass the bundle for the full screen activity
                                                                Intent webViewIntent = new Intent(getApplicationContext(), KnowledgeWallWebView.class);
                                                                webViewIntent.putExtra(Constants.description, globalWallPostDescription.getText().toString());
                                                                startActivity(webViewIntent);
                                                            }
                                                        });

                                                        eceKnowledgeView.addView(l);

                                                    }

                                                    else if (knowledgeWallPost.getBranch().contentEquals(Constants.EEE)) {

                                                         l = (RelativeLayout) layoutInflater.inflate(R.layout.home_single_listitem, null); // new RelativeLayout(NewHomeActivity.this);

                                                        TextView globalWallPostTitle = (TextView) l.findViewById(R.id.globalPostTitle);
                                                        TextView globalWallPostCreatedAt = (TextView) l.findViewById(R.id.globalPostCreatedAt);
                                                        final TextView globalWallPostDescription = (TextView) l.findViewById(R.id.globalPostDescription);
                                                        ImageView globalPostImage = (ImageView) l.findViewById(R.id.globalPostImage);

                                                        // set title and created At
                                                        globalWallPostTitle.setText(knowledgeWallPost.getTitle());
                                                        globalWallPostCreatedAt.setText(simpleDateFormat.format(knowledgeWallPost.getCreatedAt()));
                                                        globalWallPostDescription.setText(knowledgeWallPost.getDescription());
                                                        // assign default image
                                                        //globalPostImage.setImageResource(R.drawable.moon);

                                                        l.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                // pass the bundle for the full screen activity
                                                                Intent webViewIntent = new Intent(getApplicationContext(), KnowledgeWallWebView.class);
                                                                webViewIntent.putExtra(Constants.description, globalWallPostDescription.getText().toString());
                                                                startActivity(webViewIntent);
                                                            }
                                                        });

                                                        eeeKnowledgeView.addView(l);

                                                    }

                                                    else if (knowledgeWallPost.getBranch().contentEquals(Constants.IT)) {

                                                         l = (RelativeLayout) layoutInflater.inflate(R.layout.home_single_listitem, null); // new RelativeLayout(NewHomeActivity.this);

                                                        TextView globalWallPostTitle = (TextView) l.findViewById(R.id.globalPostTitle);
                                                        TextView globalWallPostCreatedAt = (TextView) l.findViewById(R.id.globalPostCreatedAt);
                                                        final TextView globalWallPostDescription = (TextView) l.findViewById(R.id.globalPostDescription);
                                                        ImageView globalPostImage = (ImageView) l.findViewById(R.id.globalPostImage);

                                                        // set title and created At
                                                        globalWallPostTitle.setText(knowledgeWallPost.getTitle());
                                                        globalWallPostCreatedAt.setText(simpleDateFormat.format(knowledgeWallPost.getCreatedAt()));
                                                        globalWallPostDescription.setText(knowledgeWallPost.getDescription());
                                                        // assign default image
                                                        //globalPostImage.setImageResource(R.drawable.moon);

                                                        l.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                // pass the bundle for the full screen activity
                                                                Intent webViewIntent = new Intent(getApplicationContext(), KnowledgeWallWebView.class);
                                                                webViewIntent.putExtra(Constants.description, globalWallPostDescription.getText().toString());
                                                                startActivity(webViewIntent);
                                                            }
                                                        });

                                                        cseKnowledgeView.addView(l);
                                                    }

                                                    else if (knowledgeWallPost.getBranch().contentEquals(Constants.MECHANICAL)) {

                                                         l = (RelativeLayout) layoutInflater.inflate(R.layout.home_single_listitem, null); // new RelativeLayout(NewHomeActivity.this);

                                                        TextView globalWallPostTitle = (TextView) l.findViewById(R.id.globalPostTitle);
                                                        TextView globalWallPostCreatedAt = (TextView) l.findViewById(R.id.globalPostCreatedAt);
                                                        final TextView globalWallPostDescription = (TextView) l.findViewById(R.id.globalPostDescription);
                                                        ImageView globalPostImage = (ImageView) l.findViewById(R.id.globalPostImage);

                                                        // set title and created At
                                                        globalWallPostTitle.setText(knowledgeWallPost.getTitle());
                                                        globalWallPostCreatedAt.setText(simpleDateFormat.format(knowledgeWallPost.getCreatedAt()));
                                                        globalWallPostDescription.setText(knowledgeWallPost.getDescription());
                                                        // assign default image
                                                        //globalPostImage.setImageResource(R.drawable.moon);

                                                        l.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                // pass the bundle for the full screen activity
                                                                Intent webViewIntent = new Intent(getApplicationContext(), KnowledgeWallWebView.class);
                                                                webViewIntent.putExtra(Constants.description, globalWallPostDescription.getText().toString());
                                                                startActivity(webViewIntent);
                                                            }
                                                        });

                                                        mechKnowledgeView.addView(l);
                                                    }

                                                    else if (knowledgeWallPost.getBranch().contentEquals(Constants.CIVIL)) {

                                                         l = (RelativeLayout) layoutInflater.inflate(R.layout.home_single_listitem, null); // new RelativeLayout(NewHomeActivity.this);

                                                        TextView globalWallPostTitle = (TextView) l.findViewById(R.id.globalPostTitle);
                                                        TextView globalWallPostCreatedAt = (TextView) l.findViewById(R.id.globalPostCreatedAt);
                                                        final TextView globalWallPostDescription = (TextView) l.findViewById(R.id.globalPostDescription);
                                                        ImageView globalPostImage = (ImageView) l.findViewById(R.id.globalPostImage);

                                                        // set title and created At
                                                        globalWallPostTitle.setText(knowledgeWallPost.getTitle());
                                                        globalWallPostCreatedAt.setText(simpleDateFormat.format(knowledgeWallPost.getCreatedAt()));
                                                        globalWallPostDescription.setText(knowledgeWallPost.getDescription());
                                                        // assign default image
                                                        //globalPostImage.setImageResource(R.drawable.moon);

                                                        l.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                // pass the bundle for the full screen activity
                                                                Intent webViewIntent = new Intent(getApplicationContext(), KnowledgeWallWebView.class);
                                                                webViewIntent.putExtra(Constants.description, globalWallPostDescription.getText().toString());
                                                                startActivity(webViewIntent);
                                                            }
                                                        });

                                                        civilKnowledgeView.addView(l);
                                                    }

                                                    else if (knowledgeWallPost.getBranch().contentEquals(Constants.BASICSCIENCE)) {

                                                         l = (RelativeLayout) layoutInflater.inflate(R.layout.home_single_listitem, null); // new RelativeLayout(NewHomeActivity.this);

                                                        TextView globalWallPostTitle = (TextView) l.findViewById(R.id.globalPostTitle);
                                                        TextView globalWallPostCreatedAt = (TextView) l.findViewById(R.id.globalPostCreatedAt);
                                                        final TextView globalWallPostDescription = (TextView) l.findViewById(R.id.globalPostDescription);
                                                        ImageView globalPostImage = (ImageView) l.findViewById(R.id.globalPostImage);

                                                        // set title and created At
                                                        globalWallPostTitle.setText(knowledgeWallPost.getTitle());
                                                        globalWallPostCreatedAt.setText(simpleDateFormat.format(knowledgeWallPost.getCreatedAt()));
                                                        globalWallPostDescription.setText(knowledgeWallPost.getDescription());
                                                        // assign default image
                                                        //globalPostImage.setImageResource(R.drawable.moon);

                                                        l.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                // pass the bundle for the full screen activity
                                                                Intent webViewIntent = new Intent(getApplicationContext(), KnowledgeWallWebView.class);
                                                                webViewIntent.putExtra(Constants.description, globalWallPostDescription.getText().toString());
                                                                startActivity(webViewIntent);
                                                            }
                                                        });

                                                        basicScienceKnowledgeView.addView(l);
                                                    }

                                                    else if (knowledgeWallPost.getBranch().contentEquals(Constants.MBA)) {

                                                         l = (RelativeLayout) layoutInflater.inflate(R.layout.home_single_listitem, null); // new RelativeLayout(NewHomeActivity.this);

                                                        TextView globalWallPostTitle = (TextView) l.findViewById(R.id.globalPostTitle);
                                                        TextView globalWallPostCreatedAt = (TextView) l.findViewById(R.id.globalPostCreatedAt);
                                                        final TextView globalWallPostDescription = (TextView) l.findViewById(R.id.globalPostDescription);
                                                        ImageView globalPostImage = (ImageView) l.findViewById(R.id.globalPostImage);

                                                        // set title and created At
                                                        globalWallPostTitle.setText(knowledgeWallPost.getTitle());
                                                        globalWallPostCreatedAt.setText(simpleDateFormat.format(knowledgeWallPost.getCreatedAt()));
                                                        globalWallPostDescription.setText(knowledgeWallPost.getDescription());
                                                        // assign default image
                                                        //globalPostImage.setImageResource(R.drawable.moon);

                                                        l.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                // pass the bundle for the full screen activity
                                                                Intent webViewIntent = new Intent(getApplicationContext(), KnowledgeWallWebView.class);
                                                                webViewIntent.putExtra(Constants.description, globalWallPostDescription.getText().toString());
                                                                startActivity(webViewIntent);
                                                            }
                                                        });

                                                        mbaKnowledgeView.addView(l);
                                                    }

                                                    else if (knowledgeWallPost.getBranch().contentEquals(Constants.MCA)) {

                                                         l = (RelativeLayout) layoutInflater.inflate(R.layout.home_single_listitem, null); // new RelativeLayout(NewHomeActivity.this);

                                                        TextView globalWallPostTitle = (TextView) l.findViewById(R.id.globalPostTitle);
                                                        TextView globalWallPostCreatedAt = (TextView) l.findViewById(R.id.globalPostCreatedAt);
                                                        final TextView globalWallPostDescription = (TextView) l.findViewById(R.id.globalPostDescription);
                                                        ImageView globalPostImage = (ImageView) l.findViewById(R.id.globalPostImage);

                                                        // set title and created At
                                                        globalWallPostTitle.setText(knowledgeWallPost.getTitle());
                                                        globalWallPostCreatedAt.setText(simpleDateFormat.format(knowledgeWallPost.getCreatedAt()));
                                                        globalWallPostDescription.setText(knowledgeWallPost.getDescription());
                                                        // assign default image
                                                        //globalPostImage.setImageResource(R.drawable.moon);

                                                        l.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                // pass the bundle for the full screen activity
                                                                Intent webViewIntent = new Intent(getApplicationContext(), KnowledgeWallWebView.class);
                                                                webViewIntent.putExtra(Constants.description, globalWallPostDescription.getText().toString());
                                                                startActivity(webViewIntent);
                                                            }
                                                        });

                                                        mcaKnowledgeView.addView(l);
                                                    }


                                                    // hide the progress bar
                                                    knowledgeWallProgressBar.setVisibility(View.GONE);

                                                }



                                                else {

                                                    // set the mediaTable file string to image base 64 string
                                                    knowledgeWallPost.setMediaFile(Base64.encodeToString(bytes, Base64.DEFAULT).toString());
                                                    knowledgeWallPosts.add(knowledgeWallPost);

                                                    // assign the views to horizantal view based on branches
                                                    if (knowledgeWallPost.getBranch().contentEquals(Constants.CSE)) {

                                                         l = (RelativeLayout) layoutInflater.inflate(R.layout.home_single_listitem, null); // new RelativeLayout(NewHomeActivity.this);

                                                        TextView globalWallPostTitle = (TextView) l.findViewById(R.id.globalPostTitle);
                                                        TextView globalWallPostCreatedAt = (TextView) l.findViewById(R.id.globalPostCreatedAt);
                                                        final TextView globalWallPostDescription = (TextView) l.findViewById(R.id.globalPostDescription);
                                                        ImageView globalPostImage = (ImageView) l.findViewById(R.id.globalPostImage);

                                                        // set title and created At
                                                        globalWallPostTitle.setText(knowledgeWallPost.getTitle());
                                                        globalWallPostCreatedAt.setText(simpleDateFormat.format(knowledgeWallPost.getCreatedAt()));
                                                        globalWallPostDescription.setText(knowledgeWallPost.getDescription());
                                                        // assign the bitmap
                                                        Bitmap bitmap = decodeSampledBitmapFromResource(bytes, 200, 200);
                                                        globalPostImage.setImageBitmap(bitmap);


                                                        l.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                // pass the bundle for the full screen activity
                                                                Intent webViewIntent = new Intent(getApplicationContext(), KnowledgeWallWebView.class);
                                                                webViewIntent.putExtra(Constants.description, globalWallPostDescription.getText().toString());
                                                                startActivity(webViewIntent);
                                                            }
                                                        });

                                                        cseKnowledgeView.addView(l);

                                                    }

                                                    else if (knowledgeWallPost.getBranch().contentEquals(Constants.ECE)) {

                                                         l = (RelativeLayout) layoutInflater.inflate(R.layout.home_single_listitem, null); // new RelativeLayout(NewHomeActivity.this);

                                                        TextView globalWallPostTitle = (TextView) l.findViewById(R.id.globalPostTitle);
                                                        TextView globalWallPostCreatedAt = (TextView) l.findViewById(R.id.globalPostCreatedAt);
                                                        //final TextView globalWallPostDescription = (TextView) l.findViewById(R.id.globalPostDescription);
                                                        //ImageView globalPostImage = (ImageView) l.findViewById(R.id.globalPostImage);

                                                        // set title and created At
                                                        globalWallPostTitle.setText(knowledgeWallPost.getTitle());
                                                        globalWallPostCreatedAt.setText(simpleDateFormat.format(knowledgeWallPost.getCreatedAt()));
                                                        //globalWallPostDescription.setText(knowledgeWallPost.getDescription());
                                                        // assign the bitmap
                                                        Bitmap bitmap = decodeSampledBitmapFromResource(bytes, 200, 200);
                                                        //globalPostImage.setImageBitmap(bitmap);

                                                        l.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                // pass the bundle for the full screen activity
                                                                Intent webViewIntent = new Intent(getApplicationContext(), KnowledgeWallWebView.class);
                                                                webViewIntent.putExtra(Constants.description, knowledgeWallPost.getDescription());
                                                                startActivity(webViewIntent);
                                                            }
                                                        });


                                                        eceKnowledgeView.addView(l);

                                                    }

                                                    else if (knowledgeWallPost.getBranch().contentEquals(Constants.EEE)) {

                                                         l = (RelativeLayout) layoutInflater.inflate(R.layout.home_single_listitem, null); // new RelativeLayout(NewHomeActivity.this);

                                                        TextView globalWallPostTitle = (TextView) l.findViewById(R.id.globalPostTitle);
                                                        TextView globalWallPostCreatedAt = (TextView) l.findViewById(R.id.globalPostCreatedAt);
                                                        //final TextView globalWallPostDescription = (TextView) l.findViewById(R.id.globalPostDescription);
                                                        //ImageView globalPostImage = (ImageView) l.findViewById(R.id.globalPostImage);

                                                        // set title and created At
                                                        globalWallPostTitle.setText(knowledgeWallPost.getTitle());
                                                        globalWallPostCreatedAt.setText(simpleDateFormat.format(knowledgeWallPost.getCreatedAt()));
                                                        //globalWallPostDescription.setText(knowledgeWallPost.getDescription());
                                                        // assign the bitmap
                                                        Bitmap bitmap = decodeSampledBitmapFromResource(bytes, 200, 200);
                                                        //globalPostImage.setImageBitmap(bitmap);

                                                        l.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                // pass the bundle for the full screen activity
                                                                Intent webViewIntent = new Intent(getApplicationContext(), KnowledgeWallWebView.class);
                                                                //webViewIntent.putExtra(Constants.description, globalWallPostDescription.getText().toString());
                                                                startActivity(webViewIntent);
                                                            }
                                                        });


                                                        eeeKnowledgeView.addView(l);

                                                    }

                                                    else if (knowledgeWallPost.getBranch().contentEquals(Constants.IT)) {

                                                         l = (RelativeLayout) layoutInflater.inflate(R.layout.home_single_listitem, null); // new RelativeLayout(NewHomeActivity.this);

                                                        TextView globalWallPostTitle = (TextView) l.findViewById(R.id.globalPostTitle);
                                                        TextView globalWallPostCreatedAt = (TextView) l.findViewById(R.id.globalPostCreatedAt);
                                                        //final TextView globalWallPostDescription = (TextView) l.findViewById(R.id.globalPostDescription);
                                                        //ImageView globalPostImage = (ImageView) l.findViewById(R.id.globalPostImage);

                                                        // set title and created At
                                                        globalWallPostTitle.setText(knowledgeWallPost.getTitle());
                                                        globalWallPostCreatedAt.setText(simpleDateFormat.format(knowledgeWallPost.getCreatedAt()));
                                                        //globalWallPostDescription.setText(knowledgeWallPost.getDescription());
                                                        // assign the bitmap
                                                        Bitmap bitmap = decodeSampledBitmapFromResource(bytes, 200, 200);
                                                        //globalPostImage.setImageBitmap(bitmap);


                                                        l.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                // pass the bundle for the full screen activity
                                                                Intent webViewIntent = new Intent(getApplicationContext(), KnowledgeWallWebView.class);
                                                                //webViewIntent.putExtra(Constants.description, globalWallPostDescription.getText().toString());
                                                                startActivity(webViewIntent);
                                                            }
                                                        });

                                                        cseKnowledgeView.addView(l);
                                                    }


                                                    else if (knowledgeWallPost.getBranch().contentEquals(Constants.MECHANICAL)) {

                                                         l = (RelativeLayout) layoutInflater.inflate(R.layout.home_single_listitem, null); // new RelativeLayout(NewHomeActivity.this);

                                                        TextView globalWallPostTitle = (TextView) l.findViewById(R.id.globalPostTitle);
                                                        TextView globalWallPostCreatedAt = (TextView) l.findViewById(R.id.globalPostCreatedAt);
                                                        //final TextView globalWallPostDescription = (TextView) l.findViewById(R.id.globalPostDescription);
                                                        //ImageView globalPostImage = (ImageView) l.findViewById(R.id.globalPostImage);

                                                        // set title and created At
                                                        globalWallPostTitle.setText(knowledgeWallPost.getTitle());
                                                        globalWallPostCreatedAt.setText(simpleDateFormat.format(knowledgeWallPost.getCreatedAt()));
                                                        //globalWallPostDescription.setText(knowledgeWallPost.getDescription());
                                                        // assign the bitmap
                                                        Bitmap bitmap = decodeSampledBitmapFromResource(bytes, 200, 200);
                                                        //globalPostImage.setImageBitmap(bitmap);

                                                        l.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                // pass the bundle for the full screen activity
                                                                Intent webViewIntent = new Intent(getApplicationContext(), KnowledgeWallWebView.class);
                                                                //webViewIntent.putExtra(Constants.description, globalWallPostDescription.getText().toString());
                                                                startActivity(webViewIntent);
                                                            }
                                                        });

                                                        mechKnowledgeView.addView(l);
                                                    }

                                                    else if (knowledgeWallPost.getBranch().contentEquals(Constants.CIVIL)) {

                                                         l = (RelativeLayout) layoutInflater.inflate(R.layout.home_single_listitem, null); // new RelativeLayout(NewHomeActivity.this);

                                                        TextView globalWallPostTitle = (TextView) l.findViewById(R.id.globalPostTitle);
                                                        TextView globalWallPostCreatedAt = (TextView) l.findViewById(R.id.globalPostCreatedAt);
                                                        //final TextView globalWallPostDescription = (TextView) l.findViewById(R.id.globalPostDescription);
                                                        //ImageView globalPostImage = (ImageView) l.findViewById(R.id.globalPostImage);

                                                        // set title and created At
                                                        globalWallPostTitle.setText(knowledgeWallPost.getTitle());
                                                        globalWallPostCreatedAt.setText(simpleDateFormat.format(knowledgeWallPost.getCreatedAt()));
                                                        //globalWallPostDescription.setText(knowledgeWallPost.getDescription());
                                                        // assign the bitmap
                                                        Bitmap bitmap = decodeSampledBitmapFromResource(bytes, 200, 200);
                                                        //globalPostImage.setImageBitmap(bitmap);

                                                        l.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                // pass the bundle for the full screen activity
                                                                Intent webViewIntent = new Intent(getApplicationContext(), KnowledgeWallWebView.class);
                                                                //webViewIntent.putExtra(Constants.description, globalWallPostDescription.getText().toString());
                                                                startActivity(webViewIntent);
                                                            }
                                                        });

                                                        civilKnowledgeView.addView(l);
                                                    }

                                                    else if (knowledgeWallPost.getBranch().contentEquals(Constants.BASICSCIENCE)) {

                                                         l = (RelativeLayout) layoutInflater.inflate(R.layout.home_single_listitem, null); // new RelativeLayout(NewHomeActivity.this);

                                                        TextView globalWallPostTitle = (TextView) l.findViewById(R.id.globalPostTitle);
                                                        TextView globalWallPostCreatedAt = (TextView) l.findViewById(R.id.globalPostCreatedAt);
                                                        //final TextView globalWallPostDescription = (TextView) l.findViewById(R.id.globalPostDescription);
                                                        //ImageView globalPostImage = (ImageView) l.findViewById(R.id.globalPostImage);

                                                        // set title and created At
                                                        globalWallPostTitle.setText(knowledgeWallPost.getTitle());
                                                        globalWallPostCreatedAt.setText(simpleDateFormat.format(knowledgeWallPost.getCreatedAt()));
                                                        //globalWallPostDescription.setText(knowledgeWallPost.getDescription());
                                                        // assign the bitmap
                                                        Bitmap bitmap = decodeSampledBitmapFromResource(bytes, 200, 200);
                                                        //globalPostImage.setImageBitmap(bitmap);

                                                        l.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                // pass the bundle for the full screen activity
                                                                Intent webViewIntent = new Intent(getApplicationContext(), KnowledgeWallWebView.class);
                                                                //webViewIntent.putExtra(Constants.description, globalWallPostDescription.getText().toString());
                                                                startActivity(webViewIntent);
                                                            }
                                                        });

                                                        basicScienceKnowledgeView.addView(l);
                                                    }

                                                    else if (knowledgeWallPost.getBranch().contentEquals(Constants.MBA)) {

                                                         l = (RelativeLayout) layoutInflater.inflate(R.layout.home_single_listitem, null); // new RelativeLayout(NewHomeActivity.this);

                                                        TextView globalWallPostTitle = (TextView) l.findViewById(R.id.globalPostTitle);
                                                        TextView globalWallPostCreatedAt = (TextView) l.findViewById(R.id.globalPostCreatedAt);
                                                        //final TextView globalWallPostDescription = (TextView) l.findViewById(R.id.globalPostDescription);
                                                        //ImageView globalPostImage = (ImageView) l.findViewById(R.id.globalPostImage);

                                                        // set title and created At
                                                        globalWallPostTitle.setText(knowledgeWallPost.getTitle());
                                                        globalWallPostCreatedAt.setText(simpleDateFormat.format(knowledgeWallPost.getCreatedAt()));
                                                        //globalWallPostDescription.setText(knowledgeWallPost.getDescription());
                                                        // assign the bitmap
                                                        Bitmap bitmap = decodeSampledBitmapFromResource(bytes, 200, 200);
                                                        //globalPostImage.setImageBitmap(bitmap);

                                                        l.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                // pass the bundle for the full screen activity
                                                                Intent webViewIntent = new Intent(getApplicationContext(), KnowledgeWallWebView.class);
                                                                //webViewIntent.putExtra(Constants.description, globalWallPostDescription.getText().toString());
                                                                startActivity(webViewIntent);
                                                            }
                                                        });

                                                        mbaKnowledgeView.addView(l);
                                                    }

                                                    else if (knowledgeWallPost.getBranch().contentEquals(Constants.MCA)) {

                                                         l = (RelativeLayout) layoutInflater.inflate(R.layout.home_single_listitem, null); // new RelativeLayout(NewHomeActivity.this);

                                                        TextView globalWallPostTitle = (TextView) l.findViewById(R.id.globalPostTitle);
                                                        TextView globalWallPostCreatedAt = (TextView) l.findViewById(R.id.globalPostCreatedAt);
                                                        //final TextView globalWallPostDescription = (TextView) l.findViewById(R.id.globalPostDescription);
                                                        //ImageView globalPostImage = (ImageView) l.findViewById(R.id.globalPostImage);

                                                        // set title and created At
                                                        globalWallPostTitle.setText(knowledgeWallPost.getTitle());
                                                        globalWallPostCreatedAt.setText(simpleDateFormat.format(knowledgeWallPost.getCreatedAt()));
                                                        //globalWallPostDescription.setText(knowledgeWallPost.getDescription());
                                                        // assign the bitmap
                                                        Bitmap bitmap = decodeSampledBitmapFromResource(bytes, 200, 200);
                                                        //globalPostImage.setImageBitmap(bitmap);

                                                        l.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                // pass the bundle for the full screen activity
                                                                Intent webViewIntent = new Intent(getApplicationContext(), KnowledgeWallWebView.class);
                                                                //webViewIntent.putExtra(Constants.description, globalWallPostDescription.getText().toString());
                                                                startActivity(webViewIntent);
                                                            }
                                                        });

                                                        mcaKnowledgeView.addView(l);
                                                    }


                                                }

                                                // hide the progress bar
                                                knowledgeWallProgressBar.setVisibility(View.GONE);

                                            }
                                        }
                                    });

                                }
                            } else {
                                // no posts yet
                                knowledgeWallProgressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                });
            }

            // parse user or other data not found exceptions
            catch (Exception e){
                finish();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);

            runOnUiThread(new Runnable() {
                public void run() {




                }
            });
        }
    }
*/

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


    }

    // get decoded sample bitmap
    public static Bitmap decodeSampledBitmapFromResource(byte[] bytes, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    // get calculated sample size
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
