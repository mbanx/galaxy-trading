package org.mbanx.challenge.galaxy.service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.mbanx.challenge.galaxy.model.GalaxyNumberUnit;
import org.mbanx.challenge.galaxy.model.QueryOutput;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class TradingService {

	private Map<Character,Integer> romanToNumbeMap;
	private Map<String, String> galaxyToRomanMap;
	private Map<String, Integer> galaxyToNumberMap;
	private Map<String, Double> unitToValueMap;

	public TradingService() {
		romanToNumbeMap = new HashMap<>();
		galaxyToRomanMap = new HashMap<>();
		galaxyToNumberMap = new HashMap<>();
		unitToValueMap = new HashMap<>();

		this.initRomanToNumber();
	}

	public void initRomanToNumber() {
		//initial value
		romanToNumbeMap.put('I',1);
		romanToNumbeMap.put('V',5);
		romanToNumbeMap.put('X',10);
		romanToNumbeMap.put('L',50);
		romanToNumbeMap.put('C',100);   
		romanToNumbeMap.put('D',500);   
		romanToNumbeMap.put('M',1000); 
	}

	public boolean isRomanValid(String roman) throws Exception {
		String regex = "(^(?=[MDCLXVI])M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(roman);
		boolean valid = m.matches();
		if(!valid) {
			throw new Exception("Invalid roman number '"+roman+"'");
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

	public String galaxyToRoman(String text) throws Exception {
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
						throw new Exception("galaxyNumber '"+galaxyNumber+"' unrecognize");
					}
				}
			}
		}
		this.isRomanValid(roman);
		return roman;
	}

	public int galaxyToNumber(String text) throws Exception {
		int number = 0;
		String romanNumber = this.galaxyToRoman(text);
		number = this.romanToNumber(romanNumber);
		return number;
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

	public double galaxyUnitToUnitValue(String text) {
		String normText = text.toLowerCase();
		if(unitToValueMap.containsKey(normText)) {
			return unitToValueMap.get(normText);
		}
		else {
			throw new NullPointerException();
		}
	}

	public double galaxyNumberUnitToCredit(String text) throws Exception {
		GalaxyNumberUnit gnu = this.textToGalaxyNumberUnit(text);
		int number = gnu.getNumber();
		String galaxyUnit = gnu.getGalaxyUnit();
		double unitValue = this.galaxyUnitToUnitValue(galaxyUnit);
		double total = number * unitValue;
		return total;
	}

	public QueryOutput galaxyToRomanProcessor(String text) {
		QueryOutput qo = new QueryOutput();

		boolean valid = false;
		if(StringUtils.isNotBlank(text)) {
			String regex = "([a-zA-Z]+)\\s+(?i)(is)\\s+([I,V,X,L,C,D,M]+)";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(text);
			while (m.find()) {
				String group1 = StringUtils.trim(m.group(1));
				String group2 = StringUtils.trim(m.group(2));
				String group3 = StringUtils.trim(m.group(3));
				log.debug("group1={}, group2={}, group3={}", group1, group2, group3);

				String galaxy = group1;
				String roman = group3;

				try {
					this.isRomanValid(roman);
					int number = this.romanToNumber(roman);

					galaxyToRomanMap.put(galaxy.toLowerCase(), roman);
					galaxyToNumberMap.put(galaxy.toLowerCase(), number);
					valid = true;
				}
				catch (Exception e) {
					log.error("{}", roman);
				}
			}
		}

		qo.setInput(text);
		qo.setValid(valid);
		return qo;
	}

	public QueryOutput galaxyUnitToNumberProcessor(String text) {
		QueryOutput qo = new QueryOutput();

		boolean valid = false;
		if(StringUtils.isNotBlank(text)) {
			String regex = "(\\D+)(?i)(is)\\s+([0-9|.]+)\\s+(?i)(Credits)";
			Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(text);
			while (m.find()) {
				String group1 = StringUtils.trim(m.group(1));
				String group2 = StringUtils.trim(m.group(2));
				String group3 = StringUtils.trim(m.group(3));
				String group4 = StringUtils.trim(m.group(4));
				log.debug("group1={}, group2={}, group3={}, group4={}", group1, group2, group3, group4);

				try {
					GalaxyNumberUnit gnu = this.textToGalaxyNumberUnit(group1);
					double total = Double.parseDouble(group3);
					double number = (double) gnu.getNumber();
					double unitValue = total / number;

					unitToValueMap.put(gnu.getGalaxyUnit().toLowerCase(), unitValue);
					valid = true;
				}
				catch(Exception e) {
					log.error("{}", e.getMessage());
				}

			}
		}

		qo.setInput(text);
		qo.setValid(valid);
		return qo;
	}

	public QueryOutput galaxyNumberCalculationProcessor(String text) {
		QueryOutput qo = new QueryOutput();

		boolean valid = false;
		String output = "I have no idea what you are talking about";
		if(StringUtils.isNotBlank(text)) {
			String regex = "(?i)(how\\s+much\\s+is\\s+)([\\w\\s]+)(\\?)";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(text);
			while (m.find()) {
				String group1 = StringUtils.trim(m.group(1));
				String group2 = StringUtils.trim(m.group(2));
				String group3 = StringUtils.trim(m.group(3));
				log.debug("group1={}, group2={}, group3={}", group1, group2, group3);

				String galaxy = group2;
				try {
					int number = this.galaxyToNumber(galaxy);
					output = String.format("%s is %s", galaxy, number);
					valid = true;
				}
				catch(Exception e) {
					log.error("{}", e.getMessage());
				}
			}
		}

		qo.setInput(text);
		qo.setOutput(output);
		qo.setValid(valid);
		return qo;
	}

	public QueryOutput galaxyCreditsCalculationProcessor(String text) {
		QueryOutput qo = new QueryOutput();

		boolean valid = false;
		String output = "I have no idea what you are talking about";
		if(StringUtils.isNotBlank(text)) {
			String regex = "(?i)(how\\s+many\\s+Credits\\s+is\\s+)([\\w\\s]+)(\\?)";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(text);
			while (m.find()) {
				String group1 = StringUtils.trim(m.group(1));
				String group2 = StringUtils.trim(m.group(2));
				String group3 = StringUtils.trim(m.group(3));
				log.debug("group1={}, group2={}, group3={}", group1, group2, group3);

				try {
					GalaxyNumberUnit gnu = this.textToGalaxyNumberUnit(group2);
					int number = gnu.getNumber();
					String galaxyUnit = gnu.getGalaxyUnit();
					double unitValue = this.galaxyUnitToUnitValue(galaxyUnit);
					double credit = number * unitValue;
					
					DecimalFormat format = new DecimalFormat("0.###############");
					output = String.format("%s is %s", group2, format.format(credit));
					valid = true;
				}
				catch(Exception e) {
					log.error("{}", e.getMessage());
				}
			}
		}

		qo.setInput(text);
		qo.setOutput(output);
		qo.setValid(valid);
		return qo;
	}

	public String processTrading(String text) {
		StringBuilder builder = new StringBuilder();
		String[] multiLineText = text.split("\\r?\\n");
		for(String line: multiLineText) {
			String output = this.processLine(line);
			if(StringUtils.isNotBlank(output)) {
				builder.append(output).append(System.lineSeparator());
			}
		}
		return builder.toString();
	}

	public String processLine(String text) {
		String output = "";
		if(StringUtils.isNotBlank(text)) {
			QueryOutput firstOutput = this.galaxyToRomanProcessor(text);
			if(!firstOutput.isValid()) {
				QueryOutput secondOutput = this.galaxyUnitToNumberProcessor(text);
				if(!secondOutput.isValid()) {
					QueryOutput thirdOutput = this.galaxyNumberCalculationProcessor(text);
					if(!thirdOutput.isValid()) {
						QueryOutput forthOutput = this.galaxyCreditsCalculationProcessor(text);
						if(!forthOutput.isValid()) {
							output = "I have no idea what you are talking about";
						}
						else {
							output = forthOutput.getOutput();
						}
					}
					else {
						output = thirdOutput.getOutput();
					}
				}
			}
		}
		return output;
	}
}
