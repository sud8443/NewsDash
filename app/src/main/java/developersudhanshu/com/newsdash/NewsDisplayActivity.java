package developersudhanshu.com.newsdash;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import developersudhanshu.com.newsdash.fragments.TopHeadlinesFragment;

public class NewsDisplayActivity extends AppCompatActivity {

    FragmentManager fragmentManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_top_headlines:
                    if (getSupportActionBar() != null)
                        getSupportActionBar().setTitle(R.string.title_top_headlines);
                    fragmentManager.beginTransaction().replace(R.id.fl_news_activity_container,
                                    new TopHeadlinesFragment()).commit();
                    return true;
                case R.id.navigation_your_feed:
                    if (getSupportActionBar() != null)
                        getSupportActionBar().setTitle(R.string.title_your_feed);
                    fragmentManager.beginTransaction().replace(R.id.fl_news_activity_container,
                            new TopHeadlinesFragment()).commit();
                    return true;
                case R.id.navigation_favourites:
                    if (getSupportActionBar() != null)
                        getSupportActionBar().setTitle(R.string.title_favorites);
                    return true;
                case R.id.navigation_search:
                    if (getSupportActionBar() != null)
                        getSupportActionBar().setTitle(R.string.title_search);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_display);

        fragmentManager = getSupportFragmentManager();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
