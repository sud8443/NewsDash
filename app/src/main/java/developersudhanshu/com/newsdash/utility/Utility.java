package developersudhanshu.com.newsdash.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import developersudhanshu.com.newsdash.models.UserInterestModel;

public class Utility {

    private static SharedPreferences sp;

    public static boolean getIsFirstLaunch(Context context) {
        if (sp == null)
         sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(Constants.IS_FIRST_LAUNCH_KEY, true);
    }

    public static void setIsFirstLaunch(Context context, boolean isFirstLaunch) {
        if (sp == null)
            sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(Constants.SHARED_PREFERENCE_NAME, isFirstLaunch).apply();
    }

    public static ArrayList<UserInterestModel> getUserInterests(Context context) {
        if (sp == null)
            sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String data = sp.getString(Constants.USER_CHOICES_ARRAY_LIST_KEY,
                gson.toJson(new ArrayList<String>(){{add(Constants.CATEGORY_TECHNOLOGY);
                add(Constants.CATEGORY_STARTUPS);
                add(Constants.CATEGORY_TRAVEL);}}));
        Type type = new TypeToken<List<UserInterestModel>>(){}.getType();
        List<UserInterestModel> userInterestsList = gson.fromJson(data, type);
        return new ArrayList<UserInterestModel>(userInterestsList);
    }

    public static void setUserInterests(Context context, ArrayList<String> userChoices) {
        if (sp == null)
            sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        sp.edit().putString(Constants.SHARED_PREFERENCE_NAME, gson.toJson(userChoices)).apply();
    }
}
