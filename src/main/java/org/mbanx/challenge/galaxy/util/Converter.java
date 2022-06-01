package org.mbanx.challenge.galaxy.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.mbanx.challenge.galaxy.exception.InvalidNumberFormatException;
import org.mbanx.challenge.galaxy.model.GalaxyNumberUnit;

public class Converter {
	
	private Map<Character,Integer> romanToNumbeMap;
	private Map<String, String> galaxyToRomanMap;
	private Map<String, Integer> galaxyToNumberMap;
	private Map<String, Double> galaxyUnitToNumberMap;
	
	public Converter(
			Map<Character, Integer> romanToNumbeMap, 
			Map<String, String> galaxyToRomanMap,
			Map<String, Integer> galaxyToNumberMap, 
			Map<String, Double> galaxyUnitToNumberMap) {

		this.romanToNumbeMap = romanToNumbeMap;
		this.galaxyToRomanMap = galaxyToRomanMap;
		this.galaxyToNumberMap = galaxyToNumberMap;
		this.galaxyUnitToNumberMap = galaxyUnitToNumberMap;
	}

	public boolean isRomanValid(String roman) throws InvalidNumberFormatException {
		String regex = "(^(?=[MDCLXVI])M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(roman);
		boolean valid = m.matches();
		if(!valid) {
			throw new InvalidNumberFormatException();
		}
		return valid;
	}

	public int romanToNumber(String roman) {
		int number = 0;
		for (int i = 0; i < roman.length(); i++) {
			if (i + 1 == roman.length() || romanToNumbeMap.get(roman.charAt(i)) >= romanToNumbeMap.get(roman.charAt(i + 1))) {
				number += romanToNumbeMap.get(roman.charAt(i));
			} else {
				number -= romanToNumbeMap.get(roman.charAt(i));
			}
		}
		return number;
	}

	public String galaxyToRoman(String text) throws InvalidNumberFormatException {
		String roman = "";
		if(StringUtils.isNotBlank(text)) {
			String[] g1Splits = text.split(Pattern.quote(" "));
			for (int i = 0; i < g1Splits.length; i++) {
				String galaxyNumber = g1Splits[i];
				
				// process only not blank character
				if(StringUtils.isNotBlank(galaxyNumber)) {
					String normGalaxyNumber = StringUtils.trim(galaxyNumber).toLowerCase();
					if(galaxyToRomanMap.containsKey(normGalaxyNumber)) {
						roman += galaxyToRomanMap.get(normGalaxyNumber);
					}
					else {
						throw new InvalidNumberFormatException();
					}
				}
			}
		}
		this.isRomanValid(roman);
		return roman;
	}

	public int galaxyToNumber(String text) throws InvalidNumberFormatException {
		int number = 0;
		String romanNumber = this.galaxyToRoman(text);
		number = this.romanToNumber(romanNumber);
		return number;
	}

	public double galaxyUnitToNumber(String text) {
		String normText = text.toLowerCase();
		if(galaxyUnitToNumberMap.containsKey(normText)) {
			return galaxyUnitToNumberMap.get(normText);
		}
		else {
			throw new NullPointerException();
		}
	}

	public double galaxyNumberUnitToCredit(GalaxyNumberUnit gnu) {
		int number = gnu.getNumber();
		String galaxyUnit = gnu.getGalaxyUnit();
		double unitValue = this.galaxyUnitToNumber(galaxyUnit);
		double total = number * unitValue;
		return total;
	}
	
	public GalaxyNumberUnit textToGalaxyNumberUnit(String text) throws Exception {
		//Break group1 into galaxyNumber and unit
		//Break using white space
		String[] splits = text.split(Pattern.quote(" "));
		int splitsLength = splits.length;
		// get last word

		String galaxyUnit = StringUtils.trim(splits[splitsLength - 1]);
		String galaxyNumber = StringUtils.trim(StringUtils.replace(text, " "+galaxyUnit, ""));
		
		String romanNumber = this.galaxyToRoman(galaxyNumber);
		int number = this.romanToNumber(romanNumber);

		GalaxyNumberUnit gnu = new GalaxyNumberUnit();
		gnu.setGalaxyNumber(galaxyNumber);
		gnu.setGalaxyUnit(galaxyUnit);
		gnu.setRomanNumber(romanNumber);
		gnu.setNumber(number);
		return gnu;
	}
	
}
