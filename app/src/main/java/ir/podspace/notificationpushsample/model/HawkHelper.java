package ir.podspace.notificationpushsample.model;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

public class HawkHelper {
    public static void setData(Context context, String name, Object data) {

        if (!Hawk.isBuilt()) {
            Hawk.init(context).build();
        }

        Hawk.put(name, data);
    }

    public static Object getData(Context context, String key) {
        if (!Hawk.isBuilt()) {
            Hawk.init(context).build();
        }

        return Hawk.get(key);
    }

    public static void clearData(Context context) {
        Hawk.init(context).build();
        Hawk.deleteAll();
    }

}
