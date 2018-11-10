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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopHeadlinesFragment extends Fragment implements View.OnClickListener {

    private RecyclerView topHeadlinesRecyclerView;
    private NewsHeadlineRecyclerViewAdapter adapter;
    private ArrayList<NewsFeedModel> mNewsFeeds;
    private APIInterface apiInterface;
    private LinearLayout loadingLayout;
    private TextView loadingLayoutMessage;
    private ProgressBar loadingBar;
    private ImageView loadingLayoutImage;
    private Button retryLoadingButton;

    public TopHeadlinesFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_headlines, container, false);

        loadingLayout = view.findViewById(R.id.ll_loading_layout_frag);
        loadingLayoutImage = view.findViewById(R.id.img_view_error_img_frag);
        loadingBar = view.findViewById(R.id.prg_bar_frag);
        loadingLayoutMessage = view.findViewById(R.id.tv_message_ll_frag);
        retryLoadingButton = view.findViewById(R.id.btn_retry_loading_frag);

        retryLoadingButton.setOnClickListener(this);

        retryLoadingButton.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        loadingLayoutImage.setVisibility(View.GONE);
        loadingLayoutMessage.setText(getResources().getString(R.string.loading_news_feeds));
        setUpRecyclerView(view);

        apiInterface = APIClient.getRetrofitClient().create(APIInterface.class);

        getTopHeadlines();

        return view;
    }

    private void getTopHeadlines() {

        apiInterface.getTopNewsHeadlines(Constants.COUNTRY_INDIA, Constants.API_KEY, Constants.LANGUANGE_ENGLISH, null, null)
                    .enqueue(new Callback<NewsHeadlineResponse>() {
                        @Override
                        public void onResponse(Call<NewsHeadlineResponse> call, Response<NewsHeadlineResponse> response) {
                            if (response.isSuccessful()) {
                                loadingLayout.setVisibility(View.INVISIBLE);
                                for(Articles articles: response.body().getArticles()){
                                    mNewsFeeds.add(new NewsFeedModel(articles.getTitle(),
                                                    articles.getPublishedAt(),
                                                    articles.getUrlToImage(),
                                                    articles.getAuthor(),
                                                    articles.getContent(),
                                                    articles.getSource().getName(),
                                                    articles.getUrl()));
                                }
                                Log.d("News Data:", response.body().toString());
                                adapter.notifyDataSetChanged();
                            } else {
                                retryLoading();
                            }
                        }

                        @Override
                        public void onFailure(Call<NewsHeadlineResponse> call, Throwable t) {
                            retryLoading();
                        }
                    });
    }

    private void retryLoading() {
        loadingBar.setVisibility(View.GONE);
        loadingLayoutImage.setVisibility(View.VISIBLE);
        loadingLayoutMessage.setText(getResources().getString(R.string.error_loading_feeds));
        retryLoadingButton.setVisibility(View.VISIBLE);
    }

    private void setUpRecyclerView(View view) {

        topHeadlinesRecyclerView = view.findViewById(R.id.rv_frag_top_headlines);

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

        topHeadlinesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,
                false));
        topHeadlinesRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_retry_loading_frag:
                loadingLayoutMessage.setText(getResources().getString(R.string.loading_news_feeds));
                loadingBar.setVisibility(View.VISIBLE);
                retryLoadingButton.setVisibility(View.GONE);
                loadingLayoutImage.setVisibility(View.GONE);
                getTopHeadlines();
                break;
        }
    }
}
