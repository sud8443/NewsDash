package developersudhanshu.com.newsdash.architecture_comp;

import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

import developersudhanshu.com.newsdash.models.NewsFeedModel;

public class TopHeadlinesFragmentViewModel extends ViewModel {

    private ArrayList<NewsFeedModel> mNewsFeeds;

    public TopHeadlinesFragmentViewModel(ArrayList<NewsFeedModel> models) {
        this.mNewsFeeds = models;
    }

    public ArrayList<NewsFeedModel> getmNewsFeeds() {
        return mNewsFeeds;
    }

    public void setmNewsFeeds(ArrayList<NewsFeedModel> mNewsFeeds) {
        this.mNewsFeeds = mNewsFeeds;
    }
}
