package svecw.smartcampus;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import utils.Constants;

/**
 * Created by Pavan on 7/5/15.
 */
public class Gre_WordsListActivity extends AppCompatActivity {

    //Declaring the ArrayLists to display the content of the screen
    ArrayList<String> words=new ArrayList<String>();
    ArrayList<String> poss=new ArrayList<String>();
    ArrayList<String> meanings=new ArrayList<String>();
    ArrayList<String> synonyms=new ArrayList<String>();
    ArrayList<String> antonyms=new ArrayList<String>();
    ArrayList<String> usages=new ArrayList<String>();

    //this string is used to get the alphabet that is selected in GridView
    String alphabet;

    //this variable is used to get the ListView defined
    ListView lv;

    //List for reading words from method - Readlist()
    ArrayList<String> list;

    //adapter for populating ListView
    ArrayAdapter<String> a;
    ArrayAdapter<String> a1;

    CharSequence as;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gre_wordslist_activity);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText("GRE");
        title.setTypeface(sansFont);

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        //get the alphabet from the previous activity
        alphabet = getIntent().getStringExtra(Constants.alphabet);

        //calling the method to populate the word List
        list=Readlist(alphabet);

        //get the ListView of activity
        lv=(ListView)findViewById(R.id.wordsListView);

        //set adapter to ListView
        a=new ArrayAdapter<String>(this,R.layout.greword_singlelistitem,R.id.textView1,list);
        lv.setAdapter(a);
        a.setNotifyOnChange(true);

        //Get the Search text and populate the list accordingly
        EditText inputSearch = (EditText) findViewById(R.id.searchWord);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                as=cs;
                Gre_WordsListActivity.this.a.getFilter().filter(cs);
                //lv.setAdapter(AlphabetList.this.a.getFilter().filter(cs));
                a.notifyDataSetChanged();

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

                // 	AlphabetList.this.a.getFilter().filter(as);
                //	a.notifyDataSetChanged();

            }
        });


        ////////// Set the On click event for list item and pass on to the word data activity ////////////////

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i=new Intent(getApplicationContext(),Gre_WordDataActivity.class);
                //sending data to new activity


                //String word=((TextView)v).getText().toString();
                String word=lv.getItemAtPosition(position).toString();
                i.putExtra("word",word);

                int wi = words.lastIndexOf(word);

                String partsofspeech=poss.get(wi);
                i.putExtra("partsofspeech",partsofspeech);

                String meaning=meanings.get(wi);
                i.putExtra("meaning",meaning);

                String synonym=synonyms.get(wi);
                i.putExtra("synonym",synonym);

                String antonym=antonyms.get(wi);
                i.putExtra("antonym",antonym);

                String usage=usages.get(wi);
                i.putExtra("usage",usage);

                int wordpos=wi;
                i.putExtra("position", wordpos);

                i.putExtra("alphabet", alphabet);

                //starting new activity
                startActivity(i);

            }
        });

    }

    //gets the word details from files //
    private ArrayList<String> Readlist(String str){

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
                if(RowData[0].toUpperCase(Locale.getDefault()).startsWith(str.toUpperCase(Locale.getDefault())))
                {
                    //add values to ArrayList
                    words.add(RowData[0]);
                    poss.add(RowData1[0]);
                    meanings.add(RowData2[0]);
                    synonyms.add(RowData3[0]);
                    antonyms.add(RowData4[0]);
                    usages.add(RowData5[0]);
                }



            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return words;
    }
}
