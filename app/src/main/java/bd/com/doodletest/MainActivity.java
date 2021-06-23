package bd.com.doodletest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import bd.com.doodletest.adapter.CustomListAdapter;
import bd.com.doodletest.adapter.Listener;
import bd.com.doodletest.adapter.SelectListener;
import bd.com.doodletest.models.CategoryResponse;
import bd.com.doodletest.networking.ApiClient;
import co.lujun.androidtagview.TagContainerLayout;

public class MainActivity extends AppCompatActivity implements SelectListener {

    String BASE_URL = "https://www.test.api.liker.com/get_categories";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list);
        containerLayout = findViewById(R.id.containerLayout);
        ApiClient.getInstance().getData(new Listener<CategoryResponse>() {
            @Override
            public void onResponse(CategoryResponse response) {
                onDataRetrieved(response);
            }
        });



    }



    ExpandableListView listView;
    String TAG = "Doodle";
    CustomListAdapter adapter;
    CategoryResponse response;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataRetrieved(CategoryResponse response){
        this.response = response;
        Log.i(TAG,"Found response");
        adapter = new CustomListAdapter(response);
        adapter.setContainerLayout(containerLayout);
        adapter.setListener(this);
        listView .setAdapter(adapter);
    }
    TagContainerLayout containerLayout;


    @Override
    public void expand(int position) {
        if(response.getCategories().size()>position){
            if(!listView.isGroupExpanded(position)){
                listView.expandGroup(position,true);
            }else{
                listView.collapseGroup(position);
            }
        }
    }
}