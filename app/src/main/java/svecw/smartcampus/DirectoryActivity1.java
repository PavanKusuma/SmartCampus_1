package svecw.smartcampus;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import adapters.DirectoryAdapter;
import internaldb.SmartCampusDB;
import utils.Constants;

/**
 * Created by Pavan on 7/3/15.
 */
public class DirectoryActivity1 extends AppCompatActivity {

    // directory adapter
    DirectoryAdapter directoryAdapter;

    // complete list of contacts
    List<String> userNamesList = new ArrayList<String>();
    List<String> contactsList = new ArrayList<String>();
    List<String> designationList = new ArrayList<String>();
    List<String> departmentList = new ArrayList<String>();

    // priviliege list of contacts
    List<String> dummy_userNamesList = new ArrayList<String>();
    List<String> dummy_contactsList = new ArrayList<String>();
    List<String> dummy_designationList = new ArrayList<String>();
    List<String> dummy_departmentList = new ArrayList<String>();

    // searched list of contacts
    List<String> search_userNamesList = new ArrayList<String>();
    List<String> search_contactsList = new ArrayList<String>();
    List<String> search_designationList = new ArrayList<String>();
    List<String> search_departmentList = new ArrayList<String>();

    // editText for search
    EditText searchContact;
    String searchContactText;
    ImageView searchContactButton;

    // emptyTextView when no contacts are present
    TextView emptyListText;
    int emptyListCounter = 0;

    // branches
    String[] departments = {Constants.ALL, Constants.MANAGEMENT, Constants.BASICSCIENCE, Constants.CIVIL,
            Constants.CSE, Constants.ECE, Constants.EEE, Constants.IT, Constants.MECHANICAL, Constants.MBA, Constants.MCA};

    // department to sort
    String departmentSort = Constants.ALL;

    // toolbar for activity
    Toolbar toolbar;

    // get views from activity
    ListView contactsListView;

    // object for internal db
    SmartCampusDB smartCampusDB = new SmartCampusDB(this);

