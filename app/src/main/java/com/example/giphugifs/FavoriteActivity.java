package com.example.giphugifs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity implements CustomDialogClassDelete.DialogInterface{

    static final String SHARED_PREF_LIST = "MyURLS";
    Context context = this;

    ArrayList<String> list = new ArrayList<String>();
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        gridView = findViewById(R.id.gridViewFav);
        loadData();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(),SingleGifActivity.class);
                intent.putExtra("fav", list.get(position).toString());
                intent.putExtra("show", false);
                startActivity(intent);
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                callDeleteDialog(position);
                return true;
            }
        });

    }

    public void callDeleteDialog(int position){
        CustomDialogClassDelete cdd=new CustomDialogClassDelete(this, list.get(position),position,context,this);
        cdd.show();
    }

    public void loadData(){
        SaveData saveData = new SaveData(context);
        list = saveData.getListString(SHARED_PREF_LIST);
        gridView.setAdapter(
                new SimpleListAdapter(
                        FavoriteActivity.this,
                        list
                )
        );
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void dataChanged() {
        FavoriteActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        });
    }
}
