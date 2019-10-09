package com.example.giphugifs;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


class SimpleListAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> imageUrls;


    public SimpleListAdapter(Context baseContext, ArrayList<String> listItems) {
        super(baseContext,R.layout.list_item_image,listItems);

        this.context = baseContext;
        this.imageUrls = listItems;
        inflater = LayoutInflater.from(baseContext);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (null == convertView){
            convertView = inflater.inflate(R.layout.list_item_image,parent,false);
        }


        Glide
                .with(context)
                .asGif()
                .load(imageUrls.get(position))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into((ImageView)convertView);

        return convertView;
    }
}

