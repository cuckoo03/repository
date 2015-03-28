package scripts

import groovy.transform.TypeChecked;

import com.apress.isf.spring.ch14.Login;

@TypeChecked
class GroovyLoginService implements Login{
	String username
	
	@Override
	public boolean isAuthorized() {
		return false;
	}
}
