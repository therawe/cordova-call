package com.therawe.cordovacall;

import android.content.Context;

public class ResourceUtilities {

    private Context context;

    public ResourceUtilities(Context context) {
        this.context = context;
    }

    public int getId(String key) {
        return getResourceId(context, "id", key);
    }

    public int getString(String key) {
        return getResourceId(context, "string", key);
    }

    public int getDrawable(String key) {
        return getResourceId(context, "drawable", key);
    }

    public int getLayout(String key) {
        return getResourceId(context, "layout", key);
    }

    public static int getResourceId(Context context, String group, String key) {
        return context.getResources().getIdentifier(key, group, context.getPackageName());
    }
}