    // branch select
    Spinner searchBranch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directory_activity);


        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.directory));
        title.setTypeface(sansFont);

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        // get the raw files
        InputStream names_is = getResources().openRawResource(R.raw.facultynames);
        InputStream contacts_is = getResources().openRawResource(R.raw.contacts);
        InputStream designations_is = getResources().openRawResource(R.raw.designations);
        InputStream departments_is = getResources().openRawResource(R.raw.departments);

        // reader
        BufferedReader names_reader = new BufferedReader(new InputStreamReader(names_is));
        BufferedReader contacts_reader = new BufferedReader(new InputStreamReader(contacts_is));
        BufferedReader designations_reader = new BufferedReader(new InputStreamReader(designations_is));
        BufferedReader departments_reader = new BufferedReader(new InputStreamReader(departments_is));

        String line1, line2, line3, line4;
        String[] rowData1, rowData2, rowData3, rowData4 = null;

        try {
            while ((line1 = names_reader.readLine()) != null) {

                line2 = contacts_reader.readLine();
                line3 = designations_reader.readLine();
                line4 = departments_reader.readLine();


                rowData1 = line1.split("\n");
                rowData2 = line2.split("\n");
                rowData3 = line3.split("\n");
                rowData4 = line4.split("\n");

                userNamesList.add(rowData1[0]);
                contactsList.add(rowData2[0]);
                designationList.add(rowData3[0]);
                departmentList.add(rowData4[0]);
            }
        }catch (Exception ex){
            Log.v(Constants.appName, ex.getMessage());
        }


        // get the list view from activity
        contactsListView = (ListView) findViewById(R.id.contactsListView);
        searchContact = (EditText) findViewById(R.id.searchContact);
        emptyListText = (TextView) findViewById(R.id.emptyContactListText); emptyListText.setTypeface(sansFont);
        searchContactButton = (ImageView) findViewById(R.id.searchContactButton);
        searchBranch = (Spinner) findViewById(R.id.searchBranch);
        searchContact.setEnabled(true);
        searchContactButton.setEnabled(true);

        // get the instance of adapter
        directoryAdapter = new DirectoryAdapter(DirectoryActivity1.this);

        // set the adapter to listView
        contactsListView.setAdapter(directoryAdapter);

        // collegeDirectory privilege will ensure to display all the contacts to the user
        // irrespective of branches
        if(((int)smartCampusDB.getUser().get(Constants.collegeDirectory))==1) {

            // set the default list
            directoryAdapter.updateItems(userNamesList, contactsList, designationList, departmentList);
            directoryAdapter.notifyDataSetChanged();

            // change the emptyListCounter
            emptyListCounter = 2;
        }

        // individual privileges are used to show the contacts
        else {

            if (((int) smartCampusDB.getUser().get(Constants.cseDirectory)) == 1) {

                int start = departmentList.indexOf(Constants.CSE);
                int stop = departmentList.lastIndexOf(Constants.CSE);

                // create sub list for the branch selection
                dummy_userNamesList = userNamesList.subList(start, stop + 1);
                dummy_contactsList = contactsList.subList(start, stop + 1);
                dummy_designationList = designationList.subList(start, stop + 1);
                dummy_departmentList = departmentList.subList(start, stop + 1);

                // get the instance of adapter
                directoryAdapter.updateItems(dummy_userNamesList, dummy_contactsList, dummy_designationList, dummy_departmentList);
                directoryAdapter.notifyDataSetChanged();

                // change the emptyListCounter
                emptyListCounter = 1;

            }

            if (((int) smartCampusDB.getUser().get(Constants.itDirectory)) == 1) {

                int start = departmentList.indexOf(Constants.IT);
                int stop = departmentList.lastIndexOf(Constants.IT);

                // create sub list for the branch selection
                dummy_userNamesList = userNamesList.subList(start, stop + 1);
                dummy_contactsList = contactsList.subList(start, stop + 1);
                dummy_designationList = designationList.subList(start, stop + 1);
                dummy_departmentList = departmentList.subList(start, stop + 1);

                // get the instance of adapter
                directoryAdapter.updateItems(dummy_userNamesList, dummy_contactsList, dummy_designationList, dummy_departmentList);
                directoryAdapter.notifyDataSetChanged();

                // change the emptyListCounter
                emptyListCounter = 1;

            }

            if (((int) smartCampusDB.getUser().get(Constants.eceDirectory)) == 1) {

                int start = departmentList.indexOf(Constants.ECE);
                int stop = departmentList.lastIndexOf(Constants.ECE);

                // create sub list for the branch selection
                dummy_userNamesList = userNamesList.subList(start, stop + 1);
                dummy_contactsList = contactsList.subList(start, stop + 1);
                dummy_designationList = designationList.subList(start, stop + 1);
                dummy_departmentList = departmentList.subList(start, stop + 1);

                // get the instance of adapter
                directoryAdapter.updateItems(dummy_userNamesList, dummy_contactsList, dummy_designationList, dummy_departmentList);
                directoryAdapter.notifyDataSetChanged();

                // change the emptyListCounter
                emptyListCounter = 1;

            }

            if (((int) smartCampusDB.getUser().get(Constants.eeeDirectory)) == 1) {

                int start = departmentList.indexOf(Constants.EEE);
                int stop = departmentList.lastIndexOf(Constants.EEE);

                // create sub list for the branch selection
                dummy_userNamesList = userNamesList.subList(start, stop + 1);
                dummy_contactsList = contactsList.subList(start, stop + 1);
                dummy_designationList = designationList.subList(start, stop + 1);
                dummy_departmentList = departmentList.subList(start, stop + 1);

                // get the instance of adapter
                directoryAdapter.updateItems(dummy_userNamesList, dummy_contactsList, dummy_designationList, dummy_departmentList);
                directoryAdapter.notifyDataSetChanged();

                // change the emptyListCounter
                emptyListCounter = 1;

            }

            if (((int) smartCampusDB.getUser().get(Constants.mechDirectory)) == 1) {

                int start = departmentList.indexOf(Constants.MECHANICAL);
                int stop = departmentList.lastIndexOf(Constants.MECHANICAL);

                // create sub list for the branch selection
                dummy_userNamesList = userNamesList.subList(start, stop + 1);
                dummy_contactsList = contactsList.subList(start, stop + 1);
                dummy_designationList = designationList.subList(start, stop + 1);
                dummy_departmentList = departmentList.subList(start, stop + 1);

                // get the instance of adapter
                directoryAdapter.updateItems(dummy_userNamesList, dummy_contactsList, dummy_designationList, dummy_departmentList);
                directoryAdapter.notifyDataSetChanged();

                // change the emptyListCounter
                emptyListCounter = 1;

            }

            if (((int) smartCampusDB.getUser().get(Constants.civilDirectory)) == 1) {

                int start = departmentList.indexOf(Constants.CIVIL);
                int stop = departmentList.lastIndexOf(Constants.CIVIL);

                // create sub list for the branch selection
                dummy_userNamesList = userNamesList.subList(start, stop + 1);
                dummy_contactsList = contactsList.subList(start, stop + 1);
                dummy_designationList = designationList.subList(start, stop + 1);
                dummy_departmentList = departmentList.subList(start, stop + 1);

                // get the instance of adapter
                directoryAdapter.updateItems(dummy_userNamesList, dummy_contactsList, dummy_designationList, dummy_departmentList);
                directoryAdapter.notifyDataSetChanged();

                // change the emptyListCounter
                emptyListCounter = 1;

            }

            if (((int) smartCampusDB.getUser().get(Constants.mbaDirectory)) == 1) {

                int start = departmentList.indexOf(Constants.MBA);
                int stop = departmentList.lastIndexOf(Constants.MBA);

                // create sub list for the branch selection
                dummy_userNamesList = userNamesList.subList(start, stop + 1);
                dummy_contactsList = contactsList.subList(start, stop + 1);
                dummy_designationList = designationList.subList(start, stop + 1);
                dummy_departmentList = departmentList.subList(start, stop + 1);

                // get the instance of adapter
                directoryAdapter.updateItems(dummy_userNamesList, dummy_contactsList, dummy_designationList, dummy_departmentList);
                directoryAdapter.notifyDataSetChanged();

                // change the emptyListCounter
                emptyListCounter = 1;

            }

            if (((int) smartCampusDB.getUser().get(Constants.mcaDirectory)) == 1) {

                int start = departmentList.indexOf(Constants.MCA);
                int stop = departmentList.lastIndexOf(Constants.MCA);

                // create sub list for the branch selection
                dummy_userNamesList = userNamesList.subList(start, stop + 1);
                dummy_contactsList = contactsList.subList(start, stop + 1);
                dummy_designationList = designationList.subList(start, stop + 1);
                dummy_departmentList = departmentList.subList(start, stop + 1);

                // get the instance of adapter
                directoryAdapter.updateItems(dummy_userNamesList, dummy_contactsList, dummy_designationList, dummy_departmentList);
                directoryAdapter.notifyDataSetChanged();

                // change the emptyListCounter
                emptyListCounter = 1;

            }

            if (((int) smartCampusDB.getUser().get(Constants.basicScienceDirectory)) == 1) {

                int start = departmentList.indexOf(Constants.BASICSCIENCE);
                int stop = departmentList.lastIndexOf(Constants.BASICSCIENCE);

                // create sub list for the branch selection
                dummy_userNamesList = userNamesList.subList(start, stop + 1);
                dummy_contactsList = contactsList.subList(start, stop + 1);
                dummy_designationList = designationList.subList(start, stop + 1);
                dummy_departmentList = departmentList.subList(start, stop + 1);

                // get the instance of adapter
                directoryAdapter.updateItems(dummy_userNamesList, dummy_contactsList, dummy_designationList, dummy_departmentList);
                directoryAdapter.notifyDataSetChanged();

                // change the emptyListCounter
                emptyListCounter = 1;

            }
        }

        // check if there are any contacts present
        if(emptyListCounter == 0){

            // show the empty text if no contacts are present
            emptyListText.setVisibility(View.VISIBLE);
            searchContact.setEnabled(false);
            searchContactButton.setEnabled(false);
        }


        // get the searchContact username and populate the list
        // with contacts that match the username
        // and if there is no searchContact username is present
        // default list should be displayed with respect to the privilege assigned to user
        searchContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get the search text
                searchContactText = searchContact.getText().toString();

                // check if entered text length is greater than 0
                // if so notify adapter with search criteria
                // else notify adapter with available contacts
                if (searchContactText.length() > 0) {

                    // clear the searchContacts before updating
                    search_userNamesList.clear();
                    search_contactsList.clear();
                    search_departmentList.clear();
                    search_designationList.clear();

                    // check if entire directory is present
                    if (emptyListCounter == 2) {

                        // navigate through all names
                        for (int i = 0; i < userNamesList.size(); i++) {

                            if (searchBranch.getSelectedItem().toString().contentEquals(Constants.ALL)) {

                                // check if any name contains the given string
                                if (userNamesList.get(i).toLowerCase().contains(searchContactText.toLowerCase())) {

                                    search_userNamesList.add(userNamesList.get(i));
                                    search_contactsList.add(contactsList.get(i));
                                    search_designationList.add(designationList.get(i));
                                    search_departmentList.add(departmentList.get(i));
                                }
                            } else {
                                // check if any name contains the given string
                                if (userNamesList.get(i).toLowerCase().contains(searchContactText.toLowerCase()) && departmentList.get(i).toLowerCase().contentEquals(searchBranch.getSelectedItem().toString().toLowerCase())) {

                                    search_userNamesList.add(userNamesList.get(i));
                                    search_contactsList.add(contactsList.get(i));
                                    search_designationList.add(designationList.get(i));
                                    search_departmentList.add(departmentList.get(i));
                                }
                            }
                        }

                        // get the instance of adapter
                        directoryAdapter.updateNewItems(search_userNamesList, search_contactsList, search_designationList, search_departmentList);
                        directoryAdapter.notifyDataSetChanged();

                    }
                    // applicable if only few directories are present
                    else if (emptyListCounter == 1) {

                        // navigate through all names
                        for (int i = 0; i < dummy_userNamesList.size(); i++) {

                            if (searchBranch.getSelectedItem().toString().contentEquals(Constants.ALL)) {

                                // check if any name contains the given string
                                if (dummy_userNamesList.get(i).toLowerCase().contains(searchContactText.toLowerCase())) {

                                    search_userNamesList.add(dummy_userNamesList.get(i));
                                    search_contactsList.add(dummy_contactsList.get(i));
                                    search_designationList.add(dummy_designationList.get(i));
                                    search_departmentList.add(dummy_departmentList.get(i));
                                }
                            }else {
                                // check if any name contains the given string
                                if (dummy_userNamesList.get(i).toLowerCase().contains(searchContactText.toLowerCase()) && dummy_departmentList.get(i).toLowerCase().contentEquals(searchBranch.getSelectedItem().toString().toLowerCase())) {

                                    search_userNamesList.add(dummy_userNamesList.get(i));
                                    search_contactsList.add(dummy_contactsList.get(i));
                                    search_designationList.add(dummy_designationList.get(i));
                                    search_departmentList.add(dummy_departmentList.get(i));
                                }
                            }
                        }

                            // get the instance of adapter
                            directoryAdapter.updateNewItems(search_userNamesList, search_contactsList, search_designationList, search_departmentList);
                            directoryAdapter.notifyDataSetChanged();

                        }else{

                            // do nothing
                        }
                    }

                    // notify adapter with existing contacts if search text length is zero
                    else {

                        // check if entire directory is present
                        if (emptyListCounter == 2) {



                            // get the instance of adapter
                            directoryAdapter.updateNewItems(userNamesList, contactsList, designationList, departmentList);
                            directoryAdapter.notifyDataSetChanged();

                        }
                        // applicable if only few directories are present
                        else if (emptyListCounter == 1) {

                            // get the instance of adapter
                            directoryAdapter.updateNewItems(dummy_userNamesList, dummy_contactsList, dummy_designationList, dummy_departmentList);
                            directoryAdapter.notifyDataSetChanged();

                        } else {

                            // do nothing
                        }
                }
            }
                                               }

        );

        // replace the list back if there is no text present in searchContact

        searchContact.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {

                 // check if length is 0
                 if (s.length() == 0) {

                     searchBranch.setSelection(0);
                     // check if entire directory is present
                     if (emptyListCounter == 2) {

                         // get the instance of adapter
                         directoryAdapter.updateNewItems(userNamesList, contactsList, designationList, departmentList);
                         directoryAdapter.notifyDataSetChanged();

                     }
                     // applicable if only few directories are present
                     else if (emptyListCounter == 1) {

                         // get the instance of adapter
                         directoryAdapter.updateNewItems(dummy_userNamesList, dummy_contactsList, dummy_designationList, dummy_departmentList);
                         directoryAdapter.notifyDataSetChanged();

                     } else {

                         // do nothing
                     }

                 }
             }

             @Override
             public void afterTextChanged(Editable s) {

             }
         }

        );

        searchBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // clear the searchContacts before updating
                search_userNamesList.clear();
                search_contactsList.clear();
                search_departmentList.clear();
                search_designationList.clear();

                // navigate through all names
                for (int i = 0; i < dummy_userNamesList.size(); i++) {

                    if (searchBranch.getSelectedItem().toString().contentEquals(Constants.ALL)) {

                            search_userNamesList.add(dummy_userNamesList.get(i));
                            search_contactsList.add(dummy_contactsList.get(i));
                            search_designationList.add(dummy_designationList.get(i));
                            search_departmentList.add(dummy_departmentList.get(i));

                    }else {
                        // check if any name contains the given string
                        if (dummy_departmentList.get(i).toLowerCase().contentEquals(searchBranch.getSelectedItem().toString().toLowerCase())) {

                            search_userNamesList.add(dummy_userNamesList.get(i));
                            search_contactsList.add(dummy_contactsList.get(i));
                            search_designationList.add(dummy_designationList.get(i));
                            search_departmentList.add(dummy_departmentList.get(i));
                        }
                    }
                }

                // get the instance of adapter
                directoryAdapter.updateNewItems(search_userNamesList, search_contactsList, search_designationList, search_departmentList);
                directoryAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        }


    }
