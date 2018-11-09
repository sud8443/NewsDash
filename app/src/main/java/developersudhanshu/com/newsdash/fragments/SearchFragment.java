package developersudhanshu.com.newsdash.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import developersudhanshu.com.newsdash.R;
import developersudhanshu.com.newsdash.activities.NewsDetailDisplayActivity;
import developersudhanshu.com.newsdash.adapters.NewsHeadlineRecyclerViewAdapter;
import developersudhanshu.com.newsdash.models.Articles;
import developersudhanshu.com.newsdash.models.NewsFeedModel;
import developersudhanshu.com.newsdash.models.NewsHeadlineResponse;
import developersudhanshu.com.newsdash.networking.APIClient;
import developersudhanshu.com.newsdash.networking.APIInterface;
import developersudhanshu.com.newsdash.utility.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements View.OnClickListener {

    private RecyclerView searchResultsRecyclerView;
    private EditText searchQueryText;
    private Button searchButton;
    private NewsHeadlineRecyclerViewAdapter adapter;
    private ArrayList<NewsFeedModel> mNewsFeed;
    private APIInterface apiInterface;
    private LinearLayout loadingLayout;
    private TextView loadingLayoutMessage;
    private ProgressBar loadingBar;
    private ImageView loadingLayoutImage;
    private Button retryLoadingButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        setUpViews(view);

        loadingLayout = view.findViewById(R.id.ll_loading_layout_frag);
        loadingLayoutImage = view.findViewById(R.id.img_view_error_img_frag);
        loadingBar = view.findViewById(R.id.prg_bar_frag);
        loadingLayoutMessage = view.findViewById(R.id.tv_message_ll_frag);
        retryLoadingButton = view.findViewById(R.id.btn_retry_loading_frag);

        retryLoadingButton.setOnClickListener(this);

        setLoadingLayoutVisibility();
        apiInterface = APIClient.getRetrofitClient().create(APIInterface.class);
        loadSearchedQueryNewsFeeds("latest");
        return view;
    }

    private void setLoadingLayoutVisibility() {
        retryLoadingButton.setVisibility(View.GONE);
        loadingBar.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.VISIBLE);
        loadingLayoutImage.setVisibility(View.GONE);
        loadingLayoutMessage.setText(getResources().getString(R.string.loading_news_feeds));
        searchResultsRecyclerView.setVisibility(View.GONE);
    }

    private void setUpViews(View view) {
        searchResultsRecyclerView = view.findViewById(R.id.rv_searched_news_feeds_frag_search);
        searchQueryText = view.findViewById(R.id.et_search_text_frag_search);
        searchButton = view.findViewById(R.id.btn_search_frag_search);

        mNewsFeed = new ArrayList<>();

        // Setting up Recycler View
        adapter = new NewsHeadlineRecyclerViewAdapter(getActivity(), mNewsFeed);
        searchResultsRecyclerView.setAdapter(adapter);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,
                false));

        adapter.setOnItemClickListener(new NewsHeadlineRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Intent i = new Intent(getContext(), NewsDetailDisplayActivity.class);
                i.putExtra(Constants.NEWS_FEED_INTENT_EXTRA_KEY, mNewsFeed.get(position));
                startActivity(i);
            }
        });
        // Setting onClick listener on the Search button
        searchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search_frag_search:
                setLoadingLayoutVisibility();
                verifySearchQueryAndMakeRequest();
                break;
            case R.id.btn_retry_loading_frag:
                loadingLayoutImage.setVisibility(View.GONE);
                loadingBar.setVisibility(View.VISIBLE);
                retryLoadingButton.setVisibility(View.GONE);
                loadingLayoutMessage.setText(getResources().getString(R.string.loading_news_feeds));
                String searchQuery = String.valueOf(searchQueryText.getText());

                if (!TextUtils.isEmpty(searchQuery)) {
                    loadSearchedQueryNewsFeeds(searchQuery);
                } else {
                    loadSearchedQueryNewsFeeds("latest");
                }
                break;
        }
    }

    private void verifySearchQueryAndMakeRequest() {
        String searchQuery = String.valueOf(searchQueryText.getText());

        if (!TextUtils.isEmpty(searchQuery)) {
            loadSearchedQueryNewsFeeds(searchQuery);
        } else {
            Toast.makeText(getActivity(), "Enter some text to be searched", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSearchedQueryNewsFeeds(String searchQuery) {

        apiInterface.getNewsHeadlineOfQuery(searchQuery, Constants.API_KEY, "en",
                                            null, null).enqueue(new Callback<NewsHeadlineResponse>() {
            @Override
            public void onResponse(Call<NewsHeadlineResponse> call, Response<NewsHeadlineResponse> response) {
                if (response.isSuccessful()) {
                    mNewsFeed.clear();
                    loadingLayout.setVisibility(View.INVISIBLE);
                    searchResultsRecyclerView.setVisibility(View.VISIBLE);
                    for (Articles article: response.body().getArticles()) {
                        mNewsFeed.add(new NewsFeedModel(article.getTitle()
                                ,article.getPublishedAt(), article.getUrlToImage(),
                                article.getAuthor(), article.getContent(),
                                article.getSource().getName(), article.getUrl()));
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    displayRetryLayout();
                }
            }

            @Override
            public void onFailure(Call<NewsHeadlineResponse> call, Throwable t) {
                displayRetryLayout();
            }
        });
    }

    private void displayRetryLayout() {
        retryLoadingButton.setVisibility(View.VISIBLE);
        loadingBar.setVisibility(View.GONE);
        loadingLayoutImage.setVisibility(View.VISIBLE);
        loadingLayoutMessage.setText(getResources().getString(R.string.error_loading_feeds));
    }
}
