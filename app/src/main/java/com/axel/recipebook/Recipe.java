package com.axel.recipebook;;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A recipe object which contains the name of the recipe as well as any
 * number of ingredients. Implements Serializable. Can be saved to a file as well
 * as read from a file.
 *
 * @author Axel Lindeberg
 */
public class Recipe implements Serializable, Comparable<Recipe> {
    private String name;
    private ArrayList<Ingredient> ingredients;

    // this number works. Dont ask me why
    static final long serialVersionUID = -5660726973683173084L;

    /**
     * Only for reading in from FireBase. Not meant to be used.
     */
    public Recipe() {
        this.name = "";
    }

    /**
     * Regular constructor. Takes in the name of the recipe.
     * Ingredients have to be added later.
     *
     * @param name name of the recipe
     */
    public Recipe(String name) {
        this.name = name;
        this.ingredients = new ArrayList<>();
    }

    /**
     * Creates a recipe object by reading it from a saved file.
     * Takes in the file name and the context. Calls the readFromFile() method.
     *
     * @param fileName name of the file to be read
     * @param ctx
     */
    public Recipe(File f) {
        this.readFromFile(f);
    }

    /**
     * Returns the name of the recipe
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns an ArrayList of the ingredients of the recipe
     *
     * @return ArrayList of ingredients
     */
    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Takes in an array list of ingredients and overwrites the recipe's current
     * array list of ingredients.
     *
     * @param ingredients - new array list of ingredients
     */
    public void setNewIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Adds an ingredient to the recipe.
     *
     * @param name name of ingredient
     * @param amount amount of the ingredient
     */
    public void addIngredient(String name, String amount) {
        ingredients.add( new Ingredient(name, amount) );
    }

    /**
     * Creates a file with the name of the recipe's name.
     * Outputs the object via ObjectOutputStream.
     * Saves to [current directory]/recipes/
     *
     * @param ctx
     */
    public void saveToFile(Context ctx) {
        if (name == null) {
            return;
        }
        try {
            String filePath = ctx.getFilesDir() + "/recipes/" + name;
            File recipe = new File(filePath);

            recipe.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(recipe));
            oos.writeObject(this);
            oos.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads in a recipe object from a saved file and sets this recipe
     * equal to the read in recipe. Reads from [current directory]/recipes/
     *
     * @param file - file containing recipe object
     */
    private void readFromFile(File file) {
        try {
            ObjectInputStream ois = new ObjectInputStream( new FileInputStream(file) );
            Recipe r = (Recipe) ois.readObject();
            ois.close();
            this.name = r.name;
            this.ingredients = r.ingredients;
        } catch ( ClassNotFoundException | IOException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Overrides the toString method. Simply returns the name of the recipe.
     *
     * @return
     */
    @Override public String toString() {
        return this.name;
    }

    /**
     * Implements the compareTo method, comparing on the name of the recipe.
     * Uses the String class' compare to method.
     *
     * @param recipe - recipe comparing against
     * @return
     */
    @Override
    public int compareTo(@NonNull Recipe recipe) {
        return this.name.compareTo(recipe.getName());
    }
}
