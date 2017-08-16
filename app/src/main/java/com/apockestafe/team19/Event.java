package com.apockestafe.team19;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Observable;
import java.util.List;
import android.content.Context;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import java.util.Set;
import static android.content.Context.MODE_PRIVATE;

/*
*** The Event class that stores all of the information
*** related to an event
 */
public class Event extends Observable {

    private String title, date, time, location, description;
    private List<RideInfo> rideLocation;
    private ArrayList<String> itemList;
    private  DatabaseReference ref;
    private  FirebaseDatabase database;
    public boolean deleted;
    public volatile boolean added = false;
    private SharedPreferencesEditor editor;
    private ArrayList<String> eventNumbers, attendingList;

    public Event(String title, String date, String time, String location,
                 String description, List<RideInfo> rideLocation, ArrayList<String> itemList,
                 ArrayList<String> attendingList) {

        this.title = title;
        this.date = date;
        this.time = time;
        this.location = location;
        this.description = description;
        this.rideLocation = rideLocation;
        this.itemList = itemList;
        this.attendingList = attendingList;
    }

    private void changeData (DataSnapshot dataSnapshot) {
        String key = dataSnapshot.getKey();
        if(key.equals("title")) {
            title = dataSnapshot.getValue(String.class);
        }
        if(key.equals("time")) {
            time = dataSnapshot.getValue(String.class);
        }
        if(key.equals("date")) {
            date = dataSnapshot.getValue(String.class);
        }
        if(key.equals("location")) {
            location = dataSnapshot.getValue(String.class);
        }
        if(key.equals("description")) {
            description = dataSnapshot.getValue(String.class);
        }
        if(key.equals("rideLocation")) {
            GenericTypeIndicator<List<RideInfo>> t = new GenericTypeIndicator<List<RideInfo>>() {};
            rideLocation = dataSnapshot.getValue(t);
        }
    }

    /*
    *** This method adds a new Event to the Firebase database
    *** and the users sharedPreferences
     */
    public void add() {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("TEAM19");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(added){
                    return;
                }
                long count;
                if(dataSnapshot.child("counters").child("counter").getValue() == null) {
                    count = -1;
                }
                else{
                    count = (long) dataSnapshot.child("counters").child("counter").getValue();
                }

                if(!added){
                    count++;
                    String s = Long.toString(count);
                    boolean returnVal = addHelper(s, dataSnapshot, count);
                    added = true;
                    if(returnVal) {
                        addHelper2(count);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean addHelper(String s, DataSnapshot ds, long count) {
        Context applicationContext = MainActivity.getContextOfApplication();
        editor = new SharedPreferencesEditor(applicationContext.getSharedPreferences("login", MODE_PRIVATE));

        if(added){
            return false;
        }
        added = true;
        count = count -1;
        String d = Long.toString(count);
        if(
                ds.child("events").child(d).child("title").getValue() != null &&
                (ds.child("events").child(d).child("title").getValue()).equals(this.title))
        {
           return false;
        }
        else{
            ref.child("events").child(""+s).setValue(this);
            Set<String> set = editor.getEvents();
            eventNumbers = new ArrayList<>();
            if (set != null)
                eventNumbers.addAll(set);
            eventNumbers.add(s);
            editor.addEvents((eventNumbers));
            return true;
        }
    }

    public void addHelper2(long count) {
        ref.child("counters").child("counter").setValue(count);
    }


    public void remove() {
        ref.setValue(null);
        deleted = true;
        notifyObservers();
    }

    public void changeTitle (String title) {
        if(deleted) {
            return;
        }
        this.title = title;
        ref.child("title").setValue(title);
    }

    public void changeTime (String time) {
        if(deleted) {
            return;
        }
        this.time = time;
        ref.child("time").setValue(time);
    }

    public void changeDate (String date) {
        if(deleted) {
            return;
        }
        this.date = date;
        ref.child("date").setValue(date);
    }

    public void changeLocation (String location) {
        if(deleted) {
            return;
        }
        this.location = location;
        ref.child("location").setValue(location);
    }

    public void changeDescription (String description) {
        if(deleted) {
            return;
        }
        this.description = description;
        ref.child("description").setValue(description);
    }

    public void changeRideLocation (ArrayList<RideInfo> rideLocation) {
        if(deleted) {
            return;
        }
        this.rideLocation = rideLocation;
        ref.child("rideLocation").setValue(rideLocation);
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public List<RideInfo> getRideLocation() { return rideLocation; }

    public ArrayList<String> getItemList() { return itemList; }

    public ArrayList<String> getAttendingList() {return attendingList;}

}

