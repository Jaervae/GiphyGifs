package com.example.giphugifs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {

    private Activity c;
    private Button saveButton;
    private Search searchObj;
    private MainActivity mainActivity;
    private Context context;

    private EditText editTextOffset;
    private EditText editTextAmount;
    private Spinner spinnerType;

    private String type;
    private String limit;
    private int idType;
    private String offset;

    private static final String SHARED_PREF_STRING_SEARCH_TYPE = "SearchType";
    private static final String SHARED_PREF_STRING_SEARCH_OFFSET = "SearchOffset";
    private static final String SHARED_PREF_STRING_SEARCH_AMOUNT = "SearchAmount";
    private static final String SHARED_PREF_STRING_SEARCH_ID = "SearchId";


    public CustomDialogClass(Activity a, Search searchObj, Context context) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.type = searchObj.getType();
        this.offset = searchObj.getOffset();
        this.limit = searchObj.getLimit();
        this.idType = searchObj.getIdType();
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        saveButton =findViewById(R.id.buttonDialogSave);
        saveButton.setOnClickListener(this);
        editTextOffset = findViewById(R.id.editTextDialogSearchOffset);
        editTextAmount = findViewById(R.id.editTextDialogSearchAmmount);
        spinnerType = findViewById(R.id.spinnerDialogSearchType);
        mainActivity = new MainActivity();

        editTextAmount.setText(limit);
        editTextOffset.setText(offset);
        spinnerType.setSelection(idType);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonDialogSave:
                offset = editTextOffset.getText().toString();
                limit = editTextAmount.getText().toString();
                type = spinnerType.getSelectedItem().toString();
                idType = spinnerType.getSelectedItemPosition();
                callMainActivity(type,limit,offset,idType);
                break;
            default:
                break;
        }
        dismiss();
    }
    private void callMainActivity(String type, String limit, String offset, int idType){
        searchObj = new Search(type,limit,offset,idType);
        SaveData saveData = new SaveData(context);
        saveData.saveString(SHARED_PREF_STRING_SEARCH_OFFSET, offset);
        saveData.saveString(SHARED_PREF_STRING_SEARCH_AMOUNT, limit);
        saveData.saveString(SHARED_PREF_STRING_SEARCH_TYPE, type);
        saveData.saveInt(SHARED_PREF_STRING_SEARCH_ID, idType);
        mainActivity.setObj(searchObj);
    }

}

