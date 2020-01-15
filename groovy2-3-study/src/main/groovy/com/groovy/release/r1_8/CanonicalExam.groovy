package com.groovy.release.r1_8


import groovy.transform.Canonical;
import groovy.transform.ToString;
import groovy.transform.TypeChecked;

@TypeChecked
class CanonicalExam {
	static main(args) {
		def p1 = new Person5(name: "p1", age: 1)
//		def p2 = new Person5("p1", 1)
//		p2.name = "p2"
//		println p1.equals(p2)
//		println p1
	}
}
/**
 * groovy eclipse 3.4부터 @canonical 애노테이션을 쓸경우 에러 발생 
 * Internal compiler error: java.lang.IncompatibleClassChangeError: Expecting non-static method org.codehaus.groovy.transform.CanonicalASTTransformation.getMemberList(Lorg/codehaus/groovy/ast/AnnotationNode;Ljava/lang/String;)Ljava/util/List; at org.codehaus.groovy.transform.CanonicalASTTransformation.visit(CanonicalASTTransformation.java:60)
 * 이클립스 oxgen, photon에서 발견됨
 * @author jkko
 *
 */
//@Canonical
@ToString(includeNames = true)
class Person5 {
	String name
	int age
}
