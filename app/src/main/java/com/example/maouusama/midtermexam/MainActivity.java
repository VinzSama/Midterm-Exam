package com.example.maouusama.midtermexam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.maouusama.midtermexam.Fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    /*
    API key	d5292261363cda069c8bbd35b5ac4a7a
    Shared secret	aa32046c75df3de3a87a1f9be2255e9b
    */

    private MainFragment mainFragment;
    private RelativeLayout relativeLayout;
    private android.support.v4.app.FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainFragment = MainFragment.newInstance();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.activity_main, mainFragment).commit();
    }
}
