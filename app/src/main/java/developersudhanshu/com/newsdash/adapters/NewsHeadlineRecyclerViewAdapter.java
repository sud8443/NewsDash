package developersudhanshu.com.newsdash.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import developersudhanshu.com.newsdash.R;
import developersudhanshu.com.newsdash.models.NewsFeedModel;

public class NewsHeadlineRecyclerViewAdapter extends RecyclerView.Adapter<NewsHeadlineRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<NewsFeedModel> mNewsFeeds;

    public NewsHeadlineRecyclerViewAdapter(Context context, ArrayList<NewsFeedModel> news) {
        this.mContext = context;
        mNewsFeeds = news;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_news_feed, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Picasso.with(mContext)
                .load(mNewsFeeds.get(i).getImageUrl())
                .error(R.drawable.error_news_image)
                .placeholder(R.drawable.news_placeholder)
                .into(viewHolder.newsImage);

        viewHolder.newsHeadline.setText(mNewsFeeds.get(i).getName());
        viewHolder.newsDate.setText(mNewsFeeds.get(i).getDate());
    }

    @Override
    public int getItemCount() {
        if (mNewsFeeds == null)
            return 0;
        return mNewsFeeds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView newsHeadline, newsDate;
        ImageView newsImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsHeadline = itemView.findViewById(R.id.tv_news_title);
            newsDate = itemView.findViewById(R.id.tv_news_date);
            newsImage = itemView.findViewById(R.id.img_view_news_image);
        }
    }
}
