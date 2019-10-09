package com.example.giphugifs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.PRINT_SERVICE;

public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {
    public Activity c;
    public Dialog d;
    public Button save;
    Search searchObj;
    MainActivity mainActivity;

    EditText editTextOffset;
    EditText editTextAmount;
    Spinner spinnerType;

    private String type;
    private String limit;
    private String offset;

    static final String SHARED_PREF_FILE = "GiphyApp";
    static final String SHARED_PREF_STRING_SEARCH_TYPE = "SearchType";
    static final String SHARED_PREF_STRING_SEARCH_OFFSET = "SearchOffset";
    static final String SHARED_PREF_STRING_SEARCH_AMOUNT = "SearchAmount";



    public CustomDialogClass(Activity a, Search searchObj) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.type = searchObj.getType();
        this.offset = searchObj.getOffset();
        this.limit = searchObj.getLimit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        save = (Button) findViewById(R.id.buttonDialogSave);
        save.setOnClickListener(this);
        editTextOffset = findViewById(R.id.editTextDialogSearchOffset);
        editTextAmount = findViewById(R.id.editTextDialogSearchAmmount);
        spinnerType = findViewById(R.id.spinnerDialogSearchType);
        mainActivity = new MainActivity();

        editTextAmount.setText(limit);
        editTextOffset.setText(offset);

    }

    private SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonDialogSave:
                offset = editTextOffset.getText().toString();
                limit = editTextAmount.getText().toString();
                type = spinnerType.getSelectedItem().toString();
                callMainActivity(type,limit,offset);
                break;
            default:
                break;
        }
        dismiss();
    }
    public void callMainActivity(String type, String limit,String offset){
        searchObj = new Search(type,limit,offset);

        SharedPreferences.Editor editor = getPrefs(getContext()).edit();
        editor.putString(SHARED_PREF_STRING_SEARCH_OFFSET,offset);
        editor.putString(SHARED_PREF_STRING_SEARCH_AMOUNT,limit);
        editor.putString(SHARED_PREF_STRING_SEARCH_TYPE,type);
        editor.apply();

        Log.d("giphyApp",type + " " + limit + " " + offset + " saving done");
        mainActivity.setObj(searchObj);
    }

}

