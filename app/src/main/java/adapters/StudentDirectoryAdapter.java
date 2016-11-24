package adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import model.User;
import svecw.smartcampus.R;
import utils.Constants;
import utils.ImageManager;
import utils.Routes;

/**
 * Created by Pavan on 5/21/15.
 */
public class StudentDirectoryAdapter extends BaseAdapter {

    Context context;

    LayoutInflater layoutInflater;

    // lists
    List<User> usernamesList;

    ViewHolder holder;
    public ImageManager imageManager;

    public StudentDirectoryAdapter(Context context) {
        this.context = context;

        usernamesList = new ArrayList<User>();

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        imageManager =
                new ImageManager(context.getApplicationContext(), 1024);

    }

    // create holder class to contain xml file elements
    private class ViewHolder {

        ImageView studentUserImageView;
        TextView userName;
        TextView branch;
        TextView collegeId;
        TextView phoneNumber;
        ImageView call;
        ImageView msg;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        RelativeLayout itemView;

        if (convertView == null) {

            // inflate single list item for each row
            itemView = (RelativeLayout) layoutInflater.inflate(R.layout.directory_single_listitem_student, parent, false);

            Typeface sansFont = Typeface.createFromAsset(context.getResources().getAssets(), Constants.fontName);

            // view holder object to contain xml file elements
            holder = new ViewHolder();
            holder.studentUserImageView = (ImageView) itemView.findViewById(R.id.studentUserImageView);
            holder.userName = (TextView) itemView.findViewById(R.id.studentUserName); holder.userName.setTypeface(sansFont);
            holder.branch = (TextView) itemView.findViewById(R.id.studentBranch); holder.branch.setTypeface(sansFont);
            holder.collegeId = (TextView) itemView.findViewById(R.id.studentCollegeId); holder.collegeId.setTypeface(sansFont);
            //holder.phoneNumber = (TextView) itemView.findViewById(R.id.studentPhoneNumber);

            holder.call = (ImageView) itemView.findViewById(R.id.ic_studentCall);
            holder.msg = (ImageView) itemView.findViewById(R.id.ic_studentMsg);

            // set holder with layout inflater
            itemView.setTag(holder);



        } else {
            itemView = (RelativeLayout) convertView;

            holder = (ViewHolder) convertView.getTag();
        }



