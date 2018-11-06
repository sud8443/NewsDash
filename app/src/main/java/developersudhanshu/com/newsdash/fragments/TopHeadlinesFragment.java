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

public class TopHeadlinesFragment extends Fragment {

    private RecyclerView topHeadlinesRecyclerView;
    private NewsHeadlineRecyclerViewAdapter adapter;
    private ArrayList<NewsFeedModel> mNewsFeeds;
    private APIInterface apiInterface;

    public TopHeadlinesFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_headlines, container, false);

        setUpRecyclerView(view);

        getTopHeadlines();

        return view;
    }

    private void getTopHeadlines() {
        apiInterface = APIClient.getRetrofitClient().create(APIInterface.class);

        apiInterface.getTopNewsHeadlines("in", Constants.API_KEY, "en", null, null)
                    .enqueue(new Callback<NewsHeadlineResponse>() {
                        @Override
                        public void onResponse(Call<NewsHeadlineResponse> call, Response<NewsHeadlineResponse> response) {
                            if (response.isSuccessful()) {
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
                            }
                        }

                        @Override
                        public void onFailure(Call<NewsHeadlineResponse> call, Throwable t) {

                        }
                    });
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
}
