package developersudhanshu.com.newsdash.utility;

import developersudhanshu.com.newsdash.BuildConfig;

public class Constants {
    //News category constants
    public static final String CATEGORY_POLITICS = "Politics";
    public static final String CATEGORY_SPORTS = "Sports";
    public static final String CATEGORY_TECHNOLOGY = "Technology";
    public static final String CATEGORY_ENTERTAINMENT = "Entertainment";
    public static final String CATEGORY_SCIENCE = "Science";
    public static final String CATEGORY_FASHION = "Fashion";
    public static final String CATEGORY_TRAVEL = "Travel";
    public static final String CATEGORY_STARTUPS = "Start Ups";

    public static final String API_KEY = BuildConfig.API_KEY;

    // Intent Keys
    public static final String NEWS_FEED_INTENT_EXTRA_KEY = "NewsFeedKey";

    // API Interface constants
    public static final String TOP_HEADLINES_API_REQUEST = "top-headlines";
    public static final String EVERYTHING_API_REQUEST = "everything";
    public static final String QUERY_PARAM_QUERY = "q";
    public static final String QUERY_PARAM_COUNTRY_API = "country";
    public static final String QUERY_PARAM_API_KEY = "apiKey";
    public static final String QUERY_PARAM_LANGUAGE = "language";
    public static final String QUERY_PARAM_FROM_DATE = "from";
    public static final String QUERY_PARAM_TO_DATE = "to";

}
