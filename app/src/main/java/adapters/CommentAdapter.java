package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import model.Comment;
import svecw.smartcampus.R;
import utils.Constants;
import utils.Routes;

/**
 * Created by Pavan on 4/16/15.
 */
public class CommentAdapter extends BaseAdapter {


    // list of comments
    List<Comment> commentsList;

    // current context
    Context context;

    // inflater for Layout
    LayoutInflater layoutInflater;

    // constructor
    public CommentAdapter(Context context) {

        this.context = context;

        commentsList = new ArrayList<Comment>();

        // layout Inflater
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return commentsList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        RelativeLayout itemView;
        if (convertView == null) {
            itemView = (RelativeLayout) layoutInflater.inflate(R.layout.college_wallpost_single_listitem, parent, false);

        } else {
            itemView = (RelativeLayout) convertView;
        }

        try {

            Typeface sansFont = Typeface.createFromAsset(context.getResources().getAssets(), Constants.fontName);

            // inflate single list item for each row
            itemView = (RelativeLayout) layoutInflater.inflate(R.layout.comment_single_layout, parent, false);

            // view holder object to contain xml file elements
            ImageView commentUserImage = (ImageView) itemView.findViewById(R.id.commentUserImage);
            TextView commentUserName = (TextView) itemView.findViewById(R.id.commentUserName);
            TextView commentId = (TextView) itemView.findViewById(R.id.postObjectId);
            TextView userObjectId = (TextView) itemView.findViewById(R.id.userObjectId);
            TextView commentText = (TextView) itemView.findViewById(R.id.commentText);


            commentUserName.setText(commentsList.get(position).getUsername()); commentUserName.setTypeface(sansFont);
            commentId.setText(commentsList.get(position).getCommentId()); commentId.setTypeface(sansFont);
            commentText.setText(commentsList.get(position).getComment()); commentText.setTypeface(sansFont);
            userObjectId.setText(commentsList.get(position).getUserObjectId()); userObjectId.setTypeface(sansFont);

            // set user image
            if (commentsList.get(position).getUserImage().contentEquals(Constants.null_indicator)) {

                commentUserImage.setImageResource(R.drawable.ic_user_profile);
            } else {
                // assign the bitmap
                //Bitmap bitmap = decodeSampledBitmapFromResource(comment.getCommentUserImage(), 48, 48);
                //holder.commentUserImage.setImageBitmap(bitmap);

                // get the connection url for the media
                URL url = new URL(Routes.getMedia + commentsList.get(position).getUserImage());
                URLConnection urlConnection = url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.connect();

                if (urlConnection.getContentLength() > 0) {

                    // getInputStream
                    InputStream is = urlConnection.getInputStream();

                    // bitmap options
                    BitmapFactory.Options options = new BitmapFactory.Options();
    /*
                            //Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
                            options.inJustDecodeBounds = true;
                            //BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                            BitmapFactory.decodeStream(is, null, options);

                            // Calculate inSampleSize
                            options.inSampleSize = calculateInSampleSize(options, 200, 200);

                            // Decode bitmap with inSampleSize set
                            options.inJustDecodeBounds = false;
                            //return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);*/
                    Bitmap bitmap = BitmapFactory.decodeStream(is);


                    commentUserImage.setImageBitmap(bitmap);

                }
            }


        }
        catch (Exception e){

        }

        return itemView;
    }

    /**
     * update the adapter list items and notify
     */
    public void updateItems(List<Comment> commentsList) {
        this.commentsList = commentsList;
        //likeList = like1List;
        notifyDataSetChanged();
    }

    // get decoded sample bitmap
    public static Bitmap decodeSampledBitmapFromResource(InputStream is, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        BitmapFactory.decodeStream(is, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        //return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        return BitmapFactory.decodeStream(is, null, options);
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
