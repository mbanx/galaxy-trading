package org.mbanx.challenge.galaxy.processor.factory;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.mbanx.challenge.galaxy.processor.GalaxyCreditCalculationProcessor;
import org.mbanx.challenge.galaxy.processor.GalaxyCreditComparisonProcessor;
import org.mbanx.challenge.galaxy.processor.GalaxyNumberCalculationProcessor;
import org.mbanx.challenge.galaxy.processor.GalaxyNumberComparisonProcessor;
import org.mbanx.challenge.galaxy.processor.GalaxyToRomanProcessor;
import org.mbanx.challenge.galaxy.processor.GalaxyUnitToNumberProcessor;
import org.mbanx.challenge.galaxy.processor.TextProcessor;
import org.mbanx.challenge.galaxy.util.Converter;

public class TextProcessorFactory {
	
	private Converter converter;
	private Map<Character,Integer> romanToNumbeMap;
	private Map<String, String> galaxyToRomanMap;
	private Map<String, Integer> galaxyToNumberMap;
	private Map<String, Double> galaxyUnitToNumberMap; 
	
	public TextProcessorFactory(
			Converter converter, Map<Character, Integer> romanToNumbeMap,
			Map<String, String> galaxyToRomanMap, 
			Map<String, Integer> galaxyToNumberMap,
			Map<String, Double> galaxyUnitToNumberMap) {
		
		super();
		this.converter = converter;
		this.romanToNumbeMap = romanToNumbeMap;
		this.galaxyToRomanMap = galaxyToRomanMap;
		this.galaxyToNumberMap = galaxyToNumberMap;
		this.galaxyUnitToNumberMap = galaxyUnitToNumberMap;
	}

	public TextProcessor getTextProcessor(String name, String pattern){
		TextProcessor processor = null;
		if(StringUtils.isNotBlank(name)) {
			if(StringUtils.equalsAnyIgnoreCase(name, GalaxyToRomanProcessor.class.getSimpleName())) {
				processor = new GalaxyToRomanProcessor(pattern, converter, romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);
			}
			else if(StringUtils.equalsAnyIgnoreCase(name, GalaxyUnitToNumberProcessor.class.getSimpleName())) {
				processor = new GalaxyUnitToNumberProcessor(pattern, converter, romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);
			}
			else if(StringUtils.equalsAnyIgnoreCase(name, GalaxyNumberCalculationProcessor.class.getSimpleName())) {
				processor = new GalaxyNumberCalculationProcessor(pattern, converter, romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);
			}
			else if(StringUtils.equalsAnyIgnoreCase(name, GalaxyCreditCalculationProcessor.class.getSimpleName())) {
				processor = new GalaxyCreditCalculationProcessor(pattern, converter, romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);
			}
			else if(StringUtils.equalsAnyIgnoreCase(name, GalaxyCreditComparisonProcessor.class.getSimpleName())) {
				processor = new GalaxyCreditComparisonProcessor(pattern, converter, romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);
			}
			else if(StringUtils.equalsAnyIgnoreCase(name, GalaxyNumberComparisonProcessor.class.getSimpleName())) {
				processor = new GalaxyNumberComparisonProcessor(pattern, converter, romanToNumbeMap, galaxyToRomanMap, galaxyToNumberMap, galaxyUnitToNumberMap);
			}
		}
		return processor;
	}

}
