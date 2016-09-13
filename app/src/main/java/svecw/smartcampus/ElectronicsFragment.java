package svecw.smartcampus;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapters.KnowledgeWallAdapter;
import model.KnowledgeInfo;
import utils.Constants;

/**
 * Created by Pavan on 12/27/15.
 */
public class ElectronicsFragment extends Fragment {

    ProgressBar knowledgeWallProgressBar;
    ListView knowledgeList;
    ArrayList<KnowledgeInfo> knowledgeWallPosts;

    KnowledgeWallAdapter knowledgeWallAdapter;

    public ElectronicsFragment() {

        // wall post list
        knowledgeWallPosts = new ArrayList<KnowledgeInfo>();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the posts
        new GetGlobalPosts().execute(getArguments().getString(Constants.category));


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate the layout
        RelativeLayout itemView = (RelativeLayout) inflater.inflate(R.layout.knowledge_single_fragment, container, false);

        // progress bar
        knowledgeWallProgressBar = (ProgressBar) itemView.findViewById(R.id.knowledgeWallProgressBar);
        knowledgeList = (ListView) itemView.findViewById(R.id.knowledgeList);

        knowledgeWallAdapter = new KnowledgeWallAdapter(getActivity().getApplicationContext());
        knowledgeList.setAdapter(knowledgeWallAdapter);

        return itemView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * This AsyncTask class will fetch globalInfo wall posts from
     * database and assign it to adapter for populating listView
     */
    class GetGlobalPosts extends AsyncTask<String, Float, String> {


        @Override
        protected String doInBackground(String... strings) {

            // create parse object for the "GlobalInfo"
            ParseQuery<ParseObject> globalInfoQuery = ParseQuery.getQuery(Constants.globalInfoTable);

            // get mapping for the list of objectIds
            globalInfoQuery.addDescendingOrder(Constants.createdAt);
            globalInfoQuery.whereEqualTo(Constants.category, strings[0]);

            // get the list of shouts
            globalInfoQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {

                    if (e == null) {

                        if (parseObjects.size() > 0) {

                            // navigate through the list of objects
                            for (int i = 0; i < parseObjects.size(); i++) {

                                // create object for collegeWallPost class
                                final KnowledgeInfo knowledgeWallPost = new KnowledgeInfo();

                                // set the values of the wall post into object
                                knowledgeWallPost.setTitle(parseObjects.get(i).getString(Constants.title));
                                knowledgeWallPost.setDescription(parseObjects.get(i).getString(Constants.description));
                                knowledgeWallPost.setBranch(parseObjects.get(i).getString(Constants.category));
                                knowledgeWallPost.setRole(parseObjects.get(i).getString(Constants.role));
                                knowledgeWallPost.setCreatedAt(parseObjects.get(i).getCreatedAt());
                                knowledgeWallPost.setUpdatedAt(parseObjects.get(i).getUpdatedAt());
                                knowledgeWallPost.setObjectId(parseObjects.get(i).getObjectId());

                                // get the mediaFile of the wall post
                                ParseFile mediaFile = (ParseFile) parseObjects.get(i).get(Constants.mediaFile);

                                mediaFile.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] bytes, ParseException e) {

                                        // check if the image is available
                                        if (e == null) {

                                            // check if the mediaTable file bytes are null indicator bytes
                                            // if so assign the mediaTable file string to null indicator
                                            // else fetch the mediaTable file bytes and assign
                                            if (Arrays.equals(bytes, Constants.null_indicator.getBytes())) {

                                                knowledgeWallPost.setMediaFile(Constants.null_indicator);
                                                knowledgeWallPosts.add(knowledgeWallPost);

                                                //knowledgeWallAdapter.updateItems(knowledgeWallPosts);
                                                knowledgeWallAdapter.notifyDataSetChanged();
                                            } else {

                                                knowledgeWallPost.setMediaFile(Base64.encodeToString(bytes, Base64.DEFAULT));
                                                knowledgeWallPosts.add(knowledgeWallPost);


                                                //knowledgeWallAdapter.updateItems(knowledgeWallPosts);
                                                knowledgeWallAdapter.notifyDataSetChanged();
                                            }

                                        }

                                    }
                                });

                                knowledgeWallAdapter.notifyDataSetChanged();

                                knowledgeWallProgressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            });

            return null;
        }

    }
}
