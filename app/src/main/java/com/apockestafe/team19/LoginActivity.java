package com.apockestafe.team19;

import com.apockestafe.team19.Event;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import com.firebase.client.Firebase;


public class LoginActivity extends AppCompatActivity {

    private ListView scrollList;
    private Button addbutton;
    private Event e;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Firebase.setAndroidContext(this);
        ArrayList<String> rides = new ArrayList<>();
        rides.add("2915 Estancia, San Clemente, CA, 92670");
        rides.add("21 Edendale St, Ladera Ranch, CA, 92694");
        String[] eventString = new String[] {"Leslie's Super Sweet 16 09/20"};
//        e = new Event("Leslie's Super Sweet 16", "11/15/16", "9:00pm", "3853 Van Dyke Ave, San Diego, CA, 92105", "Let's Party", rides);
        final List<String> listStringer = new ArrayList<String>(Arrays.asList(eventString));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, eventString);

        setContentView(R.layout.activity_main);
        scrollList = (ListView) findViewById(R.id.listView);
        scrollList.setAdapter(arrayAdapter);
        scrollList = (ListView) findViewById(R.id.listView);
        scrollList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newActivity = new Intent(LoginActivity.this, EventInfo.class);
                startActivity(newActivity);
            }
        });


        addbutton = (Button) findViewById(R.id.button1);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, EventActivity.class));
            }
        });
    }
}
