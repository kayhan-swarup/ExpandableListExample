package bd.com.doodletest.networking;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import bd.com.doodletest.adapter.Listener;
import bd.com.doodletest.models.Category;
import bd.com.doodletest.models.CategoryResponse;

public class ApiClient {
    private static ApiClient apiClient = null;
    String TAG ="Doodle";
    String BASE_URL = "https://bdofficemanage.com/doodle/file.json";
    public AnalyticsListener analyticsListener = new AnalyticsListener() {
        @Override
        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
            Log.d(TAG, " bytesSent : " + bytesSent);
            Log.d(TAG, " bytesReceived : " + bytesReceived);
            Log.d(TAG, " isFromCache : " + isFromCache);
        }
    };



    public static ApiClient getInstance() {
        if (apiClient == null)
            apiClient = new ApiClient();
        return apiClient;
    }

    public void getData(Listener<CategoryResponse> responseListener){
        Log.i(TAG,"Loading");
        AndroidNetworking.get(BASE_URL)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(analyticsListener)
                .getAsObject(CategoryResponse.class, new ParsedRequestListener<CategoryResponse>() {
                    @Override
                    public void onResponse(CategoryResponse response) {
//                        Log.i(TAG,"Size: "+response.toString());
//                        EventBus.getDefault().post(response);
                        responseListener.onResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG,anError.toString());
                    }
                });
//                .getAsString(new StringRequestListener() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d(TAG,response);
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//
//                    }
//                });
    }


}