        try {
            //Log.v(Constants.appName, "List size : " + usernamesList.size());
            if (usernamesList.size() > 0) {
                //Log.v(Constants.appName, "List size : " + usernamesList.size()+position);
                //User user = usernamesList.get(position);

                Log.v(Constants.appName, "List size : " + usernamesList.size()+position);
                {
                    Log.v(Constants.appName, "List size : " + usernamesList.size()+position);
                    if (usernamesList.get(position).getPhoneNumber().contentEquals(Constants.null_indicator)) {
                        Log.v(Constants.appName, "List size : " + usernamesList.size()+position);
                        holder.call.setImageResource(R.drawable.ic_no_call);
                    } else {
                        holder.call.setImageResource(R.drawable.ic_call);
                    }
                }




                {
                    if (usernamesList.get(position).getPhoneNumber().contentEquals(Constants.null_indicator)) {
                        holder.msg.setImageResource(R.drawable.ic_no_message);
                    } else {
                        holder.msg.setImageResource(R.drawable.ic_message);
                    }
                }


                //holder.call.setTag(usernamesList.get(position).getPhoneNumber());
                //holder.msg.setTag(usernamesList.get(position).getPhoneNumber());
                // userImage
                // check if userImage exists for the post
                // if so, fetch and display the userImage
               /* if (usernamesList.get(position).getMediaCount() != 0) {
Log.v(Constants.appName, "image : " +usernamesList.get(position).getMedia());
                    // get the connection url for the media
                    URL url = new URL(Routes.getMedia + user.getMedia());
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
                        BitmapFactory.decodeStream(is, null, options);

                        // Calculate inSampleSize
                        options.inSampleSize = calculateInSampleSize(options, 200, 200);

                        // Decode bitmap with inSampleSize set
                        options.inJustDecodeBounds = false;
                        //return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);

                        holder.studentUserImageView.setImageBitmap(bitmap);
                    }
                } else {
                    Log.v(Constants.appName, "image : " +usernamesList.get(position).getMedia());
                    // set the userImage
                    holder.studentUserImageView.setImageResource(R.drawable.ic_user_profile);

                }*/




                // userImage
                // check if userImage exists for the post
                // if so, fetch and display the userImage
                if(!usernamesList.get(position).getMedia().contentEquals(Constants.null_indicator)) {
                    Log.v(Constants.appName, Routes.getMedia + usernamesList.get(position).getMedia());

                    // check if the user image contains the image name
                    // if so fetch the image from url and display
                    // else fetch the image from local and display as it is just posted by current user
                    if (usernamesList.get(position).getMedia().contains(".")) {

                        // get the connection url for the media
                        /*URL url = new URL(Routes.getMedia + usernamesList.get(position).getMedia());
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

                            holder.studentUserImageView.setImageBitmap(bitmap);
                        }
                        else{

                            // hide user image layout
                            holder.studentUserImageView.setImageResource(R.drawable.ic_user_profile);
                        }*/

                        Picasso.with(context).load(Routes.getMedia+usernamesList.get(position).getMedia()).placeholder(R.drawable.ic_user_profile).into(holder.studentUserImageView);

                    } else {


                        if(!usernamesList.get(position).getMedia().equals("-")){

                            byte[] b = Base64.decode(usernamesList.get(position).getMedia(), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

                            //RoundImage roundedImage = new RoundImage(bitmap, 220, 220);
                            //RoundImage roundedImage1 = new RoundImage(roundedImage.getBitmap(), bitmap.getWidth(), bitmap.getHeight());
                            //holder.userImage.setImageDrawable(roundedImage1);

                            //userImageLayout.setVisibility(View.VISIBLE);
                            holder.studentUserImageView.setImageBitmap(bitmap);

                        }
                        else{

                            // hide user image layout
                            holder.studentUserImageView.setImageResource(R.drawable.ic_user_profile);
                        }
                    }



                }
                else {

                    // set the userImage
                    holder.studentUserImageView.setImageResource(R.drawable.ic_user_profile);

                }


            }
        }
        catch (Exception e){


        }


        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check whether phoneNumber is present
                if(usernamesList.get(position).getPhoneNumber().contentEquals(Constants.null_indicator)){

                    Toast.makeText(context.getApplicationContext(), "Number not available", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context.getApplicationContext(), "Preparing to call", Toast.LENGTH_SHORT).show();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + usernamesList.get(position).getPhoneNumber()));
                    context.startActivity(callIntent);
                }
            }
        });

        holder.msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.v(Constants.appName, "position : " + position);
                // currently sending message using phone

                // if(holder.msg.getTag() != null)
                // check whether phoneNumber is present
                if(usernamesList.get(position).getPhoneNumber().contentEquals(Constants.null_indicator)){

                    Toast.makeText(context.getApplicationContext(), "Number not available", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context.getApplicationContext(), "Preparing to message", Toast.LENGTH_SHORT).show();
                    // add the phone number in the data
                    Uri uri = Uri.parse("smsto:" + usernamesList.get(position).getPhoneNumber());

                    Intent smsSIntent = new Intent(Intent.ACTION_SENDTO, uri);
                    // add the message at the sms_body extra field
                    //smsSIntent.putExtra("sms_body", msg);
                    try {
                        smsSIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(smsSIntent);
                    } catch (Exception ex) {
                        Toast.makeText(context, "Your sms has failed...", Toast.LENGTH_LONG).show();
                        ex.printStackTrace();
                    }
                }
            }
        });

        holder.studentUserImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(context);
                // Include dialog.xml file
                dialog.setContentView(R.layout.userimage_maxview);
                // Set dialog title
                dialog.setTitle(usernamesList.get(position).getUserName());

                // set values for custom dialog components - text, image and button
                /*TextView text = (TextView) dialog.findViewById(R.id.textDialog);
                text.setText("Custom dialog Android example.");*/
                ImageView image = (ImageView) dialog.findViewById(R.id.maxView);
                TextView resText = (TextView) dialog.findViewById(R.id.resText);
                //image.setImageResource(R.drawable.image0);

                try{

                    if (usernamesList.get(position).getMediaCount() > 0) {

                        // get the connection url for the media
                        URL url = new URL(Routes.getMedia + "profile_"+ usernamesList.get(position).getMedia());
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

                    Picasso.with(context).load(Routes.getMedia+usernamesList.get(position).getMedia()).placeholder(R.drawable.ic_user_profile).into(image);
                }

                dialog.show();
            }
        });

        Log.v(Constants.appName, "Phone : " +usernamesList.get(position).getPhoneNumber());
        holder.userName.setText(usernamesList.get(position).getUserName());
        holder.branch.setText(usernamesList.get(position).getBranch());
        holder.collegeId.setText(usernamesList.get(position).getCollegeId());
        //holder.phoneNumber.setText(usernamesList.get(position).getPhoneNumber());
        return itemView;
    }

    @Override
    public int getCount() {
        return usernamesList.size();
    }

    @Override
    public Object getItem(int position) {
        return usernamesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * update the adapter list items and notify
     */
    public void updateItems(List<User> usernamesList) {
        this.usernamesList = usernamesList;

        notifyDataSetChanged();
    }


}
