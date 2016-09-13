package svecw.smartcampus;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import internaldb.SmartCampusDB;
import utils.Constants;
import utils.Routes;
import utils.Snippets;

/**
 * Created by Pavan_Kusuma on 4/19/2015.
 */
public class KnowledgeWallNewPostActivity extends AppCompatActivity {

    // view from activity
    EditText globalWallPostTitle;
    EditText globalWallPostDescription;
    EditText globalWallPostLink;
    ImageView globalWallPostImage;
    TextView cancelPost, createPost;
    Spinner globalWallCategory;

    // progress indicator
    ProgressDialog progressDialog;

    // toobar for action bar
    Toolbar toolbar;

    private Uri fileUri; // file url to store image when camera is used
    public String picturePath; // url for the image when gallery is used to pick image
    public static String imageLocation, imageLocation_thumb; // variable for storing image location

    public static Bitmap bitmap;
    public static String base64String; // encoded image string that will be uploaded

    //SmartDB smartDB = new SmartDB(this);
    byte[] b = Constants.null_indicator.getBytes();
    InputStream is;

    // object for internal db
    SmartCampusDB smartCampusDB = new SmartCampusDB(this);

    // user objectId to refer current user
    public String userObjectId = "", selectedCategory = "", postTitle="", postDescription="-", postLink="-";

    // notification progress
    //NotificationManager notificationManager;
    //NotificationCompat.Builder builder;

    LayoutInflater layoutInflater;

    int mediaCount;

    JSONObject jsonResponse, jsonResponse1;
    int status, status1, serverResponseCode;

    Intent backIntent;
    String currentInfoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_wall_newpost_activity);


        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.knowledgeWallPost));

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        // object for LayoutInflater
        layoutInflater = LayoutInflater.from(this);

        // get the intent
        backIntent = getIntent();

        // get views from activity
        globalWallPostTitle = (EditText) findViewById(R.id.globalWallNewPostTitle);
        globalWallPostDescription = (EditText) findViewById(R.id.globalWallNewPostDescription);
        globalWallPostLink = (EditText) findViewById(R.id.globalWallNewPostLink);
        //globalWallPostImage = (ImageView) findViewById(R.id.globalWallSelectImage);
        cancelPost = (TextView) findViewById(R.id.cancelPost);
        createPost = (TextView) findViewById(R.id.sendPost);
        globalWallCategory = (Spinner) findViewById(R.id.globalWallCategory);

        userObjectId = (String) smartCampusDB.getUser().get(Constants.objectId);
