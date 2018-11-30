package com.zyt.base;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

public class FTXCity  implements IPickerViewData {
    List<FTXCity> cities;
    String g;
    String n;
    String py;

    public List<FTXCity> getCities() {
        return cities;
    }

    public void setCities(List<FTXCity> cities) {
        this.cities = cities;
    }

    public String getG() {
        return g;
    }

    public void setG(String g) {
        this.g = g;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    @Override
    public String getPickerViewText() {
        return getN();
    }
}
