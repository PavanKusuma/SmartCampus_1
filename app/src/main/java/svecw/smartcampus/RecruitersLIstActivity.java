package svecw.smartcampus;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import utils.Constants;

/**
 * Created by Pavan_Kusuma on 6/21/2016.
 */
public class RecruitersLIstActivity extends AppCompatActivity {

    ListView placementsListView;

    String year;

    List<String> recruitersList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recruiters_list_layout);


        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.recruiters));
        title.setTypeface(sansFont);

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        placementsListView = (ListView) findViewById(R.id.listView1);
        recruitersList = new ArrayList<String>();

        // get the placement year
        year = getIntent().getStringExtra(Constants.year);


            // get the raw files
            InputStream is_07 = getResources().openRawResource(R.raw.recruiterslist);
            //is_count_07 = getResources().openRawResource(R.raw.count7);

            // reader
            BufferedReader reader_07 = new BufferedReader(new InputStreamReader(is_07));
            //reader_count_07 = new BufferedReader(new InputStreamReader(is_count_07));

            String line1;
            String[] rowData1 = null;


            try {
                while ((line1 = reader_07.readLine()) != null) {


                    rowData1 = line1.split("\n");



                    recruitersList.add(rowData1[0]);

                }
            } catch (Exception ex) {
                Log.v(Constants.appName, ex.getMessage());
            }

System.out.print(recruitersList.size() + ", " + recruitersList.get(0) );
/*
        for(int i=0; i<recruitersList.size(); i++){

            Placements placements = new Placements();
            placements.setCompany(companyList.get(i));
            placements.setCount(countList.get(i));
            placements.setYear(year);

            placementsList.add(placements);
        }*/

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recruitersList);
        placementsListView.setAdapter(adapter);


    }
}
