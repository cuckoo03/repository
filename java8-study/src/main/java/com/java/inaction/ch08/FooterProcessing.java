package com.java.inaction.ch08;

import com.java.inaction.ch08.chain.ProcessingObject;

public class FooterProcessing extends ProcessingObject<String>{
	@Override
	protected String handleWork(String input) {
		return "footer:" + input;
	}

}
