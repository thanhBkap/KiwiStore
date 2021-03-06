package com.store.kiwi.kiwistore.xuly;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by admin on 5/22/2017.
 */

public class DuLieu {

    //  public static String URL = "http://phonecase.890m.com";
    public static String URL = "http://phone.websumo.vn";
    public static String URL_IMAGE = "http://phone.websumo.vn/images";
    public static String URL_FILE = "http://phone.websumo.vn/files";

    public static List<ApplicationInfo> getListInstalledApplication(Context context) {
        List<ApplicationInfo> applicationInfoList = new ArrayList<>();
        applicationInfoList = context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        return applicationInfoList;
    }

    public static boolean checkInstalledApplication(String label, Context context) {
        List<ApplicationInfo> applicationInfoList = new ArrayList<>();
        applicationInfoList = getListInstalledApplication(context);
        for (int i = 0; i < applicationInfoList.size(); i++) {
            String appLabel = (String) context.getPackageManager().getApplicationLabel(applicationInfoList.get(i));
            if (appLabel.trim().toLowerCase().replace(" ", "").equals(label.trim().toLowerCase().replace(" ", ""))) {
                return true;
            }
        }
        return false;
    }

    public static boolean capNhatVersion(String packageName, int versionCode, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            if (pi.versionCode < versionCode) {
                return true;
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String getPackageName(String label, Context context) {
        String packageName = "";
        List<ApplicationInfo> listApplicationInfo = context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        for (int i = 0; i < listApplicationInfo.size(); i++) {
            if (context.getPackageManager().getApplicationLabel(listApplicationInfo.get(i)).toString().trim().toLowerCase().replace(" ", "").equals(
                    label.trim().toLowerCase().replace(" ", ""))) {
                packageName = context.getPackageManager().getInstalledPackages(PackageManager.GET_META_DATA).get(i).packageName;
                //Toast.makeText(context, "Ok-" + packageName, Toast.LENGTH_LONG).show();
                break;
            }
        }
        return packageName;
    }

    public static String unzip(String filePath) {
        String apkFileName = "";
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            ZipInputStream zipStream = new ZipInputStream(inputStream);
            ZipEntry zEntry = null;
            while ((zEntry = zipStream.getNextEntry()) != null) {
                Log.d("Unzip", "Unzipping " + zEntry.getName() + " at "
                        + filePath);

                if (zEntry.isDirectory()) {
                    // cử lý trong trường hợp file nến chứa thư mục : hanldeDirectory(zEntry.getName());

                } else {
                    //unzip file ra thư mục con download trong kiwistore
                    FileOutputStream fout = new FileOutputStream(
                            Environment.getExternalStorageDirectory() + "/KiwiStore/download/" + zEntry.getName());
                    BufferedOutputStream bufout = new BufferedOutputStream(fout);
                    apkFileName = zEntry.getName();
                    byte[] buffer = new byte[1024];
                    int read = 0;
                    while ((read = zipStream.read(buffer)) != -1) {
                        bufout.write(buffer, 0, read);
                    }

                    zipStream.closeEntry();
                    bufout.close();
                    fout.close();
                }
            }
            zipStream.close();
            Log.d("Unzip", "Unzipping complete. path :  " + filePath);
        } catch (Exception e) {
            apkFileName = "";
            Log.d("Unzip", "Unzipping failed");
            e.printStackTrace();
        }
        return apkFileName;
    }

    public static void InstallAPK(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            try {
                String command;
                command = "pm install -r " + filename;
                Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", command});
                proc.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void installApp(Context context, String apkFileName) {
        String pathToDownloadFolder = "/KiwiStore/download/";
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        install.setDataAndType(
                Uri.fromFile(new File(Environment.getExternalStorageDirectory() + pathToDownloadFolder + apkFileName)),
                "application/vnd.android.package-archive");
        context.startActivity(install);
    }

    public static void uninstallApp(String packageName, Context context) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static String getFileZipName(String url) {
        return url.substring(url.lastIndexOf('/') + 1, url.length());
    }

    public static boolean compareDate(String checkDate, String currentDate) {

        String date1 = splitDateTime(checkDate, 1)[0];
        String time1 = splitDateTime(checkDate, 1)[1];
        String date2 = splitDateTime(currentDate, 1)[0];
        String time2 = splitDateTime(currentDate, 1)[1];


        if (Integer.parseInt(splitDateTime(date1, 2)[0]) < Integer.parseInt(splitDateTime(date2, 2)[0])) {
            return true;
        }
        if (Integer.parseInt(splitDateTime(date1, 2)[1]) < Integer.parseInt(splitDateTime(date2, 2)[1])) {
            return true;
        }
        if (Integer.parseInt(splitDateTime(date1, 2)[2]) < Integer.parseInt(splitDateTime(date2, 2)[2])) {
            return true;
        }
        if (Integer.parseInt(splitDateTime(time1, 3)[0]) < Integer.parseInt(splitDateTime(time2, 3)[0])) {
            return true;
        }
        if (Integer.parseInt(splitDateTime(time1, 3)[1]) < Integer.parseInt(splitDateTime(time2, 3)[1])) {
            return true;
        }
        if (Integer.parseInt(splitDateTime(time1, 3)[2]) <= Integer.parseInt(splitDateTime(time2, 3)[2])) {
            return true;
        }
        return false;
    }

    public static String[] splitDateTime(String dateTime, int type) {
        String[] kq;
        if (type == 1) {
            kq = dateTime.split(" ");
        } else if (type == 2) {
            kq = dateTime.split("-");
        } else {
            kq = dateTime.split(":");
        }
        return kq;
    }

    /*public void hanldeDirectory(String dir) {
        File f = new File(this.destination + dir);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }*/
    /*public static void unzip(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            *//* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            *//*
            }
        } finally {
            zis.close();
        }
    }*/
}
