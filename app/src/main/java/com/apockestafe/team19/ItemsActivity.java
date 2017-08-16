package com.apockestafe.team19;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

/*
*** ItemsActivity is the activity that displays the items
*** attendees have added to the current event
 */
public class ItemsActivity extends AppCompatActivity {
    private Button backButton, addItemButton;
    private ListView itemsListView;
    private EditText addItemEditText;
    private ArrayList<String> itemList;
    private DatabaseReference ref;
    private FirebaseDatabase database;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        setTitle("Item List");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        final String s = getIntent().getStringExtra("eventNumber");

        itemsListView = (ListView) findViewById(R.id.itemsListView);
        initializeItemsListView(s);

        /*
        *** When clicked, launches the EventInfo activity
         */
        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(ItemsActivity.this, EventInfo.class);
            i.putExtra("eventNumber", s);
            startActivity(i);
            }
        });

        /*
        *** When clicked, add the item from the EditText variable
        *** and adds it to Firebase and displays it in the listview
         */
        addItemEditText = (EditText) findViewById(R.id.addItemEditText);
        addItemButton = (Button) findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            final String value = addItemEditText.getText().toString();
            database = FirebaseDatabase.getInstance();
            ref = database.getReference("TEAM19");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                    itemList = dataSnapshot.child("events").child(s).child("itemsList").getValue(t);
                    if (itemList == null)
                        itemList = new ArrayList<>();
                    if(value.length() > 0)
                        itemList.add(value);
                    adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, itemList);
                    itemsListView.setAdapter(adapter);
                    addItemEditText.setText("");
                    ref.child("events").child(s).child("itemsList").setValue(itemList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            }
        });

        /*
        *** When a long click is registered, a popup is displayed verifying
        *** is the user wants to delete the item from the listview
         */
        itemsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(ItemsActivity.this);
            alert.setTitle("Alert!!");
            alert.setMessage("Are you sure to delete item");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(final DialogInterface dialog, int which) {
                database = FirebaseDatabase.getInstance();
                ref = database.getReference("TEAM19");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {
                        };
                        itemList = dataSnapshot.child("events").child(s).child("itemsList").getValue(t);
                        if (itemList == null)
                            itemList = new ArrayList<>();
                        itemList.remove(position);
                        adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, itemList);
                        itemsListView.setAdapter(adapter);
                        ref.child("events").child(s).child("itemsList").setValue(itemList);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                }
            });

            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            alert.show();

            return true;
            }
        });
    }

    /*
    *** Creates the listview and pulls items from Firebase associated
    *** with the event and displays them
     */
    private  void initializeItemsListView(final String s) {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("TEAM19");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                itemList = dataSnapshot.child("events").child(s).child("itemsList").getValue(t);
                if (itemList != null) {
                    adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, itemList);
                    itemsListView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
