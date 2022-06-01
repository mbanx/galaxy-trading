package org.mbanx.challenge.galaxy.processor;

import java.text.DecimalFormat;
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
public class GalaxyCreditCalculationProcessor extends TextProcessor{

	public GalaxyCreditCalculationProcessor(
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
//			String regex = "(?i)(how\\s+many\\s+Credits\\s+is\\s+)([\\w\\s]+)(\\?)";
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(text);
			while (m.find()) {
				String group1 = StringUtils.trim(m.group(1));
				String group2 = StringUtils.trim(m.group(2));
				String group3 = StringUtils.trim(m.group(3));
				log.debug("group1={}, group2={}, group3={}", group1, group2, group3);

				try {
					GalaxyNumberUnit gnu = converter.textToGalaxyNumberUnit(group2);
					int number = gnu.getNumber();
					String galaxyUnit = gnu.getGalaxyUnit();
					double unitValue = converter.galaxyUnitToNumber(galaxyUnit);
					double credit = number * unitValue;

					DecimalFormat format = new DecimalFormat("0.###############");
					output = String.format("%s is %s Credits", group2, format.format(credit));
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
