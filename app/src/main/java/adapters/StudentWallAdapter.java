package adapters;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
import com.google.android.gms.vision.text.Line;
*/

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import internaldb.SmartCampusDB;
import model.Wall;
import svecw.smartcampus.FullImageActivity;
import svecw.smartcampus.KnowledgeWallWebView;
import svecw.smartcampus.NewCommentActivity;
import svecw.smartcampus.R;
import utils.Constants;
import utils.ImageManager;
import utils.Routes;
import utils.Snippets;

/**
 * Created by Pavan on 4/16/15.
 */
public class StudentWallAdapter extends BaseAdapter {


    // context of present class
    Context context;

    // usernames and likelist
    List<Wall> collegeWallPostsList;
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

    int status;
    JSONObject jsonResponse;
    String hideURL;

    ViewHolder holder;

    public ImageManager imageManager;

    HashMap<String, Bitmap> imageMapping = new HashMap<String, Bitmap>();

    // constructor
    public StudentWallAdapter(Context context) {

        this.context = context;

        // intialize variables
        collegeWallPostsList = new ArrayList<Wall>();
        //likeList = new ArrayList<Boolean>();

        // layoutInflator object
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // object for db
        smartCampusDB = new SmartCampusDB(context);

        // object ids of liked posts
        previousLikedAndDisLikedObjectIds = new HashMap<String, String>();

        // create the mediaPlayer object
        mediaPlayer = MediaPlayer.create(context, R.raw.like_sound);

        imageManager =
                new ImageManager(context.getApplicationContext(), 1024);
    }

    @Override
    public int getCount() {
        return collegeWallPostsList.size();
    }

