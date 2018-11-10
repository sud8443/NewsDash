package developersudhanshu.com.newsdash.architecture_comp;

import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

import developersudhanshu.com.newsdash.models.NewsFeedModel;

public class YourFeedsFragmentViewModel extends ViewModel {

    private ArrayList<NewsFeedModel> mNewsFeeds;
    private String selectedChoice;

    public YourFeedsFragmentViewModel(ArrayList<NewsFeedModel> models, String choice) {
        this.mNewsFeeds = models;
        this.selectedChoice = choice;
    }

    public String getSelectedChoice() {
        return selectedChoice;
    }

    public void setSelectedChoice(String selectedChoice) {
        this.selectedChoice = selectedChoice;
    }

    public ArrayList<NewsFeedModel> getmNewsFeeds() {
        return mNewsFeeds;
    }

    public void setmNewsFeeds(ArrayList<NewsFeedModel> mNewsFeeds) {
        this.mNewsFeeds = mNewsFeeds;
    }
}
