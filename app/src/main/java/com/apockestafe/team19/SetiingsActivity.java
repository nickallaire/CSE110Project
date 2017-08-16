package com.apockestafe.team19;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SetiingsActivity extends AppCompatActivity {
    private Button backButton, signOutButton;
    private SharedPreferencesEditor editor;
    private EditText firstName, lastName;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setiings);
        setTitle("Settings");
        name = null;
        firstName = (EditText) findViewById(R.id.firstNameEditText);
        lastName = (EditText) findViewById(R.id.lastNameEditText);
        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editor = new SharedPreferencesEditor(getSharedPreferences("login", MODE_PRIVATE));
                if (firstName.getText().length() != 0)
                  name = firstName.getText().toString();
                if (lastName.getText().length() != 0){
                    if(name != null) {
                        name = name + " " + lastName.getText().toString();
                    }
                    else{
                        name = lastName.getText().toString();
                    }
                }
                if(name != null) {
                    editor.addName(name);
                }
                startActivity(new Intent(SetiingsActivity.this, MainActivity.class));
            }
        });

        signOutButton = (Button) findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(SetiingsActivity.this, SigninActivity.class);
                i.putExtra("accessToken", "null");
                startActivity(i);
            }
        });
    }
}