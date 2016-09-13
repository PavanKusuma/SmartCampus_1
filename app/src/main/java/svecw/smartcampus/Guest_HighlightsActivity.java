package svecw.smartcampus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import utils.Constants;

/**
 * Created by Pavan on 7/7/15.
 */
public class Guest_HighlightsActivity extends AppCompatActivity {

    RelativeLayout awardsView, vishuRDView, missionRDView, jkcView, atlView, ibmView, texasView, talentSprintView, kCenterView, radioView, foreignView, womenTechView,
    iuceeView, wifiView, couselingView, teqipView, mouView, clubView, qeeeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_highlights_activity);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText("Highlights");

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        awardsView = (RelativeLayout) findViewById(R.id.awardsView);
        vishuRDView = (RelativeLayout) findViewById(R.id.vishuRDHeadingView);
        missionRDView = (RelativeLayout) findViewById(R.id.missionRDHeadingView);
        jkcView = (RelativeLayout) findViewById(R.id.jkcHeadingView);
        atlView = (RelativeLayout) findViewById(R.id.atlHeadingView);
        ibmView = (RelativeLayout) findViewById(R.id.ibmHeadingView);
        texasView = (RelativeLayout) findViewById(R.id.texasLabHeadingView);
        talentSprintView = (RelativeLayout) findViewById(R.id.talentSprintHeadingView);
        kCenterView = (RelativeLayout) findViewById(R.id.knowledgeCenterHeadingView);

        radioView = (RelativeLayout) findViewById(R.id.radioView);
        foreignView = (RelativeLayout) findViewById(R.id.foreignView);
        womenTechView = (RelativeLayout) findViewById(R.id.womenTechView);
        iuceeView = (RelativeLayout) findViewById(R.id.iuceeView);
        wifiView = (RelativeLayout) findViewById(R.id.wifiView);
        couselingView = (RelativeLayout) findViewById(R.id.counselingView);
        teqipView = (RelativeLayout) findViewById(R.id.teqipView);
        mouView = (RelativeLayout) findViewById(R.id.mouView);
        clubView = (RelativeLayout) findViewById(R.id.clubView);
        qeeeView = (RelativeLayout) findViewById(R.id.qeeeView);

        awardsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Guest_HighlightsDetailActivity.class);
                i.putExtra(Constants.awards, Constants.awards);
                startActivity(i);

            }
        });

        vishuRDView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Guest_HighlightsDetailActivity.class);
                i.putExtra(Constants.awards, Constants.vishuRD);
                startActivity(i);

            }
        });
        missionRDView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Guest_HighlightsDetailActivity.class);
                i.putExtra(Constants.awards, Constants.missionRD);
                startActivity(i);

            }
        });
        jkcView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Guest_HighlightsDetailActivity.class);
                i.putExtra(Constants.awards, Constants.jkc);
                startActivity(i);

            }
        });
        atlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Guest_HighlightsDetailActivity.class);
                i.putExtra(Constants.awards, Constants.atl);
                startActivity(i);

            }
        });
        ibmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Guest_HighlightsDetailActivity.class);
                i.putExtra(Constants.awards, Constants.ibm);
                startActivity(i);

            }
        });
        texasView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Guest_HighlightsDetailActivity.class);
                i.putExtra(Constants.awards, Constants.texas);
                startActivity(i);

            }
        });
        talentSprintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Guest_HighlightsDetailActivity.class);
                i.putExtra(Constants.awards, Constants.talentSprint);
                startActivity(i);

            }
        });
        kCenterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Guest_HighlightsDetailActivity.class);
                i.putExtra(Constants.awards, Constants.kCenter);
                startActivity(i);

            }
        });

        radioView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Guest_HighlightsDetailActivity.class);
                i.putExtra(Constants.awards, Constants.radio);
                startActivity(i);

            }
        });


        foreignView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Guest_HighlightsDetailActivity.class);
                i.putExtra(Constants.awards, Constants.foreign);
                startActivity(i);

            }
        });

        womenTechView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Guest_HighlightsDetailActivity.class);
                i.putExtra(Constants.awards, Constants.womenTech);
                startActivity(i);

            }
        });

        iuceeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Guest_HighlightsDetailActivity.class);
                i.putExtra(Constants.awards, Constants.iucee);
                startActivity(i);

            }
        });
        wifiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Guest_HighlightsDetailActivity.class);
                i.putExtra(Constants.awards, Constants.wifi);
                startActivity(i);

            }
        });

        couselingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Guest_HighlightsDetailActivity.class);
                i.putExtra(Constants.awards, Constants.counseling);
                startActivity(i);

            }
        });
        teqipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Guest_HighlightsDetailActivity.class);
                i.putExtra(Constants.awards, Constants.teqip);
                startActivity(i);

            }
        });
        mouView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Guest_HighlightsDetailActivity.class);
                i.putExtra(Constants.awards, Constants.mou);
                startActivity(i);

            }
        });

        clubView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Guest_HighlightsDetailActivity.class);
                i.putExtra(Constants.awards, Constants.club);
                startActivity(i);

            }
        });
        qeeeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Guest_HighlightsDetailActivity.class);
                i.putExtra(Constants.awards, Constants.qeee);
                startActivity(i);

            }
        });

    }
}
