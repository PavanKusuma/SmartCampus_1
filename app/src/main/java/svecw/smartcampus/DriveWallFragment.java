package svecw.smartcampus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import adapters.KnowledgeWallAdapter;
import model.Drive;
import model.GlobalInfo;
import utils.Constants;

/**
 * Created by Pavan_Kusuma on 10/7/2016.
 */

public class DriveWallFragment extends Fragment {

    ProgressBar driveProgressBar;
    ListView driveList;
    ArrayList<Drive> drivePosts = new ArrayList<Drive>();;
    String category;
    ArrayList<Drive> driveCategoryPosts = new ArrayList<Drive>();

    // adapter
    KnowledgeWallAdapter driveAdapter;

    public DriveWallFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the parcelable arrayList of posts and category
        drivePosts = getArguments().getParcelableArrayList(Constants.driveInfo);
        category = getArguments().getString(Constants.category);

        driveAdapter = new KnowledgeWallAdapter(getActivity().getApplicationContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate the layout
        RelativeLayout itemView = (RelativeLayout) inflater.inflate(R.layout.drive_fragment, container, false);

        // progress bar
        driveProgressBar = (ProgressBar) itemView.findViewById(R.id.driveProgressBar);
        driveList = (ListView) itemView.findViewById(R.id.driveList);

        driveList.setAdapter(driveAdapter);

        // as onCreateView loads every time the fragment is clicked
        // add the empty arrayList to listView so that the list items will not be added again
        driveCategoryPosts.clear();
        //driveAdapter.updateItems(driveCategoryPosts);
        driveAdapter.notifyDataSetChanged();
/*

        // navigate through the list to fetch the required category list items
        for(int i=0; i<drivePosts.size(); i++)
            if(drivePosts.get(i).getCategory().contentEquals(category)) {

                //Log.i(Constants.category, knowledgeWallPosts.get(i).getCategory());
                driveCategoryPosts.add(drivePosts.get(i));

            }
*/

        // add the items to the adapter
        //knowledgeWallAdapter.updateItems(knowledgeWallCategoryPosts);
        //knowledgeWallAdapter.notifyDataSetChanged();

        // close the progress bar
        driveProgressBar.setVisibility(View.GONE);

        return itemView;
    }

    @Override
    public void onResume() {
        // add the items to the adapter
        //driveAdapter.updateItems(driveCategoryPosts);
        driveAdapter.notifyDataSetChanged();

        super.onResume();
    }
}