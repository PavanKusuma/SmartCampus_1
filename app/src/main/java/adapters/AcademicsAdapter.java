package adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import internaldb.SmartCampusDB;
import model.Academics;
import svecw.smartcampus.FullImageActivity;
import svecw.smartcampus.R;
import utils.Constants;

/**
 * Created by Pavan on 4/16/15.
 */
public class AcademicsAdapter extends BaseAdapter {

    // context of present class
    Context context;

    // usernames and likelist
    List<Academics> academicsPostsList;
    //ArrayList<Boolean> likeList;

    // layout Inflator
    LayoutInflater layoutInflater;

    // instance of the current db
    SmartCampusDB smartCampusDB;

    private LruCache<String, byte[]> mMemoryCache;

    // constructor
    public AcademicsAdapter(Context context) {

        this.context = context;

        // intialize variables
        academicsPostsList = new ArrayList<Academics>();

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
        return academicsPostsList.size();
    }

    @Override
    public Object getItem(int position) {
        return academicsPostsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        RelativeLayout itemView;
        if (convertView == null) {
            itemView = (RelativeLayout) layoutInflater.inflate(R.layout.academics_single_listitem, parent, false);

        } else {
            itemView = (RelativeLayout) convertView;
        }

        // date format for displaying created date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm");

        // get all views of single shouts list item
        TextView post = (TextView) itemView.findViewById(R.id.academicsPostDescription);
        TextView userName = (TextView) itemView.findViewById(R.id.academicsUserName);
        final ImageView userImage = (ImageView) itemView.findViewById(R.id.academicsPostImage);
        TextView createdAt = (TextView) itemView.findViewById(R.id.academicsCreatedAt);

        // set the username of the user
        post.setText(academicsPostsList.get(position).getDescription());
        userName.setText(academicsPostsList.get(position).getUserName());
        createdAt.setText(simpleDateFormat.format(academicsPostsList.get(position).getCreatedAt()));

        // set user image
        if(academicsPostsList.get(position).getMediaFile() != null)
            if(!Arrays.equals(academicsPostsList.get(position).getMediaFile(), Constants.null_indicator.getBytes())) {

                // assign the bitmap
                Bitmap bitmap = decodeSampledBitmapFromResource(academicsPostsList.get(position).getMediaFile(), 48, 48);
                userImage.setImageBitmap(bitmap);

            } else {

                userImage.setImageResource(R.drawable.moon);
            }

        // show the large image
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v(Constants.appName, "Clicked");
                // build the bundle for sending mediaTable file bytes

                byte[] b = academicsPostsList.get(position).getMediaFile();
                Bundle bundle = new Bundle();

                if (b.length > 1048576) {

                    final String imageKey = String.valueOf(userImage);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 1;

                    Bitmap bm = decodeSampledBitmapFromResource(academicsPostsList.get(position).getMediaFile(), 200, 200);
                            //BitmapFactory.decodeByteArray(b, 0, b.length,options);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bm.recycle();
                    bm = null;
                    byte[] data = bos.toByteArray();

                    //bundle.putByteArray(Constants.fullImage, data);

                    bundle.putByteArray(Constants.fullImage, getBitmapFromMemCache(imageKey));
                    //ByteArrayBody bab = new ByteArrayBody(data, "image.jpg");
                    //reqEntity.addPart("image_data", bab);
                } else {
                    //ByteArrayBody bab = new ByteArrayBody(imgData, "image.jpg");
                    //reqEntity.addPart("image_data", bab);
                    bundle.putByteArray(Constants.fullImage, b);

                }


                // pass the bundle for the full screen activity
                Intent fullImageIntent = new Intent(context, FullImageActivity.class);
                fullImageIntent.putExtras(bundle);
                context.startActivity(fullImageIntent);
            }
        });

        // return the itemView
        return itemView;
    }

    /**
     * update the adapter list items and notify
     */
    public void updateItems(List<Academics> postsList) {
        this.academicsPostsList = postsList;
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
