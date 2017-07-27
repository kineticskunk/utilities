package com.kineticskunk.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class IdentificationNumbers {
	
	private final Logger logger = LogManager.getLogger(IdentificationNumbers.class.getName());
	private final Marker IDENTIFICATIONUMBERS = MarkerManager.getMarker("IDENTIFICATIONUMBERS");
	
	private static Random random = new Random();
	
	private String clientType;
	private String clientIDNumber;
	private String convertedSAIDNumber;
	private int minorBirthYear;
	private int individualBirthYear;
	private int birthMonth;
	private int birthDay;
	private String validBirthYear;
	private String validBirthMonth;
	private String validBirthDay;
	private String clientDateOfBirth;
	
	public IdentificationNumbers() {
		this.clientType = null;
		this.clientIDNumber = null;
		this.convertedSAIDNumber = null;
		this.minorBirthYear = 0;
		this.individualBirthYear = 0;
		this.birthMonth = 0;
		this.birthDay = 0;
		this.validBirthYear = null;
		this.validBirthMonth = null;
		this.validBirthDay = null;
		this.clientDateOfBirth = null;
	}
	
	public String getClientIDNumber() {
		return this.clientIDNumber;
	}
	
	public void generateClientIDNumer(String clientType) {
		int number = random.nextInt(9999999) + 1;
		int length = (int)(Math.log10(number) + 1);
		if(length != 7) {
			number = random.nextInt(9999998) + 1;
		}
		this.clientIDNumber = this.generateRandomBirthDate(clientType) + String.valueOf(number);
	}
	
	public void convertToSAIDNumber() {
		if(this.getClientIDNumber().length() == 13){
			int secondP = 0;
			char charId[] =  this.getClientIDNumber().toCharArray();
			charId[10] = Character.forDigit( (int) Math.round( Math.random() ),10) ;
			String part2 = Integer.toString(Integer.valueOf(String.valueOf(charId[1])+String.valueOf(charId[3])+String.valueOf(charId[5])+String.valueOf(charId[7])+String.valueOf(charId[9])+String.valueOf(charId[11]))*2);
			char part2_v[] = part2.toCharArray();
			for(int i = 0;i<part2_v.length;i++){
				secondP = secondP + Character.getNumericValue(part2_v[i]);
			}
			int firtsP = Character.getNumericValue(charId[0])+Character.getNumericValue(charId[2])+Character.getNumericValue(charId[4])+Character.getNumericValue(charId[6])+Character.getNumericValue(charId[8])+Character.getNumericValue(charId[10]);
			String  totalP =Integer.toString(firtsP+secondP);
			String controlDigit = Integer.toString(10-Character.getNumericValue(totalP.toCharArray()[1]));
			charId[12]= controlDigit.toCharArray()[0];
			for(int i = 0; i < charId.length; i++){
				this.clientIDNumber =  this.clientIDNumber +charId[i];
			}
		} else {
			this.generateRandomBirthDate("individual");
			this.convertToSAIDNumber();
		}
	}
	
	
	//updateFieldValue("ID_NUMBER", new_geratedId);
	 
	public void generateCompanyNumber(){
		int length = 12;
	    Random random = new Random();
	    char[] digits = new char[length];
	    digits[0] = (char) (random.nextInt(9) + '1');
	    for (int i = 1; i < length; i++) {
	        digits[i] = (char) (random.nextInt(10) + '0');
	    }
	    
	    String company_number = Arrays.toString(digits).replaceAll("\\[|\\]|,|\\s", "");
	    System.out.println("Company number : "+company_number);
	    
	}
	
	//updateFieldValue("COMPANY_NUMBER", company_number);
	
	public String generateRandomBirthDate(String clientType) {
		
		try {
			int yearLength = 0;
			int minorBirthYear = random.nextInt(15 - 01) + 01;
			int individualBirthYear = random.nextInt(98 - 65) + 65;
			int birthMonth = random.nextInt(13 - 01) + 01;
			int birthDay = random.nextInt(32 - 01) + 01;
			
			switch (clientType.toLowerCase()) {
			case "minor":
				yearLength = (int)(Math.log10(minorBirthYear) + 1);
				
				if(yearLength == 1) {
					validBirthYear = String.valueOf("0") + minorBirthYear;
				} else {
					validBirthYear = String.valueOf(minorBirthYear);
				}
				break;
			case "individual": case "company": case "group":
				yearLength = (int)(Math.log10(individualBirthYear) + 1);
				if(yearLength == 1) {
					validBirthYear = String.valueOf("0") + individualBirthYear;
				} else {
					validBirthYear = String.valueOf(individualBirthYear);
				}
				break;
			default:
				this.logger.error(IDENTIFICATIONUMBERS, "This " + (char)34 + clientType + (char)34 + " is invalid");
				return null;
			}
		
			int birthMonthLength = (int)(Math.log10(birthMonth) + 1);
			
			if (birthMonthLength == 1) {
				validBirthMonth = String.valueOf("0") + birthMonth;
			} else {
				validBirthMonth = String.valueOf(birthMonth);
			}

			int birthDayLength = (int)(Math.log10(birthDay)+1);
			
			if(birthDayLength == 1) {
				validBirthDay = String.valueOf("0") + birthDay;
			} else {
				validBirthDay = String.valueOf(birthDay);
			}
			
			clientDateOfBirth = String.valueOf(validBirthYear) +  String.valueOf(validBirthMonth) + String.valueOf(validBirthDay);
	
		} catch (Exception e) {
			this.logger.error(IDENTIFICATIONUMBERS, "Method generateRandomBirthDate generated an exception " + e.getStackTrace());
			return null;
		}
	
		return clientDateOfBirth;
	}

}
