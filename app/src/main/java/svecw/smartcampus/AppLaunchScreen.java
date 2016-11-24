package svecw.smartcampus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import internaldb.SmartCampusDB;
import utils.Constants;

/**
 * Created by Pavan_Kusuma on 4/26/2015.
 */
public class AppLaunchScreen extends AppCompatActivity {

    // views from layout
    TextView launchText;
    Button guest, login;

    // instance of internal db
    SmartCampusDB smartCampusDB = new SmartCampusDB(this);


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_launch_screen1);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // get views from layout
        launchText = (TextView) findViewById(R.id.launchText); launchText.setTypeface(sansFont);
        login = (Button) findViewById(R.id.login); login.setTypeface(sansFont);
        guest = (Button) findViewById(R.id.guest); guest.setTypeface(sansFont);


/*

        // check if the user is already registered
        if(smartCampusDB.getUser().size() > 0){

            // check if the email of user is verified
            if(smartCampusDB.isUserVerified()){

                Log.v(Constants.appName, "EmailVerified : "+ smartCampusDB.isUserVerified());
                // Navigate the user to home screen
                Intent homeIntent = new Intent(getApplicationContext(), NewHomeActivity.class);
                startActivity(homeIntent);
                finish();
            }
            // show the user Pending Verification Activity
            else{

                Log.v(Constants.appName, "EmailVerified : "+ smartCampusDB.isUserVerified());

                Intent verificationIntent = new Intent(getApplicationContext(), PendingUserVerificationActivity.class);
                startActivity(verificationIntent);
                finish();
            }

        }

*/
        // check if app is downloaded from PlayStore
        if(isStoreVersion(AppLaunchScreen.this)){

            Intent versionIntent = new Intent(getApplicationContext(), CheckAppVersionActivity.class);
            startActivity(versionIntent);
            finish();
        }


        // navigate the user to home activity
        if(smartCampusDB.getUser().size() > 0){

            Intent homeIntent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(homeIntent);
            finish();
        }

        // navigate to login activity
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // navigate to Login showing info
                //navigateToLogin();

                Intent loginIntent = new Intent(getApplicationContext(), GlobalLoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

        // navigate to guest activity
        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent guestIntent = new Intent(getApplicationContext(), GuestActivity.class);
                startActivity(guestIntent);
            }
        });




    }

    // navigate to Login showing the info in popup
    public void navigateToLogin(){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                switch (which) {

                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked

                        Intent loginIntent = new Intent(getApplicationContext(), GlobalLoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                        //Toast.makeText(EditProfileActivity.this, "Yes Clicked", Toast.LENGTH_LONG).show();
                        break;

                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Only Student and Faculty can login to the app!")
                .setPositiveButton("Ok", dialogClickListener).show();


    }


    // check if app is downloaded from PlayStore
    public static boolean isStoreVersion(Context context) {
        boolean result = false;

        try {
            String installer = context.getPackageManager()
                    .getInstallerPackageName(context.getPackageName());
            result = TextUtils.isEmpty(installer);
            Log.i(Constants.appName, "Check" +result);
        } catch (Throwable e) {
        }

        return result;
    }
}
