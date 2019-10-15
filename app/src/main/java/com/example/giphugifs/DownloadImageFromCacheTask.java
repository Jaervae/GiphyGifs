package com.example.giphugifs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;

import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

public class DownloadImageFromCacheTask extends AsyncTask<String, Void, File> {
    @SuppressLint("StaticFieldLeak")
    private final Context context;

    public DownloadImageFromCacheTask(Context context) {
        this.context = context;
    }

    //Download the clicked single gif as a file in order to share
    @Override
    protected File doInBackground(String... params) {
        FutureTarget<File> future = Glide.with(context)
                .load(params[0])
                .downloadOnly(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL);
        File file = null;
        try {
            file = future.get();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    @Override
    protected void onPostExecute(File result) {
        if (result == null) {
            return;
        }
        //Get file from fileprovider to prevent uri leakage
        Uri uri = FileProvider.getUriForFile(context, "com.example.giphugifs.fileprovider", result);
        share(uri, (Activity) context);
    }

    //Share the file we just downloaded with Glide to other apps!
    private void share(Uri result, Activity activity) {
        Intent intent = ShareCompat.IntentBuilder.from(activity)
                .setStream(result)
                .setType("text/html")
                .getIntent()
                .setAction(Intent.ACTION_SEND)
                .setDataAndType(result, "image/gif")
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intent, "Share"));
    }
}
