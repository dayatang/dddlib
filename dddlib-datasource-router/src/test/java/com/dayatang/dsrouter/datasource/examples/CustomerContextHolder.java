package com.dayatang.dsrouter.datasource.examples;

import org.springframework.util.Assert;

public class CustomerContextHolder {
	private static final ThreadLocal<CustomerType> contextHolder = new ThreadLocal<CustomerType>();

	public static void setCustomerType(CustomerType customerType) {
		Assert.notNull(customerType, "customerType cannot be null");
		contextHolder.set(customerType);
	}

	public static CustomerType getCustomerType() {
		return (CustomerType) contextHolder.get();
	}

	public static void clearCustomerType() {
		contextHolder.remove();
	}
}
