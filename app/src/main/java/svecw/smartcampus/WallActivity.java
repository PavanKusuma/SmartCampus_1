package svecw.smartcampus;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import adapters.WallTabsAdapter;

/**
 * Created by Pavan on 7/6/15.
 */
public class WallActivity extends AppCompatActivity{

    // tabLayout
    TabLayout tabLayout;

    // viewPager
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wall_activity);

        // get the views from activity
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.pager);

        viewPager.setAdapter(new WallTabsAdapter(getSupportFragmentManager()));


        tabLayout.addTab(tabLayout.newTab().setText("Student Wall").setTag("Student"));
        tabLayout.addTab(tabLayout.newTab().setText("Alumni Wall").setTag("Alumni"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                // on tab selected
                // show respected fragment view
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                //actionBar.setSelectedNavigationItem(position);
                //tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
