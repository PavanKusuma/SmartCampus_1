package svecw.smartcampus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import internaldb.SmartCampusDB;
import utils.Constants;

/**
 * Created by Pavan on 1/17/16.
 */
public class SendMessageActivity extends AppCompatActivity{

    // view of activity
    EditText messageText;
    Button sendBtn;

    private static String SOAP_ACTION = "http://www.businesssms.co.in/SubmitSMS";

    private static String NAMESPACE = "http://www.businesssms.co.in/";
    private static String METHOD_NAME = "SubmitSMS";

    private static String URL = "http://businesssms.co.in/WebService/BSWS.asmx";

    String phoneNumber, msg, groupId=Constants.null_indicator;
    Boolean isGroup = false;

    String msgType = Constants.msgTypePhone;

    // progress dialog
    ProgressDialog progressDialog;
    byte[] b = Constants.null_indicator.getBytes();
    SmartCampusDB smartCampusDB = new SmartCampusDB(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_message_layout);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.newTextMessage));

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        phoneNumber = getIntent().getStringExtra(Constants.phoneNumber);
        msgType = getIntent().getStringExtra(Constants.msgType);
        if(getIntent().hasExtra(Constants.groupId)){
            groupId = getIntent().getStringExtra(Constants.groupId);
            isGroup = true;
        }

        Log.i(Constants.appName, "Phonenumber : "+ phoneNumber);

        // get views from activity
        messageText = (EditText) findViewById(R.id.messageText);
        sendBtn = (Button) findViewById(R.id.sendBtn);

        // send message
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // based on the msg type send the message either by Gateway or using user's own phone
                if(msgType.contentEquals(Constants.msgTypeGateway)) {

                    // msg should not be more than 160 chars
                    if (messageText.getText().toString().length() > 160) {

                        msg = messageText.getText().toString().substring(0, 159);
                    } else {

                        msg = messageText.getText().toString();
                    }

                    //Initialize soap request + add parameters
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                    //Use this to add parameters
                    request.addProperty("strID", "dvnraju@svecw.edu.in");
                    request.addProperty("strPwd", "SVECW123");
                    request.addProperty("strPhNo", phoneNumber);
                    //request.addProperty("strPhNo","7799813519");
                    request.addProperty("strText", msg);
                    request.addProperty("strSchedule", "");
                    request.addProperty("intRetryMin", "100");
                    request.addProperty("strSenderID", "SVECWB");
                    request.addProperty("strSenderNo", "9542918778");

                    //Declare the version of the SOAP request
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);

                    //Needed to make the internet call
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                    androidHttpTransport.debug = true;
                    try {
                        //this is the actual part that will call the webservice
                        androidHttpTransport.call(SOAP_ACTION, envelope);

                        String result = envelope.getResponse().toString();

                    } catch (Exception e) {
                        e.printStackTrace();

                        Log.e(Constants.appName, e.getMessage());
                    }
                }

                else{

                    // add the phone number in the data
                    Uri uri = Uri.parse("smsto:" + phoneNumber);

                    Intent smsSIntent = new Intent(Intent.ACTION_SENDTO, uri);
                    // add the message at the sms_body extra field
                    smsSIntent.putExtra("sms_body", msg);
                    try{
                        startActivity(smsSIntent);
                    } catch (Exception ex) {
                        Toast.makeText(SendMessageActivity.this, "Your sms has failed...", Toast.LENGTH_LONG).show();
                        ex.printStackTrace();
                    }

                }

                // Save the message in db
                new NewMessage().execute();
            }
        });

    }



    /**
     * Send message
     */
    class NewMessage extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(SendMessageActivity.this);
            progressDialog.setMessage("Sending message..");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try{

                //byte[] data1 =  "This is awesome".getBytes();
                final ParseFile file = new ParseFile("image.txt", b);
                file.saveInBackground();

                ParseObject parseObject = new ParseObject(Constants.messagesTable);

                parseObject.put(Constants.userObjectId, smartCampusDB.getUser().get(Constants.objectId));
                parseObject.put(Constants.userName, smartCampusDB.getUser().get(Constants.userName));
                parseObject.put(Constants.branch, Constants.null_indicator);

                parseObject.put(Constants.year, 0);
                parseObject.put(Constants.semester, 0);

                parseObject.put(Constants.message, msg);
                parseObject.put(Constants.to, Constants.Group);
                parseObject.put(Constants.isGroup, isGroup);
                parseObject.put(Constants.groupId, groupId);
                parseObject.put(Constants.mediaFile, file);

                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if(e==null){

                            finish();
                        }
                    }
                });


            }
            catch (Exception e){

                progressDialog.dismiss();
                Log.e(Constants.appName, e.getMessage());

                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();
                    }
                });

                // closing this screen
                finish();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);

            // dismiss the dialog once done
            progressDialog.dismiss();

            // success message toast
            Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT).show();

            // closing this screen
            finish();
        }
    }

}
