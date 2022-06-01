package org.mbanx.challenge.galaxy.processor;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.mbanx.challenge.galaxy.model.GalaxyNumberUnit;
import org.mbanx.challenge.galaxy.model.QueryOutput;
import org.mbanx.challenge.galaxy.util.Converter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GalaxyUnitToNumberProcessor extends TextProcessor{
	
	public GalaxyUnitToNumberProcessor(
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
//			String pattern = "(\\D+)(?i)(is)\\s+([0-9|.]+)\\s+(?i)(Credits)";
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(text);
			while (m.find()) {
				String group1 = StringUtils.trim(m.group(1));
				String group2 = StringUtils.trim(m.group(2));
				String group3 = StringUtils.trim(m.group(3));
				String group4 = StringUtils.trim(m.group(4));
				log.debug("group1={}, group2={}, group3={}, group4={}", group1, group2, group3, group4);

				try {
					GalaxyNumberUnit gnu = converter.textToGalaxyNumberUnit(group1);
					double total = Double.parseDouble(group3);
					double number = (double) gnu.getNumber();
					double unitValue = total / number;

					galaxyUnitToNumberMap.put(gnu.getGalaxyUnit().toLowerCase(), unitValue);
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
