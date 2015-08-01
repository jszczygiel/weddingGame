package com.wroclawstudio.weddinggame.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.wroclawstudio.weddinggame.R;
import com.wroclawstudio.weddinggame.fragments.BasePresenterFragment;

public abstract class BaseActivity extends AppCompatActivity {


    private BasePresenterFragment fragment;

    public abstract BasePresenterFragment newInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();
        fragment = newInstance();
        manager.beginTransaction().replace(R.id.activity_game_container, fragment).commit();
    }
}
