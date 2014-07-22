package com.groovy.groovyinaction.ch07

import javax.management.remote.rmi.RMIConnectionImpl.CombinedClassLoader;

class Ch07 {
	static main(args) {
		assert ('a' =~ /./)
		assert [1]
		assert ![]
		assert 1
		assert !0
		assert new Object()
		assert !null
		
		def x = 1
		if ((x = 3)) {
			println x
		}

		def store = []
		while (x = x - 1) {
			store << x
		}
		assert store == [2, 1]
		
		while (x = 1) {
			println x
			break
		}
		
		if (['x']) {
			assert true 
		}
		
		switch (10) {
			case Integer : assert true;break
		}
		
		SomeClass some = new SomeClass()
		some.untypedField = 1
		println some.untypedField.class.name
		some.untypedField = '1'
		println some.untypedField.class.name
		
		def counter = new Counter()
		counter.count = 1
		assert counter.count == 1
		
		def fieldName = 'count'
		counter[fieldName] = 2
		assert counter['count'] == 2
		
		def defsome = new SomeClass()
		defsome.publicVoidMethod()
		assert 'hi' == defsome.publicUntypedMethod()
		assert 'ho' == defsome.publicTypedMethod()
		defsome.combinedMethod()
		
		def summer = new Summer()
		assert 2 == summer.sumWithDefaults(1, 1)
		assert 3 == summer.sumWithDefaults(1, 1, 1)
		assert 2 == summer.sumWithList([1, 1])
		assert 3 == summer.sumWithList([1, 1, 1])
		assert 2 == summer.sumWithOptionals(1, 1)
		assert 3 == summer.sumWithOptionals(1, 1, 1)
		assert 2 == summer.sumNamed(a:1, b:1)
		assert 3 == summer.sumNamed(a:1, b:1, c:1)
		
		// safety reference operator
		def map = [a:[b:[c:1]]]
		assert map.a.b.c == 1
		
		if (map && map.a && map.a.x) {
			assert map.a.x.c == null
		}
		try {
			assert map.a.x.c == null
		} catch (NullPointerException e) {
			println 'NPE'
		}
		assert map?.a?.c == null
		
		// constructor 
		def first = new VendorWithCtor('A', 'B')
		def second = ['A', 'B'] as VendorWithCtor
		VendorWithCtor thrid = ['A', 'B']
		
		// call constructor by name argument
		new Vendor()
		new Vendor(name:'A')
		new Vendor(product:'B')
		new Vendor(name:'A', product:'B')
		
		def vendor = new Vendor(name:'A')
		assert 'A' == vendor.name
		
		// use groovy script
		def script = new GroovyScript() 
		script.run()
		
		// multimethod
		Object multi1 = 1
		Object multi2 = 'foo'
		assert vendor.oracle(multi1)
		assert vendor.oracle(multi2)
		
		Object same = new Equalizer()
		Object other = new Object()
		assert new Equalizer().equals(same)
		assert !new Equalizer().equals(other)
		
		// java bean
		def mybean  = new MyBean()
		mybean.myprop = 1
		assert mybean.myprop == '1'
		assert mybean.getMyprop() == '1'
		mybean = new MyBean(typed:'typed')
		assert mybean.typed == 'typed'
		
		mybean = new MyBean(firstname:"A")
		mybean.lastname = "B"
		println mybean.firstname 
		println mybean.@firstname
		
		def bean = new DoubleBean(value:100)
		assert 200 == bean.value
		assert 100 == bean.@value
		
		def someClass2 = new SomeClass2()
		store = []
		someClass2.properties.each { property ->
			store += property.key
			store += property.value
		}
		assert store.contains('someProperty')
		assert !store.contains('someField')
		assert !store.contains('somePrivateField')
		assert store.contains('class')
		assert !store.contains('metaClass')
		assert someClass2.properties.size() == 2
		
		// use gpath
		println this
		println this.class.methods.name.grep(~/get.*/).sort()
	}
}
class SomeClass2 {
	def someProperty
	public someField
	private somePrivateField
}
class DoubleBean {
	private value
	void setValue(value) {
		this.value = value
	}
	def getValue() {
		value * 2
	}
}
class MyBean implements Serializable {
	String myprop
	def untyped
	String typed
	def item1, item2
	def assigned = 'default value'
	private firstname, lastname
	String getFirstname() {
		return "$firstname $lastname"
	}
}
class Equalizer {
	boolean equals(Equalizer e) {
		return true
	}
}
class GroovyScript extends Script {
	@Override
	public Object run() {
		println "script"
		return null
	}
}
class Vendor {
	String name, product
	def oracle(Object o) {
		return 'object'
	}
	def oracle(String o) {
		return 'string'
	}
}
class VendorWithCtor {
	String name, product
	VendorWithCtor(name, product) {
		this.name = name
		this.product = product
	}
}
class Summer {
	def sumWithDefaults(a, b, c = 0) {
		return a + b + c
	}
	def sumWithList(List args) {
		return args.inject(0) {sum, i -> sum += i}
	}
	def sumWithOptionals(a, b, Object[] optionals) {
		return a + b + sumWithList(optionals.toList())
	}
	def sumNamed(Map args) {
		['a', 'b', 'c'].each {args.get(it, 0) }
		return args.a + args.b + args.c
	}
}
class Counter {
	public count = 0
}
class SomeClass {
	public fieldWidthModifier
	String typedField
	def untypedField
	protected field1, field2, field3
	private assignedField = new Date()
	static classField
	public static final String CONSTA = 'a'
	def someMethod() {
		def localUntypedMemberVar = 1
		int localTypedMethodVar = 1
		def localVarWithoutAssignment
	}
	void publicVoidMethod(){}
	def publicUntypedMethod() {
		return 'hi'
	}
	String publicTypedMethod() {
		return 'ho'
	}
	protected static final void combinedMethod() {}
}
