package com.apockestafe.team19;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.os.SystemClock.sleep;

/*
*** EventActivty is the view used to create a new Event
 */
public class EventActivity extends AppCompatActivity {

    private EditText title, date, time, street, city, state, zipcode, description;
    private Button createButton, cancelButton;
    SharedPreferencesEditor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        setTitle("Create Event");
        editor = new SharedPreferencesEditor(getSharedPreferences("login", MODE_PRIVATE));
        title = (EditText)findViewById(R.id.title);
        date = (EditText)findViewById(R.id.date);
        time = (EditText)findViewById(R.id.time);
        street = (EditText)findViewById(R.id.street);
        city = (EditText)findViewById(R.id.city);
        state = (EditText)findViewById(R.id.state);
        zipcode = (EditText)findViewById(R.id.zipcode);
        description = (EditText)findViewById(R.id.description);
        createButton = (Button)findViewById(R.id.button_create);
        cancelButton = (Button)findViewById(R.id.button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventActivity.this, MainActivity.class));
                finish();
            }
        });

        /*
        *** When pushed, the information is pulled from the
        *** EditText variables and creates a new event with them
         */
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<RideInfo> ri = new ArrayList<>(0);
                ArrayList<String> itemsList = new ArrayList<>(0);
                ArrayList<String> attendingList = new ArrayList<>(0);
                attendingList.add(editor.getName());
                String location;
                ListRideActivity addressManager = new ListRideActivity();
                location = addressManager.createAddress(street, city, state, zipcode);

                Event event = new Event(title.getText().toString(),
                        date.getText().toString(), time.getText().toString(),
                        location, description.getText().toString(),
                         ri, itemsList, attendingList);
                event.add();
                sleep(1000);
                startActivity(new Intent(EventActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
