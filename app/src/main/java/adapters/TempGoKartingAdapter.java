package adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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
public class TempGoKartingAdapter extends BaseAdapter {

    Context context;

    LayoutInflater layoutInflater;

    // lists
    List<String> usernamesList, contactsList, regnoList;

    public TempGoKartingAdapter(Context context) {
        this.context = context;

        usernamesList = new ArrayList<String>();
        contactsList = new ArrayList<String>();
        regnoList = new ArrayList<String>();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RelativeLayout itemView;
        if (convertView == null) {
            itemView = (RelativeLayout) layoutInflater.inflate(R.layout.directory_single_listitem_2, parent, false);

        } else {
            itemView = (RelativeLayout) convertView;
        }

        Typeface sansFont = Typeface.createFromAsset(context.getResources().getAssets(), Constants.fontName);

        ImageView facultyUserImageView = (ImageView) itemView.findViewById(R.id.facultyUserImageView);
        TextView userNameTextView = (TextView) itemView.findViewById(R.id.userName); userNameTextView.setTypeface(sansFont);
        TextView designationTextView = (TextView) itemView.findViewById(R.id.designation); designationTextView.setTypeface(sansFont);
        TextView departmentTextView = (TextView) itemView.findViewById(R.id.department); departmentTextView.setTypeface(sansFont);
        final ImageView callView = (ImageView) itemView.findViewById(R.id.ic_call);
        final ImageView msgView = (ImageView) itemView.findViewById(R.id.ic_msg);

                userNameTextView.setText(usernamesList.get(position));
                designationTextView.setVisibility(View.GONE);
        if(regnoList.size() > 0) {
            departmentTextView.setText(regnoList.get(position));
        }
        else {
            departmentTextView.setVisibility(View.GONE);
        }
                callView.setTag(contactsList.get(position));
                msgView.setTag(contactsList.get(position));
                //msgView.setVisibility(View.GONE);





        if(usernamesList.get(position).contentEquals("Mr.P.V.Rama Raju [IT]") || usernamesList.get(position).contentEquals("Dr.P.Srinivasa Raju [ME]")
                || usernamesList.get(position).contentEquals("DR.G.SRINIVASA RAO(Principal)") || usernamesList.get(position).contentEquals("S. SREENIVASU")
                || usernamesList.get(position).contentEquals("S. ADINARAYANA") || usernamesList.get(position).contentEquals("P.SYAMALA RAO")
                || usernamesList.get(position).contentEquals("D.V.NAGA RAJU") || usernamesList.get(position).contentEquals("G.RATNAKANTH")
                || usernamesList.get(position).contentEquals("Dr.D.DHARMAIAH") || usernamesList.get(position).contentEquals("Dr.P.S.V.SRINIVASA RAO")) {

            // check if the username is as expected to display the user image
            if (usernamesList.get(position).contentEquals("Mr.P.V.Rama Raju [IT]")) {

                facultyUserImageView.setImageResource(R.drawable.f1201);
            }


            // check if the username is as expected to display the user image
            if (usernamesList.get(position).contentEquals("Dr.P.Srinivasa Raju [ME]")) {

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
                    context.startActivity(callIntent);
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
    public void updateItems(List<String> usernamesList,List<String> contactsList, List<String> regnoList) {
        this.usernamesList.addAll(usernamesList);
        this.contactsList.addAll(contactsList);
        this.regnoList.addAll(regnoList);

        //likeList = like1List;
        notifyDataSetChanged();
    }

    /**
     * update the adapter list with new items and notify
     */
    public void updateNewItems(List<String> usernamesList,List<String> contactsList) {
        this.usernamesList = usernamesList;
        this.contactsList = contactsList;

        //likeList = like1List;
        notifyDataSetChanged();
    }



}
