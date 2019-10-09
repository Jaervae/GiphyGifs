package com.example.giphugifs;

import androidx.appcompat.app.AppCompatActivity;
import com.example.giphugifs.BuildConfig;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

    EditText editText = null;
    Button button = null;
    String apiKey = "?api_key=" + BuildConfig.ApiKey;
    private final String urlP1 = "https://api.giphy.com/v1/gifs/";
    private String type;
    private String searchedItem = "";
    private String limit;
    private String offset;
    private final String urlP2 = "&offset=";
    private final String urlP3 = "&rating=G&lang=en";

    String finalUrl = "";

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    ArrayList<String> listItems = new ArrayList<String>();
    final private String TAG = "giphyApp";
    private GridView gridView;
    Search searchObj;

    static final String SHARED_PREF_FILE = "GiphyApp";
    static final String SHARED_PREF_STRING_SEARCH_TYPE = "SearchType";
    static final String SHARED_PREF_STRING_SEARCH_OFFSET = "SearchOffset";
    static final String SHARED_PREF_STRING_SEARCH_AMOUNT = "SearchAmount";


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
                loadData();
                switch (type) {
                    case "search":
                        finalUrl = urlP1 + type + apiKey + "&q="+ searchedItem + "&limit=" + limit + urlP2 + offset + urlP3;
                        break;
                    case "trending":
                        finalUrl = urlP1 + type + apiKey + "&limit=" + limit + "&rating=G";
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected SharedPreferences getPref() {
        return getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_searchSettings) {
            loadData();
            CustomDialogClass cdd=new CustomDialogClass(this, searchObj);
            cdd.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveToSharedPrefs(){
        Log.d("giphyApp",type + " " + limit + " " + offset + " saving");
        SharedPreferences sharedPreferences = getPref();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREF_STRING_SEARCH_OFFSET,offset);
        editor.putString(SHARED_PREF_STRING_SEARCH_AMOUNT,limit);
        editor.putString(SHARED_PREF_STRING_SEARCH_TYPE,type);
        editor.apply();
    }

    public void setObj(Search searchObj){
        offset = searchObj.getOffset();
        limit = searchObj.getLimit();
        type = searchObj.getType();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
    public void loadData(){
        SharedPreferences sharedPreferences = getPref();
        type = sharedPreferences.getString(SHARED_PREF_STRING_SEARCH_TYPE,null);
        limit = sharedPreferences.getString(SHARED_PREF_STRING_SEARCH_AMOUNT,null);
        offset = sharedPreferences.getString(SHARED_PREF_STRING_SEARCH_OFFSET,null);
        searchObj = new Search(type,limit,offset);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
