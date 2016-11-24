package adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

    ViewHolder holder;

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

    public class ViewHolder{

        ImageView commentUserImage;
        TextView commentUserName, commentId, userObjectId, commentText;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        RelativeLayout itemView;
        if (convertView == null) {
            //itemView = (RelativeLayout) layoutInflater.inflate(R.layout.college_wallpost_single_listitem, parent, false);

            // inflate single list item for each row
            itemView = (RelativeLayout) layoutInflater.inflate(R.layout.comment_single_layout, parent, false);

            // object for viewHolder
            holder = new ViewHolder();

            // view holder object to contain xml file elements
            holder.commentUserImage = (ImageView) itemView.findViewById(R.id.commentUserImage);
            holder.commentUserName = (TextView) itemView.findViewById(R.id.commentUserName);
            holder.commentId = (TextView) itemView.findViewById(R.id.postObjectId);
            holder.userObjectId = (TextView) itemView.findViewById(R.id.userObjectId);
            holder.commentText = (TextView) itemView.findViewById(R.id.commentText);

            itemView.setTag(holder);


        } else {

            itemView = (RelativeLayout) convertView;

            holder = (ViewHolder) itemView.getTag();
        }

        try {

            Typeface sansFont = Typeface.createFromAsset(context.getResources().getAssets(), Constants.fontName);

            // inflate single list item for each row
            //itemView = (RelativeLayout) layoutInflater.inflate(R.layout.comment_single_layout, parent, false);


            holder.commentUserName.setText(commentsList.get(position).getUsername()); holder.commentUserName.setTypeface(sansFont);
            holder.commentId.setText(commentsList.get(position).getCommentId()); holder.commentId.setTypeface(sansFont);
            holder.commentText.setText(commentsList.get(position).getComment()); holder.commentText.setTypeface(sansFont);
            holder.userObjectId.setText(commentsList.get(position).getUserObjectId()); holder.userObjectId.setTypeface(sansFont);

            /*// set user image
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
    *//*
                            //Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
                            options.inJustDecodeBounds = true;
                            //BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                            BitmapFactory.decodeStream(is, null, options);

                            // Calculate inSampleSize
                            options.inSampleSize = calculateInSampleSize(options, 200, 200);

                            // Decode bitmap with inSampleSize set
                            options.inJustDecodeBounds = false;
                            //return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);*//*
                    Bitmap bitmap = BitmapFactory.decodeStream(is);


                    commentUserImage.setImageBitmap(bitmap);

                }
            }


*/




            // userImage
            // check if userImage exists for the post
            // if so, fetch and display the userImage
            if(!commentsList.get(position).getUserImage().contentEquals(Constants.null_indicator)) {

                // show the default user profile image
                holder.commentUserImage.setImageResource(R.drawable.ic_user_profile);

                // check if the user image contains the image name
                // if so fetch the image from url and display
                // else fetch the image from local and display as it is just posted by current user
                if (commentsList.get(position).getUserImage().contains(".")) {

                    // load using picasso lib
                    Picasso.with(context).load(Routes.getMedia+commentsList.get(position).getUserImage()).placeholder(R.drawable.ic_user_profile).into(holder.commentUserImage);

/*                    // get the connection url for the media
                    URL url = new URL(Routes.getMedia + collegeWallPostsList.get(position).getUserImage());
                    URLConnection urlConnection = url.openConnection();
                    urlConnection.setDoInput(true);
                    urlConnection.connect();

                    if (urlConnection.getContentLength() > 0) {

                        // getInputStream
                        InputStream is = urlConnection.getInputStream();

                        // bitmap options
                        BitmapFactory.Options options = new BitmapFactory.Options();
*//*
                        //Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
                        options.inJustDecodeBounds = true;
                        //BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                        BitmapFactory.decodeStream(is, null, options);

                        // Calculate inSampleSize
                        options.inSampleSize = calculateInSampleSize(options, 200, 200);

                        // Decode bitmap with inSampleSize set
                        options.inJustDecodeBounds = false;
                        //return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);*//*
                        Bitmap bitmap = BitmapFactory.decodeStream(is);

                        holder.userImage.setImageBitmap(bitmap);
                    }
                    else {

                        // hide user image layout
                        holder.userImage.setImageResource(R.drawable.ic_user_profile);
                    }*/
                } else {


                    if (!commentsList.get(position).getUserImage().equals("-")) {

                        byte[] b = Base64.decode(commentsList.get(position).getUserImage(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

                        //RoundImage roundedImage = new RoundImage(bitmap, 220, 220);
                        //RoundImage roundedImage1 = new RoundImage(roundedImage.getBitmap(), bitmap.getWidth(), bitmap.getHeight());
                        //holder.userImage.setImageDrawable(roundedImage1);

                        //userImageLayout.setVisibility(View.VISIBLE);
                        holder.commentUserImage.setImageBitmap(bitmap);

                    }
                    else {

                        // hide user image layout
                        holder.commentUserImage.setImageResource(R.drawable.ic_user_profile);
                    }
                }

            }
            else {

                // set the userImage
                holder.commentUserImage.setImageResource(R.drawable.ic_user_profile);

            }



            holder.commentUserImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Dialog dialog = new Dialog(context);
                    // Include dialog.xml file
                    dialog.setContentView(R.layout.userimage_maxview);
                    // Set dialog title
                    dialog.setTitle(commentsList.get(position).getUsername());

                    // set values for custom dialog components - text, image and button
                /*TextView text = (TextView) dialog.findViewById(R.id.textDialog);
                text.setText("Custom dialog Android example.");*/
                    ImageView image = (ImageView) dialog.findViewById(R.id.maxView);
                    TextView resText = (TextView) dialog.findViewById(R.id.resText);
                    //image.setImageResource(R.drawable.image0);

                    try{

                        if (!commentsList.get(position).getUserImage().contentEquals(Constants.null_indicator)) {

                            // get the connection url for the media

                            URL url = new URL(Routes.getMedia + "profile_"+ commentsList.get(position).getUserImage());
                            //URL url = new URL(Routes.getMedia + collegeWallPostsList.get(position).getUserImage());
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

                                image.setImageBitmap(bitmap);
                            }
                        } else {

                            // set the userImage
                            image.setImageResource(R.drawable.ic_user_profile);

                        }
                    }
                    catch (Exception e){

                        Picasso.with(context).load(Routes.getMedia+commentsList.get(position).getUserImage()).placeholder(R.drawable.ic_user_profile).into(image);


                    }

                    dialog.show();
                }
            });



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
