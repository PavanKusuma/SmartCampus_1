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
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import internaldb.SmartCampusDB;
import utils.Constants;

/**
 * Created by Pavan on 6/26/15.
 */
public class AcademicsNewActivity extends AppCompatActivity{

    // layout views
    EditText writeAcademics;
    TextView cancelPost, sendPost;
    LinearLayout selectedImagesView;
    Spinner academicsBranch, academicsYear, academicsSemester, selectAcademicsModule;
    HorizontalScrollView selectedImagesScroll;

    // progress dialog
    private ProgressDialog progressDialog;

    private Uri fileUri; // file url to store image when camera is used
    public String picturePath; // url for the image when gallery is used to pick image
    public static String imageLocation, imageLocation_thumb; // variable for storing image location

    public static Bitmap bitmap;
    public static String base64String; // encoded image string that will be uploaded

    //SmartDB smartDB = new SmartDB(this);
    byte[] b = Constants.null_indicator.getBytes();

    List<byte[]> selectedImages = new ArrayList<byte[]>();

    LayoutInflater layoutInflater;

    // object for db
    SmartCampusDB smartCampusDB = new SmartCampusDB(this);

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    Random random = new Random();

    // layout for showing images
    RelativeLayout l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.academics_new_activity);


        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.newAcademicsPost));
        title.setTypeface(sansFont);

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        // object for LayoutInflater
        layoutInflater = LayoutInflater.from(this);

        // get views from layout
        writeAcademics = (EditText) findViewById(R.id.writeAcademics);
        cancelPost = (TextView) findViewById(R.id.cancelAcademics); cancelPost.setTypeface(sansFont);
        sendPost = (TextView) findViewById(R.id.sendAcademics); sendPost.setTypeface(sansFont);
        academicsBranch = (Spinner) findViewById(R.id.academicsBranch);
        academicsYear = (Spinner) findViewById(R.id.academicsYear);
        academicsSemester = (Spinner) findViewById(R.id.academicsSemester);
        selectAcademicsModule = (Spinner) findViewById(R.id.selectAcademicsModule);
        selectedImagesView = (LinearLayout) findViewById(R.id.selectedImagesView);
        selectedImagesScroll = (HorizontalScrollView) findViewById(R.id.selectedImagesScroll);

        sendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(writeAcademics.getText().length()>0) {

                    // create new academics post
                    new NewAcademicsPost().execute();

                } else {

                    Toast.makeText(getApplicationContext(), "Enter description", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancelPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.newattachment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.attachFile) {

            // show the dialog to choose camera or gallery
            // custom dialog
            final AlertDialog.Builder menuAlert = new AlertDialog.Builder(AcademicsNewActivity.this);
            final String[] menuList = { "Camera", "Gallery" };
            menuAlert.setTitle("Select from");
            menuAlert.setItems(menuList, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    switch (item) {
                        // camera selected
                        case 0:
                            // check if device contains camera
                            if (!isDeviceSupportCamera())
                                Toast.makeText(getApplicationContext(), "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
                            else {
                                try {

                                    // call the capture image method where we call the camera of phone
                                    captureImage();

                                } catch (ActivityNotFoundException anfe) {
                                    //display an error message
                                    String errorMessage = "Whoops - your device doesn't support capturing images!";
                                    Toast toast = Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                            break;

                        // gallery selected
                        case 1:

                            Intent pickIntent = new Intent(Intent.ACTION_PICK);
                            pickIntent.setType("image/*");
                            //we will handle the returned data in onActivityResult
                            startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), Constants.IMG_PICK);

                            break;
                    }
                }


            });
            AlertDialog menuDrop = menuAlert.create();
            menuDrop.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

            //get the Uri for the captured image
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

            bitmap = BitmapFactory.decodeStream(imageStream);
            //bitmap = bitmap.createScaledBitmap(bitmap, 500, 500, false);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            b = baos.toByteArray();

            base64String = Base64.encodeToString(b, Base64.DEFAULT);

            // get the layout
            l = (RelativeLayout) layoutInflater.inflate(R.layout.home_single_listitem, null); // new RelativeLayout(NewHomeActivity.this);

            TextView globalWallPostTitle = (TextView) l.findViewById(R.id.globalPostTitle);
            TextView globalWallPostCreatedAt = (TextView) l.findViewById(R.id.globalPostCreatedAt);
            //TextView globalWallPostDescription = (TextView) l.findViewById(R.id.globalPostDescription);
            //ImageView globalPostImage = (ImageView) l.findViewById(R.id.globalPostImage);

            // set title and created At
            globalWallPostTitle.setVisibility(View.GONE);
            globalWallPostCreatedAt.setVisibility(View.GONE);
            //globalWallPostDescription.setVisibility(View.GONE);
            // assign the bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            //globalPostImage.setImageBitmap(bitmap);

            // add selected images to a list
            selectedImages.add(b);

            // make the images scrollView visible
            selectedImagesScroll.setVisibility(View.VISIBLE);

            // add the images to the scrollView
            selectedImagesView.addView(l);

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

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            // set the picturePath
            picturePath = fileUri.getPath().toString();

            bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
            //bitmap = bitmap.createScaledBitmap(bitmap, 500, 500, false);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos); //bm is the bitmap object
            b = baos.toByteArray();
            base64String = Base64.encodeToString(b, Base64.DEFAULT );


            // get the layout
            l = (RelativeLayout) layoutInflater.inflate(R.layout.home_single_listitem, null); // new RelativeLayout(NewHomeActivity.this);

            TextView globalWallPostTitle = (TextView) l.findViewById(R.id.globalPostTitle);
            TextView globalWallPostCreatedAt = (TextView) l.findViewById(R.id.globalPostCreatedAt);
            //TextView globalWallPostDescription = (TextView) l.findViewById(R.id.globalPostDescription);
            //ImageView globalPostImage = (ImageView) l.findViewById(R.id.globalPostImage);

            // set title and created At
            globalWallPostTitle.setVisibility(View.GONE);
            globalWallPostCreatedAt.setVisibility(View.GONE);
            //globalWallPostDescription.setVisibility(View.GONE);
            // assign the bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            //globalPostImage.setImageBitmap(bitmap);

            // add selected images to a list
            selectedImages.add(b);

            // make the images scrollView visible
            selectedImagesScroll.setVisibility(View.VISIBLE);

            // add the images to the scrollView
            selectedImagesView.addView(l);

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

        outState.putInt("selectedImages", selectedImages.size());
        for(int i=0; i<selectedImages.size(); i++){

            outState.putByteArray("Images"+i, selectedImages.get(i));
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
        //System.out.println(fileUri.getPath());

        int images = savedInstanceState.getInt("selectedImages");
        for(int i=0; i<images; i++){

            selectedImages.add(savedInstanceState.getByteArray("Images"+i));
        }
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


    /**
     * Send message
     */
    class NewAcademicsPost extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(AcademicsNewActivity.this);
            progressDialog.setMessage("Creating post ..");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try{

                final StringBuilder randomObjectId = new StringBuilder( 10 );
                for( int i = 0; i < 10; i++ )
                    randomObjectId.append( AB.charAt( random.nextInt(AB.length()) ) );

                ParseObject academicsObject = new ParseObject(Constants.Academics);

                academicsObject.put(Constants.userObjectId, smartCampusDB.getUser().get(Constants.objectId));
                academicsObject.put(Constants.userName, smartCampusDB.getUser().get(Constants.userName));
                //academicsObject.put(Constants.description, writeAcademics.getText().toString());

                //academicsObject.put(Constants.year, Integer.valueOf(academicsYear.getSelectedItem().toString()));
                //academicsObject.put(Constants.semester, Integer.valueOf(academicsSemester.getSelectedItem().toString()));
                //academicsObject.put(Constants.branch, academicsBranch.getSelectedItem().toString());
                //academicsObject.put(Constants.module, selectAcademicsModule.getSelectedItem().toString());

                // check if mediaTable exists for the post
                if(selectedImages.size()>0) {

                    academicsObject.put(Constants.mediaId, randomObjectId.toString());
                }
                else {

                    academicsObject.put(Constants.mediaId, "0");
                }

                academicsObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {

                            Log.v(Constants.appName, "count = " + selectedImages.size());


                            for(int i=0; i<selectedImages.size(); i++) {

                                ParseFile file = new ParseFile("image.txt", selectedImages.get(i));
                                file.saveInBackground();

                                // create object for 'Media' table
                                ParseObject mediaObject = new ParseObject(Constants.mediaTable);

                                mediaObject.put(Constants.mediaFile, file);
                                mediaObject.put(Constants.relativeId, randomObjectId.toString());

                                mediaObject.saveInBackground();
                            }
                        }
                    }
                });


            }
            catch (Exception e){

                progressDialog.dismiss();
                //Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();
                // closing this screen
                Log.v(Constants.appName, e.getMessage());
                runOnUiThread(new Runnable() {
                    public void run() {

                        // success message toast
                        Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();

                    }
                });

                finish();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);

            // dismiss the dialog once done
            progressDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {

                    // success message toast
                    Toast.makeText(getApplicationContext(), "Post sent", Toast.LENGTH_SHORT).show();

                }
            });

            // closing this screen
            finish();
        }
    }

}