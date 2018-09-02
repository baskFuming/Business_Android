package com.zwonline.top28.web;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.zwonline.top28.R;

public class InputActivity extends ZWBaseActivity {

    private Button inputButton;
    private EditText inputText;
    private RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        inputButton = (Button)findViewById(R.id.inputButton);
        inputText = (EditText)findViewById(R.id.inputText);
        mainLayout = (RelativeLayout)findViewById(R.id.inputLayout);

        mainLayout.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                hideKeyboard();
                finish();
                return true;
            }
        });

        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                String result = inputText.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("result",result);
                setResult(123,intent);
                finish();
            }
        });


        getSupportActionBar().hide();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("ACTIVITY","============");

        inputText.requestFocus();
        final InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        inputText.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                inputText.requestFocus();
                imm.showSoftInput(inputText, 0);
            }
        }, 100);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
