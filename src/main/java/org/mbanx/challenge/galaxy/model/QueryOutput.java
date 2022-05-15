package org.mbanx.challenge.galaxy.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryOutput {
	private String input;
	private String output;
	private boolean valid;
}
