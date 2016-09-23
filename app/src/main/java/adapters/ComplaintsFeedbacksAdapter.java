package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.Base64;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import internaldb.SmartCampusDB;
import model.ComplaintAndFeedback;
import svecw.smartcampus.R;
import utils.Constants;
import utils.Routes;

/**
 * Created by Pavan on 4/16/15.
 */
public class ComplaintsFeedbacksAdapter extends BaseAdapter {

    // context of present class
    Context context;

    // usernames and likelist
    ArrayList<ComplaintAndFeedback> complaintsOrFeedbacksList;
    //ArrayList<Boolean> likeList;

    // layout Inflator
    LayoutInflater layoutInflater;

    // initial like record
    boolean likedRecord = false;

    // mediaTable player
    MediaPlayer mediaPlayer;

    // instance of the current db
    SmartCampusDB smartCampusDB;

    // array list of object ids of liked posts
    HashMap<String, String> previousLikedAndDisLikedObjectIds;

    ViewHolder holder;

    // constructor
    public ComplaintsFeedbacksAdapter(Context context) {

        this.context = context;

        // intialize variables
        complaintsOrFeedbacksList = new ArrayList<ComplaintAndFeedback>();
        //likeList = new ArrayList<Boolean>();

        // layoutInflator object
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // object for db
        smartCampusDB = new SmartCampusDB(context);

        // object ids of liked posts
        previousLikedAndDisLikedObjectIds = new HashMap<String, String>();

        // create the mediaPlayer object
        mediaPlayer = MediaPlayer.create(context, R.raw.like_sound);

    }

    @Override
    public int getCount() {
        return complaintsOrFeedbacksList.size();
    }

    @Override
    public Object getItem(int position) {
        return complaintsOrFeedbacksList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {

        TextView description, userName, collegeId, createdAt;
        ImageView userImage;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        RelativeLayout itemView;
        if (convertView == null) {
            itemView = (RelativeLayout) layoutInflater.inflate(R.layout.complaints_feedbacks_singlelistitem, parent, false);

            // object for viewHolder
            holder = new ViewHolder();

            // get all views of single shouts list item
            holder.description = (TextView) itemView.findViewById(R.id.complaintDescription);
            holder.userName = (TextView) itemView.findViewById(R.id.complaintUserName);
            holder.collegeId = (TextView) itemView.findViewById(R.id.complaintCollegeId);
            holder.createdAt = (TextView) itemView.findViewById(R.id.complaintDate);
            holder.userImage = (ImageView) itemView.findViewById(R.id.complaintUserImage);

            itemView.setTag(holder);

        } else {
            itemView = (RelativeLayout) convertView;

            holder = (ViewHolder) itemView.getTag();
        }

        try {

            Typeface sansFont = Typeface.createFromAsset(context.getResources().getAssets(), Constants.fontName);

            // date format for displaying created date
            // provide date format present in server
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            // set the username of the user
            holder.description.setText(complaintsOrFeedbacksList.get(position).getDescription()); holder.description.setTypeface(sansFont);
            holder.userName.setText(complaintsOrFeedbacksList.get(position).getUserName()); holder.userName.setTypeface(sansFont);
            holder.collegeId.setText(complaintsOrFeedbacksList.get(position).getCollegeId()); holder.collegeId.setTypeface(sansFont);

            // get the date format and convert it into required format to display
            java.util.Date date = simpleDateFormat.parse(complaintsOrFeedbacksList.get(position).getCreatedAt());
            simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm aa");

            //type.setText(String.valueOf(complaintsOrFeedbacksList.get(position).getType()));
            holder.createdAt.setText(simpleDateFormat.format(date)); holder.createdAt.setTypeface(sansFont);


            // userImage
            // check if userImage exists for the post
            // if so, fetch and display the userImage
            if(!complaintsOrFeedbacksList.get(position).getUserImage().contentEquals(Constants.null_indicator)) {
                //Log.v(Constants.appName, Routes.getMedia + collegeWallPostsList.get(position).getUserImage());

                // check if the user image contains the image name
                // if so fetch the image from url and display
                // else fetch the image from local and display as it is just posted by current user
                if (complaintsOrFeedbacksList.get(position).getUserImage().contains(".")) {

                    // get the connection url for the media
                    URL url = new URL(Routes.getMedia + complaintsOrFeedbacksList.get(position).getUserImage());
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

                        holder.userImage.setImageBitmap(bitmap);
                    }
                    else{

                        // hide user image layout
                        holder.userImage.setImageResource(R.drawable.ic_user_profile);
                    }

                }
                else {


                    if(!complaintsOrFeedbacksList.get(position).getUserImage().equals("-")){

                        byte[] b = Base64.decode(complaintsOrFeedbacksList.get(position).getUserImage(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

                        //RoundImage roundedImage = new RoundImage(bitmap, 220, 220);
                        //RoundImage roundedImage1 = new RoundImage(roundedImage.getBitmap(), bitmap.getWidth(), bitmap.getHeight());
                        //holder.userImage.setImageDrawable(roundedImage1);

                        //userImageLayout.setVisibility(View.VISIBLE);
                        holder.userImage.setImageBitmap(bitmap);

                    }
                    else{

                        // hide user image layout
                        holder.userImage.setImageResource(R.drawable.ic_user_profile);
                    }
                }



            }
            else {

                // set the userImage
                holder.userImage.setImageResource(R.drawable.ic_user_profile);

            }
        }
        catch (Exception e){


        }


        // return the itemView
        return itemView;
    }

    /**
     * update the adapter list items and notify
     */
    public void updateItems(ArrayList<ComplaintAndFeedback> postsList) {
        this.complaintsOrFeedbacksList = postsList;
        //likeList = like1List;
        notifyDataSetChanged();
    }


}
