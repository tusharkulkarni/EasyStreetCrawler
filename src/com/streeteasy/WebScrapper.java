package com.streeteasy;

import java.io.CharConversionException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.json.JSONObject;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.streeteasy.comparator.JSONComparator;
import com.streeteasy.constants.Constants;

public class WebScrapper implements Constants{

	/**
	 * @param args
	 */
	public List<JSONObject> getListings(String initialAddress, ListingType listingType) {

		JSONObject jsonObject = null;
		Document page = null;
		List<JSONObject> listingsList = null;
		List<JSONObject> returnList = new ArrayList<JSONObject>();
		String url = "";
		int count = 1;
		switch(listingType){
		case RENTAL: 
			url = initialAddress + URL_FOR_RENT;
			break;
		case SALE: 
			url = initialAddress + URL_FOR_SALE;
			break;
		}		
		try{			
			while(true){
				page = Jsoup.connect(url).get();
				System.out.println("Reading data from url : " + url);
				Elements elem = page.getElementsByClass(LEFT_TWO_THIRDS);
				if(elem.hasClass(LEFT_TWO_THIRDS)){
					listingsList = new ArrayList<JSONObject>();
					listingsList = scrapPage(page, listingType);
					returnList.addAll(listingsList);
				}else {					
					break;
				}			
				if(count == 1){
					url += NEXT_PAGE+ ++count;
				}else{
					url = url.replaceAll(Integer.toString(count), Integer.toString(++count)) ;
				}	
				
			}

		} catch (HttpStatusException he){
			System.out.println(count + " pages scrapped");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return returnList;
	}

	public List<JSONObject> scrapPage(Document page, ListingType listingType) {

		JSONObject jsonObject;
		Elements rows = page.getElementsByClass("details");	
		List<JSONObject> listingsList = new ArrayList<JSONObject>();		 
		for (Element element : rows) {
			jsonObject = getListingObject(element, listingType);	
			listingsList.add(jsonObject);
		}		
		return listingsList;

	}

	private JSONObject getListingObject(Element element, ListingType listingType) {
		JSONObject jsonObject = new JSONObject();
		String listingClass = listingType.toString();

		Document divElement;
		String firstDetailCell;
		String lastDetailCell;
		String detailCell;
		divElement = Jsoup.parse(element.toString());

		//Extract address and url information from <a> tag
		Elements addressInfo = divElement.getElementsByClass(DETAILS_TITLE);
		Element a = addressInfo.select(ANCHOR_TAG).first();
		String url = URL_PREFIX + a.attr(HREF_ATTRIBUTE);
		String address = a.text();	

		//Extract price information from div price-info
		Elements priceInfo = divElement.getElementsByClass(PRICE_INFO);
		Elements priceSpan = priceInfo.select(SPAN_TAG);
		String price = priceSpan.text();				
		String[] priceArray = price.split(BLANK_SPACE);
		for (String checkPriceString : priceArray) {
			if(checkPriceString.contains(DOLLAR_SYMBOL)){
				price=checkPriceString;
				break;
			}
		}

		//Extract price information from div details_Info
		Elements unitInfo = divElement.getElementsByClass(DETAILS_INFO);
		Elements unitSpan = unitInfo.select(SPAN_TAG);
		Document spanElement = Jsoup.parse(unitSpan.toString());
		StringBuffer unitBuffer = new StringBuffer();
		unitBuffer.append(spanElement.getElementsByClass(FIRST_DETAIL_CELL).text()).append(BLANK_SPACE);
		unitBuffer.append(spanElement.getElementsByClass(DETAIL_CELL).text()).append(BLANK_STRING);
		unitBuffer.append(spanElement.getElementsByClass(LAST_DETAIL_CELL).text());			

		//Build json Object
		jsonObject.put(LISTING_CLASS, listingClass);
		jsonObject.put(ADDRESS, address);
		jsonObject.put(UNIT, unitBuffer.toString());
		jsonObject.put(URL, url);
		jsonObject.put(PRICE, Integer.parseInt(price.substring(1,price.length()).replaceAll(COMMA, BLANK_STRING)));
	
		
	
			

		return jsonObject;
	}
}