    @Override
    public Object getItem(int position) {
        return collegeWallPostsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {

        TextView post, link, userName, collegeId, likeTextView, commentTextView, likeView, shareView, commentView, createdAt;
        ImageView userImage, postImage, deleteView;
        RelativeLayout votesLayout;
        LinearLayout likeViewLayout, shareViewLayout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final RelativeLayout itemView;
        if (convertView == null) {
            itemView = (RelativeLayout) layoutInflater.inflate(R.layout.student_wallpost_single_listitem, parent, false);

            // object for viewHolder
            holder = new ViewHolder();

            // get all views of single shouts list item
            holder.post = (TextView) itemView.findViewById(R.id.collegeWallPostDescription);
            holder.link = (TextView) itemView.findViewById(R.id.collegeWallPostLink);
            holder.userName = (TextView) itemView.findViewById(R.id.collegeWallUserName);
            holder.collegeId = (TextView) itemView.findViewById(R.id.collegeIdOfUser);
            holder.userImage = (ImageView) itemView.findViewById(R.id.collegeWallUserImage);
            holder.postImage = (ImageView) itemView.findViewById(R.id.collegeWallPostImage);
            holder.likeTextView = (TextView) itemView.findViewById(R.id.likeTextView);
            //final TextView disLikeTextView = (TextView) itemView.findViewById(R.id.disLikeTextView);
            holder.commentTextView = (TextView) itemView.findViewById(R.id.commentTextView);
            holder.likeView = (TextView) itemView.findViewById(R.id.likeView);
            //final TextView disLikeView = (TextView) itemView.findViewById(R.id.disLikeView);
            holder.shareView = (TextView) itemView.findViewById(R.id.shareView);
            holder.deleteView = (ImageView) itemView.findViewById(R.id.deleteView);
            holder.commentView = (TextView) itemView.findViewById(R.id.commentView);
            holder.createdAt = (TextView) itemView.findViewById(R.id.collegePostCreatedAt);

            holder.votesLayout = (RelativeLayout) itemView.findViewById(R.id.votesLayout);

            holder.likeViewLayout = (LinearLayout) itemView.findViewById(R.id.voteLayout_1);
            holder.shareViewLayout = (LinearLayout) itemView.findViewById(R.id.voteLayout_3);

            itemView.setTag(holder);

        } else {
            itemView = (RelativeLayout) convertView;

            holder = (ViewHolder) itemView.getTag();
        }

        try {

            Typeface sansFont = Typeface.createFromAsset(context.getResources().getAssets(), Constants.fontName);

            // date format for displaying created date
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            // enable the visibility of deleteView if the user has moderating privilege
            // before that check whether user is privileged
            if(smartCampusDB.isUserPrivileged()) {

                if (smartCampusDB.getUserPrivileges().getModerating() == 1) {
                    holder.deleteView.setVisibility(View.VISIBLE);
                }
                else{

                    holder.deleteView.setVisibility(View.GONE);
                }
            }
            else {

                // no privileges
                holder.deleteView.setVisibility(View.GONE);

            }

            // get the date format and convert it into required format to display
            java.util.Date date = simpleDateFormat.parse(collegeWallPostsList.get(position).getCreatedAt());
            simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm aa");

            // set the username of the user
            // build the description string
            String description = collegeWallPostsList.get(position).getPostDescription();
            if(!collegeWallPostsList.get(position).getFeeling().contentEquals(Constants.null_indicator)){
                description = description + " - feeling " + collegeWallPostsList.get(position).getFeeling();
            }
            if(!collegeWallPostsList.get(position).getLocation().contentEquals(Constants.null_indicator)){
                description = description + " - at " + collegeWallPostsList.get(position).getLocation();
            }
            holder.post.setText(description); holder.post.setTypeface(sansFont);
            // check if any link is available for the current post
            if(!collegeWallPostsList.get(position).getLinkUrl().contentEquals(Constants.null_indicator)){
                holder.link.setVisibility(View.VISIBLE);
                holder.link.setText(collegeWallPostsList.get(position).getLinkTitle());
            }
            else {
                holder.link.setVisibility(View.GONE);
            }
            holder.link.setTypeface(sansFont);
            holder.userName.setText(collegeWallPostsList.get(position).getUserName()); holder.userName.setTypeface(sansFont);
            holder.collegeId.setText(collegeWallPostsList.get(position).getCollegeId()); holder.collegeId.setTypeface(sansFont);
            holder.likeTextView.setText(String.valueOf(collegeWallPostsList.get(position).getLikes()) + " likes"); holder.likeTextView.setTypeface(sansFont);
            //disLikeTextView.setText(String.valueOf(collegeWallPostsList.get(position).getDislikes()) + " dislikes");
            holder.commentTextView.setText(String.valueOf(collegeWallPostsList.get(position).getComments()) + " comments"); holder.commentTextView.setTypeface(sansFont);
            holder.createdAt.setText(simpleDateFormat.format(date)); holder.createdAt.setTypeface(sansFont);

            // media
            // check the media count for the post
            // if media exists, load the media
            holder.postImage.setVisibility(View.GONE);

            if(collegeWallPostsList.get(position).getMediaCount() > 0) {

                holder.postImage.setVisibility(View.VISIBLE);

                // if media exists for the post
                // fetch the names String of media
                String mediaName = collegeWallPostsList.get(position).getMedia();

                //Log.v(Constants.appName, "Adapter Checking"+mediaName);
                // get individual media names
                String[] mediaNames = mediaName.split(",");

                // iterate through number of media items and display accordingly
                for (int i = 0; i < collegeWallPostsList.get(position).getMediaCount(); i++) {

                    //imageManager.displayImage(Routes.getMedia + mediaNames[i], holder.postImage, R.drawable.load_img);

                    // load using picasso lib
                    Picasso.with(context).load(Routes.getMedia+mediaNames[i]).placeholder(R.drawable.load_img).into(holder.postImage);

                    //Drawable placeholder =  context.getResources().getDrawable(R.drawable.emptyimg1);
                    //postImage.setImageDrawable(placeholder);

                    // fetch media in bg
                    //new FetchMedia(postImage).execute(Routes.getMedia, mediaNames[i], collegeWallPostsList.get(position).getWallId());
/*
                    new ImageDownloaderTask(postImage).execute(Routes.getMedia+mediaNames[i]);

                    if(imageMapping.containsKey(collegeWallPostsList.get(position).getWallId())) {

                        postImage.setVisibility(View.VISIBLE);
                        postImage.setImageBitmap(imageMapping.get(collegeWallPostsList.get(position).getWallId()));
                    }
                    else {

                        postImage.setVisibility(View.VISIBLE);
                        postImage.setImageResource(R.drawable.emptyimg1);
                    }*/

/*
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
*//*

                        //Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
                        options.inJustDecodeBounds = true;
                        //BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                        BitmapFactory.decodeStream(is, null, options);

                        // Calculate inSampleSize
                        options.inSampleSize = calculateInSampleSize(options, 200, 200);

                        // Decode bitmap with inSampleSize set
                        options.inJustDecodeBounds = false;
*//*
                        //return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);

                        holder.postImage.setVisibility(View.VISIBLE);
                        holder.postImage.setImageBitmap(bitmap);


                        Log.v(Constants.appName, "---------------- image found ---------------");
                    }
                    else {
                        holder.postImage.setVisibility(View.GONE);

                        Log.v(Constants.appName, "---------------- No image found ---------------");
                    }*/
                }


            }
            else{

                // hide post image
                holder.postImage.setVisibility(View.GONE);
            }


            // userImage
            // check if userImage exists for the post
            // if so, fetch and display the userImage
            if(!collegeWallPostsList.get(position).getUserImage().contentEquals(Constants.null_indicator)) {

                // show the default user profile image
                holder.userImage.setImageResource(R.drawable.ic_user_profile);

                // check if the user image contains the image name
                // if so fetch the image from url and display
                // else fetch the image from local and display as it is just posted by current user
                if (collegeWallPostsList.get(position).getUserImage().contains(".")) {

                    // load using picasso lib
                    Picasso.with(context).load(Routes.getMedia+collegeWallPostsList.get(position).getUserImage()).placeholder(R.drawable.ic_user_profile).into(holder.userImage);

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


                    if (!collegeWallPostsList.get(position).getUserImage().equals("-")) {

                        byte[] b = Base64.decode(collegeWallPostsList.get(position).getUserImage(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

                        //RoundImage roundedImage = new RoundImage(bitmap, 220, 220);
                        //RoundImage roundedImage1 = new RoundImage(roundedImage.getBitmap(), bitmap.getWidth(), bitmap.getHeight());
                        //holder.userImage.setImageDrawable(roundedImage1);

                        //userImageLayout.setVisibility(View.VISIBLE);
                        holder.userImage.setImageBitmap(bitmap);

                    }
                    else {

                        // hide user image layout
                        holder.userImage.setImageResource(R.drawable.ic_user_profile);
                    }
                }

            }
            else {

                // set the userImage
                holder.userImage.setImageResource(R.drawable.ic_user_profile);

            }


            // on click of thumbnail image
            // open new activity which will fetch the related large image of thumb and then display it
            holder.postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // get the exact itemView which the user clicked
                    ViewHolder holder1 =  (ViewHolder) itemView.getTag();

                    Bitmap bitmap = ((BitmapDrawable)holder1.postImage.getDrawable()).getBitmap();

                    if(bitmap != null) {

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] bitmapdata = stream.toByteArray();

                        // call the full image activity
                        Intent largeImageIntent = new Intent(context, FullImageActivity.class);
                        largeImageIntent.putExtra(Constants.fullImage, bitmapdata);
                        largeImageIntent.putExtra(Constants.media, collegeWallPostsList.get(position).getMedia().split(","));
                        context.startActivity(largeImageIntent);
                    }
                    else {

                        // as no bitmap is present, no image can be displayed
                    }
                }
            });






            // get previous reacted objectIds from db to check if current object id is present
            previousLikedAndDisLikedObjectIds = smartCampusDB.getUserLikesAndDisLikes();

            // if current object Id exists in db, then disable the like and dislike button
            if(previousLikedAndDisLikedObjectIds.containsKey(collegeWallPostsList.get(position).getWallId())){

                // previously liked by current user
                holder.likeViewLayout.setVisibility(View.GONE);
                //disLikeView.setVisibility(View.GONE);
            }
            else {

                // previously not liked by current user
                holder.likeViewLayout.setVisibility(View.VISIBLE);
                //disLikeView.setVisibility(View.VISIBLE);
            }


            // update the like for the post by current user
            // though it is like or dislike jst update the internal db and increase the corresponding like or dislike count
            holder.likeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // get the exact itemView which the user clicked
                    ViewHolder holder1 =  (ViewHolder) itemView.getTag();

                    // change the views accordingly
                    collegeWallPostsList.get(position).setLikes(collegeWallPostsList.get(position).getLikes() + 1);
                    holder1.likeTextView.setText(String.valueOf(collegeWallPostsList.get(position).getLikes()) + " likes");

                    holder1.likeViewLayout.setVisibility(View.GONE);
                    //disLikeView.setVisibility(View.GONE);

                    // play the like sound
                    mediaPlayer.start();

                    // store the object id of the current wall post in internal db
                    smartCampusDB.insertUserLikesAndDisLikes(collegeWallPostsList.get(position).getWallId());

                    String reactionURL = Routes.createWallReaction + Constants.key + "/" + Snippets.getUniqueReactionId() + "/" +
                            collegeWallPostsList.get(position).getWallId() + "/" + collegeWallPostsList.get(position).getUserObjectId();

                    // update the reaction
                    new UpdateReaction().execute(Routes.createWallReaction, Snippets.getUniqueReactionId(), collegeWallPostsList.get(position).getWallId(), collegeWallPostsList.get(position).getUserObjectId());




                }
            });

            // create a new comment for the current post by current user
            holder.commentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Log.v(Constants.appName, "WallId"+collegeWallPostsList.get(position).getWallId());

                    // before passing on to comments activity, increment the comments value by 1
                    // if the value back is false, decrement the comment
                    // else no changes to the increment
                    //commentTextView.setText(String.valueOf(collegeWallPostsList.get(position).getComments()+1) + " comments");

                    // pass the objectId of the current post to new comment layout
                    Intent newCommentIntent = new Intent(context, NewCommentActivity.class);
                    newCommentIntent.putExtra(Constants.wallId, collegeWallPostsList.get(position).getWallId());
                    newCommentIntent.putExtra(Constants.userObjectId, collegeWallPostsList.get(position).getUserObjectId());
                    newCommentIntent.putExtra(Constants.userName, collegeWallPostsList.get(position).getUserName());
                    newCommentIntent.putExtra(Constants.likes, collegeWallPostsList.get(position).getLikes());
                    newCommentIntent.putExtra(Constants.dislikes, collegeWallPostsList.get(position).getDislikes());
                    newCommentIntent.putExtra(Constants.comments, collegeWallPostsList.get(position).getComments());

                    // send position, so that it can relate to which wall post the new comment request came from
                    newCommentIntent.putExtra(Constants.position, position);

                    //context.startActivity(newCommentIntent);
                    ((Activity)context).startActivityForResult(newCommentIntent, 100);

                }
            });

            // navigate to comments screen on click of votes layout
            holder.votesLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Log.v(Constants.appName, "WallId"+collegeWallPostsList.get(position).getWallId());
                    // pass the objectId of the current post to new comment layout
                    Intent newCommentIntent = new Intent(context, NewCommentActivity.class);
                    newCommentIntent.putExtra(Constants.wallId, collegeWallPostsList.get(position).getWallId());
                    newCommentIntent.putExtra(Constants.userObjectId, collegeWallPostsList.get(position).getUserObjectId());
                    newCommentIntent.putExtra(Constants.userName, collegeWallPostsList.get(position).getUserName());
                    newCommentIntent.putExtra(Constants.likes, collegeWallPostsList.get(position).getLikes());
                    newCommentIntent.putExtra(Constants.dislikes, collegeWallPostsList.get(position).getDislikes());
                    newCommentIntent.putExtra(Constants.comments, collegeWallPostsList.get(position).getComments());
                    context.startActivity(newCommentIntent);
                }
            });

            // set post image
        /*if(collegeWallPostsList.get(position).getMediaFile()!=null)
            if(!Arrays.equals(collegeWallPostsList.get(position).getMediaFile(), Constants.null_indicator.getBytes())) {

                // do not show imageView if there are no bytes
                postImage.setVisibility(View.VISIBLE);

                // assign the bitmap
                Bitmap bitmap = decodeSampledBitmapFromResource(collegeWallPostsList.get(position).getMediaFile(), 200, 200);
                postImage.setImageBitmap(bitmap);

            } else {

                // do not show imageView if there are no bytes
                postImage.setVisibility(View.GONE);
            }

        // set user image
        if(collegeWallPostsList.get(position).getUserImage() != null)
            if(!Arrays.equals(collegeWallPostsList.get(position).getUserImage(), Constants.null_indicator.getBytes())) {

                // assign the bitmap
                Bitmap bitmap = decodeSampledBitmapFromResource(collegeWallPostsList.get(position).getUserImage(), 48, 48);
                userImage.setImageBitmap(bitmap);

            } else {

                userImage.setImageResource(R.drawable.ic_user_profile);
            }
*/



      /*


        // update the dislike for the post by current user
        // though it is like or dislike jst update the internal db and increase the corresponding like or dislike count
        disLikeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // play the like sound
                mediaPlayer.start();

                // update the likes for wall post in db
                // create a pointer to an object of class Point with object id
                ParseObject disLikesObject = ParseObject.createWithoutData(Constants.collegeWallTable, collegeWallPostsList.get(position).getObjectId());

                // Increment the current value by 1
                disLikesObject.increment(Constants.dislikes, 1);

                // save
                disLikesObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            // saved successfully

                            // store the object id of the current wall post in internal db
                            smartCampusDB.insertUserLikesAndDisLikes(collegeWallPostsList.get(position).getObjectId());

                            // change the views accordingly
                            collegeWallPostsList.get(position).setDislikes(collegeWallPostsList.get(position).getDislikes() + 1);
                            disLikeTextView.setText(String.valueOf(collegeWallPostsList.get(position).getDislikes()) + " dislikes");

                            likeView.setVisibility(View.GONE);
                            disLikeView.setVisibility(View.GONE);
                        } else {
                            // save failed
                        }
                    }
                });
            }
        });



        votesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // pass the objectId of the current post to new comment layout
                Intent newCommentIntent = new Intent(context, NewCommentActivity.class);
                newCommentIntent.putExtra(Constants.objectId, collegeWallPostsList.get(position).getObjectId());
                newCommentIntent.putExtra(Constants.role, Constants.faculty);
                newCommentIntent.putExtra(Constants.likes, collegeWallPostsList.get(position).getLikes());
                newCommentIntent.putExtra(Constants.dislikes, collegeWallPostsList.get(position).getDislikes());
                newCommentIntent.putExtra(Constants.comments, collegeWallPostsList.get(position).getComments());
                context.startActivity(newCommentIntent);
            }
        });


        // show large image on click of post image
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // build the bundle for sending mediaTable file bytes
                Bundle bundle = new Bundle();
                bundle.putByteArray(Constants.fullImage, collegeWallPostsList.get(position).getMediaFile());

                // pass the bundle for the full screen activity
                Intent fullImageIntent = new Intent(context, FullImageActivity.class);
                fullImageIntent.putExtras(bundle);
                context.startActivity(fullImageIntent);
            }
        });
*/
            // share the content of the post
            holder.shareViewLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get the exact itemView which the user clicked
                    ViewHolder holder1 =  (ViewHolder) itemView.getTag();

                    try {

                        // check if image is present for post
                        // if so, get the bitmap of the image for sharing
                        // else only share the content of the post

                        Bitmap sharePostImageBitmap = null;

                        if(collegeWallPostsList.get(position).getMediaCount() > 0) {

                            // check the permission
                            checkStorageAccess();

                            // fetch the drawable from the imageView and then bitmap from it
                            sharePostImageBitmap = ((BitmapDrawable) holder1.postImage.getDrawable()).getBitmap();
                            /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte[] bitmapdata = stream.toByteArray();
*/

                            /*// First decode with inJustDecodeBounds=true to check dimensions
                            final BitmapFactory.Options options = new BitmapFactory.Options();
                            sharePostImageBitmap = BitmapFactory.decodeByteArray(collegeWallPostsList.get(position).getMediaFile(), 0, collegeWallPostsList.get(position).getMediaFile().length, options);
*/

                            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                            File imageFile = new File(path, "ok" + ".jpeg");
                            FileOutputStream fileOutPutStream = new FileOutputStream(imageFile);
                            sharePostImageBitmap.compress(Bitmap.CompressFormat.PNG, 80, fileOutPutStream);

                            fileOutPutStream.flush();
                            fileOutPutStream.close();


                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + imageFile.getAbsolutePath()));
                            shareIntent.putExtra(Intent.EXTRA_TEXT, collegeWallPostsList.get(position).getPostDescription());
                            shareIntent.setType("*//*");
                            context.startActivity(Intent.createChooser(shareIntent, "Share via.."));

                        } else {

                            // share only content of the post
                            Intent intent2 = new Intent();
                            intent2.setAction(Intent.ACTION_SEND);
                            intent2.setType("text/plain");
                            intent2.putExtra(Intent.EXTRA_TEXT, collegeWallPostsList.get(position).getPostDescription());
                            context.startActivity(Intent.createChooser(intent2, "Share via"));
                        }


                    } catch (Exception ex) {

                        Log.v(Constants.appName + " Error", ex.getMessage());
                    }
                }
            });



            // delete the post from external db
            holder.deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*// delete the post for the given post id
                    ParseObject updateCommentCountObject = ParseObject.createWithoutData(Constants.collegeWallTable, collegeWallPostsList.get(position).getObjectId());
                    updateCommentCountObject.deleteInBackground();
