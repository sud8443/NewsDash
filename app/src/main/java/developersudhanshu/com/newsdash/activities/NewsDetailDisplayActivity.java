package developersudhanshu.com.newsdash.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import developersudhanshu.com.newsdash.R;
import developersudhanshu.com.newsdash.database.AppDatabase;
import developersudhanshu.com.newsdash.database.AppExecutors;
import developersudhanshu.com.newsdash.database.NewsHeadlineEntity;
import developersudhanshu.com.newsdash.models.NewsFeedModel;
import developersudhanshu.com.newsdash.utility.Constants;
import developersudhanshu.com.newsdash.utility.Utility;

public class NewsDetailDisplayActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView newsHeadline, newsDate, newsAuthor, newsSource;
    private ImageView newsImage;
    private TextView newsDescription;
    private Button shareStory, viewFullStory, markFavoriteStory;
    private String newsUrl;
    private static final String TAG = NewsDetailDisplayActivity.class.getSimpleName();
    private AppDatabase mDb;
    private AppExecutors executors;
    private NewsFeedModel model;
    private boolean newsIsFav;
    private NewsHeadlineEntity mNewsEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail_display);

        setUpViews();

        if (getIntent() != null && getIntent().hasExtra(Constants.NEWS_FEED_INTENT_EXTRA_KEY)) {
            model = getIntent().getParcelableExtra(Constants.NEWS_FEED_INTENT_EXTRA_KEY);
            populateViewsWithData(model);
        }

        executors = AppExecutors.getInstance();

        loadNewsFeedByName();

        if (getSupportActionBar() != null) {
            // Enable back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // Disable title
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void loadNewsFeedByName() {
        mDb = AppDatabase.getInstance(this);

        final LiveData<NewsHeadlineEntity> news = mDb.getNewsHeadlinesDao().loadNewsByHeadline(model.getName());
        news.observe(this, new Observer<NewsHeadlineEntity>() {
            @Override
            public void onChanged(@Nullable NewsHeadlineEntity headlineEntity) {
                if (headlineEntity == null) {
                    markUnFav();
                } else {
                    markFav();
                    mNewsEntity = headlineEntity;
                }
            }
        });
    }

    private void markFav() {
        newsIsFav = true;
        final Drawable drawable = getDrawable(R.drawable.ic_favorite_24dp);
        drawable.setTint(getResources().getColor(android.R.color.holo_red_dark));
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                markFavoriteStory.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            }
        });
        Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show();
    }

    private void markUnFav() {
        newsIsFav = false;
        Drawable drawable = getDrawable(R.drawable.ic_favorite_24dp);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                markFavoriteStory.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_24dp, 0, 0);
            }
        });
        Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateViewsWithData(NewsFeedModel model) {

        newsHeadline.setText(model.getName());
        newsDate.setText(Utility.getDateInReadableFormat(model.getDate()));
        if (!TextUtils.isEmpty(model.getAuthorName())) {
            newsAuthor.setText(model.getAuthorName());
        } else {
            newsAuthor.setText(getResources().getString(R.string.no_author_message));
        }
        if (!TextUtils.isEmpty(model.getDescription())) {
            newsDescription.setText(model.getDescription());
        } else {
            newsDescription.setText(getResources().getString(R.string.no_description_message));
        }
        if (!TextUtils.isEmpty(model.getNewsSource())) {
            newsSource.setText("Source: " + model.getNewsSource());
        } else {
            newsSource.setText(getResources().getString(R.string.no_source_available));
        }

        // Saving the news URL to be shared and opened in Browser
        newsUrl = model.getNewsUrl();

        // Loading the image in the image view
        Picasso.with(this)
                .load(model.getImageUrl())
                .placeholder(R.drawable.news_placeholder)
                .placeholder(R.drawable.error_news_image)
                .into(newsImage);
    }

    private void setUpViews() {
        newsHeadline = findViewById(R.id.tv_news_headline_act_news_detail_display);
        newsDate = findViewById(R.id.tv_date_act_news_detail_display);
        newsAuthor = findViewById(R.id.tv_author_act_news_detail_display);
        newsDescription = findViewById(R.id.tv_desc_act_news_detail_display);
        newsImage = findViewById(R.id.img_view_act_news_detail);
        newsSource = findViewById(R.id.tv_source_act_news_detail_display);

        markFavoriteStory = findViewById(R.id.btn_mark_fav_act_news_details_display);
        shareStory = findViewById(R.id.btn_share_act_news_details_display);
        viewFullStory = findViewById(R.id.btn_open_in_browser_act_news_details_display);

        markFavoriteStory.setOnClickListener(this);
        shareStory.setOnClickListener(this);
        viewFullStory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_mark_fav_act_news_details_display:
                // Save the News Headline in the app Database
                executors.getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        NewsHeadlineEntity entity = new NewsHeadlineEntity(model.getName(),
                                model.getDate(), model.getImageUrl(), model.getAuthorName(),
                                model.getDescription(), model.getNewsSource(), model.getNewsUrl());
                        if (newsIsFav) {
                            mDb.getNewsHeadlinesDao().deleteNewsHeadline(mNewsEntity);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    markUnFav();
//                                }
//                            });
                        } else {
                            mDb.getNewsHeadlinesDao().insertNewsHeadline(entity);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    markFav();
//                                }
//                            });
                        }
                    }
                });
                break;
            case R.id.btn_share_act_news_details_display:
                if (!TextUtils.isEmpty(newsUrl)) {
                    Intent shareStoryIntent = new Intent();
                    shareStoryIntent.setType("text/plain");
                    shareStoryIntent.setAction(Intent.ACTION_SEND);
                    shareStoryIntent.putExtra(Intent.EXTRA_TEXT, newsUrl);
                    startActivity(shareStoryIntent);
                } else {
                    Log.d(TAG, "News Url is Empty");
                }
                break;
            case R.id.btn_open_in_browser_act_news_details_display:
                if (!TextUtils.isEmpty(newsUrl)) {
                    Intent openInBrowserIntent = new Intent();
                    openInBrowserIntent.setAction(Intent.ACTION_VIEW);
                    openInBrowserIntent.setData(Uri.parse(newsUrl));
                    startActivity(Intent.createChooser(openInBrowserIntent, "Open with"));
                } else {
                    Log.d(TAG, "News Url is Empty");
                }
                break;
        }
    }
}
