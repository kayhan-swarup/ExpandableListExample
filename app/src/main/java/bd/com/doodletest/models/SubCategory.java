package bd.com.doodletest.models;

import org.greenrobot.eventbus.EventBus;

public class SubCategory {

    String sub_category_id;
    String sub_category_name;

    boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        EventBus.getDefault().post(SubCategory.this);
    }

    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getSub_category_name() {
        return sub_category_name;
    }

    public void setSub_category_name(String sub_category_name) {
        this.sub_category_name = sub_category_name;
    }

    @Override
    public String toString() {
        return "SubCategory{" +
                "sub_category_id='" + sub_category_id + '\'' +
                ", sub_category_name='" + sub_category_name + '\'' +
                '}';
    }
}