*/
                    // prepare url for hiding post
                    hideURL = Routes.hideWallPost + Constants.key + "/" + collegeWallPostsList.get(position).getWallId();

                    // confirm the hide operation from user
                    confirmationMessage(Routes.hideWallPost, collegeWallPostsList.get(position).getWallId(), position);

                }
            });



            // open the link in the webView activity
            holder.link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // pass the bundle for the the webView activity
                    Intent webViewIntent = new Intent(context, KnowledgeWallWebView.class);
                    webViewIntent.putExtra(Constants.title, collegeWallPostsList.get(position).getLinkTitle());
                    webViewIntent.putExtra(Constants.description, collegeWallPostsList.get(position).getLinkTitle());
                    webViewIntent.putExtra(Constants.link, collegeWallPostsList.get(position).getLinkUrl());
                    webViewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(webViewIntent);
                }
            });


            // insert into db

            // return the itemView




            holder.userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Dialog dialog = new Dialog(context);
                    // Include dialog.xml file
                    dialog.setContentView(R.layout.userimage_maxview);
                    // Set dialog title
                    dialog.setTitle(collegeWallPostsList.get(position).getUserName());

                    // set values for custom dialog components - text, image and button
                /*TextView text = (TextView) dialog.findViewById(R.id.textDialog);
                text.setText("Custom dialog Android example.");*/
                    ImageView image = (ImageView) dialog.findViewById(R.id.maxView);
                    TextView resText = (TextView) dialog.findViewById(R.id.resText);
                    //image.setImageResource(R.drawable.image0);

                    try{

                        if (!collegeWallPostsList.get(position).getUserImage().contentEquals(Constants.null_indicator)) {

                            // get the connection url for the media

                            URL url = new URL(Routes.getMedia + "large_"+ collegeWallPostsList.get(position).getUserImage());
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

                        Picasso.with(context).load(Routes.getMedia+collegeWallPostsList.get(position).getUserImage()).placeholder(R.drawable.ic_user_profile).into(image);


                    }

                    dialog.show();
                }
            });


