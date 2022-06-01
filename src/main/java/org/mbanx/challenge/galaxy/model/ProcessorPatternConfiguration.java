package org.mbanx.challenge.galaxy.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Entity
@Table(
		indexes = {
				@Index(columnList = "processorName"),
				@Index(columnList = "pattern"),
				@Index(columnList = "enable"),
		})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(alphabetic = true)
public class ProcessorPatternConfiguration {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;

	protected String pattern;
	protected String processorName;
	
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@org.hibernate.annotations.ColumnDefault("1")
	protected Boolean enable;
	
	public ProcessorPatternConfiguration() {
		this.init();
	}
	
	protected void init() {
		enable = true;
	}
	
	@Override
	public String toString() {
		String toString = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			toString =  mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			log.error("", e);
		}
		return toString;
	}
}
