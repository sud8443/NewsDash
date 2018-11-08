package developersudhanshu.com.newsdash.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import developersudhanshu.com.newsdash.R;
import developersudhanshu.com.newsdash.utility.Utility;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Utility.getIsFirstLaunch(SplashScreen.this)) {
                    Utility.setIsFirstLaunch(SplashScreen.this, false);
                    Intent i = new Intent(SplashScreen.this, UsersInterestsActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(SplashScreen.this, NewsDisplayActivity.class);
                    startActivity(i);
                }
            }
        }, 2000);

    }
}