/*
            // check if the username is as expected to display the user image
            if(collegeWallPostsList.get(position).getCollegeId().contentEquals("12B01A0533") || collegeWallPostsList.get(position).getCollegeId().contentEquals("12B01A0554")
                    || collegeWallPostsList.get(position).getCollegeId().contentEquals("12B01A1266") || collegeWallPostsList.get(position).getCollegeId().contentEquals("12B01A1269")
                    || collegeWallPostsList.get(position).getCollegeId().contentEquals("12B01A1273") || collegeWallPostsList.get(position).getCollegeId().contentEquals("12B01A1295")) {

                if (collegeWallPostsList.get(position).getCollegeId().contentEquals("12B01A0533")) {

                    userImage.setImageResource(R.drawable.s12b01a0533);
                }


                // check if the username is as expected to display the user image
                if (collegeWallPostsList.get(position).getCollegeId().contentEquals("12B01A0554")) {

                    userImage.setImageResource(R.drawable.s12b01a0554);
                }


                // check if the username is as expected to display the user image
                if (collegeWallPostsList.get(position).getCollegeId().contentEquals("12B01A1266")) {

                    userImage.setImageResource(R.drawable.s12b01a1266);
                }

                // check if the username is as expected to display the user image
                if (collegeWallPostsList.get(position).getCollegeId().contentEquals("12B01A1269")) {

                    userImage.setImageResource(R.drawable.s12b01a1269);
                }


                // check if the username is as expected to display the user image
                if (collegeWallPostsList.get(position).getCollegeId().contentEquals("12B01A1273")) {

                    userImage.setImageResource(R.drawable.s12b01a1273);
                }


                // check if the username is as expected to display the user image
                if (collegeWallPostsList.get(position).getCollegeId().contentEquals("12B01A1295")) {

                    userImage.setImageResource(R.drawable.s12b01a1295);
                }

            }else {
                // do nothing
                userImage.setImageResource(R.drawable.ic_user_profile);
            }*/


        }
        catch (Exception e){

        }

        return itemView;
    }


    /**
     * This method will confirm user from updating the details to server
     */
    public void confirmationMessage(String url1, String wallId1, int position1) {

        final String url = url1;
        final String wallId = wallId1;
        final int position = position1;

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                switch (which) {

                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked

                        // removing post from the list
                        collegeWallPostsList.remove(position);
                        notifyDataSetChanged();

                        // request fro hiding wall post
                        new HideWallPost().execute(url, wallId);

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked // do nothing
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure to remove this post from this wall?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MyAdapter", "onActivityResult");
    }


    public void checkStorageAccess(){


        // this device has a camera
        // check if this device has SDK less than or equal to Marshmallow
        if(Build.VERSION.SDK_INT >= 23){

            // check if this app is granted with camera access permission
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // The permission is NOT already granted.
                // Check if the user has been asked about this permission already and denied
                // it. If so, we want to give more explanation about why the permission is needed.
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Show our own UI to explain to the user why we need to read the contacts
                    // before actually requesting the permission and showing the default UI
                }

                // Fire off an async request to actually get the permission
                // This will show the standard permission request dialog UI
                ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);


            }

        }


    }

    /**
     * update the adapter list items and notify
     */
    public void updateItems(List<Wall> postsList) {
        this.collegeWallPostsList = postsList;
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

    /**
     * This background task will update the reaction
     * for the respective wallId
     */
    private class UpdateReaction extends AsyncTask<String, Void, Void> {

        private String Content = "";
        private String Error = null;
        String data = "";

        @Override
        protected void onPreExecute() {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
        }

        @Override
        protected Void doInBackground(String... urls) {


            /************ Make Post Call To Web Server ***********/
            BufferedReader reader = null;

            // Send data
            try {


                // Set Request parameter
                data += "?&" + URLEncoder.encode(Constants.KEY, "UTF-8") + "=" + Constants.key
                        + "&" + URLEncoder.encode(Constants.reactionId, "UTF-8") + "=" + (urls[1])
                        + "&" + URLEncoder.encode(Constants.wallId, "UTF-8") + "=" + (urls[2])
                        + "&" + URLEncoder.encode(Constants.userObjectId, "UTF-8") + "=" + (urls[3]);


                Log.v(Constants.appName, urls[0] + data);

                // Defined URL  where to send data
                URL url = new URL(urls[0] + data);

                // Send POST data request
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                //conn.setDoInput(true);
                //OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                //wr.write(data);
                //wr.flush();

                // Get the server response
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + " ");
                }

                // Append Server Response To Content String
                Content = sb.toString();

                // close the reader
                //reader.close();

            } catch (Exception ex) {

                ex.printStackTrace();
                Error = ex.getMessage();


            } finally {

                try {

                    reader.close();

                } catch (Exception ex) {
                    Error = ex.getMessage();
                }
            }

            return null;
        }

        protected void onPostExecute(Void unused) {

            // clear the dialog
            //progressDialog.dismiss();

            if (Error != null) {

                Log.i("Connection", Error);

            } else {

                //Log.i("Connection", Content);
                /****************** Start Parse Response JSON Data *************/


                try {

                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(Content);


                    /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                    status = jsonResponse.getInt(Constants.status);

                    // check if mediaCount > 0
                    // if so, get the unique image names
                    if(status == 1){

                        // successfully posted
                    }


                } catch (JSONException e) {

                    e.printStackTrace();

                }

            }
        }
    }

    /**
     * This background task will hide the post
     * for the respective wallId
     */
    private class HideWallPost extends AsyncTask<String, Void, Void> {

        private String Content = "";
        private String Error = null;
        String data = "";

        @Override
        protected void onPreExecute() {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
        }

        @Override
        protected Void doInBackground(String... urls) {


            /************ Make Post Call To Web Server ***********/
            BufferedReader reader = null;

            // Send data
            try {

                // Set Request parameter
                data += "?&" + URLEncoder.encode(Constants.KEY, "UTF-8") + "=" + Constants.key
                        + "&" + URLEncoder.encode(Constants.wallId, "UTF-8") + "=" + (urls[1]);

                //Log.v(Constants.appName, urls[0]+data);

                // Defined URL  where to send data
                URL url = new URL(urls[0]+data);

                // Send POST data request
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                //conn.setDoInput(true);
                //OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                //wr.write(data);
                //wr.flush();

                // Get the server response
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + " ");
                }

                // Append Server Response To Content String
                Content = sb.toString();

                // close the reader
                //reader.close();

            } catch (Exception ex) {

                ex.printStackTrace();
                Error = ex.getMessage();


            } finally {

                try {

                    reader.close();

                } catch (Exception ex) {
                    Error = ex.getMessage();
                }
            }

            return null;
        }

        protected void onPostExecute(Void unused) {

            // clear the dialog
            //progressDialog.dismiss();

            if (Error != null) {

                Log.i("Connection", Error);

            } else {

                //Log.i("Connection", Content);
                /****************** Start Parse Response JSON Data *************/


                try {

                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(Content);


                    /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                    status = jsonResponse.getInt(Constants.status);

                    // check if mediaCount > 0
                    // if so, get the unique image names
                    if(status == 1){

                        // successfully posted
                    }


                } catch (JSONException e) {

                    e.printStackTrace();

                }

            }
        }
    }


    private class FetchMedia extends AsyncTask<String, Void, Void> {

        private String Error = null;

        ImageView imageView;
        Bitmap bitmap;

        public FetchMedia(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected void onPreExecute() {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
        }

        @Override
        protected Void doInBackground(String... urls) {


            /************ Make Post Call To Web Server ***********/
            BufferedReader reader = null;

            // Send data
            try {

                // Defined URL  where to send data
                URL url = new URL(urls[0]+urls[1]);

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
                    bitmap = BitmapFactory.decodeStream(is);


                }
                //postImage.setVisibility(View.VISIBLE);
                //postImage.setImageBitmap(bitmap);



            } catch (Exception ex) {

                ex.printStackTrace();
                Error = ex.getMessage();


            } finally {

                try {


                } catch (Exception ex) {
                    Error = ex.getMessage();
                }
            }

            return null;
        }

        protected void onPostExecute(Void unused) {

            // clear the dialog
            //progressDialog.dismiss();

            if (Error != null) {

                Log.i("Connection", Error);

            } else {


                //Log.v("Image", "Checking8");
                if (bitmap != null) {
                  //  Log.v("Image", "Checking9");
                    imageView.setImageBitmap(bitmap);
                } else {
                    //Log.v("Image", "Checking10");
                    Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.emptyimg1);
                    imageView.setImageDrawable(placeholder);
                }

            }
        }
    }


}
