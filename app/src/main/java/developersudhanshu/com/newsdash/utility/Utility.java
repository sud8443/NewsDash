package developersudhanshu.com.newsdash.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        sp.edit().putBoolean(Constants.IS_FIRST_LAUNCH_KEY, isFirstLaunch).apply();
    }

    public static ArrayList<String> getUserInterests(Context context) {
        if (sp == null)
            sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String data = sp.getString(Constants.USER_CHOICES_ARRAY_LIST_KEY,
                Constants.CATEGORY_TECHNOLOGY + ", " + Constants.CATEGORY_STARTUPS + ", " +
                Constants.CATEGORY_TRAVEL);
        final String[] userInterestsList = data.split(", ");
        if (userInterestsList.length > 0) {
            return new ArrayList<String>() {{
                add(userInterestsList[0]);
                add(userInterestsList[1]);
                add(userInterestsList[2]);
            }};
        } else {
            Log.d(Utility.class.getSimpleName(), "Falling back to default choices as list is not split properly");
            return new ArrayList<String>() {{
                add(Constants.CATEGORY_TECHNOLOGY);
                add(Constants.CATEGORY_STARTUPS);
                add(Constants.CATEGORY_TRAVEL);
            }};
        }
    }

    public static String getDateInReadableFormat(String date) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat inputDateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Date d = null;
        try {
            d = inputDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (d == null) {
            try {
                d = inputDateFormat2.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return d != null ? outputDateFormat.format(d):date;
    }

    public static void setUserInterests(Context context, ArrayList<String> userChoices) {
        if (sp == null)
            sp = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < userChoices.size() - 1; i++) {
            data.append(userChoices.get(i));
            data.append(", ");
        }
        data.append(userChoices.get(userChoices.size() - 1));
        sp.edit().putString(Constants.USER_CHOICES_ARRAY_LIST_KEY, data.toString()).apply();
    }
}
