package com.java.inaction.ch10;

import java.util.Optional;

public class Ch10Main {

	public static void main(String[] args) {
		Optional<Car> optCar = Optional.empty();
		System.out.println(optCar);
		Car car = new Car();
		optCar = Optional.of(car);
		System.out.println(optCar);
		car = null;
		optCar = Optional.ofNullable(car);
		System.out.println(optCar);

		Insurance insurance = new Insurance();
		Optional<Insurance> optInsurance = Optional.ofNullable(insurance);
		Optional<String> name = optInsurance.map(Insurance::getName);
		System.out.println(name);

		// flatMap
		Person person = new Person();
		Optional<Person> optPerson = Optional.of(person);
		// Optional<String> name2 = optPerson.flatMap(Person::getCar)
		// .flatMap(Car::getInsurance).map(Insurance::getName);

		// default action
		// concat optional
		optInsurance = nullSafeFindCheapestInsurance(optPerson, optCar);
		System.out.println(optInsurance);
		// filter
		optInsurance.filter(i -> "".equals(i.getName())).ifPresent(
				x -> System.out.println("ok"));
		getCarInsuranceName(optPerson, 15);
		
		Optional<Integer> stringToInt = stringToInt("1");
		System.out.println(stringToInt);
		
		
	}
	public static Optional<Integer> stringToInt(String s) {
		try {
			return Optional.of(Integer.parseInt(s));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}

	public static String getCarInsuranceName(Optional<Person> person, int minAge) {
		return person.filter(p -> p.getAge() >= minAge).flatMap(Person::getCar)
				.flatMap(Car::getInsurance).map(Insurance::getName).orElse("x");
	}

	public static Insurance find(Person person, Car car) {
		return null;
	}

	public static Optional<Insurance> nullSafeFindCheapestInsurance(
			Optional<Person> person, Optional<Car> car) {
		// if (person.isPresent() && car.isPresent()) {
		// return Optional.of(find(person.get(), car.get()));
		// }
		person.flatMap(p -> car.map(c -> find(p, c)));
		return Optional.empty();
	}

}
