package svecw.smartcampus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Pavan on 6/2/15.
 */
public class GuestActivity extends Activity {

    // views from activity
    LinearLayout aboutUsView, managementView, coursesView, placementsView, initiativesView, contactUsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_activity);

        // get views from activity
        aboutUsView = (LinearLayout) findViewById(R.id.guestAboutUsView);
        managementView = (LinearLayout) findViewById(R.id.guestManagementView);
        coursesView = (LinearLayout) findViewById(R.id.guestCoursesView);
        placementsView = (LinearLayout) findViewById(R.id.guestPlacementsView);
        initiativesView = (LinearLayout) findViewById(R.id.guestInitiativesView);
        contactUsView = (LinearLayout) findViewById(R.id.guestContactUsView);


        ImageView rl = (ImageView) findViewById(R.id.completeLayout1);
        rl.setImageResource(R.drawable.bg_animate);
        AnimationDrawable ad = (AnimationDrawable) rl.getDrawable();//getBackground();
        ad.start();

        aboutUsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent aboutUsIntent = new Intent(getApplicationContext(), GuestAboutActivity.class);
                startActivity(aboutUsIntent);

            }
        });

        managementView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent managementIntent = new Intent(getApplicationContext(), GuestManagementActivity.class);
                startActivity(managementIntent);

            }
        });

        coursesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent coursesIntent = new Intent(getApplicationContext(), GuestCoursesActivity.class);
                startActivity(coursesIntent);
            }
        });

        placementsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent placementsIntent = new Intent(getApplicationContext(), PlacementsActivity.class);
                startActivity(placementsIntent);
            }
        });

        initiativesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent achievementsIntent = new Intent(getApplicationContext(), Guest_HighlightsActivity.class);
                startActivity(achievementsIntent);
            }
        });

        contactUsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent contactUsIntent = new Intent(getApplicationContext(), GuestContactUsActivity.class);
                startActivity(contactUsIntent);
            }
        });



    }
}
