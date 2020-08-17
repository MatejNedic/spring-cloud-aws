package org.springframework.cloud.aws.messaging.core;

import java.util.Objects;

public class Car  {
	private String name = "Opel";
	private int price = 10000;

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final Car car = (Car) o;
		return price == car.price &&
			Objects.equals(name, car.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, price);
	}
}
