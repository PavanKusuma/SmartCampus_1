package svecw.smartcampus;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import utils.Constants;

/**
 * Created by Pavan on 7/5/15.
 */
public class Gre_MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String alphabet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gre_mainactivity);

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


        TextView t1=(TextView)findViewById(R.id.TextView01);
        TextView t2=(TextView)findViewById(R.id.TextView02);
        TextView t3=(TextView)findViewById(R.id.TextView03);
        TextView t4=(TextView)findViewById(R.id.TextView04);
        TextView t5=(TextView)findViewById(R.id.TextView05);
        TextView t6=(TextView)findViewById(R.id.TextView06);
        TextView t7=(TextView)findViewById(R.id.TextView07);
        TextView t8=(TextView)findViewById(R.id.TextView08);
        TextView t9=(TextView)findViewById(R.id.TextView09);
        TextView t10=(TextView)findViewById(R.id.TextView10);
        TextView t11=(TextView)findViewById(R.id.TextView11);
        TextView t12=(TextView)findViewById(R.id.TextView12);
        TextView t13=(TextView)findViewById(R.id.TextView13);
        TextView t14=(TextView)findViewById(R.id.TextView14);
        TextView t15=(TextView)findViewById(R.id.TextView15);
        TextView t16=(TextView)findViewById(R.id.TextView16);
        TextView t17=(TextView)findViewById(R.id.TextView17);
        TextView t18=(TextView)findViewById(R.id.TextView18);
        TextView t19=(TextView)findViewById(R.id.TextView19);
        TextView t20=(TextView)findViewById(R.id.TextView20);
        TextView t21=(TextView)findViewById(R.id.TextView21);
        TextView t22=(TextView)findViewById(R.id.TextView22);
        TextView t23=(TextView)findViewById(R.id.TextView23);
        TextView t24=(TextView)findViewById(R.id.TextView24);
        TextView t25=(TextView)findViewById(R.id.TextView25);
        TextView t26=(TextView)findViewById(R.id.TextView26);

        t1.setTypeface(sansFont);
        t2.setTypeface(sansFont);
        t3.setTypeface(sansFont);
        t4.setTypeface(sansFont);
        t5.setTypeface(sansFont);
        t6.setTypeface(sansFont);
        t7.setTypeface(sansFont);
        t8.setTypeface(sansFont);
        t9.setTypeface(sansFont);
        t10.setTypeface(sansFont);
        t11.setTypeface(sansFont);
        t12.setTypeface(sansFont);
        t13.setTypeface(sansFont);
        t14.setTypeface(sansFont);
        t15.setTypeface(sansFont);
        t16.setTypeface(sansFont);
        t17.setTypeface(sansFont);
        t18.setTypeface(sansFont);
        t19.setTypeface(sansFont);
        t20.setTypeface(sansFont);
        t21.setTypeface(sansFont);
        t22.setTypeface(sansFont);
        t23.setTypeface(sansFont);
        t24.setTypeface(sansFont);
        t25.setTypeface(sansFont);
        t26.setTypeface(sansFont);

        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
        t3.setOnClickListener(this);
        t4.setOnClickListener(this);
        t5.setOnClickListener(this);
        t6.setOnClickListener(this);
        t7.setOnClickListener(this);
        t8.setOnClickListener(this);
        t9.setOnClickListener(this);
        t10.setOnClickListener(this);
        t11.setOnClickListener(this);
        t12.setOnClickListener(this);
        t13.setOnClickListener(this);
        t14.setOnClickListener(this);
        t15.setOnClickListener(this);
        t16.setOnClickListener(this);
        t17.setOnClickListener(this);
        t18.setOnClickListener(this);
        t19.setOnClickListener(this);
        t20.setOnClickListener(this);
        t21.setOnClickListener(this);
        t22.setOnClickListener(this);
        t23.setOnClickListener(this);
        t24.setOnClickListener(this);
        t25.setOnClickListener(this);
        t26.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.TextView01:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView02:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView03:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView04:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView05:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView06:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView07:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView08:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView09:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView10:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView11:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView12:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView13:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView14:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView15:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView16:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView17:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView18:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView19:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView20:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView21:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView22:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView23:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView24:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView25:
                alphabet=((TextView)v).getText().toString();
                break;
            case R.id.TextView26:
                alphabet=((TextView)v).getText().toString();
                break;

        }


        Intent i=new Intent(getApplicationContext(),Gre_WordsListActivity.class);
        i.putExtra(Constants.alphabet, alphabet);
        startActivity(i);
    }
}
