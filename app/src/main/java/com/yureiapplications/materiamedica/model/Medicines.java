package com.yureiapplications.materiamedica.model;

public class Medicines {

    private int medicines_id;
    private String medicines_categories, medicines_name, medicines_description, medicines_image_path, medicines_references;

    public Medicines(String medicines_name, String medicines_description,String medicines_image_path, String medicines_references) {

        //this.medicines_categories = medicines_categories;
        this.medicines_image_path = medicines_image_path;
        this.medicines_name = medicines_name;
        this.medicines_description = medicines_description;
        this.medicines_references = medicines_references;
    }

    public Medicines() {}

    public int getMedicines_id() {return medicines_id;}

    public String getMedicines_categories() {return medicines_categories;}

    public String getMedicines_name() {return medicines_name;}

    public String getMedicines_description(){return medicines_description;}

    public String getMedicines_references() {return medicines_references;}

    public String getMedicines_image() {return medicines_image_path;}

}
