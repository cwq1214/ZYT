package com.zyt.base;

/**
 * Created by chenweiqi on 2018/5/30.
 */

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

/**
 * 省
 */
public class Province implements IPickerViewData{
    String name;
    List<City> cities;

    public String getName() {
        if (name!=null ){
            if (name.endsWith("市")){
                name = name.substring(0,name.lastIndexOf("市"));
            }
            if (name.endsWith("特别行政区")){
                name =  name.substring(0,name.lastIndexOf("特别行政区"));
            }
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public String getPickerViewText() {

        return  getName();
    }
}
