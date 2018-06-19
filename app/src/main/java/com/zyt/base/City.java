package com.zyt.base;

/**
 * Created by chenweiqi on 2018/5/30.
 */

import com.contrarywind.interfaces.IPickerViewData;

/**
 * City
 */
public class City implements IPickerViewData{
    String name;

    public String getName() {
        if (name!=null && name.endsWith("市")){
            name =  name.substring(0,name.lastIndexOf("市"));
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City(String name) {
        this.name = name;
    }

    @Override
    public String getPickerViewText() {

        return getName();
    }
}
