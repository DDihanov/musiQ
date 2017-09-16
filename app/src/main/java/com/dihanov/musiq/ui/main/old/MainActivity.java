//package com.dihanov.musiq.ui.main.old;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//
//import com.dihanov.musiq.R;
//import com.dihanov.musiq.ui.detail.DetailActivity;
//
//import javax.inject.Inject;
//
//import dagger.android.AndroidInjection;
//
//
//public class MainActivity extends Activity implements MainView{
//    @Inject
//    MainPresenter mainPresenter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        AndroidInjection.inject(this);
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        mainPresenter.loadMain();
//
//        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, DetailActivity.class));
//            }
//        });
//    }
//
//    @Override
//    public void onMainLoaded() {
//        Log.v("TEST", "Main page is loaded.");
//    }
//}
