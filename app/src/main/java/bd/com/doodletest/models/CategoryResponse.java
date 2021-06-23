package bd.com.doodletest.models;

import java.util.ArrayList;
import java.util.List;

public class CategoryResponse {
    List<Category> categories=new ArrayList<>();

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "CategoryResponse{" +
                "categories=" + categories +
                '}';
    }
}
