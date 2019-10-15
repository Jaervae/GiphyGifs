package com.example.giphugifs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    private EditText editText = null;
    private Button button = null;
    private final String urlP1 = "https://api.giphy.com/v1/gifs/";
    private String apiKey = "?api_key=" + BuildConfig.ApiKey;
    private String type, limit, offset;
    private String searchedItem = "";
    private String finalUrl = "";
    private final String urlP2 = "&offset=";
    private final String urlP3 = "&rating=G&lang=en";
    private int idType;


    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private ArrayList<String> listItems = new ArrayList<String>();
    private final String TAG = "giphyApp";
    private GridView gridView;
    private Search searchObj;
    private Context context = this;

    static final String SHARED_PREF_STRING_SEARCH_TYPE = "SearchType";
    static final String SHARED_PREF_STRING_SEARCH_OFFSET = "SearchOffset";
    static final String SHARED_PREF_STRING_SEARCH_AMOUNT = "SearchAmount";
    static final String SHARED_PREF_STRING_SEARCH_ID = "SearchId";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editTextMain);
        gridView = findViewById(R.id.gridViewMain);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(),SingleGifActivity.class);
                intent.putExtra("fav", listItems.get(position).toString());
                intent.putExtra("show", true);
                startActivity(intent);
            }
        });
        button = findViewById(R.id.buttonMain);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItems.clear();
                searchedItem = editText.getText().toString();
                switch (type) {
                    case "search":
                        finalUrl = urlP1 + type + apiKey + "&q="+ searchedItem + "&limit=" + limit + urlP2 + offset + urlP3;
                        break;
                    case "trending":
                        finalUrl = urlP1 + type + apiKey + "&limit=" + limit + "&rating=G";
                        Log.d(TAG,finalUrl);
                        break;
                    case "random":
                        finalUrl = urlP1 + type + apiKey + "&tag=&rating=G";
                        break;
                }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_searchSettings) {
            loadData();
            CustomDialogClass cdd=new CustomDialogClass(this, searchObj,context);
            cdd.show();
            return true;
        }
        if (id == R.id.action_favorites){
            Intent intent = new Intent(this, FavoriteActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setObj(Search searchObj){
        offset = searchObj.getOffset();
        limit = searchObj.getLimit();
        type = searchObj.getType();
        idType = searchObj.getIdType();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData(){
        SaveData saveData = new SaveData(context);
        type = saveData.getString(SHARED_PREF_STRING_SEARCH_TYPE);
        limit = saveData.getString(SHARED_PREF_STRING_SEARCH_AMOUNT);
        offset = saveData.getString(SHARED_PREF_STRING_SEARCH_OFFSET);
        idType = saveData.getInt(SHARED_PREF_STRING_SEARCH_ID);

        searchObj = new Search(type,limit,offset,idType);
    }
}