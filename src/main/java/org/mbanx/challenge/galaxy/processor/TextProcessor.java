package org.mbanx.challenge.galaxy.processor;

import java.util.Map;

import org.mbanx.challenge.galaxy.model.QueryOutput;
import org.mbanx.challenge.galaxy.util.Converter;

public abstract class TextProcessor {
	
	protected TextProcessor nextProcessor;
	
	protected String pattern;
	protected Converter converter;
	protected Map<Character,Integer> romanToNumbeMap;
	protected Map<String, String> galaxyToRomanMap;
	protected Map<String, Integer> galaxyToNumberMap;
	protected Map<String, Double> galaxyUnitToNumberMap; 
	
	public TextProcessor(
			String pattern,
			Converter converter,
			Map<Character, Integer> romanToNumbeMap, 
			Map<String, String> galaxyToRomanMap,
			Map<String, Integer> galaxyToNumberMap, 
			Map<String, Double> galaxyUnitToNumberMap) {
		super();
		
		this.pattern = pattern;
		this.converter = converter;
		this.romanToNumbeMap = romanToNumbeMap;
		this.galaxyToRomanMap = galaxyToRomanMap;
		this.galaxyToNumberMap = galaxyToNumberMap;
		this.galaxyUnitToNumberMap = galaxyUnitToNumberMap;
	}

	public abstract QueryOutput process(String text);
	
}
