package svecw.smartcampus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarFragment;
import com.roughike.bottombar.OnTabSelectedListener;

/**
 * Created by Pavan_Kusuma on 6/13/2016.
 */
public class GlobalHome extends AppCompatActivity {

   // bottom bar
   private BottomBar bottomBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_home);

        // attach the bottom bar
        bottomBar = BottomBar.attach(this, savedInstanceState);

        bottomBar.setFragmentItems(getSupportFragmentManager(), R.id.fragmentContainer,
                new BottomBarFragment(new KnowledgeWallFragment(), R.drawable.ic_college_wall, "Recents"),
                new BottomBarFragment(new KnowledgeWallFragment(), R.drawable.ic_student_wall, "Food"),
                new BottomBarFragment(new KnowledgeWallFragment(), R.drawable.ic_alumni_wall, "Favorites")
                );


        // Setting colors for different tabs when there's more than three of them.
        bottomBar.mapColorForTab(0, "#3B494C");
        bottomBar.mapColorForTab(1, "#00796B");
        bottomBar.mapColorForTab(2, "#7B1FA2");

        bottomBar.setOnItemSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                switch (position) {
                    case 0:
                        // Item 1 Selected
                }
            }
        });

    }
}
