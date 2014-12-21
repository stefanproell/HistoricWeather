package API;

import com.github.dvdme.ForecastIOLib.FIODaily;
import com.github.dvdme.ForecastIOLib.FIOHourly;
import com.github.dvdme.ForecastIOLib.ForecastIO;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderGeometry;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;

import java.io.IOException;
import java.util.List;

/**
 * Created by stefan on 21.12.14.
 */
public class HistoricalWeatherMain {

    public static void main(String[] args) {
        System.out.println("Weatherdata");
        RetrieveData dataRetrieval = new RetrieveData();
        dataRetrieval.retrieve();

        System.exit(0);


    }







}
