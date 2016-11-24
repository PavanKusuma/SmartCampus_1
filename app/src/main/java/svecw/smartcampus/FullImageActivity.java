package svecw.smartcampus;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import utils.Constants;
import utils.Routes;

/**
 * Created by Pavan_Kusuma on 4/29/2015.
 */
public class FullImageActivity extends Activity{

    // full image view
    TextView save;
    ImageView fullImageView, close;
    ProgressBar progressBar;
    Bundle bundle;
    Bitmap bitmap, highResBitmap;

    ScaleGestureDetector sgd;
    private float scale = 1f;
    private Matrix matrix = new Matrix();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image_activity);

        // get view from layout
        save = (TextView) findViewById(R.id.save);
        fullImageView = (ImageView) findViewById(R.id.largeImage);
        close = (ImageView) findViewById(R.id.close);
        progressBar = (ProgressBar) findViewById(R.id.progressBarImage);

        // get image bytes bundle
        byte[] bytes = getIntent().getByteArrayExtra(Constants.fullImage);
        final String[] mediaNames = getIntent().getStringArrayExtra(Constants.media);
        //bundle.getByteArray(Constants.fullImage);

        // convert the bytes to bitmap and assign to image view
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        fullImageView.setImageBitmap(bitmap);

        // close the full image activity
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


        // gesture detector for pinch and zoom
        sgd = new ScaleGestureDetector(this, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {

                scale *= scaleGestureDetector.getScaleFactor();
                scale = Math.max(0.1f, Math.min(scale, 5.0f));

                matrix.setScale(scale, scale);
                fullImageView.setImageMatrix(matrix);

                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
                return false;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    /*output = new FileOutputStream(file);

                    // Compress into png format image from 0% - 100%
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                    output.flush();
                    output.close();
                    */
                    MediaStore.Images.Media.insertImage(getContentResolver(), highResBitmap,
                            mediaNames[0], null);


                    Toast.makeText(FullImageActivity.this, "Image saved to device", Toast.LENGTH_SHORT).show();
                }

                catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        // show the progress bar
        progressBar.setVisibility(View.VISIBLE);
        Log.v("Image", "Fetching 1");
        // fetch the large image and assign to the image view
        new AssignBitmap().execute(fullImageView, mediaNames[0]);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        sgd.onTouchEvent(ev);
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();

    }

    class AssignBitmap extends AsyncTask<Object, Float, Bitmap> {

        //ImageView imageView;
        byte[] bytes;


        @Override
        protected Bitmap doInBackground(Object... params) {
            Log.v("Image", "Fetching 2");
            /*imageView = (ImageView) params[0];
            bytes = (byte[]) params[1];
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
*/
            try {

                //Log.v(Constants.appName, mediaNames[i]);
                // get the connection url for the media
                //URL url = new URL(Routes.getMedia + "large_" + (String)params[1]);
                URL url = new URL(Routes.getMedia + (String)params[1]);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.connect();
                Log.v("Image", "Fetching 3");
                if (urlConnection.getContentLength() > 0) {

                    // getInputStream
                    InputStream is = urlConnection.getInputStream();

                    // bitmap options
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    Log.v("Image", "Fetching 4");
/*

                        //Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
                        options.inJustDecodeBounds = true;
                        //BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                        BitmapFactory.decodeStream(is, null, options);

                        // Calculate inSampleSize
                        options.inSampleSize = calculateInSampleSize(options, 200, 200);

                        // Decode bitmap with inSampleSize set
                        options.inJustDecodeBounds = false;
*/
                    //return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                     highResBitmap = BitmapFactory.decodeStream(is);
                    Log.v("Image", "Fetching 5");
                    //postImage.setVisibility(View.VISIBLE);
                    //postImage.setImageBitmap(bitmap);
                }

            }
            catch (Exception e){

                Log.v("Image", e.getMessage());
            }


            return highResBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            Log.v("Image", "Fetching 6");
            fullImageView.setImageBitmap(bitmap);
            //if (bitmap != null && imageView != null) {
                Log.v("Image", "Fetching 7");
            progressBar.setVisibility(View.GONE);
              //  imageView.setImageBitmap(bitmap);
            //}
        }
    }


}
