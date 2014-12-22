package com.thread.ch12_list12_1;

public class DisplayStringRequest extends MethodRequest<Object>{
	private final String string;

	public DisplayStringRequest(Servant servant, String string) {
		super(servant, null);
		this.string = string;
	}
	
	public void execute() {
		servant.displayString(string);
	}
}
