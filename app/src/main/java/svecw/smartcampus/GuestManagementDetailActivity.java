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
 * Created by Pavan on 7/1/15.
 */
public class GuestManagementDetailActivity extends AppCompatActivity {

    RelativeLayout founderView, chairmanView, viceChairmanView, secretaryView, jointSecretaryView, principalView, vicePrincipalView;

    String guest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_management_detail_activity);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText("Management");
        title.setTypeface(sansFont);

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        guest = getIntent().getStringExtra(Constants.guest);

        founderView = (RelativeLayout) findViewById(R.id.management1);
        chairmanView = (RelativeLayout) findViewById(R.id.management2);
        viceChairmanView = (RelativeLayout) findViewById(R.id.management3);
        secretaryView = (RelativeLayout) findViewById(R.id.management5);
        jointSecretaryView = (RelativeLayout) findViewById(R.id.management4);
        principalView = (RelativeLayout) findViewById(R.id.management6);
        vicePrincipalView = (RelativeLayout) findViewById(R.id.management7);

        if(guest.contentEquals(Constants.founder)){

            founderView.setVisibility(View.VISIBLE);
        }
        else if(guest.contentEquals(Constants.chairman)){

            chairmanView.setVisibility(View.VISIBLE);
        }
        else if(guest.contentEquals(Constants.viceChairman)){

            viceChairmanView.setVisibility(View.VISIBLE);
        }
        else if(guest.contentEquals(Constants.secretary)){

            secretaryView.setVisibility(View.VISIBLE);
        }
        else if(guest.contentEquals(Constants.jointSecretary)){

            jointSecretaryView.setVisibility(View.VISIBLE);
        }
        else if(guest.contentEquals(Constants.principal)){

            principalView.setVisibility(View.VISIBLE);
        }
        else if(guest.contentEquals(Constants.vicePrincipal)){

            vicePrincipalView.setVisibility(View.VISIBLE);
        }


    }
}
