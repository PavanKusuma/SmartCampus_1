package svecw.smartcampus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import utils.Constants;

/**
 * Created by Pavan on 7/6/15.
 */
public class GuestManagementActivity extends AppCompatActivity {

    RelativeLayout founderView, chairmanView, viceChairmanView, secretaryView, jointSecretaryView, principalView, vicePrincipalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_management_activity);

        founderView = (RelativeLayout) findViewById(R.id.founderView);
        chairmanView = (RelativeLayout) findViewById(R.id.chairmanView);
        viceChairmanView = (RelativeLayout) findViewById(R.id.viceChairmanView);
        secretaryView = (RelativeLayout) findViewById(R.id.secretaryView);
        jointSecretaryView = (RelativeLayout) findViewById(R.id.jointSecretaryView);
        principalView = (RelativeLayout) findViewById(R.id.principalView);
        vicePrincipalView = (RelativeLayout) findViewById(R.id.vicePrincipalView);

        founderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), GuestManagementDetailActivity.class);
                i.putExtra(Constants.guest, Constants.founder);
                startActivity(i);
            }
        });

        chairmanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), GuestManagementDetailActivity.class);
                i.putExtra(Constants.guest, Constants.chairman);
                startActivity(i);
            }
        });

        viceChairmanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), GuestManagementDetailActivity.class);
                i.putExtra(Constants.guest, Constants.viceChairman);
                startActivity(i);
            }
        });

        secretaryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), GuestManagementDetailActivity.class);
                i.putExtra(Constants.guest, Constants.secretary);
                startActivity(i);
            }
        });

        jointSecretaryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), GuestManagementDetailActivity.class);
                i.putExtra(Constants.guest, Constants.jointSecretary);
                startActivity(i);
            }
        });

        principalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), GuestManagementDetailActivity.class);
                i.putExtra(Constants.guest, Constants.principal);
                startActivity(i);
            }
        });

        vicePrincipalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), GuestManagementDetailActivity.class);
                i.putExtra(Constants.guest, Constants.vicePrincipal);
                startActivity(i);
            }
        });
    }
}
