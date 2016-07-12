package com.zen.android.brite.dbflow.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.zen.android.brite.dbflow.TestDatabase;

import java.util.Date;

/**
 * Weather
 *
 * @author yangz
 * @version 2016/7/12
 */
@Table(name = "Weather", database = TestDatabase.class)
public class Weather extends BaseModel{

    public enum  Type{
        Sunny, Cloudy;
    }

    @Column
    private int temperature;
    @Column
    private String city;
    @Column
    @PrimaryKey
    private Date day;
    @Column
    private Type type;

    public Weather() {
    }

    public Weather(int temperature, String city, Date day, Type type) {
        this.temperature = temperature;
        this.city = city;
        this.day = day;
        this.type = type;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Weather weather = (Weather) o;

        if (temperature != weather.temperature) return false;
        if (city != null ? !city.equals(weather.city) : weather.city != null) return false;
        if (day != null ? !day.equals(weather.day) : weather.day != null) return false;
        return type == weather.type;

    }

    @Override
    public int hashCode() {
        int result = temperature;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (day != null ? day.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
