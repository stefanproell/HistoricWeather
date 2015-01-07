package API;


import WeatherData.*;
import com.github.dvdme.ForecastIOLib.FIOHourly;
import com.github.dvdme.ForecastIOLib.ForecastIO;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderGeometry;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import org.hibernate.Query;
import org.hibernate.Session;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by stefan on 21.12.14.
 */
public class RetrieveData {
    private Session session;


    public RetrieveData() {
        this.init();
    }

    private void init(){


    }

    private void persist(WeatherData weatherData){

        if(!this.recordExists(weatherData)){

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.session.beginTransaction();
            this.session.save(weatherData);
            this.session.getTransaction().commit();
            this.session.flush();
            this.session.close();
    }


    }

    public void getYears(String location, String dateString, int amountYears){
        Date startDate = this.getDateFromISO8601String(dateString);

        Calendar cal = null;
        try {
            cal = this.toCalendar(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }





        for(int i = 0; i < amountYears;i++){
            cal.add(Calendar.YEAR,-1);
            Date newDate = cal.getTime();
            String newDateString = this.fromCalendar(cal);
            System.out.println("Current Date: "  + this.fromCalendar(cal));
            this.retrieve(location,newDateString);

        }


    }

    public void retrieve(String location, String dateString) {
        String latitude = "";
        String longitude = "";
        String address = "";
        HashMap<String,String> weatherDataHashMap = new HashMap();

        final Geocoder geocoder = new Geocoder();
        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(location).setLanguage("en").getGeocoderRequest();
        GeocodeResponse geocoderResponse = null;

        try {
            geocoderResponse = geocoder.geocode(geocoderRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }


        List<GeocoderResult> geoResultList = geocoderResponse.getResults();

        System.out.println("Retrieved geo");
        for (GeocoderResult result : geoResultList) {
            address = result.getFormattedAddress();
            GeocoderGeometry geometry = result.getGeometry();
            latitude = String.valueOf(geometry.getLocation().getLat());
            longitude = String.valueOf(geometry.getLocation().getLng());

            System.out.println(result.getFormattedAddress() + " lat: " + latitude + " long: " + longitude);

        }

        ForecastIO fio = new ForecastIO("211150e7d2e3257327aa43570d0f2d85"); //instantiate the class with the API key.
        fio.setUnits(ForecastIO.UNITS_SI);             //sets the units as SI - optional

        fio.setLang(ForecastIO.LANG_ENGLISH);

        fio.setTime(dateString);

        fio.getForecast(latitude, longitude);
      //  System.out.println("Latitude: " + fio.getLatitude());
      //  System.out.println("Longitude: " + fio.getLongitude());
      //  System.out.println("Timezone: " + fio.getTimezone());

        String key ="";
        String value ="";


        FIOHourly hourly = new FIOHourly(fio);
        //In case there is no hourly data available
        if (hourly.hours() < 0)
            System.out.println("No hourly data.");
        else
            System.out.println("\nHourly:\n");
        //Print hourly data
        for (int i = 0; i < hourly.hours(); i++) {
            String[] h = hourly.getHour(i).getFieldsArray();
            String hour = String.valueOf(i + 1);
            System.out.println("Hour #" +hour);

            /**
             * Populate the map of data values
             */
            weatherDataHashMap.clear();
            weatherDataHashMap.put("Hour",hour);
            for (int j = 0; j < h.length; j++){

                key = h[j];
                value = hourly.getHour(i).getByKey(h[j]);
                if (value == null){
                    System.out.println("value war NULL");
                    value="";
                }
                System.out.println(key + ": " + value);
                System.out.println("\n");

                weatherDataHashMap.put(key,value);

            }

            /**
             * Create the weather object
             */
            WeatherData hourData;
            hourData = new WeatherData();
            System.out.println("---------- " + weatherDataHashMap.get("Hour"));
            hourData.setHour(this.parseIntValue(weatherDataHashMap.get("Hour")));
            hourData.setSummary(weatherDataHashMap.get("summary"));
            hourData.setIcon(weatherDataHashMap.get("icon"));
            hourData.setWindspeed(this.parseDoubleValue(weatherDataHashMap.get("windSpeed")));
            Date measureData = this.getDateFromString(weatherDataHashMap.get("time"));
            hourData.setWeather_timestamp(measureData);
            hourData.setHumidity(this.parseDoubleValue(weatherDataHashMap.get("humidity")));
            hourData.setVisibility(this.parseDoubleValue(weatherDataHashMap.get("visibility")));
            hourData.setWindBearing(this.parseIntValue(weatherDataHashMap.get("windBearing")));
            hourData.setApparentTemperature(this.parseDoubleValue(weatherDataHashMap.get("apparentTemperature")));
            hourData.setWindBearing(this.parseIntValue(weatherDataHashMap.get("windBearing")));
            hourData.setPrecipProbability(this.parseDoubleValue(weatherDataHashMap.get("precipProbability")));
            hourData.setPrecipIntensity(this.parseDoubleValue(weatherDataHashMap.get("precipIntensity")));
            hourData.setDewPoint(this.parseDoubleValue(weatherDataHashMap.get("dewPoint")));
            hourData.setTemperature(this.parseDoubleValue(weatherDataHashMap.get("temperature")));
            hourData.setPrecipType(this.removeQuotes(weatherDataHashMap.get("precipType")));
            hourData.setAddress(address);
            hourData.setLatitude(latitude);
            hourData.setLongitude(longitude);


            this.persist(hourData);


        }




/*
        FIODaily daily = new FIODaily(fio);
        //In case there is no daily data available
        if (daily.days() < 0)
            System.out.println("No daily data.");
        else
            System.out.println("\nDaily:\n");
        //Print daily data
        for (int i = 0; i < daily.days(); i++) {
            String[] h = daily.getDay(i).getFieldsArray();
            System.out.println("Day #" + (i + 1));
            for (int j = 0; j < h.length; j++)
                System.out.println(h[j] + ": " + daily.getDay(i).getByKey(h[j]));
            System.out.println("\n");
        }*/

    }

    private String removeQuotes(String input){
        String output = null;
        if (input != null){
            output= input.replaceAll("^\"|\"$", "");
        }

        return output;
    }



    private Double parseDoubleValue(String input){
        if(input ==null){
            return null;
        } else{
            return Double.parseDouble(input);
        }


    }


    private int parseIntValue(String input){
        if(input ==null){
            return -1;
        } else{
            return Integer.parseInt(input);
        }


    }

    private Date getDateFromString(String dateString){

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:SS");
        Date date = null;
        try {
            date = format.parse(dateString);
            System.out.println("DATE " + date.getHours() +" m "+  date.getMinutes() +" s "+  date.getSeconds());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;


    }

    private Date getDateFromISO8601String(String dateString){

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(dateString);
            System.out.println("DATE " + date.getHours() +" m "+  date.getMinutes() +" s "+  date.getSeconds());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;


    }



    private String getMySQLDateFormatFromString(Date inputDate){


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(inputDate);


    }

    /**
     * Check if the record is already in the DB
     * @param hourData
     * @return
     */
    public Boolean recordExists (WeatherData hourData) {
        this.session = HibernateUtil.getSessionFactory().openSession();

        boolean exists = false;


    String queryString = "select identifier FROM WeatherData WHERE latitude = "+hourData.getLatitude()+" AND longitude = "+ hourData.getLongitude()
            + " AND weather_timestamp = \'" + this.getMySQLDateFormatFromString(hourData.getWeather_timestamp())+"\'";


        Query query = this.session.createQuery(queryString);

        System.out.println("Query: " +queryString);

        List result = query.list();
        int count = result.size();
        if(count >0){
            System.out.println("record exists, count is " + count);
            exists = true;
        } else{
            exists = false;
        }


        this.session.close();
        return exists;
    }

    /** Transform Calendar to ISO 8601 string. */
    public String fromCalendar(final Calendar calendar) {
        Date date = calendar.getTime();
        String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .format(date);
        return formatted;
    }



    /** Transform ISO 8601 string to Calendar. */
    public Calendar toCalendar(final String iso8601string)
            throws ParseException {
        Calendar calendar = GregorianCalendar.getInstance();

        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(iso8601string);
        calendar.setTime(date);
        return calendar;
    }
}
