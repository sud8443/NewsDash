package developersudhanshu.com.newsdash.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import developersudhanshu.com.newsdash.R;
import developersudhanshu.com.newsdash.fragments.FavoritesFragment;
import developersudhanshu.com.newsdash.fragments.SearchFragment;
import developersudhanshu.com.newsdash.fragments.TopHeadlinesFragment;
import developersudhanshu.com.newsdash.fragments.YourFeedFragment;

public class NewsDisplayActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    private static TopHeadlinesFragment topHeadlinesFragment;
    private static YourFeedFragment yourFeedFragment;
    private static FavoritesFragment favoritesFragment;
    private static SearchFragment searchFragment;
    private static String currentlyDisplayedFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_top_headlines:
                    if (getSupportActionBar() != null)
                        if (!currentlyDisplayedFragment.equals(getResources().getString(R.string.title_top_headlines))) {
                            getSupportActionBar().setTitle(R.string.title_top_headlines);
                            fragmentManager.beginTransaction().replace(R.id.fl_news_activity_container,
                                    topHeadlinesFragment).commit();
                            currentlyDisplayedFragment = getResources().getString(R.string.title_top_headlines);
                        }
                    return true;
                case R.id.navigation_your_feed:
                    if (getSupportActionBar() != null)
                        if (!currentlyDisplayedFragment.equals(getResources().getString(R.string.title_your_feed))) {
                            getSupportActionBar().setTitle(R.string.title_your_feed);
                            fragmentManager.beginTransaction().replace(R.id.fl_news_activity_container,
                                    yourFeedFragment).commit();
                            currentlyDisplayedFragment = getResources().getString(R.string.title_your_feed);
                        }
                    return true;
                case R.id.navigation_favourites:
                    if (!currentlyDisplayedFragment.equals(getResources().getString(R.string.title_favorites))) {
                        if (getSupportActionBar() != null)
                            getSupportActionBar().setTitle(R.string.title_favorites);
                        fragmentManager.beginTransaction().replace(R.id.fl_news_activity_container,
                                favoritesFragment).commit();
                        currentlyDisplayedFragment = getResources().getString(R.string.title_favorites);
                    }
                    return true;
                case R.id.navigation_search:
                    if (!currentlyDisplayedFragment.equals(getResources().getString(R.string.title_search))) {
                        if (getSupportActionBar() != null)
                            getSupportActionBar().setTitle(R.string.title_search);
                        fragmentManager.beginTransaction().replace(R.id.fl_news_activity_container,
                                searchFragment).commit();
                        currentlyDisplayedFragment = getResources().getString(R.string.title_search);
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_display);

        // Creating fragments using singleton design pattern
        if (topHeadlinesFragment == null) {
            topHeadlinesFragment = new TopHeadlinesFragment();
        }
        if (yourFeedFragment == null) {
            yourFeedFragment = new YourFeedFragment();
        }
        if (favoritesFragment == null) {
            favoritesFragment = new FavoritesFragment();
        }
        if (searchFragment == null) {
            searchFragment = new SearchFragment();
        }

        fragmentManager = getSupportFragmentManager();

        currentlyDisplayedFragment = getResources().getString(R.string.title_top_headlines);
        fragmentManager.beginTransaction().replace(R.id.fl_news_activity_container,
                topHeadlinesFragment).commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
