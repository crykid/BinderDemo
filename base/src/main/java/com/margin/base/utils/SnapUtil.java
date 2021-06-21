package com.margin.base.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created at: 2021/6/9 at 23:58
 * Created by: blank
 * Description:
 */
public class SnapUtil {
    private static Map<String, SnapEntrys> mSnaps = new HashMap<>();
    private static final L.TAG TAG = new L.TAG("SnapUtil");

    public static void findMethod(Activity target, ListView listView) {
        if (target == null || listView == null) {
            L.e(TAG, "snapSnaps: target or listview is null");
            return;
        }
        Method[] methods = target.getClass().getDeclaredMethods();
        L.d(TAG, "findMethod: methods " + methods.length);
        SnapEntrys entries = null;
        for (Method method : methods) {
            Snap annotation;
            if ((annotation = method.getAnnotation(Snap.class)) != null) {
                String currentView;
                if ((entries = mSnaps.get(currentView = target.getClass().getSimpleName())) == null) {
                    mSnaps.put(currentView, entries = new SnapEntrys());
                }
                String name;
                entries.add(TextUtils.isEmpty(name = annotation.name()) ? method.getName() : name
                        , method);
            }
        }
        if (entries == null || entries.isEmpty()) {
            L.e(TAG, "findMethod: entries is empty");
            return;
        }
        snapSnaps(target, listView, entries);
    }

    private static void snapSnaps(Activity activity, ListView listView, SnapEntrys entries) {
        if (listView == null) {
            L.e(TAG, "snapSnaps: listview is null");
            return;
        }

        listView.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, entries.mNames));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Method method = entries.mMethods.get(position);

                L.d(TAG, "- snapSnaped : " + entries.mNames.get(position));
                invoke(method);
            }

            private void invoke(Method method) {
                method.setAccessible(true);
                try {
                    method.invoke(activity);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void clear(Activity activity) {
        mSnaps.remove(activity.getClass().getSimpleName());
    }

    static class SnapEntrys {
        public List<String> mNames;
        public List<Method> mMethods;

        public SnapEntrys() {
            mNames = new ArrayList<>();
            mMethods = new ArrayList<>();
        }

        public void add(String name, Method method) {
            if (mNames.contains(name)) {
                L.d(TAG, "add: 已经包含了该方法 ：" + name);
                return;
            }
            mNames.add(name);
            mMethods.add(method);
        }

        public boolean isEmpty() {
            return mNames.isEmpty() || mMethods.isEmpty();
        }
    }


}
