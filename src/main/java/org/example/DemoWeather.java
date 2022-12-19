package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Date;
import java.util.List;
import java.util.Locale;

import org.example.de.hska.iwii.db1.weather.JDBCWetter;
import org.example.de.hska.iwii.db1.weather.model.Weather;
import org.example.de.hska.iwii.db1.weather.model.WeatherForecast;
import org.example.de.hska.iwii.db1.weather.reader.WeatherReader;

/**
 * Demo-Klasse fuer den Zugriff auf das Wetter der Stadt Karlsruhe. 
 */
public class DemoWeather {

	public static void main(String[] args) throws Exception {
		// 1. Erzeugt ein WeatherReader-Objekt fuer die komplette
		//    Serverkommunikation. Fuer den Zugriff uber den
		//    Proxy der Hochschule muss der zweite Konstruktur mit
		//    den Proxy-Parametern verwendet werden.
		//    Proxy-Server: proxy.hs-karlsruhe.de
		//    Port des Proxy-Servers: 8888
		//WeatherReader reader = new WeatherReader("proxy.hs-karlsruhe.de",8888, ConnectionData.name(), ConnectionData.password());
		WeatherReader reader = new WeatherReader();
		//WeatherForecast weatherForecast = reader.readWeatherForecast(10519);
		//List<Weather> weatherList = weatherForecast.getWeather();
		//System.out.println(weatherList.get(0).getSunshine());
		System.out.println(reader.toString());
		// 2. Auslesen von Informationen ueber einen oder mehrere Orte.
		// Die Liste der Stationen ist hier verlinkt (4. Spalte enthaelt die ID):
		// https://www.dwd.de/DE/leistungen/klimadatendeutschland/statliste/statlex_html.html
		// Beispiele:
		// 10519: Karlsruhe Durlach
		// 10321: Stuttgart-Degerloch
		// Direktes Aufrufen der API:
		// https://dwd.api.bund.dev/
		WeatherForecast forecast = reader.readWeatherForecast(10460);
		if (forecast != null) {
			for (Weather weather: forecast.getWeather()) {
				System.out.println(weather.getDate() + ", " + weather.getMinTemp() / 10.0 + ", " + weather.getMaxTemp() / 10.0);
			}
		}
		DBConnector dbConnector = new DBConnector();
		dbConnector.connectDB();
		dbConnector.getConnection().setAutoCommit(false);
		Statement statement = dbConnector.getConnection().createStatement();
		String sql;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		int[] stations = new int[]{10309,10442,12982,10552,10020};
		for (int i = 0; i < 5; i++) {
			forecast = reader.readWeatherForecast(stations[i]);
			sql = "INSERT INTO wetter (stationsid, datum, mintemp, maxtemp, precipitation, sunshine) VALUES (%d,%s,%f,%f,%d,%d)";
			for (Weather weather : forecast.getWeather()) {
				String newsql = String.format(Locale.CANADA,sql, stations[i], "DATE '"+weather.getDate()+"'", weather.getMinTemp(), weather.getMaxTemp(), weather.getPrecipitation(), weather.getSunshine());
				System.out.println(newsql);
				statement.addBatch(newsql);
			}
		}
		statement.executeBatch();
		dbConnector.getConnection().commit();


//		sql = "SELECT * FROM wetter WHERE stationsid = ?";
//		prep = dbConnector.getConnection().prepareStatement(sql);
//		System.out.println("stationsid: ");
//		String station = bufferedReader.readLine();
//		prep.setString(1, station);
//		ResultFormatter resultFormatter = new ResultFormatter();
//		System.out.println(resultFormatter.processResultSet(prep.executeQuery()));
//
//
//		sql = "SELECT * FROM wetter WHERE datum = ? AND mintemp >= ? AND maxtemp <= ?";
//		prep = dbConnector.getConnection().prepareStatement(sql);
//		System.out.println("Datum: ");
//		String datum = bufferedReader.readLine();
//		System.out.println("\nmintemp: ");
//		String mintemp = bufferedReader.readLine();
//		System.out.println("\nmaxtemp:");
//		String maxtemp = bufferedReader.readLine();
//		prep.setDate(1, (java.sql.Date.valueOf(datum)));
//		prep.setFloat(2, Float.parseFloat(mintemp));
//		prep.setFloat(3, Float.parseFloat(maxtemp));
//
//		resultFormatter = new ResultFormatter();
//		System.out.println(resultFormatter.processResultSet(prep.executeQuery()));
	}
}