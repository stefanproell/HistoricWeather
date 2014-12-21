package API;

import WeatherData.HibernateUtil;
import WeatherData.WeatherData;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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

    public void retrieve() {
        String latitude = "";
        String longitude = "";
        String address = "";
        HashMap<String,String> weatherDataHashMap = new HashMap();

        final Geocoder geocoder = new Geocoder();
        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress("Reutte,Tirol, Austria").setLanguage("en").getGeocoderRequest();
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

        fio.setTime("2013-12-24T17:00:00-0400");

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

                System.out.println(key + ": " + value);
                System.out.println("\n");

                weatherDataHashMap.put(key,value);

            }

            /**
             * Create the weather object
             */
            WeatherData hourData = new WeatherData();
            System.out.println("---------- "+weatherDataHashMap.get("Hour"));
            hourData.setHour(Integer.parseInt(weatherDataHashMap.get("Hour")));
            hourData.setSummary(weatherDataHashMap.get("summary"));
            hourData.setIcon(weatherDataHashMap.get("icon"));
            hourData.setWindspeed(Double.parseDouble(weatherDataHashMap.get("windSpeed")));
            Date measureData = this.getDateFromString(weatherDataHashMap.get("time"));
            hourData.setWeather_timestamp(measureData);
            hourData.setHumidity(Double.parseDouble(weatherDataHashMap.get("humidity")));
            hourData.setVisibility(Double.parseDouble(weatherDataHashMap.get("visibility")));
            hourData.setWindBearing(Integer.parseInt(weatherDataHashMap.get("windBearing")));
            hourData.setApparentTemperature(Double.parseDouble(weatherDataHashMap.get("apparentTemperature")));
            hourData.setWindBearing(Integer.parseInt(weatherDataHashMap.get("windBearing")));
            hourData.setPrecipProbability(Double.parseDouble(weatherDataHashMap.get("precipProbability")));
            hourData.setPrecipIntensity(Double.parseDouble(weatherDataHashMap.get("precipIntensity")));
            hourData.setDewPoint(Double.parseDouble(weatherDataHashMap.get("dewPoint")));
            hourData.setTemperature(Double.parseDouble(weatherDataHashMap.get("temperature")));
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

}
