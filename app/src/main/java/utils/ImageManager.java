package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Pavan on 8/10/16.
 */
public class ImageManager {

    private long cacheDuration;
    private DateFormat mDateFormatter;

    final private HashMap<String, SoftReference<Bitmap>> imageMap = new HashMap<String, SoftReference<Bitmap>>();

    private File cacheDir;
    private ImageQueue imageQueue = new ImageQueue();
    private Thread imageLoaderThread = new Thread(new ImageQueueManager());

    public ImageManager(Context context, long _cacheDuration) {

        cacheDuration = _cacheDuration;
        //TODO Make this match the date format of your server
        mDateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        // Make background thread low priority, to avoid affecting UI performance
        imageLoaderThread.setPriority(Thread.NORM_PRIORITY-1);

        // Find the dir to save cached images
        String sdState = android.os.Environment.getExternalStorageState();
        if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
            File sdDir = android.os.Environment.getExternalStorageDirectory();
            cacheDir = new File(sdDir,"data/svecw");

        } else {
            cacheDir = context.getCacheDir();
        }

        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }

    /**
     * This method will display the image if it exists in the imageMap
     * Checks for the url and if it is not null, it fetches bitmap from saved url and assigns to imageView
     * else It fetches bitmap from the provided url and assigns to imageView
     * @param url
     * @param imageView
     * @param defaultDrawableId
     */
    public void displayImage(String url, ImageView imageView, int defaultDrawableId) {

        if(imageMap.containsKey(url)) {
            //Log.v(Constants.appName, "Here is the url stored : " +imageMap.get(url));
            //Log.v(Constants.appName, "Here is the url stored : " +imageMap.get(url).get());

            // check if the url exists for the image
            if(imageMap.get(url).get() != null) {
                imageView.setImageBitmap(imageMap.get(url).get());
            }
            else {

                // Show loading image till the image is fetched
                queueImage(url, imageView, defaultDrawableId);
                imageView.setImageResource(defaultDrawableId);

                // get the bitmap from the url
                Bitmap bmp = getBitmap(url);
                imageMap.put(url, new SoftReference<Bitmap>(bmp));

                // show the bitmap to the imageView
                imageView.setImageBitmap(imageMap.get(url).get());
            }


        } else {
            queueImage(url, imageView, defaultDrawableId);
            imageView.setImageResource(defaultDrawableId);
        }
    }

    private void queueImage(String url, ImageView imageView, int defaultDrawableId) {
        // This ImageView might have been used for other images, so we clear
        // the queue of old tasks before starting.
        imageQueue.Clean(imageView);
        ImageRef p = new ImageRef(url, imageView, defaultDrawableId);

        synchronized(imageQueue.imageRefs) {
            imageQueue.imageRefs.push(p);
            imageQueue.imageRefs.notifyAll();
        }

        // Start thread if it's not started yet
        if(imageLoaderThread.getState() == Thread.State.NEW) {
            imageLoaderThread.start();
        }
    }

    private Bitmap getBitmap(String url) {

        try {
            URLConnection openConnection = new URL(url).openConnection();

            String filename = String.valueOf(url.hashCode());

            File bitmapFile = new File(cacheDir, filename);
            Bitmap bitmap = BitmapFactory.decodeFile(bitmapFile.getPath());

            long currentTimeMillis = System.currentTimeMillis();

            // Is the bitmap in our cache?
            if (bitmap != null) {

                // Has it expired?
                long bitmapTimeMillis = bitmapFile.lastModified();
                if ( (currentTimeMillis - bitmapTimeMillis) < cacheDuration ) {
                    return bitmap;
                }
                return  bitmap;

                // Check also if it was modified on the server before downloading it
                /*String lastMod = openConnection.getHeaderField("Date modified");
                long lastModTimeMillis = mDateFormatter.parse(lastMod).getTime();

                if ( lastModTimeMillis <= bitmapTimeMillis ) {

                    //Discard the connection and return the cached version
                    return bitmap;
                }*/
            }


            openConnection.setDoInput(true);
            openConnection.connect();


                /*// getInputStream
                InputStream is = openConnection.getInputStream();

                // bitmap options
                BitmapFactory.Options options = new BitmapFactory.Options();


                //BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                BitmapFactory.decodeStream(is, null, options);

                //return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                bitmap = BitmapFactory.decodeStream(is);

                */


                // Nope, have to download it
            bitmap = BitmapFactory.decodeStream(openConnection.getInputStream());
            // save bitmap to cache for later
            writeFile(bitmap, bitmapFile);

            return bitmap;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void writeFile(Bitmap bmp, File f) {
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 80, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try { if (out != null ) out.close(); }
            catch(Exception ex) {}
        }
    }

    /** Classes **/

    private class ImageRef {
        public String url;
        public ImageView imageView;
        public int defDrawableId;

        public ImageRef(String u, ImageView i, int defaultDrawableId) {
            url=u;
            imageView=i;
            defDrawableId = defaultDrawableId;
        }
    }

    //stores list of images to download
    private class ImageQueue {
        private Stack<ImageRef> imageRefs =
                new Stack<ImageRef>();

        //removes all instances of this ImageView
        public void Clean(ImageView view) {

            for(int i = 0 ;i < imageRefs.size();) {
                if(imageRefs.get(i).imageView == view)
                    imageRefs.remove(i);
                else ++i;
            }
        }
    }

    private class ImageQueueManager implements Runnable {
        @Override
        public void run() {
            try {
                while(true) {
                    // Thread waits until there are images in the
                    // queue to be retrieved
                    if(imageQueue.imageRefs.size() == 0) {
                        synchronized(imageQueue.imageRefs) {
                            imageQueue.imageRefs.wait();
                        }
                    }

                    // When we have images to be loaded
                    if(imageQueue.imageRefs.size() != 0) {
                        ImageRef imageToLoad;

                        synchronized(imageQueue.imageRefs) {
                            imageToLoad = imageQueue.imageRefs.pop();
                        }

                        Bitmap bmp = getBitmap(imageToLoad.url);
                        imageMap.put(imageToLoad.url, new SoftReference<Bitmap>(bmp));
                        Object tag = imageToLoad.imageView.getTag();

                        // Make sure we have the right view - thread safety defender
                        if(tag != null && ((String)tag).equals(imageToLoad.url)) {
                            BitmapDisplayer bmpDisplayer =
                                    new BitmapDisplayer(bmp, imageToLoad.imageView, imageToLoad.defDrawableId);

                            Activity a =
                                    (Activity)imageToLoad.imageView.getContext();

                            a.runOnUiThread(bmpDisplayer);
                        }
                    }

                    if(Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {}
        }
    }

    //Used to display bitmap in the UI thread
    private class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        ImageView imageView;
        int defDrawableId;

        public BitmapDisplayer(Bitmap b, ImageView i, int defaultDrawableId) {
            bitmap=b;
            imageView=i;
            defDrawableId = defaultDrawableId;
        }

        public void run() {
            if(bitmap != null)
                imageView.setImageBitmap(bitmap);
            else
                imageView.setImageResource(defDrawableId);
        }
    }
}