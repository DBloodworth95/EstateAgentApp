package model.properties;

import java.io.Serializable;

public class Property implements Serializable {
    protected String branchName, address, sold, type;
    protected int roomAmount;
    protected double sellPrice, soldPrice;
    protected int floorNumber;
    protected double monthlyRate;
    protected String garden, garage;
    protected int floorAmount;
    protected int id;

    public Property(int id, String branchName, String address, String sold, String type, int roomAmount, double sellPrice, double soldPrice, int floorNumber, double monthlyRate, String garden, String garage, int floorAmount) {
        this.id = id;
        this.branchName = branchName;
        this.address = address;
        this.sold = sold;
        this.type = type;
        this.roomAmount = roomAmount;
        this.sellPrice = sellPrice;
        this.soldPrice = soldPrice;
        this.floorNumber = floorNumber;
        this.monthlyRate = monthlyRate;
        this.garden = garden;
        this.garage = garage;
        this.floorAmount = floorAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public double getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(double monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public String getGarden() {
        return garden;
    }

    public void setGarden(String garden) {
        this.garden = garden;
    }

    public String getGarage() {
        return garage;
    }

    public void setGarage(String garage) {
        this.garage = garage;
    }

    public int getFloorAmount() {
        return floorAmount;
    }

    public void setFloorAmount(int floorAmount) {
        this.floorAmount = floorAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public int getRoomAmount() {
        return roomAmount;
    }

    public void setRoomAmount(int roomAmount) {
        this.roomAmount = roomAmount;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public double getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(double soldPrice) {
        this.soldPrice = soldPrice;
    }
}
