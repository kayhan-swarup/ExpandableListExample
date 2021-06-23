package bd.com.doodletest.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bd.com.doodletest.L.L;
import bd.com.doodletest.R;
import bd.com.doodletest.models.Category;
import bd.com.doodletest.models.CategoryResponse;
import bd.com.doodletest.models.SubCategory;
import co.lujun.androidtagview.TagContainerLayout;

public class CustomListAdapter extends BaseExpandableListAdapter {

    CategoryResponse categoryResponse;
    TagContainerLayout containerLayout;
    List<String> titles = new ArrayList<>();
    HashMap<String, List<SubCategory>> sub =new HashMap<>();

    List<Category> categories = new ArrayList<>();

    SelectListener listener;


    public TagContainerLayout getContainerLayout() {
        return containerLayout;
    }

    public void setContainerLayout(TagContainerLayout containerLayout) {
        this.containerLayout = containerLayout;
    }

    public SelectListener getListener() {
        return listener;
    }

    public void setListener(SelectListener listener) {
        this.listener = listener;
    }

    public CustomListAdapter(CategoryResponse categoryResponse) {
        this.categoryResponse = categoryResponse;
        this.categories = categoryResponse.getCategories();
        EventBus.getDefault().register(this);
//        for(int i=0;i<categoryResponse.getCategories().size();i++){
//            String title = categories.get(i).getCategory_name();
//            titles.add(categories.get(i).getCategory_name());
//            List<SubCategory> subs = new ArrayList<>();
//            for(int j=0;j<categories.get(i).getSubcatg().size();j++){
//                subs.add(categories.get(i).getSubcatg().get(j));
//            }
//            sub.put(title,subs);
//        }
    }

    @Override
    public int getGroupCount() {
        return categories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return categories.get(groupPosition).getSubcatg().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return categories.get(groupPosition).getSubcatg().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Subscribe
    public void incomingSubCategory(SubCategory subCategory){
        L.i("Sub category found");
        if(subCategory.isSelected()){
            boolean notAdded = true;
            String str = subCategory.getSub_category_name();
            for(String s : containerLayout.getTags()){
                if(str.equals(s)){
                    notAdded =  false;
                    break;
                }
            }
            if(notAdded)
            containerLayout.addTag(subCategory.getSub_category_name());
        }else{
            String str = subCategory.getSub_category_name();
            int i=0;
            for(String s:containerLayout.getTags()){

                if(str.equals(s)){
                    containerLayout.removeTag(i);
                    break;
                }
                i++;
            }
        }

    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group,null);

        }
        TextView text = convertView.findViewById(R.id.groupTitle);
        text.setText(categories.get(groupPosition).getCategory_name());
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.expand(groupPosition);
            }
        });
        CheckBox checkBox= convertView.findViewById(R.id.groupCheck);
        TextView des= convertView.findViewById(R.id.deselector);
        des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(SubCategory subCategory:categories.get(groupPosition).getSubcatg()){
                    subCategory.setSelected(false);
                }
                checkBox.setChecked(false);
                notifyDataSetChanged();
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("Doodle","Checked detected");
                categories.get(groupPosition).setSelected(isChecked);
                if(isChecked){
                    des.setVisibility(View.VISIBLE);
                }else{
                    des.setVisibility(View.GONE);
                }
                notifyDataSetChanged();
            }
        });
        if(categories.get(groupPosition).isSelected()){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item,null);

        }
        TextView item = convertView.findViewById(R.id.itemTitle);
        item.setText(categories.get(groupPosition).getSubcatg().get(childPosition).getSub_category_name());
        CheckBox checkBox = convertView.findViewById(R.id.itemCheck);
//        if(categories.get(groupPosition).isSelected()){
//            checkBox.setChecked(true);
//        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked&&categories.get(groupPosition).isSelected()){
                    categories.get(groupPosition).setSelected(false);
                    notifyDataSetChanged();
                }
                categories.get(groupPosition).getSubcatg().get(childPosition).setSelected(isChecked);
                if(isChecked){
                    //containerLayout.addTag(categories.get(groupPosition).getSubcatg().get(childPosition).getSub_category_name());
                }else{
                    int i=0;
                    for(String s:containerLayout.getTags()){
                        String str = categories.get(groupPosition).getSubcatg().get(childPosition).getSub_category_name();
                        if(str.equals(s)){
                            containerLayout.removeTag(i);
                            break;
                        }
                        i++;
                    }
                }
            }
        });
        checkBox.setChecked(categories.get(groupPosition).getSubcatg().get(childPosition).isSelected());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
