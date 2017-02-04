package com.axel.recipebook;

import java.io.Serializable;

/**
 * A simple class describing an ingredient which will be contained
 * in a recipe object. Simply has two fields, one with the name of
 * the ingredient and one with the amount of it.
 *
 * Implements Serializable with no extra trouble since it's two fields
 * are strings which are in turn Serializable.
 *
 * @author Axel Lindeberg
 */
public class Ingredient implements Serializable{
    private final String name;
    private final String amount;

    public Ingredient(String name, String amount) {
        this.name = name;
        this.amount = amount;
    }

    /**
     * Returns the name of the ingredient
     *
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the amount of the ingredient
     *
     * @return Amount
     */
    public String getAmount() {
        return amount;
    }
}
