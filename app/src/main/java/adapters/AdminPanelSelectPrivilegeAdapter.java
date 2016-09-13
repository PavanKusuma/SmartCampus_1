package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import model.SelectPrivilege;
import svecw.smartcampus.R;

/**
 * Created by Pavan on 5/21/15.
 */
public class AdminPanelSelectPrivilegeAdapter extends BaseAdapter {

    Context context;

    LayoutInflater layoutInflater;

    // lists
    List<SelectPrivilege> selectPrivilegesList;

    public AdminPanelSelectPrivilegeAdapter(Context context) {
        this.context = context;

        selectPrivilegesList = new ArrayList<SelectPrivilege>();

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    // create holder class to contain xml file elements
    private class ViewHolder {

        TextView privilegeName;
        CheckBox privilegeCheckBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        RelativeLayout itemView;

        if (convertView == null) {

            // inflate single list item for each row
            itemView = (RelativeLayout) layoutInflater.inflate(R.layout.adminpanel_selectprivilege_singlelistitem, parent, false);

            // view holder object to contain xml file elements
            holder = new ViewHolder();
            holder.privilegeName = (TextView) itemView.findViewById(R.id.privilegeName);
            holder.privilegeCheckBox = (CheckBox) itemView.findViewById(R.id.privilegeCheckBox);

            // set holder with layout inflater
            itemView.setTag(holder);

        } else {
            itemView = (RelativeLayout) convertView;

            holder = (ViewHolder) convertView.getTag();
        }

        if(selectPrivilegesList.size()>0) {

            holder.privilegeName.setText(selectPrivilegesList.get(position).getPrivilege());
            holder.privilegeCheckBox.setChecked(selectPrivilegesList.get(position).getSelected());
        }

        return itemView;
    }

    @Override
    public int getCount() {
        return selectPrivilegesList.size();
    }

    @Override
    public Object getItem(int position) {
        return selectPrivilegesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * update the adapter list items and notify
     */
    public void updateItems(List<SelectPrivilege> selectPrivilegesList) {
        this.selectPrivilegesList = selectPrivilegesList;

        notifyDataSetChanged();
    }
}
