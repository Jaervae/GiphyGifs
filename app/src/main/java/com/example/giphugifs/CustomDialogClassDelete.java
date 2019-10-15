package com.example.giphugifs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class CustomDialogClassDelete extends Dialog implements android.view.View.OnClickListener {

    private Activity c;
    private Button yes, no;
    private ImageView imageView;
    private String url;
    private int position;
    private Context context;

    private static final String SHARED_PREF_LIST = "MyURLS";

    private ArrayList<String> list = new ArrayList<String>();

    public interface DialogInterface {
        void dataChanged();
    }

    private DialogInterface cbInterface = null;

    public CustomDialogClassDelete(Activity a, String url, int position,Context context, DialogInterface cb) {
        super(a);
        this.c = a;
        this.url = url;
        this.position = position;
        this.context = context;
        cbInterface = cb;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_delete);
        yes = findViewById(R.id.btn_yes);
        no = findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        imageView = findViewById(R.id.imageViewDialog);
        loadData();
        Glide
                .with(getContext())
                .asGif()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView);

    }


    private void loadData(){
        SaveData saveData = new SaveData(context);
        list = saveData.getListString(SHARED_PREF_LIST);
    }

    private void saveData(){
        SaveData saveData = new SaveData(context);
        saveData.saveListString(SHARED_PREF_LIST, list);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                list.remove(position);
                saveData();
                cbInterface.dataChanged();
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

}