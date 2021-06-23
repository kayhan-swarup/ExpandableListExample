package bd.com.doodletest.services;

import android.accessibilityservice.AccessibilityGestureEvent;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Build;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import bd.com.doodletest.L.L;

import static android.view.accessibility.AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START;
import static android.view.accessibility.AccessibilityEvent.TYPE_TOUCH_INTERACTION_START;
import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED;
import static android.view.accessibility.AccessibilityEvent.TYPE_VIEW_SCROLLED;
import static android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
import static android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;

public class MyAccessibilityService extends AccessibilityService {

    TextToSpeech tts;
    @Override
    public void onCreate() {
        super.onCreate();
        L.i("Started");
        try {
            getServiceInfo().flags =AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
            tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    tts.setLanguage(Locale.US);
                }
            });
        } catch (Exception e) {
            L.i("Error: "+e);
        }
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        L.i("Service connected");
        AccessibilityServiceInfo info =new AccessibilityServiceInfo();
        info.eventTypes =
                AccessibilityEvent.TYPE_VIEW_FOCUSED | AccessibilityEvent.TYPE_TOUCH_INTERACTION_START |
                        AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START|TYPE_VIEW_SCROLLED|
                            AccessibilityEvent.TYPE_VIEW_CLICKED|TYPE_WINDOW_STATE_CHANGED|TYPE_WINDOW_CONTENT_CHANGED|
                                AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED
                            ;

        // If you only want this service to work with specific applications, set their
        // package names here. Otherwise, when the service is activated, it will listen
        // to events from all applications.
//        info.packageNames = new String[]
//                {"com.example.android.myFirstApp", "com.example.android.mySecondApp"};

        // Set the type of feedback your service will provide.
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;

        // Default services are invoked only if no package-specific ones are present
        // for the type of AccessibilityEvent generated. This service *is*
        // application-specific, so the flag isn't necessary. If this was a
        // general-purpose service, it would be worth considering setting the
        // DEFAULT flag.

        // info.flags = AccessibilityServiceInfo.DEFAULT;

        info.notificationTimeout = 100;

        this.setServiceInfo(info);
    }

    long i=0;
    String packageName = "NULL";
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        L.i("Reached: "+event.getEventType());
        String s = "Event Detected";
        L.i("Count: "+event.getPackageName()+" "+event.getEventType());
        if(event.getText().size()>0){
            L.i("Text: "+event.getText().get(0));
        }
        if (!packageName.contains(event.getPackageName()+"")) {
            String pcg = event.getPackageName()+"";
            if(pcg.contains("youtube")){
                packageName = event.getPackageName()+"";
                speak("Opening Youtube");


            }
            else if(pcg.contains("facebook.katana")){
                packageName = event.getPackageName()+"";
                speak("Opening Facebook");
            }
            else if(pcg.contains("facebook.orca")){
                packageName = event.getPackageName()+"";
                speak("Opening Facebook Messenger");
            }else if(pcg.contains("chrome")){
                packageName = event.getPackageName()+"";
                speak("Opening Chrome");
            }else if(pcg.contains("firefox")){
                packageName = event.getPackageName()+"";
                speak("Opening Firefox");
            }else if(pcg.contains("com.google.android.apps.maps")){
                packageName = event.getPackageName()+"";
                speak("Opening Google Maps");
            }

        }
        switch (event.getEventType()){
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                L.i("Detected Text SELECTION");
                break;
            case TYPE_VIEW_CLICKED:
                try {
                    s = "View Clicked";

                } catch (Exception e) {
                    L.i("ERR+ "+e);
                }
                break;
            case TYPE_VIEW_SCROLLED:

                L.i(i+" ms");
                s = "Scroll Detected";

//                Toast.makeText(getApplicationContext(),"Touch detected!",Toast.LENGTH_SHORT).show();
                break;
            case TYPE_TOUCH_EXPLORATION_GESTURE_START:
//                Toast.makeText(getApplicationContext(),"Swipe detected!",Toast.LENGTH_SHORT).show();
                s = "Touch started";
                break;
            case TYPE_WINDOW_CONTENT_CHANGED:
//                s = "Window content changed";
                try {
                    AccessibilityNodeInfo source = event.getSource();
                    if (source == null) {
                        return;
                    }

                    List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId("YOUR PACKAGE NAME:id/RESOURCE ID FROM WHERE YOU WANT DATA");
                    if (findAccessibilityNodeInfosByViewId.size() > 0) {
                        AccessibilityNodeInfo parent = (AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0);
                        // You can also traverse the list if required data is deep in view hierarchy.
                        String requiredText = parent.getText().toString()+"TEAST";
                        Log.i("E-Gen", requiredText);
                    }else{
                        L.i("No Text found");
                    }
                } catch (Exception e) {
                    L.i("Click Error: "+e);
                }
                break;
            case TYPE_WINDOW_STATE_CHANGED:
                s = "Window state changed";
                break;
        }
        L.i((System.currentTimeMillis()-i)+"");
        if((TYPE_VIEW_SCROLLED==event.getEventType()&&System.currentTimeMillis()-i>1500)||event.getEventType()!=TYPE_VIEW_SCROLLED)
//        speak(s);
        if(TYPE_VIEW_SCROLLED==event.getEventType()){
            i = System.currentTimeMillis();
        }


    }

    void speak(String s){
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.US);
                String str = null;
                try {



                } catch (Exception e) {
                    L.i(e.toString());
                }

                tts.speak(s,TextToSpeech.QUEUE_ADD,new HashMap<>());
            }
        });
    }

    @Override
    public boolean onGesture(@NonNull AccessibilityGestureEvent gestureEvent) {
        L.i("Gesture");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            int val = gestureEvent.getGestureId();
            try {
                Toast.makeText(getApplicationContext(),"Event: "+val,Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.e("E-Gen","Error: "+e);
            }
        }
        return super.onGesture(gestureEvent);
    }

    @Override
    public void onInterrupt() {

    }
}
