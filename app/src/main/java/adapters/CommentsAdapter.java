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

import model.Comment;
import svecw.smartcampus.R;
import utils.Constants;
import utils.Routes;

/**
 * Created by Pavan on 6/10/15.
 */
public class CommentsAdapter extends BaseAdapter {

    // list of comments
    ArrayList<Comment> commentsList;

    // current context
    Context context;

    // inflater for Layout
    LayoutInflater layoutInflater;

    public CommentsAdapter(Context context) {
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

    // create holder class to contain xml file elements
    private class ViewHolder {

        ImageView commentUserImage;
        TextView commentUserName;
        TextView commentId;
        TextView userObjectId;
        TextView commentText;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        RelativeLayout itemView;

        if (convertView == null) {

            // inflate single list item for each row
            itemView = (RelativeLayout) layoutInflater.inflate(R.layout.comment_single_layout, parent, false);

            // view holder object to contain xml file elements
            holder = new ViewHolder();
            holder.commentUserImage = (ImageView) itemView.findViewById(R.id.commentUserImage);
            holder.commentUserName = (TextView) itemView.findViewById(R.id.commentUserName);
            holder.commentId = (TextView) itemView.findViewById(R.id.postObjectId);
            holder.userObjectId = (TextView) itemView.findViewById(R.id.userObjectId);
            holder.commentText = (TextView) itemView.findViewById(R.id.commentText);

            // set holder with layout inflater
            itemView.setTag(holder);

        } else {
            itemView = (RelativeLayout) convertView;

            holder = (ViewHolder) convertView.getTag();
        }

        try {

            if (commentsList.size() > 0) {

                Comment comment = commentsList.get(position);

                // set user image
                if (!comment.getUserImage().contentEquals(Constants.null_indicator)) {

                    holder.commentUserImage.setImageResource(R.drawable.ic_user_profile);
                } else {
                    // assign the bitmap
                    //Bitmap bitmap = decodeSampledBitmapFromResource(comment.getCommentUserImage(), 48, 48);
                    //holder.commentUserImage.setImageBitmap(bitmap);

                    // get the connection url for the media
                    URL url = new URL(Routes.getMedia + comment.getUserImage());
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


                        holder.commentUserImage.setImageBitmap(bitmap);

                    }
                }

                Typeface sansFont = Typeface.createFromAsset(context.getResources().getAssets(), Constants.fontName);

                holder.commentUserName.setText(comment.getUsername()); holder.commentUserName.setTypeface(sansFont);
                holder.commentId.setText(comment.getCommentId()); holder.commentId.setTypeface(sansFont);
                holder.commentText.setText(comment.getComment()); holder.commentText.setTypeface(sansFont);
                holder.userObjectId.setText(comment.getUserObjectId()); holder.userObjectId.setTypeface(sansFont);
            }
        }
        catch (Exception e){

            // do nothing
        }

        return itemView;
    }


    /**
     * update the adapter list items and notify
     */
    public void updateItems(ArrayList<Comment> commentsList) {
        this.commentsList = commentsList;

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
}
