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
import developersudhanshu.com.newsdash.models.UserInterestModel;

public class UserInterestsRecyclerViewAdapter extends RecyclerView.Adapter<UserInterestsRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<UserInterestModel> mData;
    private ArrayList<String> userInterestChoices;
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public UserInterestsRecyclerViewAdapter(Context context, ArrayList<UserInterestModel> data, ArrayList<String> userInterestChoices) {
        mContext = context;
        mData = data;
        this.userInterestChoices = userInterestChoices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user_interests, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        // Loading image into the ImageView using Picasso
        Picasso.with(mContext)
                .load(mData.get(i).getImageResourceId())
                .placeholder(R.drawable.news_placeholder)
                .error(R.drawable.error_news_image)
                .into(viewHolder.backgroundImage);

        boolean isSelected = false;
        for (String s: userInterestChoices) {
            if (s.equals(mData.get(i).getName()))
                isSelected = true;
        }
        if (isSelected){
            viewHolder.view.setBackground(mContext.getResources().getDrawable(R.drawable.black_background_scrim, null));
        } else {
            viewHolder.view.setBackground(mContext.getResources().getDrawable(R.drawable.blue_background_scrim, null));
        }
        viewHolder.userInterestText.setText(mData.get(i).getName());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userInterestText;
        ImageView backgroundImage;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userInterestText = itemView.findViewById(R.id.tv_item_user_interest);
            backgroundImage = itemView.findViewById(R.id.img_view_item_user_interest);
            view = itemView.findViewById(R.id.view_item_user_interest_background_scrim);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }
}
