package svecw.smartcampus;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import adapters.TempGoKartingAdapter;
import internaldb.SmartCampusDB;
import utils.Constants;

/**
 * Created by Pavan on 1/22/16.
 */
public class TemporaryGroup_Details extends AppCompatActivity {

    // directory adapter
    TempGoKartingAdapter directoryAdapter;

    // complete list of contacts
    List<String> userNamesList = new ArrayList<String>();
    List<String> contactsList = new ArrayList<String>();
    List<String> regnoList = new ArrayList<String>();

    // priviliege list of contacts
    List<String> dummy_userNamesList = new ArrayList<String>();
    List<String> dummy_contactsList = new ArrayList<String>();

    // searched list of contacts
    List<String> search_userNamesList = new ArrayList<String>();
    List<String> search_contactsList = new ArrayList<String>();

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

    String groupName;
    InputStream names_is;
    InputStream contacts_is;
    InputStream regno_is;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_group_gokarting);

        // fetch the group name
        groupName = getIntent().getStringExtra(Constants.groupName);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(groupName);
        title.setTypeface(sansFont);

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        // check the group name
        // get the raw files matching to the group name

        if(groupName.contentEquals("VEMC-16 Faculty")) {

            names_is = getResources().openRawResource(R.raw.vemc_fac);
            contacts_is = getResources().openRawResource(R.raw.vemc_fac_contacts);
        }
        else if(groupName.contentEquals("VEMC-16 Student")) {

            names_is = getResources().openRawResource(R.raw.vemc_stu);
            contacts_is = getResources().openRawResource(R.raw.vemc_stu_contacts);
        }
        else if(groupName.contentEquals("HOD")) {

            names_is = getResources().openRawResource(R.raw.hods);
            contacts_is = getResources().openRawResource(R.raw.hodcontacts);
        }
        else if(groupName.contentEquals("Dean")){

            names_is = getResources().openRawResource(R.raw.dean);
            contacts_is = getResources().openRawResource(R.raw.deancontacts);
        }
        else if(groupName.contentEquals("Antiragging")){

            names_is = getResources().openRawResource(R.raw.ragging);
            contacts_is = getResources().openRawResource(R.raw.raggingcontacts);
        }
        else if(groupName.contentEquals("Counselors")){

            names_is = getResources().openRawResource(R.raw.counselors);
            contacts_is = getResources().openRawResource(R.raw.counselorscontacts);
        }
        else if(groupName.contentEquals("Placement")){

            names_is = getResources().openRawResource(R.raw.placement);
            contacts_is = getResources().openRawResource(R.raw.placementcontacts);
        }
        else if(groupName.contentEquals("ECAP")){

            names_is = getResources().openRawResource(R.raw.ecap);
            contacts_is = getResources().openRawResource(R.raw.ecapcontacts);
        }
        else if(groupName.contentEquals("CRs")){

            names_is = getResources().openRawResource(R.raw._class);
            contacts_is = getResources().openRawResource(R.raw.classcontacts);
            regno_is = getResources().openRawResource(R.raw._class_regno);
        }
        else if(groupName.contentEquals("HRs")){

            names_is = getResources().openRawResource(R.raw.hostel);
            contacts_is = getResources().openRawResource(R.raw.hostelcontacts);
            regno_is = getResources().openRawResource(R.raw.hostel_regno);
        }
        else if(groupName.contentEquals("Student clubs")){

            names_is = getResources().openRawResource(R.raw.club);
            contacts_is = getResources().openRawResource(R.raw.clubcontacts);
        }


        // reader
        BufferedReader names_reader = new BufferedReader(new InputStreamReader(names_is));
        BufferedReader contacts_reader = new BufferedReader(new InputStreamReader(contacts_is));

        BufferedReader regno_reader = null;
        if(regno_is != null){
            regno_reader = new BufferedReader(new InputStreamReader(regno_is));
        }

        String line1, line2, line3, line4;
        String[] rowData1, rowData2, rowData3 =null, rowData4 = null;

        try {
            while ((line1 = names_reader.readLine()) != null) {

                line2 = contacts_reader.readLine();


                rowData1 = line1.split("\n");
                rowData2 = line2.split("\n");

                if(regno_reader!=null){
                    line3 = regno_reader.readLine();
                    rowData3 = line3.split("\n");
                    regnoList.add(rowData3[0]);
                }

                userNamesList.add(rowData1[0]);
                contactsList.add(rowData2[0]);


            }
        }catch (Exception ex){
            Log.v(Constants.appName, ex.getMessage());
        }


        // get the list view from activity
        contactsListView = (ListView) findViewById(R.id.contactsListView);
        emptyListText = (TextView) findViewById(R.id.emptyContactListText);

        // get the instance of adapter
        directoryAdapter = new TempGoKartingAdapter(TemporaryGroup_Details.this);

        // set the adapter to listView
        contactsListView.setAdapter(directoryAdapter);


            // set the default list
            directoryAdapter.updateItems(userNamesList, contactsList, regnoList);
            directoryAdapter.notifyDataSetChanged();




    }


}
