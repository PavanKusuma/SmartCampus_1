package svecw.smartcampus;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import internaldb.SmartCampusDB;
import model.Questions;
import utils.Constants;

/**
 * Created by Pavan on 6/13/15.
 */
public class QuestionsActivity extends AppCompatActivity {

    // views from activity
    TextView loadingText, questionText, answer1Text, answer2Text, answer3Text, answer4Text, nextQuestion, questionNumber;
    ImageView questionImage;
    LinearLayout verifyAnswer;

    // list of questions
    ArrayList<Questions> questions;
    int numberOfQuestions;

    // questions count
    int questionCounter=0;

    // user choice
    String selectedAnswer = "";

    // record result
    int correctAnswersCount = 0, wrongAnswersCount = 0;

    String examId=null;

    // object for internal db
    SmartCampusDB smartCampusDB = new SmartCampusDB(this);

    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_activity);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.questions));

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
        loadingText = (TextView) findViewById(R.id.loadingMessage);
        questionImage = (ImageView) findViewById(R.id.questionImage);
        questionText = (TextView) findViewById(R.id.questionText);
        answer1Text = (TextView) findViewById(R.id.answer1Text);
        answer2Text = (TextView) findViewById(R.id.answer2Text);
        answer3Text = (TextView) findViewById(R.id.answer3Text);
        answer4Text = (TextView) findViewById(R.id.answer4Text);
        nextQuestion = (TextView) findViewById(R.id.nextQuestion);
        verifyAnswer = (LinearLayout) findViewById(R.id.verifyAnswer);
        questionNumber = (TextView) findViewById(R.id.questionNumber);

        // object for list of questions
        questions = new ArrayList<Questions>();

        // get questions from exam selected
        //questions = getIntent().getParcelableExtra(Constants.questions);
        examId = getIntent().getStringExtra(Constants.examId);
        numberOfQuestions = getIntent().getIntExtra(Constants.numberOfQuestions, 0);

        // get questions of the given examId from internal db
        //questions = smartCampusDB.getQuestions(examId);
        new GetQuestions().execute(examId);


        // get user's choice
        answer1Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get the answer
                selectedAnswer = answer1Text.getText().toString();

                // change textView properties to appear selected
                answer1Text.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                answer1Text.setTextColor(getResources().getColor(R.color.white));

                answer2Text.setBackgroundColor(getResources().getColor(R.color.white));
                answer2Text.setTextColor(getResources().getColor(R.color.lightBlue));

                answer3Text.setBackgroundColor(getResources().getColor(R.color.white));
                answer3Text.setTextColor(getResources().getColor(R.color.lightBlue));

                answer4Text.setBackgroundColor(getResources().getColor(R.color.white));
                answer4Text.setTextColor(getResources().getColor(R.color.lightBlue));


            }
        });


        answer2Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get the answer
                selectedAnswer = answer2Text.getText().toString();

                // change textView properties to appear selected
                answer1Text.setBackgroundColor(getResources().getColor(R.color.white));
                answer1Text.setTextColor(getResources().getColor(R.color.lightBlue));

                answer2Text.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                answer2Text.setTextColor(getResources().getColor(R.color.white));

                answer3Text.setBackgroundColor(getResources().getColor(R.color.white));
                answer3Text.setTextColor(getResources().getColor(R.color.lightBlue));

                answer4Text.setBackgroundColor(getResources().getColor(R.color.white));
                answer4Text.setTextColor(getResources().getColor(R.color.lightBlue));


            }
        });

        answer3Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get the answer
                selectedAnswer = answer3Text.getText().toString();

                // change textView properties to appear selected
                answer1Text.setBackgroundColor(getResources().getColor(R.color.white));
                answer1Text.setTextColor(getResources().getColor(R.color.lightBlue));

                answer2Text.setBackgroundColor(getResources().getColor(R.color.white));
                answer2Text.setTextColor(getResources().getColor(R.color.lightBlue));

                answer3Text.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                answer3Text.setTextColor(getResources().getColor(R.color.white));

                answer4Text.setBackgroundColor(getResources().getColor(R.color.white));
                answer4Text.setTextColor(getResources().getColor(R.color.lightBlue));


            }
        });

        answer4Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get the answer
                selectedAnswer = answer4Text.getText().toString();

                // change textView properties to appear selected
                answer1Text.setBackgroundColor(getResources().getColor(R.color.white));
                answer1Text.setTextColor(getResources().getColor(R.color.lightBlue));

                answer2Text.setBackgroundColor(getResources().getColor(R.color.white));
                answer2Text.setTextColor(getResources().getColor(R.color.lightBlue));

                answer3Text.setBackgroundColor(getResources().getColor(R.color.white));
                answer3Text.setTextColor(getResources().getColor(R.color.lightBlue));

                answer4Text.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                answer4Text.setTextColor(getResources().getColor(R.color.white));


            }
        });

        // show the correct answer by highlighting the answer background with green color
        // and if selected answer is not correct, the highlight it with red color
        verifyAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedAnswer.contentEquals(questions.get(questionCounter).getCorrectAnswer())){

                    Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
/*
                    Snackbar snackbar = Snackbar.make(v, "Correct", Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.lightBlue));
                    snackbar.show();
*/

                } else {

                    Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();
/*
                    Snackbar snackbar = Snackbar.make(v, "Incorrect", Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.lightLightRed));
                    snackbar.show();
*/
                }

            }
        });


        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedAnswer.length()>0) {

                    // check if the displayed question is present
                    if (questionCounter < questions.size()) {

                        Log.v("Selected Answer : ", selectedAnswer);
                        Log.v("Correct Answer : ", questions.get(questionCounter).getCorrectAnswer());

                        // record the exam result
                        if (selectedAnswer.contentEquals(questions.get(questionCounter).getCorrectAnswer())) {

                            correctAnswersCount++;

                        } else {

                            wrongAnswersCount++;

                        }

                        // make selected answer to null to get the fresh selected answer for next question
                        selectedAnswer = "";


                        // increment to get next question
                        questionCounter++;

                        if (questionCounter >= questions.size()) {

                            // pass the result to result activity
                            Intent resultIntent = new Intent(getApplicationContext(), ExamsResultActivity.class);
                            resultIntent.putExtra(Constants.questionsCount, questions.size());
                            resultIntent.putExtra(Constants.correctAnswers, correctAnswersCount);
                            resultIntent.putExtra(Constants.wrongAnswers, wrongAnswersCount);
                            startActivity(resultIntent);
                            finish();
                        } else {

                            questionText.setText(questions.get(questionCounter).getQuestion());
                            answer1Text.setText(questions.get(questionCounter).getAnswer1());
                            answer2Text.setText(questions.get(questionCounter).getAnswer2());
                            answer3Text.setText(questions.get(questionCounter).getAnswer3());
                            answer4Text.setText(questions.get(questionCounter).getAnswer4());
                            questionNumber.setText((questionCounter+1) + "/" + numberOfQuestions);

                            // change textView properties to appear selected
                            answer1Text.setBackgroundColor(getResources().getColor(R.color.white));
                            answer1Text.setTextColor(getResources().getColor(R.color.lightBlue));

                            answer2Text.setBackgroundColor(getResources().getColor(R.color.white));
                            answer2Text.setTextColor(getResources().getColor(R.color.lightBlue));

                            answer3Text.setBackgroundColor(getResources().getColor(R.color.white));
                            answer3Text.setTextColor(getResources().getColor(R.color.lightBlue));

                            answer4Text.setBackgroundColor(getResources().getColor(R.color.white));
                            answer4Text.setTextColor(getResources().getColor(R.color.lightBlue));
                        }

                    }
                }

                else {

                    Toast.makeText(getApplicationContext(), "Please Answer to move forward", Toast.LENGTH_SHORT).show();
                }
                // exam is completed
                /*else {


                    // pass the result to result activity
                    Intent resultIntent = new Intent(getApplicationContext(), ExamsResultActivity.class);
                    resultIntent.putExtra(Constants.questionsCount, questions.size());
                    resultIntent.putExtra(Constants.correctAnswers, correctAnswersCount);
                    resultIntent.putExtra(Constants.wrongAnswers, wrongAnswersCount);
                    startActivity(resultIntent);
                    finish();
                }*/
            }
        });



    }


    class GetQuestions extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            runOnUiThread(new Runnable() {
                public void run() {


                    //Toast.makeText(getApplicationContext(), "Please wait, loading...", Toast.LENGTH_SHORT).show();
                    // save the data to shared preferences
                    //sharedPreferences.saveWallPosts(studentWallPostsList);

                }
            });
        }

        @Override
        protected String doInBackground(final String... params) {

            // date format for displaying created date
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy, mm:ss");
            simpleDateFormat.format(new Date());

            try {

                // create parse object for the "Exam_Questions"
                ParseQuery<ParseObject> studentPostsQuery = ParseQuery.getQuery(Constants.Exam_Questions);

                // query for the selected exam id
                studentPostsQuery.whereEqualTo(Constants.examId, params[0]);

                // get the list of shouts
                studentPostsQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {

                        if (e == null) {

                            if (parseObjects.size() > 0) {

                                // navigate through the list of objects
                                for (int i = 0; i < parseObjects.size(); i++) {

                                    try {

                                        // create object for collegeWallPost class
                                        final Questions question = new Questions();

                                        // ensuring correctAnswersCount and wrongAnswersCount is zero
                                        correctAnswersCount = 0;
                                        wrongAnswersCount = 0;

                                        // set the values of the wall post into object
                                        question.setQuestion(parseObjects.get(i).getString(Constants.question));
                                        question.setAnswer1(parseObjects.get(i).getString(Constants.answer1));
                                        question.setAnswer2(parseObjects.get(i).getString(Constants.answer2));
                                        question.setAnswer3(parseObjects.get(i).getString(Constants.answer3));
                                        question.setAnswer4(parseObjects.get(i).getString(Constants.answer4));
                                        question.setExamId(params[0]);
                                        question.setCorrectAnswer(parseObjects.get(i).getString(Constants.correctAnswer));
                                        question.setQuestionId(parseObjects.get(i).getObjectId());


                                        questions.add(question);

                                    } catch (Exception ex) {

                                        Log.v(Constants.appName, ex.getMessage());

                                    }
                                }


                                runOnUiThread(new Runnable() {
                                    public void run() {

                                        if (questions.size() > 0) {

                                            // hide the loading text
                                            loadingText.setVisibility(View.GONE);

                                            // assign the first question and answers
                                            questionText.setText(questions.get(questionCounter).getQuestion());
                                            answer1Text.setText(questions.get(questionCounter).getAnswer1());
                                            answer2Text.setText(questions.get(questionCounter).getAnswer2());
                                            answer3Text.setText(questions.get(questionCounter).getAnswer3());
                                            answer4Text.setText(questions.get(questionCounter).getAnswer4());
                                            questionNumber.setText((questionCounter+1) + "/" + numberOfQuestions);

                                        }

                                    }
                                });



                            }
                        }
                    }
                });

            }

            // all parse data not found exceptions are caught
            catch(Exception e){
                Log.v(Constants.appName, e.getMessage());
                //finish();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), "Please wait, loading...", Toast.LENGTH_SHORT).show();

/*
            if(questions.size()>0) {

                // navigate to questions activity
                Intent questionsIntent = new Intent(getApplicationContext(), QuestionsActivity.class);
                questionsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                questionsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                questionsIntent.putExtra(Constants.questions, questions);
                startActivity(questionsIntent);

            } else {


                Snackbar snackbar = Snackbar.make(questionText, "Please try later", Snackbar.LENGTH_SHORT);
                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.lightLightRed));
                snackbar.show();
            }*/
        }


    }



}
