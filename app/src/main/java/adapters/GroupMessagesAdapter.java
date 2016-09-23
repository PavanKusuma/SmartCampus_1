package adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import model.MsgGroup;
import svecw.smartcampus.MessageGroupMsgsActivity;
import svecw.smartcampus.R;
import utils.Constants;

/**
 * Created by Pavan_Kusuma on 4/17/2015.
 */
public class GroupMessagesAdapter extends BaseAdapter {

    // context of present class
    Context context;

    // usernames and likelist
    ArrayList<MsgGroup> messagesList;
    //ArrayList<Boolean> likeList;

    // layout Inflator
    LayoutInflater layoutInflater;

    // constructor
    public GroupMessagesAdapter(Context context) {

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
            itemView = (RelativeLayout) layoutInflater.inflate(R.layout.messages_single_listitem_left, parent, false);

        } else {
            itemView = (RelativeLayout) convertView;
        }

        // get all views of single shouts list item
        TextView userName = (TextView) itemView.findViewById(R.id.messagesUserName);
        TextView message = (TextView) itemView.findViewById(R.id.messagesMessage);
        TextView createdAt = (TextView) itemView.findViewById(R.id.messagescreatedAt);

        Typeface sansFont = Typeface.createFromAsset(context.getResources().getAssets(), Constants.fontName);

        // set the username of the user
        userName.setText(messagesList.get(position).getGroupName()); userName.setTypeface(sansFont);
        //message.setText(messagesList.get(position).getMessage());
        createdAt.setText(messagesList.get(position).getUpdatedAt()); createdAt.setTypeface(sansFont);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                // send the groupId
                Intent msgGrpMsgIntent = new Intent(context, MessageGroupMsgsActivity.class);
                msgGrpMsgIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                msgGrpMsgIntent.putExtra(Constants.groupId, messagesList.get(position).getGroupId());
                context.startActivity(msgGrpMsgIntent);
            }
        });

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
