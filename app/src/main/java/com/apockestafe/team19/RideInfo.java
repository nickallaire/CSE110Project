package com.apockestafe.team19;

import com.google.android.gms.maps.model.LatLng;
import java.util.List;

/*
*** RideInfo holds all fo the information associated with a listed ride
 */
public class RideInfo {

    private String carAddress;
    private List<String> peopleInCar;
    private int numberSeatsInCar;
    private LatLng latlng;

    public RideInfo(String carAddress, List<String> peopleInCar, int numberSeatsInCar) { //}, LatLng latlng) {
        this.carAddress = carAddress;
        this.peopleInCar = peopleInCar;
        this.numberSeatsInCar = numberSeatsInCar;
    }

    public RideInfo() {

    }

    public void setCarAddress(String cA) {
        this.carAddress = cA;
    }

    public void setPeopleInCar(List<String> pic) {
        this.peopleInCar = pic;
    }

    public void setNumberSeatsInCar(int nsic) {
        this.numberSeatsInCar = nsic;
    }

    public String getCarAddress() {
        return this.carAddress;
    }

    public List<String> getPeopleInCar() {
        return this.peopleInCar;
    }

    public int getNumberSeatsInCar() {
        return this.numberSeatsInCar;
    }
}
