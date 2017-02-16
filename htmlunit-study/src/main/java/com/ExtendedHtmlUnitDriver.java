package com;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;

public class ExtendedHtmlUnitDriver extends HtmlUnitDriver {
	public ExtendedHtmlUnitDriver(boolean enableJavascript) {
		super(enableJavascript);
	}

	public ExtendedHtmlUnitDriver(BrowserVersion version) {
		super(version);
	}

	public void setHeader(String name, String value) {
		this.getWebClient().addRequestHeader(name, value);
	}

	public void setThrowExceptionOnScriptError(boolean enable) {
		this.getWebClient().getOptions().setThrowExceptionOnScriptError(enable);
	}
}
