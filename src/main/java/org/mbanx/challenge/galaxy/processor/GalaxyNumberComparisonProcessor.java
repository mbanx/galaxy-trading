package org.mbanx.challenge.galaxy.processor;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.mbanx.challenge.galaxy.exception.InvalidNumberFormatException;
import org.mbanx.challenge.galaxy.model.QueryOutput;
import org.mbanx.challenge.galaxy.util.Converter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GalaxyNumberComparisonProcessor extends TextProcessor{
	
	public GalaxyNumberComparisonProcessor(
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
		String output = "I have no idea what you are talking about";
		if(StringUtils.isNotBlank(text)) {
//			String pattern = "(?i)is\\s+([\\w\\s]+)\\s+larger\\s+than\\s+([\\w\\s]+)\\?";
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(text);
			while (m.find()) {
				String group1 = StringUtils.trim(m.group(1));
				String group2 = StringUtils.trim(m.group(2));
				log.debug("group1={}, group2={}", group1, group2);

				try {
					int number1 = converter.galaxyToNumber(group1);
					int number2 = converter.galaxyToNumber(group2);

					String comparison = "";
					if(number1 == number2) {
						comparison = "is equals to";
					}
					else if(number1 > number2) {
						comparison = "is larger than";
					}
					else {
						comparison = "is smaller than";
					}

					output = String.format("%s %s %s", group1, comparison, group2);
					output = StringUtils.normalizeSpace(output);
					valid = true;
				}
				catch(InvalidNumberFormatException infe) {
					output = infe.getMessage();
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
}
