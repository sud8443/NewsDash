package developersudhanshu.com.newsdash.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import developersudhanshu.com.newsdash.R;
import developersudhanshu.com.newsdash.adapters.UserInterestsRecyclerViewAdapter;
import developersudhanshu.com.newsdash.utility.Constants;
import developersudhanshu.com.newsdash.utility.Utility;

public class UsersInterestsActivity extends AppCompatActivity {

    private TextView choiceCount, selectedChoices;
    private Button nextButton;
    private RecyclerView userInterestChoices;
    private UserInterestsRecyclerViewAdapter adapter;
    private ArrayList<String> userChoices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_interests);

        setUpViews();
        if (savedInstanceState != null) {
            userChoices = savedInstanceState.getStringArrayList(Constants.USER_CHOICES_KEY);
            choiceCount.setText(savedInstanceState.getString(Constants.CHOICE_COUNT_TEXT_VIEW_KEY));
            selectedChoices.setText(savedInstanceState.getString(Constants.USER_INTERESTS_TEXT_VIEW_KEY));
            userInterestChoices.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(Constants.RECYCLER_VIEW_STATE_KEY));
            adapter.notifyDataSetChanged();
        }
    }

    private void setUpViews() {

        // Initializing users choices
        userChoices = new ArrayList<>();

        choiceCount = findViewById(R.id.tv_choice_count_act_user_interests);
        selectedChoices = findViewById(R.id.tv_selected_choices_act_user_interests);
        nextButton = findViewById(R.id.btn_next_act_user_interests);

        userInterestChoices = findViewById(R.id.rv_interest_choices_act_user_interests);
        adapter = new UserInterestsRecyclerViewAdapter(this, Constants.userInterests, userChoices);
        userInterestChoices.setAdapter(adapter);
        userInterestChoices.setLayoutManager(new GridLayoutManager(this, 2));

        adapter.setOnItemClickListener(new UserInterestsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                if (userChoices.contains(Constants.userInterests.get(position).getName())) {
                    userChoices.remove(Constants.userInterests.get(position).getName());
                } else if (userChoices.size() < 3){
                    userChoices.add(Constants.userInterests.get(position).getName());
                } else {
                    Toast.makeText(UsersInterestsActivity.this, "You cannot enter more than 3 choices",
                            Toast.LENGTH_SHORT).show();
                }
                StringBuilder choicesString = new StringBuilder();
                for (String choice : userChoices) {
                    choicesString.append(choice);
                    choicesString.append("   ");
                }
                if (userChoices.size() > 0)
                    selectedChoices.setText(choicesString.toString());
                else
                    selectedChoices.setText(getResources().getString(R.string.splash_screen_no_choices_selected));
                choiceCount.setText(String.valueOf(3 - userChoices.size()) + " more choices to go!");
                adapter.notifyItemChanged(position);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userChoices.size() == 3) {
                    Utility.setUserInterests(UsersInterestsActivity.this, userChoices);
                    Intent newsDisplayActivityIntent = new Intent(UsersInterestsActivity.this,
                            NewsDisplayActivity.class);
                    startActivity(newsDisplayActivityIntent);
                }else {
                    Toast.makeText(UsersInterestsActivity.this, "Enter your interest first!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(Constants.USER_CHOICES_KEY, userChoices);
        outState.putString(Constants.CHOICE_COUNT_TEXT_VIEW_KEY, choiceCount.getText().toString());
        outState.putString(Constants.USER_INTERESTS_TEXT_VIEW_KEY, selectedChoices.getText().toString());
        outState.putParcelable(Constants.RECYCLER_VIEW_STATE_KEY, userInterestChoices.getLayoutManager().onSaveInstanceState());
    }
}
