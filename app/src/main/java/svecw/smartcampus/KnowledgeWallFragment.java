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
import model.GlobalInfo;
import utils.Constants;

/**
 * Created by Pavan on 12/26/15.
 */
public class KnowledgeWallFragment extends Fragment {

    ProgressBar knowledgeWallProgressBar;
    ListView knowledgeList;
    ArrayList<GlobalInfo> knowledgeWallPosts = new ArrayList<GlobalInfo>();;
    String category;
    ArrayList<GlobalInfo> knowledgeWallCategoryPosts = new ArrayList<GlobalInfo>();

    // adapter
    KnowledgeWallAdapter knowledgeWallAdapter;

    public KnowledgeWallFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the parcelable arrayList of posts and category
        knowledgeWallPosts = getArguments().getParcelableArrayList(Constants.globalInfoTable);
        category = getArguments().getString(Constants.category);


        knowledgeWallAdapter = new KnowledgeWallAdapter(getActivity().getApplicationContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate the layout
        RelativeLayout itemView = (RelativeLayout) inflater.inflate(R.layout.knowledge_single_fragment, container, false);

        // progress bar
        knowledgeWallProgressBar = (ProgressBar) itemView.findViewById(R.id.knowledgeWallProgressBar);
        knowledgeList = (ListView) itemView.findViewById(R.id.knowledgeList);

        knowledgeList.setAdapter(knowledgeWallAdapter);

        // as onCreateView loads every time the fragment is clicked
        // add the empty arrayList to listView so that the list items will not be added again
        knowledgeWallCategoryPosts.clear();
        knowledgeWallAdapter.updateItems(knowledgeWallCategoryPosts);
        knowledgeWallAdapter.notifyDataSetChanged();

        // navigate through the list to fetch the required category list items
        for(int i=0; i<knowledgeWallPosts.size(); i++)
            if(knowledgeWallPosts.get(i).getCategory().contentEquals(category)) {

                //Log.i(Constants.category, knowledgeWallPosts.get(i).getCategory());
                knowledgeWallCategoryPosts.add(knowledgeWallPosts.get(i));

            }

        // add the items to the adapter
        //knowledgeWallAdapter.updateItems(knowledgeWallCategoryPosts);
        //knowledgeWallAdapter.notifyDataSetChanged();

        // close the progress bar
        knowledgeWallProgressBar.setVisibility(View.GONE);

        return itemView;
    }

    @Override
    public void onResume() {
        // add the items to the adapter
        knowledgeWallAdapter.updateItems(knowledgeWallCategoryPosts);
        knowledgeWallAdapter.notifyDataSetChanged();

        super.onResume();
    }
}