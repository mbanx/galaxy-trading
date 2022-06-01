package org.mbanx.challenge.galaxy.processor;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.mbanx.challenge.galaxy.model.QueryOutput;
import org.mbanx.challenge.galaxy.util.Converter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GalaxyToRomanProcessor extends TextProcessor{
	
	public GalaxyToRomanProcessor(
			String pattern,
			Converter converter,
			Map<Character, Integer> romanToNumbeMap, 
			Map<String, String> galaxyToRomanMap,
			Map<String, Integer> galaxyToNumberMap, 
			Map<String, Double> galaxyUnitToNumberMap) {
		
		super(pattern, converter, romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);
	}
	
	@Override
	public QueryOutput process(String text) {
		QueryOutput qo = new QueryOutput();
	
		boolean valid = false;
		if(StringUtils.isNotBlank(text)) {
//			String pattern = "([a-zA-Z]+)\\s+(?i)(is)\\s+([I,V,X,L,C,D,M]+)";
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(text);
			while (m.find()) {
				String group1 = StringUtils.trim(m.group(1));
				String group2 = StringUtils.trim(m.group(2));
				String group3 = StringUtils.trim(m.group(3));
				log.debug("group1={}, group2={}, group3={}", group1, group2, group3);
	
				String galaxy = group1;
				String roman = group3;
	
				try {
					converter.isRomanValid(roman);
					int number = converter.romanToNumber(roman);
	
					galaxyToRomanMap.put(galaxy.toLowerCase(), roman);
					galaxyToNumberMap.put(galaxy.toLowerCase(), number);
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
}
