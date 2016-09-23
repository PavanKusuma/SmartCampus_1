package svecw.smartcampus;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import utils.Constants;

/**
 * Created by Pavan on 6/26/15.
 */
public class MultiImageViewActivity extends AppCompatActivity {

    // views of activity
    LinearLayout academcisPostImageView;

    // mediaId
    String mediaId;

    // list of mediaFiles
    List<byte[]> mediaFiles;

    // layout inflater
    LayoutInflater layoutInflater;
    LinearLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_imageview_activity);


        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.app_name));
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
        // get views from activity
        academcisPostImageView = (LinearLayout) findViewById(R.id.academcisPostImageView);

        // list of mediaFiles
        mediaFiles = new ArrayList<byte[]>();
        layoutInflater = LayoutInflater.from(this);

        // get the mediaId
        mediaId = getIntent().getStringExtra(Constants.mediaId);

        // check if given mediaId is non-zero
        if (!mediaId.contentEquals("0")) {

            ParseQuery<ParseObject> userDataObject = ParseQuery.getQuery(Constants.mediaTable);

            // check if the objectId exists as 'relativeId' in mediaTable table
            userDataObject.whereEqualTo(Constants.relativeId, mediaId);

            userDataObject.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {

                    // check if there is no exception
                    if (e == null) {

                        // iterate through the list and get images
                        for (int i = 0; i < list.size(); i++) {

                            // get the mediaFile of the wall post
                            ParseFile mediaFile = (ParseFile) list.get(i).get(Constants.mediaFile);

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
                                                mediaFiles.add(Constants.null_indicator.getBytes());


                                                //adapter.notifyDataSetChanged();
                                            } else {

                                                // set the mediaTable file string to image base 64 string
                                                //studentWall.setMediaFile(Base64.encodeToString(bytes, Base64.DEFAULT).toString());
                                                mediaFiles.add(bytes);


                                                // get the layout
                                                relativeLayout = (LinearLayout) layoutInflater.inflate(R.layout.full_image_activity, null); // new RelativeLayout(NewHomeActivity.this);

                                                //ImageView imageView = new ImageView(MultiImageViewActivity.this);
                                                //imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                                ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.largeImage);

                                                // get view from layout
                                                //ImageView imageView = (ImageView) findViewById(R.id.fullImageView);

                                                if(bytes != null){

                                                    new AssignBitmap().execute(imageView, bytes);

                                                }
                                                // assign the bitmap
                                                //Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                //imageView.setImageBitmap(bitmap);

                                                // add the images to the scrollView
                                                academcisPostImageView.addView(relativeLayout);

                                            }

                                        }
                                    }
                                });
                        }

                    }
                }
            });

        } else {

            // set the mediaTable files list to null indicator
            mediaFiles.add(Constants.null_indicator.getBytes());

        }


    }

    static class AssignBitmap extends AsyncTask<Object, Float, Bitmap> {

        ImageView imageView;
        byte[] bytes;
        Bitmap bitmap = null;

        @Override
        protected Bitmap doInBackground(Object... params) {

            imageView = (ImageView) params[0];
            bytes = (byte[]) params[1];
            //bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            bitmap = decodeSampledBitmapFromResource(bytes, 400, 400);


            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null && imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
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
