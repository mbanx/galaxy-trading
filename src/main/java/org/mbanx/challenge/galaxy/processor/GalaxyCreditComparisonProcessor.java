package org.mbanx.challenge.galaxy.processor;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.mbanx.challenge.galaxy.exception.InvalidNumberFormatException;
import org.mbanx.challenge.galaxy.model.GalaxyNumberUnit;
import org.mbanx.challenge.galaxy.model.QueryOutput;
import org.mbanx.challenge.galaxy.util.Converter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GalaxyCreditComparisonProcessor extends TextProcessor{

	public GalaxyCreditComparisonProcessor(
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
//			String pattern = "(?i)does\\s+([\\w\\s]+)(has|have)\\s+(more|less)\\s+credits\\s+than\\s+([\\w\\s]+)\\?";
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(text);
			while (m.find()) {
				String group1 = StringUtils.trim(m.group(1));
				String group2 = StringUtils.trim(m.group(4));
				log.debug("group1={}, group2={}", group1, group2);

				try {
					GalaxyNumberUnit gnu1 = converter.textToGalaxyNumberUnit(group1);
					GalaxyNumberUnit gnu2 = converter.textToGalaxyNumberUnit(group2);

					double credit1 = converter.galaxyNumberUnitToCredit(gnu1);
					double credit2 = converter.galaxyNumberUnitToCredit(gnu2);

					String comparison = "";
					if(credit1 == credit2) {
						comparison = "has equals Credits to";
					}
					else if(credit1 > credit2) {
						comparison = "has more Credits than";
					}
					else {
						comparison = "has less Credits than";
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
