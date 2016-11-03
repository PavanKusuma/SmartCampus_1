package adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import internaldb.SmartCampusDB;
import model.Messages;
import svecw.smartcampus.FullImageActivity;
import svecw.smartcampus.R;
import utils.Constants;
import utils.Routes;

/**
 * Created by Pavan_Kusuma on 4/17/2015.
 */
public class MessagesAdapter extends BaseAdapter {

    // context of present class
    Context context;

    // usernames and likelist
    ArrayList<Messages> messagesList;
    //ArrayList<Boolean> likeList;

    // layout Inflator
    LayoutInflater layoutInflater;

    SmartCampusDB smartCampusDB;

    // constructor
    public MessagesAdapter(Context context) {

        this.context = context;

        // initialize variables
        messagesList = new ArrayList<Messages>();
        //likeList = new ArrayList<Boolean>();

        // layoutInflator object
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        smartCampusDB = new SmartCampusDB(context);
    }

    @Override
    public int getCount() {
        return messagesList.size();
    }

    @Override
    public Object getItem(int position) {
        return messagesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        RelativeLayout itemView = null;
        if (convertView == null) {

            // show the in/out message variation in the single message layout by aligning it
            // if message is aligned to Right, its a sent message
            // if message is aligned to Left, then its a received message

            // differentiate layout to left and right
            // if current user object id is equal to fromUserObjectId, then its sent message
            if(messagesList.get(position).getFromUserObjectId().contentEquals(smartCampusDB.getUser().get(Constants.userObjectId).toString())) {

                //Log.v(Constants.appName, "Sent message");
                itemView = (RelativeLayout) layoutInflater.inflate(R.layout.messages_single_listitem_right, parent, false);

                AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
               // params.setMargins(50, 0, 0, 0);
                itemView.setLayoutParams(params);
                //itemView1.setLayoutParams(params);
            }
            else if(!messagesList.get(position).getFromUserObjectId().contentEquals(smartCampusDB.getUser().get(Constants.userObjectId).toString())) {

                //Log.v(Constants.appName, "received message");
                itemView = (RelativeLayout) layoutInflater.inflate(R.layout.messages_single_listitem_left, parent, false);

                AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
                //params.setMargins(0, 0, 50, 0);
                itemView.setLayoutParams(params);
                //itemView1.setLayoutParams(params);
            }

            //itemView = (RelativeLayout) layoutInflater.inflate(R.layout.messages_single_listitem_left, parent, false);

        } else {
            itemView = (RelativeLayout) convertView;
        }

        try {

            Typeface sansFont = Typeface.createFromAsset(context.getResources().getAssets(), Constants.fontName);

            // date format for displaying created date
            // provide date format present in server
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            // get all views of single shouts list item
            TextView userName = (TextView) itemView.findViewById(R.id.messagesUserName);
            TextView message = (TextView) itemView.findViewById(R.id.messagesMessage);
            TextView createdAt = (TextView) itemView.findViewById(R.id.messagescreatedAt);
            final ImageView messageImage = (ImageView) itemView.findViewById(R.id.messageImage);

            userName.setTypeface(sansFont);
            message.setTypeface(sansFont);
            createdAt.setTypeface(sansFont);
            // as messages are displayed accordingly to the userType
            // when the user is "student" then display the messages with reference to the user who sent it
            // when the user is other than "student" like "faculty or admin" then display the messages with reference to the year, branch, semester

            // set the username of the user
            if (smartCampusDB.getUser().get(Constants.role).toString().contentEquals(Constants.student)) {

                userName.setText(messagesList.get(position).getUsername() + " (" + messagesList.get(position).getDepartment() + " " + Constants.faculty + ")");

            } else {

                if (messagesList.get(position).getYear() == 0) {

                    userName.setText(messagesList.get(position).getUsername() + " (" + messagesList.get(position).getDepartment() + " " + Constants.faculty + ")");
                }
                if (messagesList.get(position).getYear() != 0) {

                    userName.setText(messagesList.get(position).getUsername() + " : " + messagesList.get(position).getDepartment());
                }
            }

            // get the date format and convert it into required format to display
            java.util.Date date = simpleDateFormat.parse(messagesList.get(position).getCreatedAt());
            simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm aa");

            message.setText(messagesList.get(position).getMessage());
            //createdAt.setText(simpleDateFormat.format(messagesList.get(position).getCreatedAt()));
            createdAt.setText(simpleDateFormat.format(date));

            // check the media count for the message for displaying image
            if(messagesList.get(position).getMediaCount() > 0) {

                // make image visible at first place
                messageImage.setVisibility(View.VISIBLE);

                // if media exists for the post
                // fetch the names String of media
                String mediaName = messagesList.get(position).getMedia();

                Log.v(Constants.appName, "Adapter Checking"+mediaName);
                // get individual media names
                String[] mediaNames = mediaName.split(",");

                // iterate through number of media items and display accordingly
                for (int i = 0; i < messagesList.get(position).getMediaCount(); i++) {

                    // load using picasso lib
                    Picasso.with(context).load(Routes.getMedia+mediaNames[i]).placeholder(R.drawable.load_img).into(messageImage);

/*                   Log.v(Constants.appName, mediaNames[i]);
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
                        //return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);*//*
                        Bitmap bitmap = BitmapFactory.decodeStream(is);

                        messageImage.setVisibility(View.VISIBLE);
                        messageImage.setImageBitmap(bitmap);
                    }*/
                }


            }

            else{

                messageImage.setVisibility(View.GONE);
            }

            messageImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bitmap bitmap = ((BitmapDrawable)messageImage.getDrawable()).getBitmap();

                    if(bitmap != null) {

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] bitmapdata = stream.toByteArray();

                        // call the full image activity
                        Intent largeImageIntent = new Intent(context, FullImageActivity.class);
                        largeImageIntent.putExtra(Constants.fullImage, bitmapdata);
                        largeImageIntent.putExtra(Constants.media, messagesList.get(position).getMedia().split(","));
                        context.startActivity(largeImageIntent);
                    }
                    else {

                        // as no bitmap is present, no image can be displayed
                    }
                }
            });

        }
        catch(Exception e){


        }
        return itemView;
    }

    /**
     * update the adapter list items and notify
     */
    public void updateItems(ArrayList<Messages> postsList) {
        this.messagesList = postsList;
        //likeList = like1List;
        notifyDataSetChanged();
    }
}
