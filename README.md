# HistoricWeather

This is a small sample application showing how to retrieve and process data from the Forecast IO API and Google Geo-Coding API and store it for personal use in a MySQL database. This tool allows you to retrieve data for a specific date and iterate over previous years in order to compare the weather conditions. The data records are stored by using Hibernate.

The tool is written in Java and uses thw following technologies:

    Google Geo-Coding API to retrieve longitude and latitude coordinates
    Forecast IO to retrieve the weather data for a specific location
    MySQL database to store the data
    Hibernate
    Maven


Details are described on my personal blog: http://blog.stefanproell.at/2015/01/07/historicalweather/
