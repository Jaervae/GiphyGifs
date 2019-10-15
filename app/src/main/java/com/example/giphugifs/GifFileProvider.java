package com.example.giphugifs;

import android.net.Uri;

import androidx.core.content.FileProvider;

public class GifFileProvider extends FileProvider {
    //Cache file doesn't have any extension on it so we must define the
    //type of the file we want to share
    @Override public String getType(Uri uri) {
        return "image/gif";
    }
}
