package adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;

import svecw.smartcampus.R;

/**
 */
class ImageDownloaderTask extends AsyncTask<String, Void, String> {

    private final WeakReference<ImageView> imageViewReference;

    public ImageDownloaderTask(ImageView imageView) {
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected void onPreExecute() {


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected String doInBackground(String... params) {

        //return downloadBitmap(params[0]);
        return params[0];
    }

    @Override
    protected void onPostExecute(String url) {
        if (isCancelled()) {
            //bitmap = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();

                if (imageView != null) {
                    if (url != null) {
                        //imageView.setImageBitmap(downloadBitmap(url));

                        Picasso.with(imageView.getContext()).load(url).into(imageView);
                    } else {
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.emptyimg1);
                        imageView.setImageDrawable(placeholder);
                    }
                }


        }
    }

    private Bitmap downloadBitmap(String urls) {
       // HttpURLConnection urlConnection = null;
        try {

            URL url = new URL(urls);

            // Send POST data request
            URLConnection urlConnection = url.openConnection();
            //urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            //OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            //wr.write(data);
            //wr.flush();


            urlConnection.connect();

            if (urlConnection.getContentLength() > 0) {

                // getInputStream
                InputStream is = urlConnection.getInputStream();

                // bitmap options
                BitmapFactory.Options options = new BitmapFactory.Options();

                return BitmapFactory.decodeStream(is);


            }





            /*URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();

            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }*/
        } catch (Exception e) {
            //urlConnection.disconnect();
            e.printStackTrace();
            Log.w("ImageDownloader", "Error downloading image from " + urls + e.getMessage());
        } finally {
            /*if (urlConnection != null) {
                urlConnection.disconnect();
            }*/
        }
        return null;
    }
}