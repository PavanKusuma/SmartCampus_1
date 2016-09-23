package adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import model.AdminPanelSelectedUserList;
import svecw.smartcampus.R;
import utils.Constants;

/**
 * Created by Pavan on 5/21/15.
 */
public class AdminPanelSelectUserAdapter extends BaseAdapter {

    Context context;

    LayoutInflater layoutInflater;

    // lists
    List<AdminPanelSelectedUserList> usernamesList;

    public AdminPanelSelectUserAdapter(Context context) {
        this.context = context;

        usernamesList = new ArrayList<AdminPanelSelectedUserList>();

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    // create holder class to contain xml file elements
    private class ViewHolder {

        TextView userName;
        TextView branch;
        TextView role;
        TextView objectId;
        TextView collegeId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        RelativeLayout itemView;

        if (convertView == null) {

            // inflate single list item for each row
            itemView = (RelativeLayout) layoutInflater.inflate(R.layout.adminpanel_selectuser_singlelistitem, parent, false);

            // view holder object to contain xml file elements
            holder = new ViewHolder();
            holder.objectId = (TextView) itemView.findViewById(R.id.selectUser_ObjectId);
            holder.userName = (TextView) itemView.findViewById(R.id.selectUser_userName);
            holder.role = (TextView) itemView.findViewById(R.id.selectUser_role);
            holder.branch = (TextView) itemView.findViewById(R.id.selectUser_branch);
            holder.collegeId = (TextView) itemView.findViewById(R.id.selectCollegeId);

            // set holder with layout inflater
            itemView.setTag(holder);

        } else {
            itemView = (RelativeLayout) convertView;

            holder = (ViewHolder) convertView.getTag();
        }

        Typeface sansFont = Typeface.createFromAsset(context.getResources().getAssets(), Constants.fontName);

        if(usernamesList.size()>0) {

            AdminPanelSelectedUserList adminPanelSelectedUserList = usernamesList.get(position);
            holder.objectId.setText(adminPanelSelectedUserList.getObjectId()); holder.objectId.setTypeface(sansFont);
            holder.userName.setText(adminPanelSelectedUserList.getUserName()); holder.userName.setTypeface(sansFont);
            holder.branch.setText(adminPanelSelectedUserList.getBranch()); holder.branch.setTypeface(sansFont);
            holder.role.setText(adminPanelSelectedUserList.getRole()); holder.role.setTypeface(sansFont);
            holder.collegeId.setText(adminPanelSelectedUserList.getCollegeId()); holder.collegeId.setTypeface(sansFont);
        }

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
    public void updateItems(List<AdminPanelSelectedUserList> usernamesList) {
        this.usernamesList = usernamesList;

        notifyDataSetChanged();
    }
}
