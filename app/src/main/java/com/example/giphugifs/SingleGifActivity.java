package com.example.giphugifs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class SingleGifActivity extends AppCompatActivity {

    private String url;
    private Boolean showFavoriteButton = true;
    ArrayList<String> list = new ArrayList<String>();
    Context context = this;
    FloatingActionButton fab;

    static final String SHARED_PREF_LIST = "MyURLS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_gif);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        url = intent.getStringExtra("fav");
        showFavoriteButton = intent.getBooleanExtra("show", true);
        ImageView imageView = findViewById(R.id.imageViewSingleGif);

        Glide
                .with(getBaseContext())
                .asGif()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView);

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Kuva tallenettu!",Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                list.add(url);
            }
        });
    }

    public void loadData(){
        SaveData saveData = new SaveData(context);
        list = saveData.getListString(SHARED_PREF_LIST);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        if (!showFavoriteButton)
            fab.hide();
        else
            fab.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SaveData saveData = new SaveData(context);
        saveData.putListString(SHARED_PREF_LIST, list);
    }
}
