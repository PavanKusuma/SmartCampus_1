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

import model.MsgGroup;
import svecw.smartcampus.R;
import utils.Constants;

/**
 * Created by Pavan_Kusuma on 4/17/2015.
 */
public class MsgGroupDetailsAdapter extends BaseAdapter {

    // context of present class
    Context context;

    // usernames and likelist
    ArrayList<MsgGroup> messagesList;
    //ArrayList<Boolean> likeList;

    // layout Inflator
    LayoutInflater layoutInflater;

    // constructor
    public MsgGroupDetailsAdapter(Context context) {

        this.context = context;

        // intialize variables
        messagesList = new ArrayList<MsgGroup>();
        //likeList = new ArrayList<Boolean>();

        // layoutInflator object
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        RelativeLayout itemView;
        if (convertView == null) {
            itemView = (RelativeLayout) layoutInflater.inflate(R.layout.directory_single_listitem_1, parent, false);

        } else {
            itemView = (RelativeLayout) convertView;
        }

        TextView userNameTextView = (TextView) itemView.findViewById(R.id.userName);
        TextView designationTextView = (TextView) itemView.findViewById(R.id.designation);
        TextView departmentTextView = (TextView) itemView.findViewById(R.id.department);
        final ImageView callView = (ImageView) itemView.findViewById(R.id.ic_call);
        final ImageView msgView = (ImageView) itemView.findViewById(R.id.ic_msg);

        Typeface sansFont = Typeface.createFromAsset(context.getResources().getAssets(), Constants.fontName);

        // set the details of the user
        userNameTextView.setText(messagesList.get(position).getUsername()); userNameTextView.setTypeface(sansFont);
        designationTextView.setText(messagesList.get(position).getRole()); designationTextView.setTypeface(sansFont);
        designationTextView.setVisibility(View.GONE);
        departmentTextView.setVisibility(View.GONE);
        callView.setTag(messagesList.get(position).getPhoneNumber());
        //msgView.setTag(messagesList.get(position).getPhoneNumber());
        msgView.setVisibility(View.GONE);
// presently only call feature is being exposed

        callView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check if the phoneNumber exists before dialing the numbser
                if(callView.getTag().toString().contentEquals(Constants.null_indicator)){

                    // number doesnot exists
                    Toast.makeText(context.getApplicationContext(), "Number not available", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + callView.getTag().toString()));
                    context.startActivity(callIntent);
                }
            }
        });

     /*   msgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

*//*                Intent msgIntent = new Intent(context, SendMessageActivity.class);
                msgIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                msgIntent.putExtra(Constants.phoneNumber, msgView.getTag().toString());
                msgIntent.putExtra(Constants.msgType, Constants.msgTypePhone);
                context.startActivity(msgIntent);*//*


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
        });*/

        return itemView;
    }

    /**
     * update the adapter list items and notify
     */
    public void updateItems(ArrayList<MsgGroup> postsList) {
        this.messagesList = postsList;
        //likeList = like1List;
        notifyDataSetChanged();
    }
}
