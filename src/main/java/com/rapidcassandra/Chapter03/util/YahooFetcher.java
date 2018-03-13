package com.rapidcassandra.Chapter03.util;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.security.cert.Certificate;
import java.io.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

import com.rapidcassandra.Chapter03.model.Quote;

// Ted Cahall 3/11/18
// Code shamelessly stolen from: 
// https://www.mkyong.com/java/java-https-client-httpsurlconnection-example/
public class YahooFetcher {

	static Logger log = Logger.getLogger(YahooFetcher.class.getName());

	// collect stock quote data from Yahoo! Finance
	static public BufferedReader getStock(String symbol) {
	// static public Boolean getStock(String symbol) {
		
		String apikey="TDECYA5L9Y4ONV0X";
		String https_url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY"
				+ "&symbol=" + symbol.toUpperCase()
				+ "&apikey=" + apikey
				+ "&outputsize=compact&datatype=csv";
		URL url;
		System.out.println("https_url: "+https_url);
		log.info(https_url);
		HttpsURLConnection connection = null;
		
		try {
			// Retrieve CSV stream
			url = new URL(https_url);
		    connection = (HttpsURLConnection)url.openConnection();


	      } catch (MalformedURLException e) {
		     e.printStackTrace();
	      } catch (IOException e) {
		     e.printStackTrace();
	      }

			// URLConnection connection = https_url.openConnection();
			InputStreamReader is = null;
			try {
				is = new InputStreamReader(
						connection.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Ted Cahall: IO connect Exception");
				e.printStackTrace();
			}

		     //dumpl all cert info
		     print_https_cert(connection);

		     //dump all the content
		     // print_content(connection);
			
			// return the BufferedReader
			return new BufferedReader(is);
			// return (true);
	}

	// convert each stock quote data into a Quote POJO
	static public Quote parseQuote(String symbol, String[] feed) {

		Date price_time = null;
		float daylow = 0;
		float dayhigh = 0;
		float dayopen = 0;
		float dayclose = 0;
		double volume = 0;

		try {
			price_time = new SimpleDateFormat("yyyy-MM-dd").parse(feed[0]);
			dayopen = Float.parseFloat(feed[1]);
			dayhigh = Float.parseFloat(feed[2]);
			daylow = Float.parseFloat(feed[3]);
			dayclose = Float.parseFloat(feed[4]);
			volume = Double.parseDouble(feed[5]);
			
		} catch (ParseException e) {
			log.log(Level.SEVERE, e.toString(), e);
		}
		// create a Quote POJO
		return new Quote(symbol.toUpperCase(), price_time, dayopen, dayhigh,
				daylow, dayclose, volume);
	}

	private static void print_https_cert(HttpsURLConnection con){

	    if(con!=null){

	      try {

		System.out.println("Response Code : " + con.getResponseCode());
		System.out.println("Cipher Suite : " + con.getCipherSuite());
		System.out.println("\n");

		Certificate[] certs = con.getServerCertificates();
		for(Certificate cert : certs){
		   System.out.println("Cert Type : " + cert.getType());
		   System.out.println("Cert Hash Code : " + cert.hashCode());
		   System.out.println("Cert Public Key Algorithm : "
	                                    + cert.getPublicKey().getAlgorithm());
		   System.out.println("Cert Public Key Format : "
	                                    + cert.getPublicKey().getFormat());
		   System.out.println("\n");
		}

		} catch (SSLPeerUnverifiedException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}

	     }

	   }

	   private static void print_content(HttpsURLConnection con){
		if(con!=null){

		try {

		   System.out.println("****** Content of the URL ********");
		   BufferedReader br =
			new BufferedReader(
				new InputStreamReader(con.getInputStream()));

		   String input;

		   while ((input = br.readLine()) != null){
		      System.out.println(input);
		   }
		   br.close();

		} catch (IOException e) {
		   e.printStackTrace();
		}

	       }

	   }
	
}
