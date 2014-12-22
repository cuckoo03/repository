package com.thread.ch12_list12_1;

public class MakeStringRequest extends MethodRequest {
	private int count;
	private char fillchar;

	@SuppressWarnings("unchecked")
	public MakeStringRequest(Servant servant, FutureResult<String> future,
			int count, char fillchar) {
		super(servant, future);
		this.count = count;
		this.fillchar = fillchar;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute() {
		Result<String> result = servant.makeString(count, fillchar);
		future.setResult(result);
	}

}
