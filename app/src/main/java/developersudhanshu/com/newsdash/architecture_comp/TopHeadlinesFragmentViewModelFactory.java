package developersudhanshu.com.newsdash.architecture_comp;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import developersudhanshu.com.newsdash.models.NewsFeedModel;

public class TopHeadlinesFragmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final ArrayList<NewsFeedModel> mNewsFeeds;

    public TopHeadlinesFragmentViewModelFactory(ArrayList<NewsFeedModel> mFeeds){
        mNewsFeeds = mFeeds;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new TopHeadlinesFragmentViewModel(mNewsFeeds);
    }
}
