package svecw.smartcampus;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adapters.ExamsAdapter;
import internaldb.SmartCampusDB;
import model.Exams;
import model.Questions;
import utils.Constants;

/**
 * Created by Pavan on 6/12/15.
 */
public class ExamsListActivity extends AppCompatActivity {

    // views of activity
    ListView examsListView;
    TextView emptyElementExams;
    ProgressBar progressBar;

    ExamsAdapter examsAdapter;
    ArrayList<Exams> examsList;

    // list of questions
    ArrayList<Questions> questions;

    // object for internal db
    SmartCampusDB smartCampusDB = new SmartCampusDB(this);

    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exams_list_activity);


        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // change the title according to the activity
        TextView title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.exams));
        title.setTypeface(sansFont);

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
        examsListView = (ListView) findViewById(R.id.examsListView);
        emptyElementExams = (TextView) findViewById(R.id.emptyElementExams); emptyElementExams.setTypeface(sansFont);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // list of exams
        examsList = new ArrayList<Exams>();
        questions = new ArrayList<Questions>();

        // object for adapter
        examsAdapter = new ExamsAdapter(ExamsListActivity.this);
        examsListView.setAdapter(examsAdapter);

        //examsListView.setEmptyView(emptyElementExams);

        // get Exams list
        //new GetExamsList().execute();

        // date format for displaying created date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy, mm:ss");
        simpleDateFormat.format(new Date());

        try {
            // create parse object for the "CollegeWall"
            ParseQuery<ParseObject> studentPostsQuery = ParseQuery.getQuery(Constants.Exams_Table);

            studentPostsQuery.whereGreaterThanOrEqualTo(Constants.expiryDate, new Date());// DescendingOrder(Constants.createdAt);
            // get mapping for the list of objectIds
            //issuesQuery.whereEqualTo(Constants.TYPE, Constants.OFFICIAL);

            // get the list of shouts
            studentPostsQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {

                    if (e == null) {

                        if (parseObjects.size() > 0) {

                            // navigate through the list of objects
                            for (int i = 0; i < parseObjects.size(); i++) {

                                try {
                                    // date format for displaying created date
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm");

                                    // create object for Exams class
                                    final Exams exam = new Exams();

                                    // set the values of the wall post into object
                                    exam.setName(parseObjects.get(i).getString(Constants.name));
                                    exam.setExpiryDate(simpleDateFormat.format(parseObjects.get(i).get(Constants.expiryDate)));
                                    exam.setNumberOfQuestions(parseObjects.get(i).getInt(Constants.numberOfQuestions));
                                    exam.setObjectId(parseObjects.get(i).getObjectId());

                                    examsList.add(exam);

                                    smartCampusDB.insertExam(exam);
                                    examsAdapter.updateItems(examsList);
                                    examsAdapter.notifyDataSetChanged();

                                    progressBar.setVisibility(View.GONE);


                                } catch (Exception ex) {

                                    Log.v(Constants.appName, ex.getMessage());
                                    progressBar.setVisibility(View.GONE);
                                }
                            }

                        } else {


                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
            });

        }

        // all parse data not found exceptions are caught
        catch(Exception e){
            finish();
        }


    }

    class GetExamsList extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            // date format for displaying created date
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy, mm:ss");
            simpleDateFormat.format(new Date());

            try {
                // create parse object for the "CollegeWall"
                ParseQuery<ParseObject> studentPostsQuery = ParseQuery.getQuery(Constants.Exams_Table);

                studentPostsQuery.whereGreaterThanOrEqualTo(Constants.expiryDate, new Date());// DescendingOrder(Constants.createdAt);
                // get mapping for the list of objectIds
                //issuesQuery.whereEqualTo(Constants.TYPE, Constants.OFFICIAL);

                // get the list of shouts
                studentPostsQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {

                        if (e == null) {

                            if (parseObjects.size() > 0) {

                                // navigate through the list of objects
                                for (int i = 0; i < parseObjects.size(); i++) {

                                    try {
                                        // date format for displaying created date
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm");

                                        // create object for Exams class
                                        final Exams exam = new Exams();

                                        // set the values of the wall post into object
                                        exam.setName(parseObjects.get(i).getString(Constants.name));
                                        exam.setExpiryDate(simpleDateFormat.format(parseObjects.get(i).get(Constants.expiryDate)));
                                        exam.setObjectId(parseObjects.get(i).getObjectId());
                                        exam.setNumberOfQuestions(parseObjects.get(i).getInt(Constants.numberOfQuestions));

                                        examsList.add(exam);

                                        examsAdapter.updateItems(examsList);
                                        examsAdapter.notifyDataSetChanged();

                                        progressBar.setVisibility(View.GONE);

                                        // verify if the given examId is present in internal db
                                        if(!smartCampusDB.verifyExamId(exam.getObjectId())) {

                                            // insert exam record
                                            smartCampusDB.insertExam(exam);

                                            // create parse object for the "Exam_Questions"
                                            ParseQuery<ParseObject> studentPostsQuery = ParseQuery.getQuery(Constants.Exam_Questions);

                                            // query for the selected exam id
                                            studentPostsQuery.whereEqualTo(Constants.examId, exam.getObjectId());

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

                                                                    // set the values of the wall post into object
                                                                    question.setQuestion(parseObjects.get(i).getString(Constants.question));
                                                                    question.setExamId(parseObjects.get(i).getObjectId());
                                                                    question.setCorrectAnswer(parseObjects.get(i).getString(Constants.correctAnswer));
                                                                    question.setQuestionId(parseObjects.get(i).getObjectId());
                                                                    question.setAnswer1(parseObjects.get(i).getString(Constants.answer1));
                                                                    question.setAnswer2(parseObjects.get(i).getString(Constants.answer2));
                                                                    question.setAnswer3(parseObjects.get(i).getString(Constants.answer3));
                                                                    question.setAnswer4(parseObjects.get(i).getString(Constants.answer4));
                                                                    question.setExplanation(parseObjects.get(i).getString(Constants.explanation));

                                                                    questions.add(question);

                                                                    System.out.println(questions);

                                                                } catch (Exception ex) {

                                                                    Log.v(Constants.appName, ex.getMessage());

                                                                }
                                                            }

                                                            // insert questions to internal db
                                                            smartCampusDB.insertQuestions(questions);
                                                        }
                                                    }
                                                }
                                            });
                                        } else {


                                        }


                                    } catch (Exception ex) {

                                        Log.v(Constants.appName, ex.getMessage());
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }

                            } else {


                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                });

            }

            // all parse data not found exceptions are caught
            catch(Exception e){
                finish();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);

            runOnUiThread(new Runnable() {
                public void run() {

                    /**
                     * Updating parsed JSON data into ListView
                     * */

                    examsAdapter.updateItems(examsList);
                    // notify the adapter
                    examsAdapter.notifyDataSetChanged();

                    // save the data to shared preferences
                    //sharedPreferences.saveWallPosts(studentWallPostsList);

                }
            });
        }
    }

    /*
     * This method will resume the activity after returning from other activity
     * hence, adapter to the listview is set here
     *
     * (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();

        examsAdapter.updateItems(examsList);
        examsAdapter.notifyDataSetChanged();
    }


}
