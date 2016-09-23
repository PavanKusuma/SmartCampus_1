package adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import internaldb.SmartCampusDB;
import model.Exams;
import svecw.smartcampus.QuestionsActivity;
import svecw.smartcampus.R;
import utils.Constants;

/**
 * Created by Pavan on 6/12/15.
 */
public class ExamsAdapter extends BaseAdapter {

    // context of present class
    Context context;

    // usernames and likelist
    ArrayList<Exams> examsList;
    //ArrayList<Boolean> likeList;

    // layout Inflator
    LayoutInflater layoutInflater;

    // initial like record
    boolean likedRecord = false;

    // mediaTable player
    MediaPlayer mediaPlayer;

    // instance of the current db
    SmartCampusDB smartCampusDB;

    // constructor
    public ExamsAdapter(Context context) {

        this.context = context;

        // intialize variables
        examsList = new ArrayList<Exams>();
        //likeList = new ArrayList<Boolean>();

        // layoutInflator object
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // object for db
        smartCampusDB = new SmartCampusDB(context);

        // create the mediaPlayer object
        mediaPlayer = MediaPlayer.create(context, R.raw.like_sound);

    }

    @Override
    public int getCount() {
        return examsList.size();
    }

    @Override
    public Object getItem(int position) {
        return examsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        RelativeLayout itemView;
        if (convertView == null) {
            itemView = (RelativeLayout) layoutInflater.inflate(R.layout.exams_singleitem, parent, false);

        } else {
            itemView = (RelativeLayout) convertView;
        }

        // get all views of single exams list item
        TextView examObjectId = (TextView) itemView.findViewById(R.id.examId);
        TextView name = (TextView) itemView.findViewById(R.id.examName);
        TextView expiryDate = (TextView) itemView.findViewById(R.id.examExpiryDate);
        TextView examId = (TextView) itemView.findViewById(R.id.examId);
        TextView examNumberOfQuestions = (TextView) itemView.findViewById(R.id.examNumberOfQuestions);

        // set the username of the user
        Typeface sansFont = Typeface.createFromAsset(context.getResources().getAssets(), Constants.fontName);

        name.setText(examsList.get(position).getName()); name.setTypeface(sansFont);
        expiryDate.setText(examsList.get(position).getExpiryDate()); expiryDate.setTypeface(sansFont);
        examId.setText(examsList.get(position).getObjectId()); examId.setTypeface(sansFont);
        examNumberOfQuestions.setText(String.valueOf(examsList.get(position).getNumberOfQuestions())); examNumberOfQuestions.setTypeface(sansFont);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get the questions
                Intent questionsIntent = new Intent(context, QuestionsActivity.class);
                questionsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                questionsIntent.putExtra(Constants.examId, examsList.get(position).getObjectId());
                questionsIntent.putExtra(Constants.numberOfQuestions, examsList.get(position).getNumberOfQuestions());
                context.startActivity(questionsIntent);
            }
        });

        // return the itemView
        return itemView;
    }

    /**
     * update the adapter list items and notify
     */
    public void updateItems(ArrayList<Exams> postsList) {
        this.examsList = postsList;
        //likeList = like1List;
        notifyDataSetChanged();
    }
}
