package adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

import me.biubiubiu.justifytext.library.JustifyTextView;
import svecw.smartcampus.R;
import utils.Constants;

/**
 * Created by Pavan on 6/25/15.
 */
public class LearnSubModuleAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader; // header titles
    private List<String> listDataItem; // list item
    // child data in format of header title, child title
    //private HashMap<String, String> listDataChild;

    public LearnSubModuleAdapter(Context context, List<String> listDataHeader, List<String> listDataItem) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataItem = listDataItem;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataItem.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        Typeface sansFont = Typeface.createFromAsset(context.getResources().getAssets(), Constants.fontName);

        JustifyTextView txtListChild = (JustifyTextView) convertView.findViewById(R.id.listItem);

        txtListChild.setText(childText); txtListChild.setTypeface(sansFont);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        Typeface sansFont = Typeface.createFromAsset(context.getResources().getAssets(), Constants.fontName);

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.listHeader);
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle); lblListHeader.setTypeface(sansFont);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
