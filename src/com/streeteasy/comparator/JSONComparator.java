package com.streeteasy.comparator;

import java.util.Comparator;

import com.streeteasy.constants.Constants;

import net.sf.json.JSONObject;

public class JSONComparator implements Comparator<JSONObject>, Constants{

	public int compare(JSONObject arg0, JSONObject arg1) {
/*		String price1String = arg0.getString(PRICE).replaceAll(COMMA, BLANK_STRING);
		String price2String = arg1.getString(PRICE).replaceAll(COMMA, BLANK_STRING);

		int price1 = Integer.parseInt(price1String.substring(1,price1String.length()));
		int price2 = Integer.parseInt(price2String.substring(1,price2String.length()));
				*/
		
		int price1 = arg0.getInt(PRICE);
		int price2 = arg1.getInt(PRICE);

		if(price1 > price2){
			return -1;
		}else if(price2 > price1){
			return 1;
		}
		return 0;
	}
}
