package developersudhanshu.com.newsdash.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import developersudhanshu.com.newsdash.utility.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourFeedFragment extends Fragment implements View.OnClickListener {

    private Button choiceOne, choiceTwo, choiceThree;
    private RecyclerView yourFeedsRecyclerView;
    private NewsHeadlineRecyclerViewAdapter adapter;
    private ArrayList<NewsFeedModel> mNewsFeeds;
    private String selectedChoice;
    private APIInterface apiInterface;
    private LinearLayout loadingLayout;
    private TextView loadingLayoutMessage;
    private ProgressBar loadingBar;
    private ImageView loadingLayoutImage;
    private Button retryLoadingButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_feed, container, false);

        setUpViews(view);
        selectedChoice = choiceOne.getText().toString();
        apiInterface = APIClient.getRetrofitClient().create(APIInterface.class);
        makeNetworkRequestForNewsHeadlines(selectedChoice);

        return view;
    }

    private void setUpViews(View view) {
        choiceOne = view.findViewById(R.id.btn_user_interest_one_frag_your_feed);
        choiceTwo = view.findViewById(R.id.btn_user_interest_two_frag_your_feed);
        choiceThree = view.findViewById(R.id.btn_user_interest_three_frag_your_feed);
        yourFeedsRecyclerView = view.findViewById(R.id.rv_news_feed_frag_your_feed);


        loadingLayout = view.findViewById(R.id.ll_loading_layout_frag);
        loadingLayoutImage = view.findViewById(R.id.img_view_error_img_frag);
        loadingBar = view.findViewById(R.id.prg_bar_frag);
        loadingLayoutMessage = view.findViewById(R.id.tv_message_ll_frag);
        retryLoadingButton = view.findViewById(R.id.btn_retry_loading_frag);

        retryLoadingButton.setOnClickListener(this);

        setLoadingLayoutVisibility();

        mNewsFeeds = new ArrayList<>();
        adapter = new NewsHeadlineRecyclerViewAdapter(getActivity(), mNewsFeeds);

        adapter.setOnItemClickListener(new NewsHeadlineRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Intent i = new Intent(getContext(), NewsDetailDisplayActivity.class);
                i.putExtra(Constants.NEWS_FEED_INTENT_EXTRA_KEY, mNewsFeeds.get(position));
                startActivity(i);
            }
        });

        yourFeedsRecyclerView.setAdapter(adapter);
        yourFeedsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                                                LinearLayoutManager.VERTICAL, false));

        // Populate the Choices button with the appropriate choice text by fetching it from
        // Shared Preference
        ArrayList<String> userInterest = Utility.getUserInterests(getActivity());
        if (!userInterest.isEmpty()) {
            choiceOne.setText(userInterest.get(0));
            choiceTwo.setText(userInterest.get(1));
            choiceThree.setText(userInterest.get(2));
        } else {
            Log.d(YourFeedFragment.class.getSimpleName(), "Error getting user interests from Shared Preferences");
        }

        // Setting OnClickListeners for the buttons to display the corresponding data
        choiceOne.setOnClickListener(this);
        choiceTwo.setOnClickListener(this);
        choiceThree.setOnClickListener(this);
    }

    private void setLoadingLayoutVisibility() {
        retryLoadingButton.setVisibility(View.GONE);
        loadingBar.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.VISIBLE);
        loadingLayoutImage.setVisibility(View.GONE);
        loadingLayoutMessage.setText(getResources().getString(R.string.loading_news_feeds));
        yourFeedsRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_user_interest_one_frag_your_feed:
                if (!selectedChoice.equals(choiceOne.getText().toString())) {
                    selectedChoice = choiceOne.getText().toString();
                    setLoadingLayoutVisibility();
                    makeNetworkRequestForNewsHeadlines(selectedChoice);
                }
                break;
            case R.id.btn_user_interest_two_frag_your_feed:
                if (!selectedChoice.equals(choiceTwo.getText().toString())) {
                    selectedChoice = choiceTwo.getText().toString();
                    setLoadingLayoutVisibility();
                    makeNetworkRequestForNewsHeadlines(selectedChoice);
                }
                break;
            case R.id.btn_user_interest_three_frag_your_feed:
                if (!selectedChoice.equals(choiceThree.getText().toString())) {
                    selectedChoice = choiceThree.getText().toString();
                    setLoadingLayoutVisibility();
                    makeNetworkRequestForNewsHeadlines(selectedChoice);
                }
                break;
            case R.id.btn_retry_loading_frag:
                loadingLayoutImage.setVisibility(View.GONE);
                loadingBar.setVisibility(View.VISIBLE);
                retryLoadingButton.setVisibility(View.GONE);
                loadingLayoutMessage.setText(getResources().getString(R.string.loading_news_feeds));
                makeNetworkRequestForNewsHeadlines(selectedChoice);
                break;
        }
    }

    private void makeNetworkRequestForNewsHeadlines(String selectedChoice) {

        apiInterface.getNewsHeadlineOfQuery(selectedChoice, Constants.API_KEY,
                "en", null, null).enqueue(new Callback<NewsHeadlineResponse>() {
            @Override
            public void onResponse(Call<NewsHeadlineResponse> call, Response<NewsHeadlineResponse> response) {
                if (response.isSuccessful()) {
                    // First clearing all the previous data
                    mNewsFeeds.clear();
                    loadingLayout.setVisibility(View.INVISIBLE);
                    yourFeedsRecyclerView.setVisibility(View.VISIBLE);
                    for (Articles article: response.body().getArticles()) {
                        mNewsFeeds.add(new NewsFeedModel(article.getTitle()
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
