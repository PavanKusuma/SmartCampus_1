package svecw.smartcampus;

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
public class TemporaryGroup_Hods extends AppCompatActivity {

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
        setContentView(R.layout.temp_group_gokarting);


        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.hods));

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        // get the raw files
        InputStream names_is = getResources().openRawResource(R.raw.hods);
        InputStream contacts_is = getResources().openRawResource(R.raw.hodcontacts);

        // reader
        BufferedReader names_reader = new BufferedReader(new InputStreamReader(names_is));
        BufferedReader contacts_reader = new BufferedReader(new InputStreamReader(contacts_is));

        String line1, line2, line3, line4;
        String[] rowData1, rowData2, rowData3, rowData4 = null;

        try {
            while ((line1 = names_reader.readLine()) != null) {

                line2 = contacts_reader.readLine();


                rowData1 = line1.split("\n");
                rowData2 = line2.split("\n");

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
        directoryAdapter = new TempGoKartingAdapter(TemporaryGroup_Hods.this);

        // set the adapter to listView
        contactsListView.setAdapter(directoryAdapter);


            // set the default list
            directoryAdapter.updateItems(userNamesList, contactsList, regnoList);
            directoryAdapter.notifyDataSetChanged();




    }


}
