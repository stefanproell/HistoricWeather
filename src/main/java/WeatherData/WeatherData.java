package WeatherData;



import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by stefan on 21.12.14.
 */

@Entity
@Table(name="WeatherData", uniqueConstraints=
@UniqueConstraint(columnNames={"latitude", "longitude", "weather_timestamp", "insert_timestamp"}))

public class WeatherData {
    int hour;
    String summary;
    String icon;
    Double windspeed;
    Date time;


    Date weather_timestamp;
    Double humidity;
    Double visibility;
    int windBearing;
    Double apparentTemperature;
    Double precipProbability;
    Double     precipIntensity;
    Double dewPoint;
    Double temperature;
    int identifier;
    Date insert_time;

    @Column(name="insert_timestamp")
    public Date getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(Date insert_time) {
        this.insert_time = insert_time;
    }

    @Column(name="longitude")
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Column(name="latitude")
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Column(name="address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    String longitude;
    String latitude;
    String address;

    public String getPrecipType() {
        return precipType;
    }

    public void setPrecipType(String precipType) {
        this.precipType = precipType;
    }

    @Column(name="precipType")
    String precipType;



    @Id
    @GeneratedValue
    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public WeatherData() {
        Date currentDate = new Date();
        this.setInsert_time(currentDate);
    }

    @Column(name="hour")
    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    @Column(name="summary")
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Column(name="icon")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Column(name="windspeed")
    public Double getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(Double windspeed) {
        this.windspeed = windspeed;
    }

    @Column(name="weather_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getWeather_timestamp() {
        return weather_timestamp;
    }

    public void setWeather_timestamp(Date weather_timestamp) {
        this.weather_timestamp = weather_timestamp;
    }




    @Column(name="humidity")
    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    @Column(name="visibility")
    public Double getVisibility() {
        return visibility;
    }

    public void setVisibility(Double visibility) {
        this.visibility = visibility;
    }

    @Column(name="wind_bearing")
    public int getWindBearing() {
        return windBearing;
    }

    public void setWindBearing(int windBearing) {
        this.windBearing = windBearing;
    }

    @Column(name="apparent_temperature")
    public Double getApparentTemperature() {
        return apparentTemperature;
    }

    public void setApparentTemperature(Double apparentTemperature) {
        this.apparentTemperature = apparentTemperature;
    }

    @Column(name="precip_probability")
    public Double getPrecipProbability() {
        return precipProbability;
    }

    public void setPrecipProbability(Double precipProbability) {
        this.precipProbability = precipProbability;
    }
    @Column(name="precip_intensity")
    public Double getPrecipIntensity() {
        return precipIntensity;
    }

    public void setPrecipIntensity(Double precipIntensity) {
        this.precipIntensity = precipIntensity;
    }
    @Column(name="dew_point")
    public Double getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(Double dewPoint) {
        this.dewPoint = dewPoint;
    }
    @Column(name="temperature")
    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
}
