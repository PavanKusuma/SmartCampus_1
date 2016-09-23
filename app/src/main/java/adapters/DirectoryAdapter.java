package adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import svecw.smartcampus.R;
import utils.Constants;

/**
 * Created by Pavan_Kusuma on 4/18/2015.
 */
public class DirectoryAdapter extends BaseAdapter {

    Context context;

    LayoutInflater layoutInflater;

    // lists
    List<String> usernamesList, contactsList, designationsList, departmentsList;

    public DirectoryAdapter(Context context) {
        this.context = context;

        usernamesList = new ArrayList<String>();
        contactsList = new ArrayList<String>();
        designationsList = new ArrayList<String>();
        departmentsList = new ArrayList<String>();

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RelativeLayout itemView;
        if (convertView == null) {
            itemView = (RelativeLayout) layoutInflater.inflate(R.layout.directory_single_listitem, parent, false);

        } else {
            itemView = (RelativeLayout) convertView;
        }

        final ImageView facultyUserImageView = (ImageView) itemView.findViewById(R.id.facultyUserImageView);
        TextView userNameTextView = (TextView) itemView.findViewById(R.id.userName);
        TextView designationTextView = (TextView) itemView.findViewById(R.id.designation);
        TextView departmentTextView = (TextView) itemView.findViewById(R.id.department);
        final ImageView callView = (ImageView) itemView.findViewById(R.id.ic_call);
        final ImageView msgView = (ImageView) itemView.findViewById(R.id.ic_msg);

        Typeface sansFont = Typeface.createFromAsset(context.getResources().getAssets(), Constants.fontName);

                userNameTextView.setText(usernamesList.get(position)); userNameTextView.setTypeface(sansFont);
                designationTextView.setText(designationsList.get(position)); designationTextView.setTypeface(sansFont);
                departmentTextView.setText(departmentsList.get(position)); departmentTextView.setTypeface(sansFont);
                callView.setTag(contactsList.get(position));
                msgView.setTag(contactsList.get(position));


        if(usernamesList.get(position).contentEquals("P.VENKATA RAMA RAJU") || usernamesList.get(position).contentEquals("DR. P.SRINIVASA RAJU (Vice-Principal)")
                || usernamesList.get(position).contentEquals("DR.G.SRINIVASA RAO(Principal)") || usernamesList.get(position).contentEquals("S. SREENIVASU")
                || usernamesList.get(position).contentEquals("S. ADINARAYANA") || usernamesList.get(position).contentEquals("P.SYAMALA RAO")
                || usernamesList.get(position).contentEquals("D.V.NAGA RAJU") || usernamesList.get(position).contentEquals("G.RATNAKANTH")
                || usernamesList.get(position).contentEquals("Dr.D.DHARMAIAH") || usernamesList.get(position).contentEquals("Dr.P.S.V.SRINIVASA RAO")) {

            // check if the username is as expected to display the user image
            if (usernamesList.get(position).contentEquals("P.VENKATA RAMA RAJU")) {

                facultyUserImageView.setImageResource(R.drawable.f1201);
            }


            // check if the username is as expected to display the user image
            if (usernamesList.get(position).contentEquals("DR. P.SRINIVASA RAJU (Vice-Principal)")) {

                facultyUserImageView.setImageResource(R.drawable.f301);
            }


            // check if the username is as expected to display the user image
            if (usernamesList.get(position).contentEquals("DR.G.SRINIVASA RAO(Principal)")) {

                facultyUserImageView.setImageResource(R.drawable.f801);
            }


            // check if the username is as expected to display the user image
            if (usernamesList.get(position).contentEquals("S. SREENIVASU")) {

                facultyUserImageView.setImageResource(R.drawable.f1202);
            }


            // check if the username is as expected to display the user image
            if (usernamesList.get(position).contentEquals("S. ADINARAYANA")) {

                facultyUserImageView.setImageResource(R.drawable.f1203);
            }


            // check if the username is as expected to display the user image
            if (usernamesList.get(position).contentEquals("P.SYAMALA RAO")) {

                facultyUserImageView.setImageResource(R.drawable.f1204);
            }


            // check if the username is as expected to display the user image
            if (usernamesList.get(position).contentEquals("D.V.NAGA RAJU")) {

                facultyUserImageView.setImageResource(R.drawable.f1205);
            }


            // check if the username is as expected to display the user image
            if (usernamesList.get(position).contentEquals("G.RATNAKANTH")) {

                facultyUserImageView.setImageResource(R.drawable.f1207);
            }


            // check if the username is as expected to display the user image
            if (usernamesList.get(position).contentEquals("Dr.D.DHARMAIAH")) {

                facultyUserImageView.setImageResource(R.drawable.f1229);
            }


            // check if the username is as expected to display the user image
            if (usernamesList.get(position).contentEquals("Dr.P.S.V.SRINIVASA RAO")) {

                facultyUserImageView.setImageResource(R.drawable.f1233);
            }

        }else {
            // do nothing
            facultyUserImageView.setImageResource(R.drawable.ic_user_profile);
        }



        callView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check whether phoneNumber is present
                if(callView.getTag().toString().contentEquals(Constants.null_indicator)){

                    Toast.makeText(context.getApplicationContext(), "Number not available", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + callView.getTag().toString()));

                    if(checkPermission(context))
                        context.startActivity(callIntent);
                    else
                        Toast.makeText(context.getApplicationContext(), "Avail CALL_PHONE permission for this app", Toast.LENGTH_SHORT).show();

                }
            }
        });


        msgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // here user should be navigated to send message from his own phone instead of using gateway
                /*Intent msgIntent = new Intent(context, SendMessageActivity.class);
                msgIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                msgIntent.putExtra(Constants.phoneNumber, msgView.getTag().toString());
                msgIntent.putExtra(Constants.msgType, Constants.msgTypePhone);
                context.startActivity(msgIntent);
*/

                // currently sending message using phone

                // add the phone number in the data
                Uri uri = Uri.parse("smsto:" + msgView.getTag().toString());

                Intent smsSIntent = new Intent(Intent.ACTION_SENDTO, uri);
                // add the message at the sms_body extra field
                //smsSIntent.putExtra("sms_body", msg);
                try{
                    smsSIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(smsSIntent);
                } catch (Exception ex) {
                    Toast.makeText(context, "Your sms has failed...", Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
            }
        });

        return itemView;
    }

    public static boolean checkPermission(final Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
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
    public void updateItems(List<String> usernamesList,List<String> contactsList,List<String> designationsList,List<String> departmentsList) {
        this.usernamesList.addAll(usernamesList);
        this.contactsList.addAll(contactsList);
        this.departmentsList.addAll(departmentsList);
        this.designationsList.addAll(designationsList);

        //likeList = like1List;
        notifyDataSetChanged();
    }

    /**
     * update the adapter list with new items and notify
     */
    public void updateNewItems(List<String> usernamesList,List<String> contactsList,List<String> designationsList,List<String> departmentsList) {
        this.usernamesList = usernamesList;
        this.contactsList = contactsList;
        this.departmentsList = departmentsList;
        this.designationsList = designationsList;

        //likeList = like1List;
        notifyDataSetChanged();
    }



}
