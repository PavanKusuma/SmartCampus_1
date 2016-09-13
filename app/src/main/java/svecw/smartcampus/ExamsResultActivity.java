package svecw.smartcampus;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import utils.Constants;

/**
 * Created by Pavan on 6/13/15.
 */
public class ExamsResultActivity extends AppCompatActivity {

    // views of activity
    ImageView resultImage;
    TextView resultsText, questionsCount, correctAnswersCount, wrongAnswersCount, examPercentage;

    int correctAnswers=0, wrongAnswers=0, questionCount=0;

    float percentage=0.0f;

    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_result_activity);


        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.examResult));

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        // object for LayoutInflater
        layoutInflater = LayoutInflater.from(this);

        // get views from activity
        resultImage = (ImageView) findViewById(R.id.resultImage);
        resultsText = (TextView) findViewById(R.id.resultText);
        questionsCount = (TextView) findViewById(R.id.questionsCount);
        correctAnswersCount = (TextView) findViewById(R.id.correctAnswersCount);
        wrongAnswersCount = (TextView) findViewById(R.id.wrongAnswersCount);
        examPercentage = (TextView) findViewById(R.id.percentageTextResult);

        // get intent values
        questionCount = getIntent().getIntExtra(Constants.questionsCount, 0);
        correctAnswers = getIntent().getIntExtra(Constants.correctAnswers, 0);
        wrongAnswers = getIntent().getIntExtra(Constants.wrongAnswers, 0);

        questionsCount.setText(String.valueOf(questionCount));
        correctAnswersCount.setText(String.valueOf(correctAnswers));
        wrongAnswersCount.setText(String.valueOf(wrongAnswers));

        // calculate the percentage
        percentage = (100 * (float) correctAnswers / (float) questionCount);

        examPercentage.setText(String.valueOf(percentage));

        // show the exam result status
        if(percentage >= 50){

            resultImage.setImageResource(R.drawable.ic_result_success);
            resultsText.setText("Passed");
            resultsText.setTextColor(getResources().getColor(R.color.lightGreen));
        }
        else {

            resultImage.setImageResource(R.drawable.ic_result_fail);
            resultsText.setText("All the best for next time");
            resultsText.setTextColor(getResources().getColor(R.color.lightLightRed));
        }



    }

    @Override
    public void onBackPressed() {

        finish();
    }
}
