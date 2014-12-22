package com.thread.ch12_exam12_2;

public class MakeNumberRequest extends MethodRequest<String> {
	private String x;
	private String y;

	protected MakeNumberRequest(Servant servant, FutureResult<String> future,
			String x, String y) {
		super(servant, future);
		this.x = x;
		this.y = y;
	}

	@Override
	public void execute() {
		Result<String> result = servant.add(x, y);
		future.setResult(result);
	}
}
