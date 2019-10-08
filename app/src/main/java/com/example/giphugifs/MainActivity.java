package com.example.giphugifs;

import androidx.appcompat.app.AppCompatActivity;
import com.example.giphugifs.BuildConfig;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editText = null;
    Button button = null;
    String apiKey = BuildConfig.ApiKey;
    private final String urlP1 = "https://api.giphy.com/v1/gifs/search?api_key=";
    private String searchedItem = "";
    private String limit = "100";
    private final String urlP2 = "&offset=0&rating=G&lang=en";

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    ArrayList<String> listItems = new ArrayList<String>();
    final private String TAG = "giphyApp";
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editTextMain);
        gridView = findViewById(R.id.gridViewMain);
        button = findViewById(R.id.buttonMain);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchedItem = editText.getText().toString();
                String finalUrl = urlP1 + apiKey + "&q="+ searchedItem + "&limit=" + limit + urlP2;
                Log.d(TAG, finalUrl);
                sendRequest(finalUrl);
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String url = listItems.get(position);
                /*Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
                //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
                //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);

                //need this to prompts email client only
                email.setType("message/rfc822");
                startActivity(Intent.createChooser());*/
                return false;
            }
        });
    }

    public void sendRequest(String aParam){
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, aParam, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");
                    for(int i = 0; i < data.length(); i++){
                        JSONObject c = data.getJSONObject(i);
                        JSONObject d = c.getJSONObject("images");
                        JSONObject e = d.getJSONObject("downsized");
                        listItems.add(e.getString("url"));
                        Log.d(TAG, listItems.get(i));
                    }
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gridView.setAdapter(
                                    new SimpleListAdapter(
                                            MainActivity.this,
                                            listItems
                                    )
                            );

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG,"Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }
}
