package com.yureiapplications.materiamedica.model;

public class Categories {

    private int categories_id;
    private String categories_name, categories_description, categories_image_path;
    public Categories(String categories_name, String categories_description, String categories_image_path) {

        this.categories_name = categories_name;
        this.categories_description = categories_description;
        this.categories_image_path = categories_image_path;
    }

    public Categories() {
    }

    public String getCategories_name() {return categories_name;}

    public String getCategories_description() {return categories_description; }

    public String getCategories_image_path() {return categories_image_path;}

}

