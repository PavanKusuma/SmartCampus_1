package svecw.smartcampus;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.HashMap;
import java.util.Locale;

import internaldb.SmartCampusDB;
import internaldb.SmartSessionManager;
import model.Privilege;
import model.User;
import utils.Constants;
import utils.Routes;

/**
 * Created by Pavan on 8/9/16.
 */
public class UserProfileActivity extends AppCompatActivity {

    // views from layout
    TextView profileUserName, profileEmail, profilePhone, profileCollegeId, profileYear, profileBranch, profileSemester, refreshView;
    ImageView profilePhoto, editProfilePhoto, editProfileDetails;
    ProgressBar progressBar;

    // instance for local db
    SmartCampusDB smartCampusDB = new SmartCampusDB(this);

    // user HashMap
    HashMap<Object, Object> userMap = new HashMap<Object, Object>();

    // progress dialog
    private ProgressDialog progressDialog;

    private Uri fileUri; // file url to store image when camera is used
    public String picturePath; // url for the image when gallery is used to pick image
    public static String imageLocation, imageLocation_thumb; // variable for storing image location

    public static Bitmap bitmap;
    public static String base64String; // encoded image string that will be uploaded

    //SmartDB smartDB = new SmartDB(this);
    byte[] b = Constants.null_indicator.getBytes();
    byte[] actualImageBytes = Constants.null_indicator.getBytes();

    SmartSessionManager session;

    static String userObjectId = "";

    JSONObject jsonResponse, jsonResponse1;
    int status, status1, serverResponseCode;
    InputStream is;

    int height, width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        profileUserName = (TextView) findViewById(R.id.profileUserName);
        profileEmail = (TextView) findViewById(R.id.profileEmail);
        profilePhone = (TextView) findViewById(R.id.profilePhone);
        profileCollegeId = (TextView) findViewById(R.id.collegeId);
        profileYear = (TextView) findViewById(R.id.profileYear);
        profileBranch = (TextView) findViewById(R.id.profileBranch);
        profileSemester = (TextView) findViewById(R.id.profileSemester);
        profilePhoto = (ImageView) findViewById(R.id.profilePhoto);
        editProfilePhoto = (ImageView) findViewById(R.id.editProfilePhoto);
        editProfileDetails = (ImageView) findViewById(R.id.editProfileDetails);
        refreshView = (TextView) findViewById(R.id.refreshView);
        progressBar = (ProgressBar) findViewById(R.id.profileProgress);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        profileUserName.setTypeface(sansFont);
        profileEmail.setTypeface(sansFont);
        profilePhone.setTypeface(sansFont);
        profileCollegeId.setTypeface(sansFont);
        profileYear.setTypeface(sansFont);
        profileBranch.setTypeface(sansFont);
        profileSemester.setTypeface(sansFont);
        refreshView.setTypeface(sansFont);

        session = new SmartSessionManager(getApplicationContext());

        // get the user Map from local db
        userMap = smartCampusDB.getUser();

        userObjectId = (String)userMap.get(Constants.objectId);

        // set the username
        profileUserName.setText((String)userMap.get(Constants.userName));

        if(! String.valueOf(userMap.get(Constants.email)).contentEquals(Constants.null_indicator)) {
            profileEmail.setText((String) userMap.get(Constants.email));
        }

        if(! String.valueOf(userMap.get(Constants.phoneNumber)).contentEquals(Constants.null_indicator)) {
            profilePhone.setText((String) userMap.get(Constants.phoneNumber));
        }

        profileCollegeId.setText((String)userMap.get(Constants.collegeId));

        // based on role of the user show the complete profile details
        // if user is student he will have complete details
        // if user is other than student he will have only branch visible
        if(smartCampusDB.getUserRole().contentEquals(Constants.student)) {

            profileYear.setVisibility(View.VISIBLE);
            profileBranch.setVisibility(View.VISIBLE);
            profileSemester.setVisibility(View.VISIBLE);

            profileYear.append(String.valueOf(userMap.get(Constants.year)));
            profileBranch.setText("Department : " + " " + String.valueOf( userMap.get(Constants.branch)));
            profileSemester.append(String.valueOf(userMap.get(Constants.semester)));
        }
        else {

            profileYear.setVisibility(View.GONE);
            profileBranch.setVisibility(View.VISIBLE);
            profileSemester.setVisibility(View.GONE);

            profileBranch.append((String) userMap.get(Constants.branch));
        }

