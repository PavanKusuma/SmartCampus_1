package svecw.smartcampus;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Pavan on 7/5/15.
 */
public class Gre_WordDataActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    //Declaring variable for accessing the word details
    String word;
    String meaning;
    String usage;
    String partsofspeech;
    String synonym;
    String antonym;
    int position;
    String alphabet;

    //TextToSpeech variable
    TextToSpeech tts;

    //Variables from the activity to assign them with values
    TextView txtword;
    TextView txtpos;
    TextView txtmeaning;
    TextView txtsynonyms;
    TextView txtantonyms;
    TextView txtusage;
    ImageView frontarrow;
    ImageView backarrow;
    ImageView imgSpeak;

    //Declaring the ArrayLists to display the content of the screen
    ArrayList<String> words=new ArrayList<String>();
    ArrayList<String> poss=new ArrayList<String>();
    ArrayList<String> meanings=new ArrayList<String>();
    ArrayList<String> synonyms=new ArrayList<String>();
    ArrayList<String> antonyms=new ArrayList<String>();
    ArrayList<String> usages=new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gre_worddata_activity);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText("GRE");

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        //map the variables with the activity elements
        txtword=(TextView)findViewById(R.id.textView1);
        txtpos=(TextView)findViewById(R.id.textView2);
        txtmeaning=(TextView)findViewById(R.id.textView4);
        txtsynonyms=(TextView)findViewById(R.id.textView6);
        txtantonyms=(TextView)findViewById(R.id.textView8);
        txtusage=(TextView)findViewById(R.id.textView10);

        //get the values from the previous activity and assign them to the variables
        Intent i = getIntent();
        word=i.getStringExtra("word");
        partsofspeech=i.getStringExtra("partsofspeech");
        meaning=i.getStringExtra("meaning");
        synonym=i.getStringExtra("synonym");
        antonym=i.getStringExtra("antonym");
        usage=i.getStringExtra("usage");

        position=i.getIntExtra("position", 1);


        alphabet=i.getStringExtra("alphabet");

        //displaying selected data
        txtword.setText(word);
        txtmeaning.setText(meaning);
        txtusage.setText(usage);
        txtpos.setText(partsofspeech);
        txtsynonyms.setText(synonym);
        txtantonyms.setText(antonym);


        ////Application speaks the word when clicked on image////
        tts = new TextToSpeech(this, this);
        imgSpeak = (ImageView) findViewById(R.id.imageView3);
        imgSpeak.setClickable(true);

        imgSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                speakOut();

            }
        });


        ////Navigating between the words////

        frontarrow=(ImageView)findViewById(R.id.imageView1);
        frontarrow.setClickable(true);

        frontarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chkbox.setChecked(false);
                int j = Readlist();

                if (position >= j) {
                    frontarrow.setImageResource(R.drawable.ic_next_disable);

                } else {
                    frontarrow.setImageResource(R.drawable.ic_next);
                    position++;
                    txtword.setText(words.get(position));
                    txtpos.setText(poss.get(position));
                    txtmeaning.setText(meanings.get(position));
                    txtsynonyms.setText(synonyms.get(position));
                    txtantonyms.setText(antonyms.get(position));
                    txtusage.setText(usages.get(position));

                    frontarrow.setImageResource(R.drawable.ic_next);
                    backarrow.setImageResource(R.drawable.ic_previous);
                }
            }
        });

        backarrow=(ImageView)findViewById(R.id.imageView2);
        backarrow.setClickable(true);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int j = Readlist();

                if (position <= 0) {
                    j = j + 1;
                    j = j - 1;
                    backarrow.setImageResource(R.drawable.ic_previous_disable);

                } else {
                    backarrow.setImageResource(R.drawable.ic_previous);
                    position--;
                    txtword.setText(words.get(position));
                    txtpos.setText(poss.get(position));
                    txtmeaning.setText(meanings.get(position));
                    txtsynonyms.setText(synonyms.get(position));
                    txtantonyms.setText(antonyms.get(position));
                    txtusage.setText(usages.get(position));

                    backarrow.setImageResource(R.drawable.ic_previous);
                    frontarrow.setImageResource(R.drawable.ic_next);

                }
            }
        });

        ////Navigating between the words////

    }


    // method to fetch the words from files
    private int Readlist(){

        InputStream is=getResources().openRawResource(R.raw.word);
        InputStream is1=getResources().openRawResource(R.raw.pos);
        InputStream is2=getResources().openRawResource(R.raw.meaning);
        InputStream is3=getResources().openRawResource(R.raw.synonyms);
        InputStream is4=getResources().openRawResource(R.raw.antonyms);
        InputStream is5=getResources().openRawResource(R.raw.usage);

        BufferedReader reader=new BufferedReader(new InputStreamReader(is));
        String line;
        String[] RowData = null;

        BufferedReader reader1=new BufferedReader(new InputStreamReader(is1));
        String line1;
        String[] RowData1 = null;

        BufferedReader reader2=new BufferedReader(new InputStreamReader(is2));
        String line2;
        String[] RowData2 = null;

        BufferedReader reader3=new BufferedReader(new InputStreamReader(is3));
        String line3;
        String[] RowData3 = null;

        BufferedReader reader4=new BufferedReader(new InputStreamReader(is4));
        String line4;
        String[] RowData4 = null;

        BufferedReader reader5=new BufferedReader(new InputStreamReader(is5));
        String line5;
        String[] RowData5 = null;
        int j=-1;

        try{
            //Read each line
            while((line=reader.readLine())!=null){
                line1=reader1.readLine();
                line2=reader2.readLine();
                line3=reader3.readLine();
                line4=reader4.readLine();
                line5=reader5.readLine();
                //split to separate the name from the capital
                RowData=line.split("\n");
                RowData1=line1.split("\n");
                RowData2=line2.split("\n");
                RowData3=line3.split("\n");
                RowData4=line4.split("\n");
                RowData5=line5.split("\n");

                //add values to ArrayList
                if(RowData[0].toUpperCase(Locale.getDefault()).startsWith(alphabet.toUpperCase(Locale.getDefault())))
                {
                    //add values to ArrayList
                    words.add(RowData[0]);
                    poss.add(RowData1[0]);
                    meanings.add(RowData2[0]);
                    synonyms.add(RowData3[0]);
                    antonyms.add(RowData4[0]);
                    usages.add(RowData5[0]);

                    j++;
                }


            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return j;
    }
    @Override
    public void onInit(int arg0) {


    }

    protected void speakOut() {

        String text = txtword.getText().toString();

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);


    }

    @Override
    public void onBackPressed() {

        tts.shutdown();
        finish();
    }
}
