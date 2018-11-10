package developersudhanshu.com.newsdash.architecture_comp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import developersudhanshu.com.newsdash.database.AppDatabase;
import developersudhanshu.com.newsdash.database.NewsHeadlineEntity;

public class FavoritesFragmentViewModel extends AndroidViewModel {

    private LiveData<List<NewsHeadlineEntity>> mFeeds;

    public FavoritesFragmentViewModel(@NonNull Application application) {
        super(application);
        mFeeds = AppDatabase.getInstance(application).getNewsHeadlinesDao().loadAllNewsHeadlines();
    }

    public LiveData<List<NewsHeadlineEntity>> getmFeeds() {
        return mFeeds;
    }
}
