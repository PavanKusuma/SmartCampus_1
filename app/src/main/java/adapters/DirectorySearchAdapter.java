package adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import svecw.smartcampus.R;
import utils.Constants;

/**
 * Created by Pavan_Kusuma on 4/18/2015.
 */
public class DirectorySearchAdapter extends BaseAdapter {

    Context context;

    LayoutInflater layoutInflater;

    // lists
    List<String> usernamesList, contactsList, collegeIdsList, departmentsList;

    public DirectorySearchAdapter(Context context) {
        this.context = context;

        usernamesList = new ArrayList<String>();
        contactsList = new ArrayList<String>();
        collegeIdsList = new ArrayList<String>();
        departmentsList = new ArrayList<String>();

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RelativeLayout itemView;
        if (convertView == null) {
            itemView = (RelativeLayout) layoutInflater.inflate(R.layout.searchcontact_single_listitem, parent, false);

        } else {
            itemView = (RelativeLayout) convertView;
        }

        TextView userNameTextView = (TextView) itemView.findViewById(R.id.userName);
        TextView departmentTextView = (TextView) itemView.findViewById(R.id.department);
        TextView facultyIdTextView = (TextView) itemView.findViewById(R.id.facultyId);
        ImageView callView = (ImageView) itemView.findViewById(R.id.ic_call);

        Typeface sansFont = Typeface.createFromAsset(context.getResources().getAssets(), Constants.fontName);

                userNameTextView.setText(usernamesList.get(position)); userNameTextView.setTypeface(sansFont);
                departmentTextView.setText(departmentsList.get(position)); departmentTextView.setTypeface(sansFont);
                facultyIdTextView.setText(collegeIdsList.get(position)); facultyIdTextView.setTypeface(sansFont);
                callView.setTag(contactsList.get(position));


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
    public void updateItems(List<String> usernamesList,List<String> contactsList, List<String> collegeIdsList, List<String> departmentsList) {
        this.usernamesList.addAll(usernamesList);
        this.contactsList.addAll(contactsList);
        this.collegeIdsList.addAll(collegeIdsList);
        this.departmentsList.addAll(departmentsList);

        //likeList = like1List;
        notifyDataSetChanged();
    }

    /**
     * update the adapter list with new items and notify
     */
/*
    public void updateNewItems(List<String> usernamesList,List<String> contactsList,List<String> designationsList,List<String> departmentsList) {
        this.usernamesList = usernamesList;
        this.contactsList = contactsList;
        this.departmentsList = departmentsList;
        this.designationsList = designationsList;

        //likeList = like1List;
        notifyDataSetChanged();
    }
*/



}
