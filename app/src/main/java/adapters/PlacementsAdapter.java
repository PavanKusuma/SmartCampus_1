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

import model.Placements;
import svecw.smartcampus.R;
import utils.Constants;

/**
 * Created by Pavan on 7/3/15.
 */
public class PlacementsAdapter extends BaseAdapter {

    // context of present class
    Context context;

    // usernames and likelist
    List<Placements> placementsList;
    //ArrayList<Boolean> likeList;

    // layout Inflator
    LayoutInflater layoutInflater;

    // constructor
    public PlacementsAdapter(Context context) {

        this.context = context;

        // initialize variables
        placementsList = new ArrayList<Placements>();
        //likeList = new ArrayList<Boolean>();

        // layoutInflater object
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return placementsList.size();
    }

    @Override
    public Object getItem(int position) {
        return placementsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        RelativeLayout itemView;
        if (convertView == null) {
            itemView = (RelativeLayout) layoutInflater.inflate(R.layout.placement_listitem, parent, false);

        } else {
            itemView = (RelativeLayout) convertView;
        }

        Typeface sansFont = Typeface.createFromAsset(context.getResources().getAssets(), Constants.fontName);

        // get all views of single shouts list item
        TextView company = (TextView) itemView.findViewById(R.id.company); company.setTypeface(sansFont);
        TextView count = (TextView) itemView.findViewById(R.id.count); count.setTypeface(sansFont);

        // set the username of the user
        company.setText(placementsList.get(position).getCompany());
        count.setText(String.valueOf(placementsList.get(position).getCount()));

        return itemView;
    }

    /**
     * update the adapter list items and notify
     */
    public void updateItems(List<Placements> postsList) {
        this.placementsList = postsList;
        //likeList = like1List;
        notifyDataSetChanged();
    }
}
