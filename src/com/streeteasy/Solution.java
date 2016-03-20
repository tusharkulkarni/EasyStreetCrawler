package com.streeteasy;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.streeteasy.comparator.JSONComparator;
import com.streeteasy.constants.Constants;


public class Solution implements Constants {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WebScrapper streetEasyScrapper = new WebScrapper();
		String url = "http://streeteasy.com";
		int count = 1;
		File saleFile = new File(OUTPUT_PATH +ListingType.SALE.toString()+JSON_EXTENSION);
		File rentalFile = new File(OUTPUT_PATH +ListingType.RENTAL.toString()+ JSON_EXTENSION);
		File outputFile = new File(OUTPUT_PATH + COMBINED_FILE + JSON_EXTENSION);
		
		List<JSONObject> salesList = streetEasyScrapper.getListings(url, ListingType.SALE);
		List<JSONObject> rentalsList = streetEasyScrapper.getListings(url, ListingType.RENTAL);
		List<JSONObject> combinedList = new ArrayList<JSONObject>();
		combinedList.addAll(salesList);
		combinedList.addAll(rentalsList);
		Collections.sort(combinedList, new JSONComparator());
		Collections.sort(salesList, new JSONComparator());
		Collections.sort(rentalsList, new JSONComparator());
		
		try{

		// if file doesnt exists, then create it
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		}
		if (!rentalFile.exists()) {
			rentalFile.createNewFile();
		}
		if (!saleFile.exists()) {
			saleFile.createNewFile();
		}
		FileWriter fw;
		BufferedWriter bw ;
		
		JSONArray salesJSOANArray = new JSONArray();
		salesJSOANArray.addAll(salesList.subList(0, 20));	
		fw = new FileWriter(saleFile);
		bw = new BufferedWriter(fw);
		bw.write(salesJSOANArray.toString());
		bw.close();
		fw.close();
		
		JSONArray rentalsJSOANArray = new JSONArray();		
		rentalsJSOANArray.add(rentalsList.subList(0,20));
		fw = new FileWriter(rentalFile);
		bw = new BufferedWriter(fw);
		bw.write(rentalsJSOANArray.toString());
		bw.close();
		fw.close();
			
		JSONArray combinedJSONArray = new JSONArray();
		combinedJSONArray.addAll(combinedList.subList(0, 20));		
		fw = new FileWriter(outputFile);
		bw = new BufferedWriter(fw);			
		bw.write(combinedJSONArray.toString());
		
		bw.close();
		fw.close();
		System.out.println("Output Files Written to disk");
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