/*

        globalWallPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // show the dialog to choose camera or gallery
                // custom dialog
                final AlertDialog.Builder menuAlert = new AlertDialog.Builder(KnowledgeWallNewPostActivity.this);
                final String[] menuList = {"Camera", "Gallery"};
                menuAlert.setTitle("Select from");
                menuAlert.setItems(menuList, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {

                            // camera selected
                            case 0:
                                // posting from camera
                                postingFromCamera();
                                break;

                            // gallery selected
                            case 1:

                                // posting from gallery
                                postingFromGallery();
                                break;
                        }
                    }


                });
                AlertDialog menuDrop = menuAlert.create();
                menuDrop.show();
            }
        });
*/

        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get the selected category and other inputs
                selectedCategory = globalWallCategory.getSelectedItem().toString();

                if(!selectedCategory.contentEquals("Select Category")) {

                    // check if required fields are provided
                    if (globalWallPostTitle.getText().length() > 0 && globalWallPostDescription.getText().length() > 0) {

                        // show the notification
                        /*notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        builder = new NotificationCompat.Builder(KnowledgeWallNewPostActivity.this);
                        builder.setContentTitle("Knowledge wall")
                                .setContentText("Posting data...")
                                .setSmallIcon(R.drawable.ic_upload);
*/

                        postTitle = globalWallPostTitle.getText().toString();

                        if (!globalWallPostDescription.getText().toString().contentEquals(""))
                            postDescription = globalWallPostDescription.getText().toString();

                        if (!globalWallPostLink.getText().toString().contentEquals(""))
                            postLink = globalWallPostLink.getText().toString();

                        // create new post
                        //new CreateGlobalNewPost().execute();

                            // get the infoId
                            currentInfoId = Snippets.getUniqueGlobalInfoId();

                            // if post link is provided, verify whether it is proper link
                            if (globalWallPostLink.getText().toString().length()> 0) {
                                if (URLUtil.isValidUrl(postLink)) {

                                    // create url
                                    String globalInfoURL = Routes.createGlobalInfo + Constants.key + "/" + currentInfoId + "/" +
                                            selectedCategory + "/" + Snippets.escapeURIPathParam(postTitle) + "/" + Snippets.escapeURIPathParam(postDescription) + "/" + postLink + "/" + smartCampusDB.getUser().get(Constants.userObjectId) + "/" + mediaCount;

                                    // create globalInfo
                                    new CreateGlobalInfo().execute(Routes.createGlobalInfo, currentInfoId, Snippets.escapeURIPathParam(selectedCategory), Snippets.escapeURIPathParam(postTitle), Snippets.escapeURIPathParam(postDescription), postLink, smartCampusDB.getUser().get(Constants.userObjectId).toString());
                                } else {

                                    // notify user to enter valid url
                                    Toast.makeText(KnowledgeWallNewPostActivity.this, "Enter a valid URL", Toast.LENGTH_SHORT).show();
                                }
                            }

                            // if there is not link provided, then surely we will have description provided.
                            else {

                                // create url
                                String globalInfoURL = Routes.createGlobalInfo + Constants.key + "/" + currentInfoId + "/" +
                                        selectedCategory + "/" + Snippets.escapeURIPathParam(postTitle) + "/" + Snippets.escapeURIPathParam(postDescription) + "/" + postLink + "/" + smartCampusDB.getUser().get(Constants.userObjectId) + "/" + mediaCount;

                                // create globalInfo
                                new CreateGlobalInfo().execute(Routes.createGlobalInfo, currentInfoId, Snippets.escapeURIPathParam(selectedCategory), Snippets.escapeURIPathParam(postTitle), Snippets.escapeURIPathParam(postDescription), postLink, smartCampusDB.getUser().get(Constants.userObjectId).toString());
                            }



                    } else {

                        Toast.makeText(getApplicationContext(), "Provide all details", Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                    Toast.makeText(getApplicationContext(), "Select a category to post", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // close the activity
                setResult(0, backIntent);
                finish();
            }
        });

    }


    /**
     * This class will create a new knowledge wall post
     */
    class CreateGlobalNewPost extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Displays the progress bar for the first time.
            //builder.setProgress(100, 0, false);
            //notificationManager.notify(1, builder.build());

            progressDialog = new ProgressDialog(KnowledgeWallNewPostActivity.this);
            progressDialog.setMessage("Creating post ..");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            //builder.setProgress(100, values[0].intValue(), false);
            //notificationManager.notify(1, builder.build());
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {

            try{

                // Sets the progress indicator completion percentage
                publishProgress((float)Math.min(10, 100));

                //byte[] data1 =  "Working at Parse is great!".getBytes();
                final ParseFile file = new ParseFile("image.txt", b);
                file.saveInBackground();

                // Sets the progress indicator completion percentage
                publishProgress((float) Math.min(30, 100));

                // create object for 'CollegeWall' table
                ParseObject postObject = new ParseObject(Constants.globalInfoTable);

                // put the required values into object
                postObject.put(Constants.userObjectId, userObjectId);
                postObject.put(Constants.title, postTitle);
                postObject.put(Constants.description, postDescription);
                postObject.put(Constants.category, selectedCategory);
                postObject.put(Constants.role, smartCampusDB.getUser().get(Constants.role));
                postObject.put(Constants.branch, smartCampusDB.getUser().get(Constants.branch));
                postObject.put(Constants.mediaFile, file);

                // Sets the progress indicator completion percentage
                publishProgress((float) Math.min(60, 100));

                // save the object
                postObject.saveInBackground();
                Log.v(Constants.appName + "Working", "working7");

                // Sets the progress indicator completion percentage

            }
            catch(Exception e){

                Log.e(Constants.appName, e.toString());
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();

                // closing this screen
                finish();
            }

            // Sets the progress indicator completion percentage
            publishProgress((float)Math.min(101, 100));

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //builder.setContentText("Post sent");
            // Removes the progress bar
            //builder.setProgress(0, 0, false);
            //notificationManager.notify(1, builder.build());

            // dismiss the dialog once done
            progressDialog.dismiss();

            // success message toast
            Toast.makeText(getApplicationContext(), "Post created", Toast.LENGTH_SHORT).show();

            // closing this screen
            //finish();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == Constants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "You cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        // if the result is ok
        if (resultCode == RESULT_OK && requestCode == Constants.IMG_PICK){

            //get the Uri for the picked image
            Uri selectedImage = data.getData();

            String[] filePathColumn = { MediaStore.MediaColumns.DATA };

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // bitmap options
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 200, 200);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            bitmap = BitmapFactory.decodeStream(imageStream, null, options);
            //bitmap = bitmap.createScaledBitmap(bitmap, 200, 200, false);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            b = baos.toByteArray();

            base64String = Base64.encodeToString(b, Base64.DEFAULT);

            //session.saveProfilePhoto(var.userImage, Base64.encodeToString(b, Base64.DEFAULT ));

            //String fondo = session.getProfilePhoto(); // pref.getString("profilephoto", "pro");

            // convert bytes into bitmap drawable
            is = new ByteArrayInputStream(b);
            Bitmap yourSelectedImage = BitmapFactory.decodeStream(is);
            BitmapDrawable bd = new BitmapDrawable(getResources(), yourSelectedImage);
            globalWallPostImage.setImageDrawable(bd);

        }

    }

    /**
     * This method will take care of camera functionality in current post
     *
     */
    private void postingFromCamera() {

        // check if device supports camera
        // if so request permission to access it
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

            // this device has a camera
            // check if this device has SDK less than or equal to Marshmallow
            if(Build.VERSION.SDK_INT >= 23){

                // check if this app is granted with camera access permission
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    // The permission is NOT already granted.
                    // Check if the user has been asked about this permission already and denied
                    // it. If so, we want to give more explanation about why the permission is needed.
                    if (shouldShowRequestPermissionRationale(
                            Manifest.permission.CAMERA)) {
                        // Show our own UI to explain to the user why we need to read the contacts
                        // before actually requesting the permission and showing the default UI
                    }

                    // Fire off an async request to actually get the permission
                    // This will show the standard permission request dialog UI
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);


                }

                // as this app is granted with camera access permission previously
                else {

                    try{
                        // call the capture image method where we call the camera of phone
                        captureImage();

                    } catch (ActivityNotFoundException anfe) {
                        //display an error message
                        String errorMessage = "Whoops - your device doesn't support capturing images!";
                        Toast toast = Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }

            // no need to tak permission as the device sdk is lower than Marshmallow
            else{

                try{
                    // call the capture image method where we call the camera of phone
                    captureImage();

                } catch (ActivityNotFoundException anfe) {
                    //display an error message
                    String errorMessage = "Whoops - your device doesn't support capturing images!";
                    Toast toast = Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }


        } else {
            // no camera on this device
            Toast.makeText(getApplicationContext(), "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();

        }
    }


    /**
     * this method will check for gallery permission
     * @return boolean
     */
    private void postingFromGallery() {

            // check if device supports camera
            if(Build.VERSION.SDK_INT >= 23){

                // check if this app is granted with gallery access permission
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // The permission is NOT already granted.
                    // Check if the user has been asked about this permission already and denied
                    // it. If so, we want to give more explanation about why the permission is needed.
                    if (shouldShowRequestPermissionRationale(
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Show our own UI to explain to the user why we need to read the contacts
                        // before actually requesting the permission and showing the default UI
                    }

                    // Fire off an async request to actually get the permission
                    // This will show the standard permission request dialog UI
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);


                }
                else{

                    // as the app is granted with camera access permission previously
                    try{

                        Intent pickIntent = new Intent(Intent.ACTION_PICK);
                        pickIntent.setType("image/*");
                        //we will handle the returned data in onActivityResult
                        startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), Constants.IMG_PICK);

                    } catch (ActivityNotFoundException anfe) {
                        //display an error message
                        String errorMessage = "Whoops - your device doesnot contain external storage!";
                        Toast toast = Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
            else{


                try{

                    Intent pickIntent = new Intent(Intent.ACTION_PICK);
                    pickIntent.setType("image/*");
                    //we will handle the returned data in onActivityResult
                    startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), Constants.IMG_PICK);

                } catch (ActivityNotFoundException anfe) {
                    //display an error message
                    String errorMessage = "Whoops - your device doesnot contain external storage!";
                    Toast toast = Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }

            }


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }



    // Callback with the request from calling requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original CAMERA request
        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                try{
                    // call the capture image method where we call the camera of phone
                    captureImage();

                } catch (ActivityNotFoundException anfe) {
                    //display an error message
                    String errorMessage = "Whoops - your device doesn't support capturing images!";
                    Toast toast = Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }


            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }


        else if (requestCode == 2) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                try{

                    Intent pickIntent = new Intent(Intent.ACTION_PICK);
                    pickIntent.setType("image/*");
                    //we will handle the returned data in onActivityResult
                    startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), Constants.IMG_PICK);

                } catch (ActivityNotFoundException anfe) {

                    //display an error message
                    String errorMessage = "Whoops - your device doesnot contain external storage!";
                    Toast toast = Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }


            } else {
                Toast.makeText(this, "Read storage permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }






    /**
     * Display image from a path to ImageView
     */

    private void previewCapturedImage() {
        try {
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            // set the picturePath
            picturePath = fileUri.getPath().toString();

            // bitmap options
            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 200, 200);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
            //bitmap = bitmap.createScaledBitmap(bitmap, 200, 200, false);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
            b = baos.toByteArray();
            base64String = Base64.encodeToString(b, Base64.DEFAULT );
            //session.saveProfilePhoto(var.userImage, Base64.encodeToString(b, Base64.DEFAULT ));

            //String fondo = session.getProfilePhoto(); // pref.getString(var.userImage, var.null_indicator);

            is = new ByteArrayInputStream(b);
            Bitmap yourSelectedImage = BitmapFactory.decodeStream(is);
            BitmapDrawable bd = new BitmapDrawable(getResources(), yourSelectedImage);

            globalWallPostImage.setImageDrawable(bd);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * this will save the file uri
     * when camera starts it will restart our activity which causes fileUri to be null
     * hence we need to save and retrieve fileUri
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
        //System.out.println(fileUri.getPath());
    }

    /**
     * This method will call the camera of device and helps in capturing images
     */
    private void captureImage()
    {
        //use standard intent to capture an image
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(Constants.MEDIA_TYPE_IMAGE);

        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        //we will handle the returned data in onActivityResult
        startActivityForResult(captureIntent, Constants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }


    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.appName);


        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(Constants.appName, "Oops! Failed to create, try again "
                        + Constants.appName + " directory");
                return null;
            }
        }

        // Create a mediaTable file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        // here we generate the random number and add to the end of the filename string
        long random_number = 100000000 + (long)(Math.random()*900000000);

        File mediaFile;
        if (type == Constants.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + "_" + random_number + ".jpg");

            // set the image location variable
            imageLocation = "IMG_" + timeStamp + "_" + random_number + ".jpg";

            // set the image thumbnail variable
            imageLocation_thumb = "IMG_" + timeStamp + "_" + random_number + "_thumb.jpg";
        }
        else {
            return null;
        }

        return mediaFile;
    }










    /**
     * this background task will post mediaTable to server
     * with the given name and the mediaTable count
     */
    private class PostMedia extends AsyncTask<String, Void, Void> {

        private String Content = "";
        private String Error = null;
        String data = "";
        FileInputStream fileInputStream;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;


        @Override
        protected void onPreExecute() {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

        }

        @Override
        protected Void doInBackground(String... strings) {

            //String upLoadServerUri = "http://orders.esprinkle.com/campus/public/index.php/upload";
            //String upLoadServerUri = "http://orders.esprinkle.com/campus/public/index.php/upload/name.jpg";
            String fileName = picturePath;

            Log.v(Constants.appName, strings[0]);
            Log.v(Constants.appName, "Image"+picturePath);

            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(fileName);
            if (!sourceFile.isFile()) {

                serverResponseCode = -1;

            }
            else{

                // image found
            }
            try { // open a URL connection to the Servlet
                fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(strings[0]);
                conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("file", fileName);
                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""+ fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);


                    Log.v(Constants.appName, "check 1");
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                //Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        public void run() {


                            //Log.v(Constants.appName, "File Upload Completed.");

                            //Toast.makeText(KnowledgeWallNewPostActivity.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                ex.printStackTrace();
                //Toast.makeText(AddEvent.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                finish();
            } catch (Exception e) {

                e.printStackTrace();
                //Toast.makeText(AddEvent.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Upload file Exception", "Exception : " + e.getMessage());
                finish();
            }



            return null;


        }


        @Override
        protected void onPostExecute(Void aVoid) {

            try {
                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();
                Log.v(Constants.appName, "check 2");
            }
            catch (Exception e){

                // checking

            }

            if(Error==null){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(serverResponseCode == -1){

                            //Log.v(Constants.appName, "check -1");
                            Toast.makeText(KnowledgeWallNewPostActivity.this, R.string.errorMsg, Toast.LENGTH_SHORT).show();


                        }
                        if(serverResponseCode == 200){

                            //Log.v(Constants.appName, "check 200");
                            Toast.makeText(getApplicationContext(), "Post sent", Toast.LENGTH_SHORT).show();

                        }
                        finish();
                    }
                });

            }
            else {

                Log.v(Constants.appName, "check error"+Error);
                Toast.makeText(KnowledgeWallNewPostActivity.this, R.string.errorMsg, Toast.LENGTH_SHORT).show();
                finish();
            }


        }
    }


    /**
     * This background task will post wall post to server
     * if there are any mediaTable along with post
     * then Response for this request is the unique names of the mediaTable
     * accordingly the mediaTable is sent to server with the given names
     *
     * else
     * only post is created
     */
    private class CreateGlobalInfo extends AsyncTask<String, Void, Void>{

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
                        + "&" + URLEncoder.encode(Constants.infoId, "UTF-8") + "=" + (urls[1])
                        + "&" + URLEncoder.encode(Constants.category, "UTF-8") + "=" + (urls[2])
                        + "&" + URLEncoder.encode(Constants.title, "UTF-8") + "=" + (urls[3])
                        + "&" + URLEncoder.encode(Constants.description, "UTF-8") + "=" + (urls[4])
                        + "&" + URLEncoder.encode(Constants.userObjectId, "UTF-8") + "=" + (urls[6])
                        + "&" + URLEncoder.encode(Constants.mediaCount, "UTF-8") + "=" + mediaCount
                        + "&" + URLEncoder.encode(Constants.link, "UTF-8") + "=" + (urls[5]);


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

                    /*// check if mediaCount > 0
                    // if so, get the unique image names
                    if(mediaCount > 0){

                        // get JSON Array of 'details'
                        String imageNamesString = jsonResponse.getString(Constants.details);

                        // names of images
                        String[] imageNames;

                        // get the string removing delimiters from the names
                        String delimiter = ",";
                        imageNames = imageNamesString.split(delimiter);

                        // get the number of images
                        // post each image with the name
                        for(int i =0; i < imageNames.length ; i++){

                            // post the image with the given image name
                            new PostingMedia().execute(Routes.postMedia +imageNames[i]);

                        }

                    }*/

                    if(status == 1){

                        // get the jsonResponse of 'post'
                        // get the JSON object inside Array
                        JSONObject jsonObject = jsonResponse.optJSONObject(Constants.details);

                        // if post is present then fetch the data
                        if(jsonObject != null) {

                            // get the info back
                            backIntent.putExtra(Constants.infoId, jsonObject.getString(Constants.infoId));
                            backIntent.putExtra(Constants.title, jsonObject.getString(Constants.title));

                            backIntent.putExtra(Constants.postDescription, jsonObject.getString(Constants.description));
                            backIntent.putExtra(Constants.link, jsonObject.getString(Constants.link));
                            backIntent.putExtra(Constants.category, selectedCategory);
                            backIntent.putExtra(Constants.createdAt, jsonObject.getString(Constants.createdAt));
                        }
                        else {

                            // no post created
                            Toast.makeText(getApplicationContext(), "Oops! something went wrong, try again later", Toast.LENGTH_SHORT).show();
                            setResult(0, backIntent);
                            finish();
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // check the status and proceed with the logic
                            switch (status){

                                // exception occurred
                                case -3:

                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // key mismatch
                                case -2:

                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // data founc
                                case 1:

                                    Toast.makeText(getApplicationContext(), "Posted to " + selectedCategory + " category in knowledge wall! wait a while for loading", Toast.LENGTH_SHORT).show();

                                        // set the result for backIntent
                                        // send 1 as success message for post creation
                                        setResult(1, backIntent);
                                        finish();

                                    break;


                            }
                        }
                    });





                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Oops! something went wrong, try again later", Toast.LENGTH_SHORT).show();

                    finish();

                }

            }
        }
    }






    /**
     * this background task will post mediaTable to server
     * with the given name and the mediaTable count
     */
    private class PostingMedia extends AsyncTask<String, Void, Void> {

        private String Content = "";
        private String Error = null;
        String data = "";
        //FileInputStream fileInputStream;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;


        @Override
        protected void onPreExecute() {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

        }

        @Override
        protected Void doInBackground(String... strings) {

            //String upLoadServerUri = "http://orders.esprinkle.com/campus/public/index.php/upload";
            //String upLoadServerUri = "http://orders.esprinkle.com/campus/public/index.php/upload/name.jpg";
            String fileName = picturePath;

            Log.v(Constants.appName, strings[0]);
            Log.v(Constants.appName, "Image"+picturePath);

            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(fileName);
            if (!sourceFile.isFile()) {

                serverResponseCode = -1;

            }
            else{

                // image found
            }
            try { // open a URL connection to the Servlet
                //fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(strings[0]);
                conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("file", fileName);
                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""+ fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                //bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size

                // get length of bytes
                bytesAvailable = b.length;

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = is.read(b, 0, b.length);
                Log.v(Constants.appName, "Size of bytes : " + b.length);
                dos.write(b, 0, b.length);
                while (bytesRead > 0) {
                    dos.write(b, 0, b.length);
                    bytesAvailable = is.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = is.read(buffer, 0, bufferSize);


                    Log.v(Constants.appName, "check 1");
                }

                // send multipart form data necessary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                //Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        public void run() {


                            //Log.v(Constants.appName, "File Upload Completed.");

                            //Toast.makeText(KnowledgeWallNewPostActivity.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                is.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                ex.printStackTrace();
                //Toast.makeText(AddEvent.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                finish();
            } catch (Exception e) {

                e.printStackTrace();
                //Toast.makeText(AddEvent.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Upload file Exception", "Exception : " + e.getMessage());
                finish();
            }



            return null;


        }


        @Override
        protected void onPostExecute(Void aVoid) {

            try {
                //close the streams //
                is.close();
                dos.flush();
                dos.close();
                Log.v(Constants.appName, "check 2");
            }
            catch (Exception e){

                // checking

            }

            if(Error==null){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(serverResponseCode == -1){

                            //Log.v(Constants.appName, "check -1");
                            Toast.makeText(KnowledgeWallNewPostActivity.this, R.string.errorMsg, Toast.LENGTH_SHORT).show();


                        }
                        if(serverResponseCode == 200){

                            //Log.v(Constants.appName, "check 200");
                            Toast.makeText(getApplicationContext(), "Post sent", Toast.LENGTH_SHORT).show();

                        }
                        finish();
                    }
                });

            }
            else {

                Log.v(Constants.appName, "check error"+Error);
                Toast.makeText(KnowledgeWallNewPostActivity.this, R.string.errorMsg, Toast.LENGTH_SHORT).show();
                finish();
            }


        }
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
