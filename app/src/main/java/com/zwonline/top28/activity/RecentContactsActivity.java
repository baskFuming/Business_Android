package com.zwonline.top28.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zwonline.top28.R;

public class RecentContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_contacts);
       View recenContacts =findViewById(R.id.recent_contacts_fragment);

    }
}
