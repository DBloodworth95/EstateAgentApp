package model.properties;

import java.io.Serializable;

public class Flat extends Property implements Serializable {

    public Flat(int id, String branchName, String address, String sold, String type, int roomAmount, double sellPrice, double soldPrice, int floorNumber, double monthlyRate, String garden, String garage, int floorAmount) {
        super(id, branchName, address, sold, type, roomAmount, sellPrice, soldPrice, floorNumber, monthlyRate, garden, garage, floorAmount);
        setType("Flat");
        setGarage("N/A");
        setGarden("N/A");
        setFloorAmount(1);
    }
}
