package developersudhanshu.com.newsdash.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import developersudhanshu.com.newsdash.R;
import developersudhanshu.com.newsdash.activities.SplashScreen;
import developersudhanshu.com.newsdash.models.NewsHeadlineResponse;
import developersudhanshu.com.newsdash.services.NewsUpdateService;
import developersudhanshu.com.newsdash.utility.Utility;

/**
 * Implementation of App Widget functionality.
 */
public class NewsWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String newsHeadline, String newsDate,
                                String newsImageUrl) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.news_widget_provider);

        Intent i = new Intent(context, SplashScreen.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);

        // Load image into the ImageView
        Bitmap image = null;
        try {
            image = Picasso.with(context)
                    .load(newsImageUrl)
                    .placeholder(R.drawable.news_placeholder)
                    .error(R.drawable.error_news_image)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String dateInReadableFormat = Utility.getDateInReadableFormat(newsDate);
        views.setTextViewText(R.id.appwidget_news_headline_text, newsHeadline);
        views.setTextViewText(R.id.appwidget_news_date_text,dateInReadableFormat);
        views.setImageViewBitmap(R.id.appwidget_image, image);
        views.setOnClickPendingIntent(R.id.appwidget_root_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        NewsUpdateService.startActionNewsWidgetUpdate(context);
    }

    public static void updateNewsWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds,
                                         String newsHeadline, String newsDate, String newsImageUrl) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, newsHeadline, newsDate, newsImageUrl);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