        // get the profile pic from current shared preference
        String photoString = session.getProfilePhoto();

        if(!photoString.equals("-")){

            byte[] b = Base64.decode(photoString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

            /*RoundImage roundedImage = new RoundImage(bitmap, 220, 220);
            RoundImage roundedImage1 = new RoundImage(roundedImage.getBitmap(), bitmap.getWidth(), bitmap.getHeight());
            profilePhoto.setImageDrawable(roundedImage1);
*/
            profilePhoto.setImageBitmap(bitmap);

        }

        // fetch user details and display it
        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // get the user details
                new FetchUserDetails().execute(Routes.getUserDetails, smartCampusDB.getUser().get(Constants.userObjectId).toString());


            }
        });

        // click on the image to change the profile photo
        // show the dialog for fetching image from gallery or from camera
        editProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // show the dialog to choose camera or gallery
                // custom dialog
                final AlertDialog.Builder menuAlert = new AlertDialog.Builder(UserProfileActivity.this);
                final String[] menuList = { "Camera", "Gallery" };
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



        // edit profile details (email and phoneNumber)
        editProfileDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent editProfileIntent = new Intent(getApplicationContext(), EditProfileActivity.class);
                editProfileIntent.putExtra(Constants.email, smartCampusDB.getUser().get(Constants.email).toString());
                editProfileIntent.putExtra(Constants.phoneNumber, smartCampusDB.getUser().get(Constants.phoneNumber).toString());
                startActivityForResult(editProfileIntent, Constants.EDIT_PROFILE);
            }
        });
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
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    // The permission is NOT already granted.
                    // Check if the user has been asked about this permission already and denied
                    // it. If so, we want to give more explanation about why the permission is needed.
                    if (shouldShowRequestPermissionRationale(
                            android.Manifest.permission.CAMERA)) {
                        // Show our own UI to explain to the user why we need to read the contacts
                        // before actually requesting the permission and showing the default UI
                    }

                    // Fire off an async request to actually get the permission
                    // This will show the standard permission request dialog UI
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 1);


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
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // The permission is NOT already granted.
                // Check if the user has been asked about this permission already and denied
                // it. If so, we want to give more explanation about why the permission is needed.
                if (shouldShowRequestPermissionRationale(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Show our own UI to explain to the user why we need to read the contacts
                    // before actually requesting the permission and showing the default UI
                }

                // Fire off an async request to actually get the permission
                // This will show the standard permission request dialog UI
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 2);


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


    /**
     * this will deal with the result depending on
     * camera capture or gallery pick
     * it will get data from the result and sets the image to the imageview
     */

    protected void onActivityResult1(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == Constants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {


            if (resultCode == RESULT_OK) {

                if(data!=null)
                    if(data.getStringExtra(Constants.valueBack)!= null){
                        if(data.getStringExtra(Constants.valueBack).contentEquals(Constants.success)){

                            // get the user Map from local db
                            userMap = smartCampusDB.getUser();

                            if(! String.valueOf(userMap.get(Constants.email)).contentEquals(Constants.null_indicator)) {
                                profileEmail.setText((String) userMap.get(Constants.email));
                            }

                            if(! String.valueOf(userMap.get(Constants.phoneNumber)).contentEquals(Constants.null_indicator)) {
                                profilePhone.setText((String) userMap.get(Constants.phoneNumber));
                            }

                        }
                        else {

                            // do nothing
                            if(data.getStringExtra(Constants.valueBack).contentEquals(Constants.error)) {

                                //Toast.makeText(getApplicationContext(),"You cancelled image capture", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else {
                        // successfully captured the image
                        // display it in image view
                        previewCapturedImage();
                    }
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                //Toast.makeText(getApplicationContext(),"You cancelled image capture", Toast.LENGTH_SHORT).show();
            } else {
                // failed to capture image
                //Toast.makeText(getApplicationContext(),"Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }
        // if the result is ok
        if (resultCode == RESULT_OK && requestCode == Constants.IMG_PICK){

            //get the Uri for the captured image
            Uri selectedImage = data.getData();

            // get the file Path column
            String[] filePathColumn = { MediaStore.MediaColumns.DATA };

            // get the cursor
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            // get the index
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            // read the inputStream
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // decode to bitmap
            bitmap = BitmapFactory.decodeStream(imageStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            b = baos.toByteArray();

            // assign the actual image bytes
            actualImageBytes = b;

            base64String = Base64.encodeToString(b, Base64.DEFAULT);



            // convert to drawable to assign it to imageView
            InputStream is = new ByteArrayInputStream(b);
            Bitmap yourSelectedImage = BitmapFactory.decodeStream(is);
            BitmapDrawable bd = new BitmapDrawable(getResources(), yourSelectedImage);

            // assign to imageView
            profilePhoto.setImageDrawable(bd);

            // assigning the image as circular image
            Bitmap bm;
            if (profilePhoto.getDrawable() instanceof BitmapDrawable) {
                bm = ((BitmapDrawable) profilePhoto.getDrawable()).getBitmap();
            } else {
                Drawable d = profilePhoto.getDrawable();
                bm = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bm);
                d.draw(canvas);
            }

            profilePhoto.setImageBitmap(bitmap);

            // save into the local shared preferences
            session.saveProfilePhoto(base64String);


            // bitmap options
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 50, 50);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
            //bitmap = bitmap.createScaledBitmap(bitmap, 200, 200, false);
            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos1); //bm is the bitmap object
            b = baos1.toByteArray();
            base64String = Base64.encodeToString(b, Base64.DEFAULT );


            /**
             * Image should be uploaded to parse from here
             */
            //new UploadProfilePhoto().execute();

            // profile picture url
            String profilePictureURL = Routes.uploadProfilePicture + Constants.key + "/" + smartCampusDB.getUser().get(Constants.userObjectId);

            // post the profile picture
            new UpdateProfilePicture().execute(Routes.uploadProfilePicture, smartCampusDB.getUser().get(Constants.userObjectId).toString());

        }

    }

    // Scale image to desired dimensions
    public Bitmap scaleImage(BitmapFactory.Options options, int reqHeight, int reqWidth, Bitmap bitmap) {

        // Raw height and width of image
        height = options.outHeight;
        width = options.outWidth;
        Log.v(Constants.appName, "Actual Height: "+ height);
        Log.v(Constants.appName, "Actual Width: "+ width);

        Bitmap scaledBitmap = bitmap.createScaledBitmap(bitmap, width-1, height-1, false);

        // verify whether image dimensions are meeting our desired dimension
        while (height > reqHeight || width > reqWidth) {

            // check if image is approximate to our desired dimension
            // if so do not compress
            if(Math.abs(height-reqHeight) > 100 || Math.abs(width-reqWidth) > 100) {

                height = height / 2;
                width = width / 2;
            }

            // scale the image to reduced dimensions
            scaledBitmap = bitmap.createScaledBitmap(bitmap, width, height, false);
        }

        Log.v(Constants.appName, "Compressed Height: "+ height);
        Log.v(Constants.appName, "Compressed Width: "+ width);

        return scaledBitmap;

    }


    /**
     * this will deal with the result depending on
     * camera capture or gallery pick
     * it will get data from the result and sets the image to the imageview
     */

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // if the result is capturing Image
        if (requestCode == Constants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // successfully captured the image
                // display it in image view
                previewCapturedImage();

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(), "You cancelled image capture", Toast.LENGTH_SHORT).show();
            } else {

                // failed to capture image
                Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }

        // if the result is from edit profile
        if(requestCode == Constants.EDIT_PROFILE){

            // check if the intent data returned is not null
            if(data!=null)
                if(data.getStringExtra(Constants.valueBack)!= null){
                    if(data.getStringExtra(Constants.valueBack).contentEquals(Constants.success)){

                        // get the user Map from local db
                        userMap = smartCampusDB.getUser();

                        if(! String.valueOf(userMap.get(Constants.email)).contentEquals(Constants.null_indicator)) {
                            profileEmail.setText((String) userMap.get(Constants.email));
                        }

                        if(! String.valueOf(userMap.get(Constants.phoneNumber)).contentEquals(Constants.null_indicator)) {
                            profilePhone.setText((String) userMap.get(Constants.phoneNumber));
                        }

                    }
                    else {

                        // do nothing
                        if(data.getStringExtra(Constants.valueBack).contentEquals(Constants.error)) {

                            //Toast.makeText(getApplicationContext(),"You cancelled image capture", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        }

        // if the result is from picking image from gallery
        if (resultCode == RESULT_OK && requestCode == Constants.IMG_PICK){

            //get the Uri for the captured image
            Uri selectedImage = data.getData();

            // get the file Path column
            String[] filePathColumn = { MediaStore.MediaColumns.DATA };

            // get the cursor
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            // get the index
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            // read the inputStream
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            // bitmap options
            BitmapFactory.Options options = new BitmapFactory.Options();

            // get the bitmap from imageStream
            bitmap = BitmapFactory.decodeStream(imageStream, null, options);

            // call imageCompression method to compress the image
            // assign bitmap to ImageView
            BitmapDrawable bd = imageCompression(bitmap, options);
            profilePhoto.setImageDrawable(bd);

            // saving the original bytes of image to local
                {
                    bitmap = BitmapFactory.decodeStream(imageStream, null, options);
                    //bitmap = bitmap.createScaledBitmap(bitmap, 500, 500, false);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                    b = baos.toByteArray();

                    // assign the actual image bytes
                    actualImageBytes = b;

                    // get base64 string
                    base64String = Base64.encodeToString(b, Base64.DEFAULT);

                    // save into the local shared preferences
                    session.saveProfilePhoto(base64String);

                }



            /*// assigning the image as circular image
            Bitmap bm;
            if (profilePhoto.getDrawable() instanceof BitmapDrawable) {
                bm = ((BitmapDrawable) profilePhoto.getDrawable()).getBitmap();
            } else {
                Drawable d = profilePhoto.getDrawable();
                bm = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bm);
                d.draw(canvas);
            }*/






            /**
             * Image should be uploaded to parse from here
             */
            //new UploadProfilePhoto().execute();

            // profile picture url
            String profilePictureURL = Routes.uploadProfilePicture + Constants.key + "/" + smartCampusDB.getUser().get(Constants.userObjectId);

            // post the profile picture
            new UpdateProfilePicture().execute(Routes.uploadProfilePicture, smartCampusDB.getUser().get(Constants.userObjectId).toString());
        }

    }

    /**
     * this method will check for device camera
     * @return boolean
     */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    /**
     * Display image from a path to ImageView
     */

    private void previewCapturedImage() {
        try {
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // set the picturePath
            picturePath = fileUri.getPath();

                // saving the original bytes of image to local
                {
                    bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                    //bitmap = bitmap.createScaledBitmap(bitmap, 500, 500, false);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                    b = baos.toByteArray();

                    // assign the actual image bytes
                    actualImageBytes = b;

                    // get base64 string
                    base64String = Base64.encodeToString(b, Base64.DEFAULT);

                    // save into the local shared preferences
                    session.saveProfilePhoto(base64String);

                }

            // decode file path to bitmap
            bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);

            // call imageCompression method to compress the image
            // assign bitmap to ImageView
            BitmapDrawable bd = imageCompression(bitmap, options);
            profilePhoto.setImageDrawable(bd);


            /**
             * User profile photo will be updated to external db
             */
            //new UploadProfilePhoto().execute();


            // profile picture url
            String profilePictureURL = Routes.uploadProfilePicture + Constants.key + "/" + smartCampusDB.getUser().get(Constants.userObjectId);

            // post the profile picture
            new UpdateProfilePicture().execute(Routes.uploadProfilePicture, smartCampusDB.getUser().get(Constants.userObjectId).toString());


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    // compress image
    public BitmapDrawable imageCompression(Bitmap bitmap, BitmapFactory.Options options){

/*
        // scale the respective image dimensions
        bitmap = scaleImage(options, 400 ,400, bitmap);

        // we need bytes from the bitmap
        ByteArrayOutputStream BOAS = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, BOAS); //bm is the bitmap object
        b = BOAS.toByteArray();
*/

        bitmap = scaleImage(options, 300 ,300, bitmap);

        // we need bytes from the bitmap
        ByteArrayOutputStream BOAS = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, BOAS); //bm is the bitmap object
        b = BOAS.toByteArray();

        /*// check the image if it is greater than the buffer size
        // if so, scale the image to the buffer size
        if(b.length > (1024*1024)){

            bitmap = scaleImage(options, 300 ,300, bitmap);

            // we need bytes from the bitmap
            ByteArrayOutputStream BOAS1 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, BOAS1); //bm is the bitmap object
            b = BOAS1.toByteArray();
        }*/



        // prepare the inputStream
        is = new ByteArrayInputStream(b);
        Bitmap yourSelectedImage = BitmapFactory.decodeStream(is);

        // assign bitmap to ImageView
        return new BitmapDrawable(getResources(), yourSelectedImage);
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

    // calculate the sample size to reduce image
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
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



    /**
     * Verify whether user has previous active login session
     *
     * if so, restrict the user for login
     * else
     *      Check if the collegeId and secretCode are matching
     *      if so, get the user details of collegeId from db and create a active login session
     */
    private class FetchUserDetails extends AsyncTask<String, Void, Void> {

        private String Content = "";
        private String Error = null;
        String data = "";

        @Override
        protected void onPreExecute() {

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
                        + "&" + URLEncoder.encode(Constants.userObjectId, "UTF-8") + "=" + (urls[1]);

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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(UserProfileActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    }
                });


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
                                    break;

                                // key mismatch
                                case -2:

                                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();
                                    break;

                                // no data found
                                case -1:

                                    Toast.makeText(getApplicationContext(), "No account found! login again", Toast.LENGTH_SHORT).show();
                                    break;

                                // session exists
                                case 0:

                                    Toast.makeText(getApplicationContext(), "No account found! login again", Toast.LENGTH_SHORT).show();
                                    break;

                                // data found
                                case 1:

                                    try {

                                        // get JSON Array of 'details'
                                        JSONArray jsonArray = jsonResponse.getJSONArray(Constants.details);
                                        // get the JSON object inside Array
                                        JSONObject jsonObject = (JSONObject) jsonArray.get(0);

                                        // get the data and store in internal db
                                        User user = new User();

                                        user.setObjectId(jsonObject.getString(Constants.userObjectId)); // secretCode.getText().toString()
                                        user.setCampusId(jsonObject.getString(Constants.campusId));
                                        user.setUserName(jsonObject.getString(Constants.userName));
                                        user.setEmail(jsonObject.getString(Constants.email));
                                        user.setPhoneNumber(jsonObject.getString(Constants.phoneNumber));
                                        user.setCollegeId(jsonObject.getString(Constants.collegeId));
                                        user.setBranch(jsonObject.getString(Constants.branch));
                                        user.setSemester(jsonObject.getInt(Constants.semester));
                                        user.setYear(jsonObject.getInt(Constants.year));
                                        user.setRole(jsonObject.getString(Constants.role));
                                        user.setDepartment(jsonObject.getString(Constants.department));

                                        // insert user details into internal db
                                        smartCampusDB.updateUserDetails(user);

                                        // get JSON Array of 'privileges'
                                        JSONArray privilegeArray = jsonResponse.getJSONArray(Constants.privileges);

                                        // check if there are privileges for user
                                        if(privilegeArray.length() > 0) {
                                            // get the JSON object inside Array
                                            JSONObject privilegeObject = (JSONObject) privilegeArray.get(0);

                                            // get the privilege data
                                            Privilege privilege = new Privilege();

                                            privilege.setPrivilegeId(privilegeObject.getString(Constants.privilegeId));
                                            privilege.setUserObjectId(privilegeObject.getString(Constants.userObjectId));
                                            privilege.setModerating(privilegeObject.getInt(Constants.moderating));
                                            privilege.setDirectory(privilegeObject.getString(Constants.directory));
                                            privilege.setCreatedAt((String) (privilegeObject.get(Constants.createdAt)));
                                            privilege.setUpdatedAt((String) privilegeObject.get(Constants.updatedAt));

                                            // save privilege data to internal db
                                            smartCampusDB.updatePrivilege(privilege);


                                        }else {

                                            // do not update privilege for the user

                                        }

                                        Toast.makeText(getApplicationContext(), "Updated details, Reopen to view changes", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                    catch(Exception e){

                                        Log.e(Constants.error, e.getMessage());
                                        Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();

                                    }


                            }
                        }
                    });





                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), R.string.errorMsg, Toast.LENGTH_SHORT).show();

                }

            }


        }

    }



    /**
     * this background task will update media name to user table
     * and request for an media upload with the given name
     */
    private class UpdateProfilePicture extends AsyncTask<String, Void, Void>{

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
                        + "&" + URLEncoder.encode(Constants.userObjectId, "UTF-8") + "=" + (urls[1]);

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
                    jsonResponse = new JSONObject(Content);


                    /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                    status = jsonResponse.getInt(Constants.status);

                    // check if it is status 'success'
                    if(status == 1) {
                        // get JSON Array of 'details'
                        String imageName = jsonResponse.getString(Constants.details);

                        // post the image with the given image name
                        new PostMedia().execute(Routes.postMediaProfile, imageName);

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

                                // data found
                                case 1:

                                    //Toast.makeText(getApplicationContext(), "Profile updated!", Toast.LENGTH_SHORT).show();
                                    //finish();
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

            //Log.v(Constants.appName, strings[0]);
            //Log.v(Constants.appName, "Image"+picturePath);

            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 4 * 1024 * 1024;
            File sourceFile = new File(fileName);
            if (!sourceFile.isFile()) {

                serverResponseCode = -1;

                //Log.v(Constants.appName, "Image not found");
            }
            else{

                //Log.v(Constants.appName, "Image found");
            }
            try {


                // Set Request parameter
                data += "?&" + URLEncoder.encode(Constants.name, "UTF-8") + "=" + (strings[1]);

                // open a URL connection to the Servlet
                fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(strings[0]+data);
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

                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        public void run() {

                            //Log.v(Constants.appName, "File Upload Completed.");

                            //Toast.makeText(CollegeWallNewPostActivity.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
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
            } catch (Exception e) {

                e.printStackTrace();
                //Toast.makeText(AddEvent.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Upload file Exception", "Exception : " + e.getMessage());
            }



            return null;


        }


        @Override
        protected void onPostExecute(Void aVoid) {

            if(Error==null){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(serverResponseCode == -1){

                            //Log.v(Constants.appName, "check -1");
                            Toast.makeText(UserProfileActivity.this, R.string.errorMsg, Toast.LENGTH_SHORT).show();

                        }
                        if(serverResponseCode == 200){

                            //Log.v(Constants.appName, "check 200");
                            Toast.makeText(UserProfileActivity.this, "Profile picture updated.", Toast.LENGTH_SHORT).show();

                        }
                        //finish();
                    }
                });

            }
            else {

                Log.v(Constants.appName, "check error"+Error);
                Toast.makeText(UserProfileActivity.this, R.string.errorMsg, Toast.LENGTH_SHORT).show();
                //finish();
            }

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
        }
    }


}
