package adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import internaldb.SmartCampusDB;
import model.GlobalInfo;
import svecw.smartcampus.KnowledgeWallWebView;
import svecw.smartcampus.R;
import utils.Constants;

/**
 * Created by Pavan on 12/27/15.
 */
public class KnowledgeWallAdapter extends BaseAdapter {

    // context of present class
    Context context;

    // usernames and likelist
    ArrayList<GlobalInfo> knowledgePostsList;
    //ArrayList<Boolean> likeList;

    // layout Inflator
    LayoutInflater layoutInflater;

    // instance of the current db
    SmartCampusDB smartCampusDB;

    private LruCache<String, byte[]> mMemoryCache;
    String category;

    // constructor
    public KnowledgeWallAdapter(Context context) {

        this.context = context;
        //category = category1;

        // intialize variables
        knowledgePostsList = new ArrayList<GlobalInfo>();

        // layoutInflator object
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // object for db
        smartCampusDB = new SmartCampusDB(context);

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, byte[]>(cacheSize) {
            @Override
            protected int sizeOf(String key, byte[] bytes) {
                // The cache size will be measured in kilobytes rather than
                // number of items.

                return bytes.length / 1024;
            }
        };
    }

    @Override
    public int getCount() {
        return knowledgePostsList.size();
    }

    @Override
    public Object getItem(int position) {
        return knowledgePostsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        RelativeLayout itemView;
        if (convertView == null) {
            itemView = (RelativeLayout) layoutInflater.inflate(R.layout.home_single_listitem, parent, false);

        } else {
            itemView = (RelativeLayout) convertView;
        }

        try {

            // date format for displaying created date
            DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            TextView globalWallPostTitle = (TextView) itemView.findViewById(R.id.globalPostTitle);
            TextView globalWallPostCreatedAt = (TextView) itemView.findViewById(R.id.globalPostCreatedAt);
            final TextView globalWallPostDescription = (TextView) itemView.findViewById(R.id.globalPostDescription);
            ImageView linkImage = (ImageView) itemView.findViewById(R.id.link);
            //ImageView globalPostImage = (ImageView) itemView.findViewById(R.id.globalPostImage);
            //ImageView globalPostShare = (ImageView) itemView.findViewById(R.id.globalPostShare);

            // get the date format and convert it into required format to display
            java.util.Date date = simpleDateFormat.parse(knowledgePostsList.get(position).getCreatedAt());
            simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm aa");

            Typeface sansFont = Typeface.createFromAsset(context.getResources().getAssets(), Constants.fontName);
            globalWallPostTitle.setTypeface(sansFont);
            globalWallPostCreatedAt.setTypeface(sansFont);
            globalWallPostDescription.setTypeface(sansFont);


            // set title and created At
            globalWallPostTitle.setText(knowledgePostsList.get(position).getTitle());
            globalWallPostCreatedAt.setText(simpleDateFormat.format(date));
            globalWallPostDescription.setText(knowledgePostsList.get(position).getDescription());

            // check if link is present
            if(!knowledgePostsList.get(position).getLink().contentEquals(Constants.null_indicator)){

                linkImage.setVisibility(View.VISIBLE);
            }
            else{

                linkImage.setVisibility(View.GONE);
            }


            // navigate to link page only if link is available
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // check if link is present and navigate to webView screen
                    if(!knowledgePostsList.get(position).getLink().contentEquals(Constants.null_indicator)){

                        // pass the bundle for the full screen activity
                        Intent webViewIntent = new Intent(context, KnowledgeWallWebView.class);
                        webViewIntent.putExtra(Constants.description, knowledgePostsList.get(position).getDescription());
                        webViewIntent.putExtra(Constants.link, knowledgePostsList.get(position).getLink());
                        webViewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(webViewIntent);
                    }
                    else{

                        // do nothing
                    }

                    Log.v(Constants.appName, knowledgePostsList.get(position).getLink());


                }
            });

            /*
            // check the media count for the post
            int mediaCount = knowledgePostsList.get(position).getMediaCount();

            if(mediaCount > 0) {
                // if media exists for the post
                // fetch the names String of media
                String mediaName = knowledgePostsList.get(position).getMedia();

                // get individual media names
                String[] mediaNames = mediaName.split(",");

                // iterate through number of media items and display accordingly
                for (int i = 0; i < mediaCount; i++) {

                    // get the connection url for the media
                    URL url = new URL(Routes.getMedia + mediaNames[i]);
                    URLConnection urlConnection = url.openConnection();
                    urlConnection.setDoInput(true);
                    urlConnection.connect();

                    if (urlConnection.getContentLength() > 0) {

                        // getInputStream
                        InputStream is = urlConnection.getInputStream();

                        // bitmap options
                        BitmapFactory.Options options = new BitmapFactory.Options();

                        //Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
                        options.inJustDecodeBounds = true;
                        //BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                        //BitmapFactory.decodeStream(is, null, options);

                        // Calculate inSampleSize
                        options.inSampleSize = calculateInSampleSize(options, 200, 200);

                        // Decode bitmap with inSampleSize set
                        options.inJustDecodeBounds = false;
                        //return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);


                        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);

                        globalPostImage.setImageBitmap(bitmap);

                    }
                }


            }
            else{

                globalPostImage.setImageResource(R.drawable.moon);
            }*/
/*

            // set user image
            if (knowledgePostsList.get(position).getMediaFile() != null)
                if (!Arrays.equals(Base64.decode(knowledgePostsList.get(position).getMediaFile(), Base64.DEFAULT), Constants.null_indicator.getBytes())) {

                    // assign the bitmap
                    Bitmap bitmap = decodeSampledBitmapFromResource(Base64.decode(knowledgePostsList.get(position).getMediaFile(), Base64.DEFAULT), 200, 200);
                    globalPostImage.setImageBitmap(bitmap);


                } else {

                    globalPostImage.setImageResource(R.drawable.moon);
                }
*/


        }
        catch (Exception e){

            // do nothing
        }
        // return the itemView
        return itemView;
    }

    /**
     * update the adapter list items and notify
     */
    public void updateItems(ArrayList<GlobalInfo> postsList) {
        knowledgePostsList = postsList;
        //this.category = category;
        //likeList = like1List;
        notifyDataSetChanged();
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

    public void addBitmapToMemoryCache(String key, byte[] bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public byte[] getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}

