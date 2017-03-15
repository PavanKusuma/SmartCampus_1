package svecw.smartcampus;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import internaldb.SmartCampusDB;
import model.GlobalInfo;
import model.KnowledgeInfo;
import utils.Constants;


/**
 * Created by Pavan on 5/26/16.
 */
public class Global_Activity extends AppCompatActivity{

    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    TextView title;

    ViewPagerAdapter viewPagerAdapter;
    FloatingActionButton create, knowledge, college, student, alumni;
    RelativeLayout bgLayout, knowledgeLayout, collegeLayout, studentLayout, alumniLayout;
    Boolean isFABOpen= false;

    // selected fragment
    Fragment currentFragment;
    FragmentLifecycle fragmentToShow;

    SmartCampusDB smartCampusDB = new SmartCampusDB(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_activity);

        // get the toolbar for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Typeface sansFont = Typeface.createFromAsset(getResources().getAssets(), Constants.fontName);
        // change the title according to the activity
        title = (TextView) toolbar.findViewById(R.id.appName);
        title.setText(getResources().getString(R.string.knowledge_new));
        title.setTypeface(sansFont);

        // set the toolbar to the actionBar
        setSupportActionBar(toolbar);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setWindowTitle(""); // hide the main title

        viewPager = (ViewPager) findViewById(R.id.viewPagerGlobal);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationGlobal);
        create = (FloatingActionButton) findViewById(R.id.create);
        knowledge = (FloatingActionButton) findViewById(R.id.knowledge);
        college = (FloatingActionButton) findViewById(R.id.college);
        student = (FloatingActionButton) findViewById(R.id.student);
        alumni = (FloatingActionButton) findViewById(R.id.alumni);
        bgLayout = (RelativeLayout) findViewById(R.id.bgLayout);
        knowledgeLayout = (RelativeLayout) findViewById(R.id.knowledgeLayout);
        collegeLayout = (RelativeLayout) findViewById(R.id.collegeLayout);
        studentLayout = (RelativeLayout) findViewById(R.id.studentLayout);
        alumniLayout = (RelativeLayout) findViewById(R.id.alumniLayout);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        knowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // make knowledge wall active
                bottomNavigationView.findViewById(R.id.nav_explore).performClick();

                // close menu
                closeFABMenu();

                // navigate to selectNewPostActivity to show posting options based on privileges
                Intent newPostIntent = new Intent(getApplicationContext(), KnowledgeWallNewPostActivity.class);
                //startActivity(newPostIntent);
                startActivityForResult(newPostIntent, 200);

            }
        });

        college.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // make college wall active
                bottomNavigationView.findViewById(R.id.nav_college).performClick();

                // close menu
                closeFABMenu();

                // navigate to selectNewPostActivity to show posting options based on privileges
                Intent newPostIntent = new Intent(getApplicationContext(), NewPost.class);
                // start activity to get back result
                startActivityForResult(newPostIntent, 200);

            }
        });

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // make student wall active
                bottomNavigationView.findViewById(R.id.nav_student).performClick();

                // close menu
                closeFABMenu();

                // navigate to selectNewPostActivity to show posting options based on privileges
                Intent newPostIntent = new Intent(getApplicationContext(), StudentNewPost.class);
                newPostIntent.putExtra(Constants.alumniPost, false);
                startActivityForResult(newPostIntent, 200);
            }
        });

        alumni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // make alumni wall active
                bottomNavigationView.findViewById(R.id.nav_alumni).performClick();

                // close menu
                closeFABMenu();

                // navigate to selectNewPostActivity to show posting options based on privileges
                Intent newPostIntent = new Intent(getApplicationContext(), StudentNewPost.class);
                newPostIntent.putExtra(Constants.alumniPost, true);
                startActivityForResult(newPostIntent, 200);
            }
        });



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_explore:

                        // set the pager and title
                        viewPager.setCurrentItem(0);
                        title.setText(getResources().getString(R.string.knowledge_new));
                        break;

                    case R.id.nav_college:

                        // set the pager and title
                        viewPager.setCurrentItem(1);
                        title.setText(getResources().getString(R.string.college_new));
                        break;

                    case R.id.nav_student:

                        // set the pager and title
                        viewPager.setCurrentItem(2);
                        title.setText(getResources().getString(R.string.student_new));
                        break;

                    case R.id.nav_alumni:

                        // set the pager and title
                        viewPager.setCurrentItem(3);
                        title.setText(getResources().getString(R.string.alumni_new));
                        break;

                    case R.id.nav_more:

                        // set the pager and title
                        viewPager.setCurrentItem(4);
                        title.setText(getResources().getString(R.string.more));
                        break;
                }

                return false;
            }
        });


        setUpViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                fragmentToShow = (FragmentLifecycle)viewPagerAdapter.getItem(position);
                currentFragment = (Fragment)viewPagerAdapter.getItem(position);
                fragmentToShow.onResumeFragment();



                switch (position){
                    case 0:

                        // set the fragment and title
                        bottomNavigationView.findViewById(R.id.nav_explore).performClick();
                        title.setText(getResources().getString(R.string.knowledge_new));
                        break;

                    case 1:

                        // set the fragment and title
                        bottomNavigationView.findViewById(R.id.nav_college).performClick();
                        title.setText(getResources().getString(R.string.college_new));
                        break;

                    case 2:

                        // set the fragment and title
                        bottomNavigationView.findViewById(R.id.nav_student).performClick();
                        title.setText(getResources().getString(R.string.student_new));
                        break;

                    case 3:

                        // set the fragment and title
                        bottomNavigationView.findViewById(R.id.nav_alumni).performClick();
                        title.setText(getResources().getString(R.string.alumni_new));
                        break;

                    case 4:

                        // set the fragment and title
                        bottomNavigationView.findViewById(R.id.nav_more).performClick();
                        title.setText(getResources().getString(R.string.more));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        /*if(getIntent().hasExtra(Constants.wallId)){

            switch(getIntent().getIntExtra(Constants.wallId, 0)){

                case 0:

                    // set the pager and title
                    viewPager.setCurrentItem(0);
                    title.setText(getResources().getString(R.string.knowledge_new));
                    break;

                case 1:

                    // set the pager and title
                    viewPager.setCurrentItem(1);
                    title.setText(getResources().getString(R.string.college_new));
                    break;

                case 2:

                    // set the pager and title
                    viewPager.setCurrentItem(2);
                    title.setText(getResources().getString(R.string.student_new));
                    break;

                case 3:

                    // set the pager and title
                    viewPager.setCurrentItem(3);
                    title.setText(getResources().getString(R.string.alumni_new));
                    break;

            }

        }*/
    }

    private void showFABMenu(){
        isFABOpen=true;

        bgLayout.setVisibility(View.VISIBLE);

        // other than student, only privileged users can post to collegeWall
        if(smartCampusDB.getUserRole().contentEquals(Constants.admin)){

            collegeLayout.setVisibility(View.VISIBLE);
            collegeLayout.animate().translationY(-getResources().getDimension(R.dimen.standard_55));

            studentLayout.setVisibility(View.VISIBLE);
            studentLayout.animate().translationY(-getResources().getDimension(R.dimen.standard_105));

            alumniLayout.setVisibility(View.VISIBLE);
            alumniLayout.animate().translationY(-getResources().getDimension(R.dimen.standard_155));

            knowledgeLayout.setVisibility(View.VISIBLE);
            knowledgeLayout.animate().translationY(-getResources().getDimension(R.dimen.standard_205));

        }
        else if(smartCampusDB.isUserPrivileged()) {

            if (smartCampusDB.getUserPrivileges().getDirectory().contains(Constants.collegeWall)) {

                collegeLayout.setVisibility(View.VISIBLE);
                collegeLayout.animate().translationY(-getResources().getDimension(R.dimen.standard_55));

            }
        }

        // if student
        if(smartCampusDB.getUser().get(Constants.role).toString().equalsIgnoreCase(Constants.student)){

            studentLayout.setVisibility(View.VISIBLE);
            studentLayout.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        }

        // if alumni
        if(smartCampusDB.getUser().get(Constants.role).toString().equalsIgnoreCase(Constants.alumni) ||
                smartCampusDB.getUser().get(Constants.role).toString().equalsIgnoreCase(Constants.student)){

            alumniLayout.setVisibility(View.VISIBLE);
            alumniLayout.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        }


        /*knowledgeLayout.setVisibility(View.VISIBLE);
        collegeLayout.setVisibility(View.VISIBLE);
        studentLayout.setVisibility(View.VISIBLE);
        alumniLayout.setVisibility(View.VISIBLE);

        knowledgeLayout.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        collegeLayout.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        studentLayout.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
        alumniLayout.animate().translationY(-getResources().getDimension(R.dimen.standard_205));
*/
    }

    private void closeFABMenu(){
        isFABOpen=false;

        bgLayout.setVisibility(View.GONE);

        knowledgeLayout.animate().translationY(0);
        collegeLayout.animate().translationY(0);
        studentLayout.animate().translationY(0);
        alumniLayout.animate().translationY(0);

        knowledgeLayout.setVisibility(View.GONE);
        collegeLayout.setVisibility(View.GONE);
        studentLayout.setVisibility(View.GONE);
        alumniLayout.setVisibility(View.GONE);

    }

    public interface FragmentLifecycle {
        public void onResumeFragment();
    }
    private void setUpViewPager(ViewPager viewPager){

        // object for pager adapter
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        //Bundle bundle = new Bundle();
        //bundle.putString(Constants.category, Constants.technology);
        //bundle.putParcelableArrayList(Constants.KNOWLEDGE , knowledgeInfos);

        //homeFragment.setArguments(bundle);



        // fragments
        FragmentKnowledge fragmentKnowledge = new FragmentKnowledge();
        FragmentCollege fragmentCollege = new FragmentCollege();
        FragmentStudent fragmentStudent = new FragmentStudent();
        FragmentAlumni fragmentAlumni = new FragmentAlumni();
        FragmentMore fragmentMore = new FragmentMore();

        // add fragments to adapter
        viewPagerAdapter.addFragment(fragmentKnowledge, Constants.KNOWLEDGE);
        viewPagerAdapter.addFragment(fragmentCollege, Constants.COLLEGE);
        viewPagerAdapter.addFragment(fragmentStudent, Constants.STUDENT);
        viewPagerAdapter.addFragment(fragmentAlumni, Constants.ALUMNI);
        viewPagerAdapter.addFragment(fragmentMore, Constants.MORE);

        // set adapter and notify
        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.notifyDataSetChanged();

        // Set the number of pages that should be retained to either side of the current page in the view hierarchy in an idle state.
        // Pages beyond this limit will be recreated from the adapter when needed.
        viewPager.setOffscreenPageLimit(3);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {


            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        currentFragment.onActivityResult(requestCode, resultCode, data);
    }
}
