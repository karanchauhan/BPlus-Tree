package com.karan.ads.bplustree;

import java.util.ArrayList;
import java.util.List;

/* 
 * author : Karan Chauhan
 */

/**
 * The Class Key.
 */
public class Key {

	/** The key. */
	double key;

	/** The list of values for the key. Set only for external nodes*/
	List<String> values;

	/**
	 * Instantiates a new key and its value.
	 *
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public Key(double key, String value) {
		this.key = key;
		if (null == this.values) {
			values = new ArrayList<>();
		}
		this.values.add(value);
	}
	
	/**
	 * Instantiates a new key
	 *
	 * @param key
	 *            the key
	 */
	public Key(double key) {
		this.key = key;
		this.values = new ArrayList<>();
	}

	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public double getKey() {
		return key;
	}

	/**
	 * Sets the key.
	 *
	 * @param key
	 *            the new key
	 */
	public void setKey(double key) {
		this.key = key;
	}

	/**
	 * Gets the values.
	 *
	 * @return the values
	 */
	public List<String> getValues() {
		return values;
	}

	/**
	 * Sets the values.
	 *
	 * @param values
	 *            the new values
	 */
	public void setValues(List<String> values) {
		this.values = values;
	}

	public String toString() {
		return "Key [key=" + key + ", values=" + values + "]";
	}

}
