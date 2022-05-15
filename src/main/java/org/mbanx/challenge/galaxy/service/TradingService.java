package org.mbanx.challenge.galaxy.service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.mbanx.challenge.galaxy.model.GalaxyNumberUnit;
import org.mbanx.challenge.galaxy.model.QueryOutput;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Service
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
		
		//init value
		romanToNumbeMap.put('I',1);
		romanToNumbeMap.put('V',5);
		romanToNumbeMap.put('X',10);
		romanToNumbeMap.put('L',50);
		romanToNumbeMap.put('C',100);   
		romanToNumbeMap.put('D',500);   
		romanToNumbeMap.put('M',1000);    
		
		
	}

	public static void main(String[] args) {
		TradingService service = new TradingService();



//		{
//			String roman = "MCMXLIV";
//			log.info("roman={}, valid={}", roman, service.isRomanValid(roman));
//			int number = service.romanToNumber(roman);
//			log.info("roman={}, number={}", roman, number);
//		}
//		{
//			String roman = "XXXIX";
//			log.info("roman={}, valid={}", roman, service.isRomanValid(roman));
//			int number = service.romanToNumber(roman);
//			log.info("roman={}, number={}", roman, number);
//		}

		//		{
		//			service.firstTypeProcessor("taip is XXXX");
		//			service.firstTypeProcessor("glob is I");
		//			service.firstTypeProcessor("prok is V");
		//			service.firstTypeProcessor("pish is X");
		//			service.firstTypeProcessor("tegj is L");
		//
		//			log.info("galaxyToRomanMap={}", service.getGalaxyToRomanMap());
		//			log.info("galaxyToNumberMap={}", service.getGalaxyToNumberMap());
		//		}
		//
		//		{
		//			service.secondTypeProcessor("glob glob Silver is 34 Credits");
		//			service.secondTypeProcessor("glob prok Gold is 57800 Credits");
		//			service.secondTypeProcessor("pish pish Iron is 3910 Credits");
		//
		//			log.info("unitToValueMap={}", service.getUnitToValueMap());
		//		}
		//
		//		{
		//			service.thirdTypeProcessor("How much is pish tegj glob glob ?");
		//		}
		//
		//		{
		//			service.forthTypeProcessor("how many Credits is glob prok Silver ?");
		//			service.forthTypeProcessor("how many Credits is glob prok Gold ?");
		//			service.forthTypeProcessor("how many Credits is glob prok Iron ?");
		//			service.forthTypeProcessor("how much wood could a woodchuck chuck if a woodchuck could chuck wood ?");
		//		}

				{
					StringBuilder builder = new StringBuilder();
					builder.append("glob is I").append(System.lineSeparator());
					builder.append("prok is V").append(System.lineSeparator());
					builder.append("pish is X").append(System.lineSeparator());
					builder.append("tegj is L").append(System.lineSeparator());
					builder.append("glob glob Silver is 34 Credits").append(System.lineSeparator());
					builder.append("glob prok Gold is 57800 Credits").append(System.lineSeparator());
					builder.append("pish pish Iron is 3910 Credits").append(System.lineSeparator());
					builder.append("how much is pish tegj glob glob ?").append(System.lineSeparator());
					builder.append("how many Credits is glob prok Silver ?").append(System.lineSeparator());
					builder.append("how many Credits is glob prok Gold ?").append(System.lineSeparator());
					builder.append("how many Credits is glob prok Iron ?").append(System.lineSeparator());
					builder.append("how much wood could a woodchuck chuck if a woodchuck could chuck wood ?").append(System.lineSeparator());
					String text = builder.toString();
					String output = service.processTrading(text);
					System.out.println(output);
					
					log.info("{}", service.getGalaxyToRomanMap());
					log.info("{}", service.getGalaxyToNumberMap());
				}
	}
	
	public boolean isRomanValid(String roman) {
		String regex = "(^(?=[MDCLXVI])M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(roman);
		return m.matches();
	}
	
	public int romanToNumber(String roman) {
		int result = 0;
		if(StringUtils.isNotBlank(roman)) {
			for (int i = 0; i < roman.length(); i++) {
				char ch = roman.charAt(i); // Current Roman Character
				// Case 1
				if (i > 0 && romanToNumbeMap.get(ch) > romanToNumbeMap.get(roman.charAt(i - 1))) {
					result += romanToNumbeMap.get(ch) - 2 * romanToNumbeMap.get(roman.charAt(i - 1));
				}
				// Case 2: just add the corresponding number to result.
				else
					result += romanToNumbeMap.get(ch);
			}
		}
		return result;
	}

	public String galaxyToRoman(String text) {
		String roman = "";
		if(StringUtils.isNotBlank(text)) {
			String[] g1Splits = text.split(Pattern.quote(" "));
			for (int i = 0; i < g1Splits.length; i++) {
				String galaxyNumber = g1Splits[i];

				if(galaxyToRomanMap.containsKey(galaxyNumber)) {
					roman += galaxyToRomanMap.get(galaxyNumber);
				}
			}
		}
		return roman;
	}

	public int galaxyToNumber(String text) {
		int number = 0;
		String romanNumber = this.galaxyToRoman(text);
		number = this.romanToNumber(romanNumber);
		return number;
	}

	public GalaxyNumberUnit textToGalaxyNumberUnit(String text) {
		//Break group1 into galaxyNumber and unit
		//Break using white space
		String[] splits = text.split(Pattern.quote(" "));
		int splitsLength = splits.length;
		// get last word

		String galaxyUnit = splits[splitsLength - 1];
		String galaxyNumber = StringUtils.replace(text, " "+galaxyUnit, "");
		String romanNumber = this.galaxyToRoman(text);
		int number = this.romanToNumber(romanNumber);

		GalaxyNumberUnit gnu = new GalaxyNumberUnit();
		gnu.setGalaxyNumber(galaxyNumber);
		gnu.setGalaxyUnit(galaxyUnit);
		gnu.setRomanNumber(romanNumber);
		gnu.setNumber(number);
		return gnu;
	}

	public double galaxyUnitToUnitValue(String text) {
		Double unitValue = 0.0;
		if(unitToValueMap.containsKey(text)) {
			unitValue = unitToValueMap.get(text);
		}
		return unitValue;
	}

	public double galaxyNumberUnitToCredit(String text) {
		GalaxyNumberUnit gnu = this.textToGalaxyNumberUnit(text);
		int number = gnu.getNumber();
		String galaxyUnit = gnu.getGalaxyUnit();
		double unitValue = this.galaxyUnitToUnitValue(galaxyUnit);
		double total = number * unitValue;
		return total;
	}

	public QueryOutput firstTypeProcessor(String text) {
		QueryOutput qo = new QueryOutput();

		boolean valid = false;
		if(StringUtils.isNotBlank(text)) {
			String regex = "^([a-zA-Z]+) (is) ([I,V,X,L,C,D,M]+$)";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(text);
			while (m.find()) {
				String group1 = m.group(1);
				String group2 = m.group(2);
				String group3 = m.group(3);
				//				log.info("group1={}, group2={}, group3={}", group1, group2, group3);

				String galaxy = group1;
				String roman = group3;
				if(isRomanValid(roman)) {
					int number = this.romanToNumber(roman);
					
					galaxyToRomanMap.put(galaxy, roman);
					galaxyToNumberMap.put(galaxy, number);
					
					valid = true;
				}
			}
		}

		qo.setInput(text);
		qo.setValid(valid);
		return qo;
	}

	public QueryOutput secondTypeProcessor(String text) {
		QueryOutput qo = new QueryOutput();

		boolean valid = false;
		if(StringUtils.isNotBlank(text)) {
			String regex = "(\\D+) (?i)(is) ([0-9]+) (Credits)";
			Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(text);
			while (m.find()) {
				String group1 = m.group(1);
				String group2 = m.group(2);
				String group3 = m.group(3);
				String group4 = m.group(4);
				//				log.info("group1={}, group2={}, group3={}, group4={}", group1, group2, group3, group4);

				GalaxyNumberUnit gnu = this.textToGalaxyNumberUnit(group1);
				double total = Double.parseDouble(group3);
				double number = (double) gnu.getNumber();
				double unitValue = total / number;

				unitToValueMap.put(gnu.getGalaxyUnit(), unitValue);
				valid = true;
			}
		}

		qo.setInput(text);
		qo.setValid(valid);
		return qo;
	}

	public QueryOutput thirdTypeProcessor(String text) {
		QueryOutput qo = new QueryOutput();

		boolean valid = false;
		String output = "I have no idea what you are talking about";
		if(StringUtils.isNotBlank(text)) {
			String regex = "(?i)(how much is) ([\\w\\s]+)(\\?)";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(text);
			while (m.find()) {
				String group1 = m.group(1);
				String group2 = m.group(2).trim();
				String group3 = m.group(3);
				//				log.info("group1={}, group2={}, group3={}", group1, group2, group3);

				String galaxy = group2;
				int number = this.galaxyToNumber(galaxy);
				//				log.info("{} is {}", galaxy, number);
				output = String.format("%s is %s", galaxy, number);
				valid = true;
			}
		}

		qo.setInput(text);
		qo.setOutput(output);
		qo.setValid(valid);
		return qo;
	}

	public QueryOutput forthTypeProcessor(String text) {
		QueryOutput qo = new QueryOutput();

		boolean valid = false;
		String output = "I have no idea what you are talking about";
		if(StringUtils.isNotBlank(text)) {
			String regex = "(?i)(how many Credits is) ([\\w\\s]+)(\\?)";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(text);
			while (m.find()) {
				String group1 = m.group(1);
				String group2 = m.group(2).trim();
				String group3 = m.group(3);
				//				log.info("group1={}, group2={}, group3={}", group1, group2, group3);

				GalaxyNumberUnit gnu = this.textToGalaxyNumberUnit(group2);
				int number = gnu.getNumber();
				String galaxyUnit = gnu.getGalaxyUnit();
				double unitValue = this.galaxyUnitToUnitValue(galaxyUnit);
				long credit = Math.round(number * unitValue);
				//				log.info("{} is {}", group2, credit);

				output = String.format("%s is %s", group2, credit);
				valid = true;
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
			QueryOutput firstOutput = this.firstTypeProcessor(text);
			if(!firstOutput.isValid()) {
				QueryOutput secondOutput = this.secondTypeProcessor(text);
				if(!secondOutput.isValid()) {
					QueryOutput thirdOutput = this.thirdTypeProcessor(text);
					if(!thirdOutput.isValid()) {
						QueryOutput forthOutput = this.forthTypeProcessor(text);
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
