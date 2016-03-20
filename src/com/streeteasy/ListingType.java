package com.streeteasy;

public enum ListingType {
	SALE, RENTAL;
	
	@Override 
	public String toString(){ 
		switch (this) {
			case SALE: 
				return "Sale";
			case RENTAL: 
				return "Rental";		
		}
		return "Invalid Listing";
	}
}