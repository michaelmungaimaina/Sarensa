package mich.gwan.sarensa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import mich.gwan.sarensa.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_splashscreen);
        }
        catch (Exception e){
            throw e;
        }
        //Objects.requireNonNull(getSupportActionBar()).hide();
        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.window));
        Thread splashScreenStarter = new Thread() {
            public void run() {
                try {
                    int delay = 0;
                    while (delay < 2000) {
                        sleep(150);
                        delay = delay + 100;
                    }
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }

        };
        splashScreenStarter.start();
    }
}
