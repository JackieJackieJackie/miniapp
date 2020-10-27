package io.dcloud.uniplugin;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import java.util.HashMap;
import java.util.Map;


public class TestModule extends WXModule {

    String TAG = "TestModule";
    public static int REQUEST_CODE = 1000;

    //run ui thread
    @JSMethod(uiThread = true)
    public void testAsyncFunc(JSONObject options, JSCallback callback) {
        Log.e(TAG, "testAsyncFunc--"+options);
        if(callback != null) {
            JSONObject data = new JSONObject();
            String age = options.getString("age");
            data.put("code", "success=age:"+age);
            callback.invoke(data);
            //callback.invokeAndKeepAlive(data);
        }
    }

    //run JS thread
    @JSMethod (uiThread = false)
    public JSONObject testSyncFunc(JSONObject options, JSCallback callback){
        JSONObject data = new JSONObject();
        String age = options.getString("age");
        data.put("code", "success=age:"+age);
        return data;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && data.hasExtra("respond")) {
            Log.e("TestModule", "原生页面返回----"+data.getStringExtra("respond"));
            Map<String,Object> testEvent = new HashMap<>();
            testEvent.put("msg",data.getStringExtra("respond"));

            mWXSDKInstance.fireGlobalEventCallback("TestEvent",testEvent);

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @JSMethod (uiThread = true)
    public void gotoNativePage(){
        if(mWXSDKInstance != null && mWXSDKInstance.getContext() instanceof Activity) {
            Intent intent = new Intent(mWXSDKInstance.getContext(), NativePageActivity.class);
            ((Activity)mWXSDKInstance.getContext()).startActivityForResult(intent, REQUEST_CODE);
        }
    }
}
