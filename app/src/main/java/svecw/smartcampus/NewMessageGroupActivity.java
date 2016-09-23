package svecw.smartcampus;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import adapters.DirectorySearchAdapter;
import internaldb.SmartCampusDB;
import utils.Constants;
import utils.Snippets;

/**
 * Created by Pavan on 1/15/16.
 */
public class NewMessageGroupActivity extends AppCompatActivity {

    // views from activity
    EditText groupName, searchName;
    TextView searchProgressText;
    ImageView searchNameBtn;
    ListView searchListView, selectedListView;

    // list of selected names
    List<String> selectedNames = new ArrayList<>();
    List<String> selectedContacts = new ArrayList<>();
    List<String> selectedObjectIds = new ArrayList<>();
    List<String> selectedDepartments = new ArrayList<>();

    // list of searched names
    List<String> searchedNames = new ArrayList<>();
    List<String> searchedContacts = new ArrayList<>();
    List<String> searchedObjectIds = new ArrayList<>();
    List<String> searchedDepartments = new ArrayList<>();

    // complete list of names in db
    List<String> namesList = new ArrayList<>();
    List<String> contactsList = new ArrayList<>();
    List<String> designationsList = new ArrayList<>();
    List<String> departmentsList = new ArrayList<>();

    DirectorySearchAdapter directorySearchAdapter;
    ArrayAdapter<String> arrayAdapter;

    // unique groupId for mapping selected users
    String groupId;
    String searchTerm;

    SmartCampusDB smartCampusDB = new SmartCampusDB(this);
    byte[] b = Constants.null_indicator.getBytes();

