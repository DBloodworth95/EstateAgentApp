package model.properties;

import java.io.Serializable;

public class House extends Property implements Serializable {

    public House(String branchName, String address, String sold, String type, int roomAmount, double sellPrice, double soldPrice, int floorNumber, double monthlyRate, String garden, String garage, int floorAmount) {
        super(branchName, address, sold, type, roomAmount, sellPrice, soldPrice, floorNumber, monthlyRate, garden, garage, floorAmount);
        setMonthlyRate(0);
        setFloorNumber(0);
        setType("House");
    }
}
