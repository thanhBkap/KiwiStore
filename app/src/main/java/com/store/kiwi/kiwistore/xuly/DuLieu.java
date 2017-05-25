package com.store.kiwi.kiwistore.xuly;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 5/22/2017.
 */

public class DuLieu {

    public static List<ApplicationInfo> getListInstalledApplication(Context context) {
        List<ApplicationInfo> applicationInfoList = new ArrayList<>();
        applicationInfoList = context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        return applicationInfoList;
    }

    public static boolean checkInstalledApplication(String label, Context context) {
        List<ApplicationInfo> applicationInfoList = new ArrayList<>();
        applicationInfoList = getListInstalledApplication(context);
        for (int i = 0; i < applicationInfoList.size(); i++) {
            String appLabel= (String) context.getPackageManager().getApplicationLabel(applicationInfoList.get(i));
            if (appLabel.equals(label)){
                return  true;
            }
        }
        return false;
    }

}
