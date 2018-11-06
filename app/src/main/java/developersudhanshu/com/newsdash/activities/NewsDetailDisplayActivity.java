package developersudhanshu.com.newsdash.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import developersudhanshu.com.newsdash.R;
import developersudhanshu.com.newsdash.models.NewsFeedModel;
import developersudhanshu.com.newsdash.utility.Constants;

public class NewsDetailDisplayActivity extends AppCompatActivity {

    private TextView newsHeadline, newsDate, newsAuthor;
    private ImageView newsImage;
    private TextView newsDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail_display);

        setUpViews();

        if (getIntent() != null && getIntent().hasExtra(Constants.NEWS_FEED_INTENT_EXTRA_KEY)) {
            NewsFeedModel model = getIntent().getParcelableExtra(Constants.NEWS_FEED_INTENT_EXTRA_KEY);
            populateViewsWithData(model);
        }
    }

    private void populateViewsWithData(NewsFeedModel model) {

        newsHeadline.setText(model.getName());
        newsDate.setText(model.getDate());
        newsAuthor.setText(model.getAuthorName());
        newsDescription.setText(model.getDescription());

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
    }
}
