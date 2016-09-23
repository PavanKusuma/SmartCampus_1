package svecw.smartcampus;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import utils.Constants;

/**
 * Created by Pavan on 7/3/15.
 */
public class Guest_HighlightsDetailActivity extends AppCompatActivity {

    RelativeLayout awards, achievement1, achievement2, achievement3, achievement4, achievement5, achievement6, achievement7, achievement8, achievement9, achievement10, achievement11,
            achievement12, achievement13, achievement14, achievement15, achievement16, achievement17, achievement18;

    String achievement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_highlightsdetail_actiivty);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText("Highlights");
        title.setTypeface(sansFont);

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        achievement = getIntent().getStringExtra(Constants.awards);

        awards = (RelativeLayout) findViewById(R.id.awards);
        achievement1 = (RelativeLayout) findViewById(R.id.achievement1);
        achievement2 = (RelativeLayout) findViewById(R.id.achievement2);
        achievement3 = (RelativeLayout) findViewById(R.id.achievement3);
        achievement4 = (RelativeLayout) findViewById(R.id.achievement4);
        achievement5 = (RelativeLayout) findViewById(R.id.achievement5);
        achievement6 = (RelativeLayout) findViewById(R.id.achievement6);
        achievement7 = (RelativeLayout) findViewById(R.id.achievement7);
        achievement8 = (RelativeLayout) findViewById(R.id.achievement8);

        achievement9 = (RelativeLayout) findViewById(R.id.achievement9);
        achievement10 = (RelativeLayout) findViewById(R.id.achievement10);
        achievement11 = (RelativeLayout) findViewById(R.id.achievement11);
        achievement12 = (RelativeLayout) findViewById(R.id.achievement12);
        achievement13 = (RelativeLayout) findViewById(R.id.achievement13);
        achievement14 = (RelativeLayout) findViewById(R.id.achievement14);
        achievement15 = (RelativeLayout) findViewById(R.id.achievement15);
        achievement16 = (RelativeLayout) findViewById(R.id.achievement16);
        achievement17 = (RelativeLayout) findViewById(R.id.achievement17);
        achievement18 = (RelativeLayout) findViewById(R.id.achievement18);


        if(achievement.contentEquals(Constants.awards)){

            awards.setVisibility(View.VISIBLE);
        }
        else if(achievement.contentEquals(Constants.vishuRD)){

            achievement1.setVisibility(View.VISIBLE);
        }
        else if(achievement.contentEquals(Constants.missionRD)){

            achievement2.setVisibility(View.VISIBLE);
        }
        else if(achievement.contentEquals(Constants.jkc)){

            achievement3.setVisibility(View.VISIBLE);
        }
        else if(achievement.contentEquals(Constants.atl)){

            achievement4.setVisibility(View.VISIBLE);
        }
        else if(achievement.contentEquals(Constants.ibm)){

            achievement5.setVisibility(View.VISIBLE);
        }
        else if(achievement.contentEquals(Constants.texas)){

            achievement6.setVisibility(View.VISIBLE);
        }
        else if(achievement.contentEquals(Constants.talentSprint)){

            achievement7.setVisibility(View.VISIBLE);
        }
        else if(achievement.contentEquals(Constants.kCenter)){

            achievement8.setVisibility(View.VISIBLE);
        }
        else if(achievement.contentEquals(Constants.radio)){

            achievement9.setVisibility(View.VISIBLE);
        }
        else if(achievement.contentEquals(Constants.foreign)){

            achievement10.setVisibility(View.VISIBLE);
        }
        else if(achievement.contentEquals(Constants.womenTech)){

            achievement11.setVisibility(View.VISIBLE);
        }
        else if(achievement.contentEquals(Constants.iucee)){

            achievement12.setVisibility(View.VISIBLE);
        }
        else if(achievement.contentEquals(Constants.wifi)){

            achievement13.setVisibility(View.VISIBLE);
        }
        else if(achievement.contentEquals(Constants.counseling)){

            achievement14.setVisibility(View.VISIBLE);
        }
        else if(achievement.contentEquals(Constants.teqip)){

            achievement15.setVisibility(View.VISIBLE);
        }
        else if(achievement.contentEquals(Constants.mou)){

            achievement16.setVisibility(View.VISIBLE);
        }
        else if(achievement.contentEquals(Constants.club)){

            achievement17.setVisibility(View.VISIBLE);
        }
        else if(achievement.contentEquals(Constants.qeee)){

            achievement18.setVisibility(View.VISIBLE);
        }

    }
}
