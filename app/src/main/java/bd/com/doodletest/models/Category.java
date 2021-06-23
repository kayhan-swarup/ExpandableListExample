package bd.com.doodletest.models;

import java.util.ArrayList;

public class Category {



    String category_id;
    String category_name;

    boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if(selected){
            for (SubCategory subCategory : subcatg) {
                subCategory.setSelected(selected);
            }
        }
    }

    ArrayList<SubCategory> subcatg =new ArrayList<>();

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public ArrayList<SubCategory> getSubcatg() {
        return subcatg;
    }

    public void setSubcatg(ArrayList<SubCategory> subcatg) {
        this.subcatg = subcatg;
    }



    @Override
    public String toString() {
        return "Category{" +
                "category_id='" + category_id + '\'' +
                ", category_name='" + category_name + '\'' +
                ", subcatg=" + subcatg +
                '}';
    }
}