    // progress dialog
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_message_group_layout);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.newGroup));
        title.setTypeface(sansFont);

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        groupName = (EditText) findViewById(R.id.groupName); groupName.setTypeface(sansFont);
        searchName = (EditText) findViewById(R.id.searchName); searchName.setTypeface(sansFont);
        searchListView = (ListView) findViewById(R.id.searchListView);
        selectedListView = (ListView) findViewById(R.id.selectedListView);
        searchNameBtn = (ImageView) findViewById(R.id.searchNameBtn);
        searchProgressText = (TextView) findViewById(R.id.searchProgressText); searchProgressText.setTypeface(sansFont);

        // get the instance of adapter
        directorySearchAdapter = new DirectorySearchAdapter(NewMessageGroupActivity.this);
        // set the adapter to listView
        searchListView.setAdapter(directorySearchAdapter);

        // set adapter for selectedListView
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.activity_list_item, android.R.id.text1, selectedNames);
        selectedListView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();


   /*     searchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                // when user is searching for other names, hide the selectedListView and show
                // the searchedListView
                selectedListView.setVisibility(View.GONE);
                searchListView.setVisibility(View.VISIBLE);

                // clear the searchNames before adding fresh search results
                searchedNames.clear();
                searchedContacts.clear();
                searchedDesignations.clear();
                searchedDepartments.clear();

                // get the instance of adapter
                directorySearchAdapter.updateItems(searchedNames, searchedContacts, searchedDesignations, searchedDepartments);
                directorySearchAdapter.notifyDataSetChanged();

                // get the searched charSequence and check whether that is present in any of the names
                // parse through the names list
                for (int j = 0; j < namesList.size(); j++) {

                    // compare the search term in the names list
                    if (namesList.get(j).toLowerCase().contains(charSequence.toString().toLowerCase())) {

                        searchedNames.add(namesList.get(j));
                        searchedContacts.add(contactsList.get(j));
                        searchedDesignations.add(designationsList.get(j));
                        searchedDepartments.add(departmentsList.get(j));

                    }
                }

                // get the instance of adapter
                directorySearchAdapter.updateItems(searchedNames, searchedContacts, searchedDesignations, searchedDepartments);
                directorySearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
*/

        searchNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchProgressText.setVisibility(View.VISIBLE);

                // check if name is entered in search and it should be more than 3 letters
                if (searchName.getText().toString().length() > 3) {

                    searchTerm = searchName.getText().toString();


                    // when user is searching for other names, hide the selectedListView and show
                    // the searchedListView
                    selectedListView.setVisibility(View.GONE);
                    searchListView.setVisibility(View.VISIBLE);

                    // clear the searchNames before adding fresh search results
                    searchedNames.clear();
                    searchedContacts.clear();
                    searchedDepartments.clear();
                    searchedObjectIds.clear();

                    // get the instance of adapter
                    //directorySearchAdapter.updateItems(searchedNames, searchedContacts, searchedDepartments);
                    directorySearchAdapter.notifyDataSetChanged();

                    //new SearchMembers().execute();




                    // search for the given search username
                    ParseQuery<ParseObject> searchQuery = ParseQuery.getQuery(Constants.users);
                    searchQuery.whereContains(Constants.userName, searchTerm);

                    searchQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {

                            // if no exception
                            if (e == null) {

                                if (list.size() > 0) {

                                    Log.i(Constants.appName, "Yes" + list.size());
                                    for (int i = 0; i < list.size(); i++) {

                                        // get the names and contact numbers of the searched users
                                        searchedNames.add(list.get(i).getString(Constants.userName));
                                        searchedContacts.add(list.get(i).getString(Constants.phoneNumber));
                                        searchedDepartments.add(list.get(i).getString(Constants.branch));
                                        searchedObjectIds.add(list.get(i).getObjectId());

                                    }

                                    // get the instance of adapter
                                    //searchListView.setAdapter(directorySearchAdapter);
                                    directorySearchAdapter.updateItems(searchedNames, searchedContacts,searchedObjectIds, searchedDepartments);
                                    directorySearchAdapter.notifyDataSetChanged();
                                } else {

                                    Toast.makeText(NewMessageGroupActivity.this, "No users found with the given search term", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });




                } else {

                    Toast.makeText(NewMessageGroupActivity.this, "Enter more than 3 characters", Toast.LENGTH_SHORT).show();
                }

                searchProgressText.setVisibility(View.GONE);
            }
        });


        // on click of searchListView
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // add the selected names from searchListView
                selectedNames.add(searchedNames.get(i));
                selectedContacts.add(searchedContacts.get(i));
                selectedDepartments.add(searchedDepartments.get(i));
                selectedObjectIds.add(searchedObjectIds.get(i));

                arrayAdapter.notifyDataSetChanged();

                selectedListView.setVisibility(View.VISIBLE);
                searchListView.setVisibility(View.GONE);
            }
        });


    }





    /**
     * Create Group
     */
    class CreateNewGroup1 extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(NewMessageGroupActivity.this);
            progressDialog.setMessage("Creating group ..");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                // get an unique groupId to place for all selected group members
                groupId = Snippets.getUniqueSessionId();

                // save the selected users in the group to db
                ParseObject parseObject = new ParseObject(Constants.MessageGroupsUserMapping);

                // save the selected members one by one for the given groupId
                for(int j=0; j<selectedNames.size(); j++){

                    Log.i(Constants.appName, "Checking "+j);
                    parseObject.put(Constants.groupId, groupId);
                    parseObject.put(Constants.userName, selectedNames.get(j));
                    parseObject.put(Constants.userObjectId, selectedObjectIds.get(j));
                    parseObject.put(Constants.phoneNumber, selectedContacts.get(j));
                    parseObject.put(Constants.role, Constants.participant);

                    // save the selected members
                    parseObject.saveInBackground();
                }

                // add the current user to this group as he is the admin
                parseObject.put(Constants.groupId, groupId);
                parseObject.put(Constants.userName, smartCampusDB.getUser().get(Constants.userName));
                parseObject.put(Constants.userObjectId, smartCampusDB.getUser().get(Constants.objectId));
                parseObject.put(Constants.phoneNumber, smartCampusDB.getUser().get(Constants.phoneNumber));
                parseObject.put(Constants.role, Constants.admin);

                // save the selected members
                parseObject.saveInBackground();

                //finish();

            } catch (Exception e) {

                Log.e(Constants.appName, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
        }
    }

    /**
     * Create Group
     */
    class SearchMembers extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            searchProgressText.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                // search for the given search username
                ParseQuery<ParseObject> searchQuery = ParseQuery.getQuery(Constants.users);
                searchQuery.whereContains(Constants.userName, searchTerm);

                searchQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {

                        // if no exception
                        if (e == null) {

                            if (list.size() > 0) {

                                Log.i(Constants.appName, "Yes"+list.size());
                                for (int i = 0; i < list.size(); i++) {

                                    // get the names and contact numbers of the searched users
                                    searchedNames.add(list.get(i).getString(Constants.userName));
                                    searchedContacts.add(list.get(i).getString(Constants.phoneNumber));
                                    searchedDepartments.add(list.get(i).getString(Constants.branch));
                                    searchedObjectIds.add(list.get(i).getObjectId());

                                }

                                directorySearchAdapter.notifyDataSetChanged();
                            } else {

                                Toast.makeText(NewMessageGroupActivity.this, "No users found with the given search term", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                // get the instance of adapter
                directorySearchAdapter.updateItems(searchedNames, searchedContacts,searchedObjectIds, searchedDepartments);
                directorySearchAdapter.notifyDataSetChanged();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        searchProgressText.setVisibility(View.GONE);
                    }
                });


            } catch (Exception e) {

                Log.e(Constants.appName, e.getMessage());
            }

            return null;
        }
    }

}
