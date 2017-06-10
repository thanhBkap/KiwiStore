package com.store.kiwi.kiwistore;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.makeramen.roundedimageview.RoundedImageView;
import com.store.kiwi.kiwistore.adapter.DanhSachAnhUngDungAdapter;
import com.store.kiwi.kiwistore.adapter.TheLoaiAdapter;
import com.store.kiwi.kiwistore.adapter.UngDungAdapter;
import com.store.kiwi.kiwistore.database.DatabaseHelper;
import com.store.kiwi.kiwistore.model.TheLoai;
import com.store.kiwi.kiwistore.model.ThoiTiet;
import com.store.kiwi.kiwistore.model.UngDung;
import com.store.kiwi.kiwistore.xuly.DuLieu;
import com.store.kiwi.kiwistore.xuly.LunarCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public final static String APIKEY = "1fd660e2a27afad8b71405f654997a62";
    private List<TheLoai> mListTheLoai;
    private RecyclerView mRecyclerViewTheLoai, mRecyclerViewUngDung, mRecyclerViewUngDungLienQuan, mRecyclerViewAnhUngDung;
    private TheLoaiAdapter mTheLoaiAdapter;
    private DanhSachAnhUngDungAdapter mDanhSachAnhAdapter;
    private List<UngDung> mListUngDung;
    private List<String> mListAnh;
    private UngDungAdapter mUngDungAdapter;
    private View mUngDungFragment, mUngDungChiTietFragment;
    private Map<String, String> today;
    private TextView mNgayDuongTxt, mNgayAmTxt, mTxtSearch, mTxtTinh, mTxtNhietDo, mTxtTenUngDung, mTxtDacTa, mTxtLuotCai, mTxtCaiDatUngDung;
    private RelativeLayout mLayoutCauHinh, mLayoutHeader, mLayoutLogo, mLayoutSearch, mLayoutTheLoai, mLayoutLienQuan, mLayoutCaiDat, mLayouCaiDatUngDung;
    private ImageView mButtonSearch;
    ImageView mAnhIcon;
    private RoundedImageView mAnhQuangCao;
    private int height;
    private int width;
    private String destination;
    private DatabaseHelper mDatabaseHelper;
    private ProgressDialog dialog;
    List<View> listMap;
    int didindex = 0, main = 3, indexchoose;
    boolean chooseUngDung = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
        addListMap();
    }

    private void addControls() {
        SharedPreferences sharedPreferences = getSharedPreferences("thoitiet", MODE_PRIVATE);
        String idThoiTiet = sharedPreferences.getString("idthoitiet", "24");
        mDatabaseHelper = new DatabaseHelper(this);
        mDatabaseHelper.checkDatabase(this);

        haveStoragePermission();
        mListTheLoai = new ArrayList<>();
        mListTheLoai = mDatabaseHelper.getListTheLoai();

        mUngDungFragment = findViewById(R.id.list_ung_dung_fragment);
        mUngDungChiTietFragment = findViewById(R.id.chi_tiet_fragment);
        mNgayDuongTxt = (TextView) findViewById(R.id.txt_ngay_duong);
        mNgayAmTxt = (TextView) findViewById(R.id.txt_ngay_am);
        mTxtSearch = (TextView) findViewById(R.id.txt_search);
        mTxtTinh = (TextView) findViewById(R.id.txt_tinh);
        mTxtNhietDo = (TextView) findViewById(R.id.txt_nhiet_do);
        mTxtTenUngDung = (TextView) mUngDungChiTietFragment.findViewById(R.id.txt_ten);
        mTxtDacTa = (TextView) mUngDungChiTietFragment.findViewById(R.id.txt_dac_ta);
        mTxtLuotCai = (TextView) mUngDungChiTietFragment.findViewById(R.id.txt_luot_cai);
        mTxtCaiDatUngDung = (TextView) mUngDungChiTietFragment.findViewById(R.id.txt_cai_dat_ung_dung);

        mLayoutHeader = (RelativeLayout) findViewById(R.id.layout_header);
        mLayoutLogo = (RelativeLayout) findViewById(R.id.layout_logo);
        mLayoutSearch = (RelativeLayout) findViewById(R.id.layout_search);
        mLayoutTheLoai = (RelativeLayout) findViewById(R.id.layout_the_loai);
        mLayouCaiDatUngDung = (RelativeLayout) mUngDungChiTietFragment.findViewById(R.id.layout_cai_dat_ung_dung);

        mButtonSearch = (ImageView) findViewById(R.id.btn_seach);
        mAnhIcon = (ImageView) mUngDungChiTietFragment.findViewById(R.id.anh_icon);
        mAnhQuangCao = (RoundedImageView) mUngDungChiTietFragment.findViewById(R.id.anh_quang_cao);
        mLayoutLienQuan = (RelativeLayout) mUngDungChiTietFragment.findViewById(R.id.layout_lien_quan);
        //mLayoutCaiDat = (RelativeLayout) mUngDungChiTietFragment.findViewById(R.id.layout_cai_dat_ung_dung);
        mLayoutCauHinh = (RelativeLayout) findViewById(R.id.layout_cau_hinh);
        mRecyclerViewTheLoai = (RecyclerView) findViewById(R.id.list_the_loai);
        mRecyclerViewUngDung = (RecyclerView) mUngDungFragment.findViewById(R.id.list_ung_dung);
        mRecyclerViewAnhUngDung = (RecyclerView) mUngDungChiTietFragment.findViewById(R.id.list_anh_ung_dung);
        mRecyclerViewUngDungLienQuan = (RecyclerView) mUngDungChiTietFragment.findViewById(R.id.list_ung_dung_lien_quan);

        setWidthAnhHeight();

        mListUngDung = new ArrayList<>();
        mListAnh = new ArrayList<>();

        mListUngDung = mDatabaseHelper.getListUngDung(mListTheLoai.get(0));
        // load quảng cáo
        Glide.with(this).load(DuLieu.URL_IMAGE + "/" + mDatabaseHelper.getLinkQuangCao()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable ad = new BitmapDrawable(resource);
                mAnhQuangCao.setBackground(ad);
            }
        });

        mRecyclerViewAnhUngDung.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewAnhUngDung.setHasFixedSize(true);
        mDanhSachAnhAdapter = new DanhSachAnhUngDungAdapter(this, mListAnh);
        mRecyclerViewAnhUngDung.setAdapter(mDanhSachAnhAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerViewUngDung.setLayoutManager(gridLayoutManager);
        mRecyclerViewUngDung.setHasFixedSize(true);
        mUngDungAdapter = new UngDungAdapter(this, mListUngDung, mUngDungFragment,
                mUngDungChiTietFragment, mListAnh, mDanhSachAnhAdapter, mTxtTenUngDung,
                mTxtDacTa, mTxtLuotCai, mAnhIcon, mLayouCaiDatUngDung, mTxtCaiDatUngDung);
        mRecyclerViewUngDung.setAdapter(mUngDungAdapter);

        mRecyclerViewUngDungLienQuan.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewUngDungLienQuan.setHasFixedSize(true);
        mRecyclerViewUngDungLienQuan.setAdapter(mUngDungAdapter);

        mRecyclerViewTheLoai.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewTheLoai.setHasFixedSize(true);
        mTheLoaiAdapter = new TheLoaiAdapter(this, mListTheLoai, mUngDungFragment, mUngDungChiTietFragment, mListUngDung, mUngDungAdapter);
        mRecyclerViewTheLoai.setAdapter(mTheLoaiAdapter);

        today = LunarCalendar.getTodayInfo();
        mNgayDuongTxt.setText("Thứ " + today.get("thu") + ", " + today.get("daySolar") + "/" + today.get("monthSolar") + "/" + today.get("yearSolar"));
        mNgayAmTxt.setText(today.get("dayLunar") + "/" + today.get("monthLunar") + " " + today.get("can") + " " + today.get("chi"));
        final String todayFormated = today.get("yearSolar") + "-" + today.get("monthSolar") + "-" + today.get("daySolar") + " "
                + today.get("hour") + ":" + today.get("minute") + ":" + today.get("second");

        mUngDungChiTietFragment.setVisibility(View.GONE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Đang tải");
        dialog.setMessage("Vui lòng đợi ứng dụng tải dữ liệu");

        String url2 = DuLieu.URL + "/first_request_store.php";
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray root = new JSONArray(response);
                    dialog.show();
                    for (int i = 0; i < root.length(); i++) {
                        JSONObject capnhat = root.getJSONObject(i);
                        String isCapNhat = capnhat.getString("is_cap_nhat");
                        if (isCapNhat.equals("0")) {
                            break;
                        } else {
                            String loaiCapNhat = capnhat.getString("loai");
                            switch (loaiCapNhat) {
                                case "quangcao":
                                    JSONArray rootQC = capnhat.getJSONArray("value");
                                    JSONObject quangCao = rootQC.getJSONObject(0);
                                    mDatabaseHelper.insertQuangCao(quangCao.getString("id"), quangCao.getString("noidung"), quangCao.getString("loaiquangcaoid"));
                                    Toast.makeText(getApplicationContext(), mDatabaseHelper.getMaxQuangCaoId() + " == max quang cao", Toast.LENGTH_SHORT).show();
                                    break;
                                case "ungdung":
                                    JSONArray rootApp = capnhat.getJSONArray("value");
                                    mDatabaseHelper.deleteListApp();
                                    for (int j = 0; j < rootApp.length(); j++) {
                                        JSONObject app = rootApp.getJSONObject(j);
                                        int install = 0;
                                        if (DuLieu.checkInstalledApplication(app.getString("ten"), MainActivity.this)) {
                                            install = 1;
                                        }
                                        mDatabaseHelper.insertApp(app.getString("id"), app.getString("ten"),
                                                install, DuLieu.URL_IMAGE + "/" + app.getString("icon")
                                                , app.getString("luotcai"), app.getString("version")
                                                , app.getString("des"), DuLieu.URL_FILE + "/" + app.getString("linkcai"));
                                    }
                                    mListUngDung.clear();
                                    mListUngDung.addAll(mDatabaseHelper.getListUngDung(mListTheLoai.get(0)));
                                    mUngDungAdapter.notifyDataSetChanged();
                                    //  Toast.makeText(getApplicationContext(), mDatabaseHelper.testInsertApp() + " == max app", Toast.LENGTH_SHORT).show();
                                    break;
                                case "luotcai":
                                    break;
                                case "anhchitiet":
                                    JSONArray rootAnhChiTiet = capnhat.getJSONArray("value");
                                    mDatabaseHelper.deleteAnhChiTiet();
                                    for (int j = 0; j < rootAnhChiTiet.length(); j++) {
                                        JSONObject app = rootAnhChiTiet.getJSONObject(j);
                                        mDatabaseHelper.insertAnhChiTiet(app.getString("id"), app.getString("ungdungid"), DuLieu.URL_IMAGE +
                                                "/" + app.getString("ten"));
                                    }
                                    mDatabaseHelper.getListAnhChiTietUngDung(new UngDung("2", "", false, "", new ArrayList<String>(), "", "", "", ""));
                                    /*Toast.makeText(getApplicationContext(),mDatabaseHelper.getListAnhChiTietUngDung(new UngDung("2","",false,"",
                                            new ArrayList<String>(),"","","","")).size()+"anh" , Toast.LENGTH_LONG).show();*/
                                    //Toast.makeText(getApplicationContext(), mDatabaseHelper.testAnhChiTiet() + " == max anh chi tiet ", Toast.LENGTH_LONG).show();
                                    break;
                                case "theloai_ungdung":
                                    JSONArray rootTheLoaiUngDung = capnhat.getJSONArray("value");
                                    mDatabaseHelper.deleteTheLoaiUngDung();
                                    for (int j = 0; j < rootTheLoaiUngDung.length(); j++) {
                                        JSONObject app = rootTheLoaiUngDung.getJSONObject(j);
                                        mDatabaseHelper.insertTheLoaiUngDung(app.getString("id"), app.getString("theloaiid"), app.getString("ungdungid"));
                                    }
                                    // Toast.makeText(getApplicationContext(), mDatabaseHelper.testTheLoaiUngDung() + " == max the loai ung dung", Toast.LENGTH_SHORT).show();
                                    break;
                                case "theloai":
                                    JSONArray rootTheLoai = capnhat.getJSONArray("value");
                                    mDatabaseHelper.deleteTheLoai();
                                    for (int j = 0; j < rootTheLoai.length(); j++) {
                                        JSONObject app = rootTheLoai.getJSONObject(j);
                                        mDatabaseHelper.insertTheLoai(app.getString("id"), app.getString("ten"), app.getString("soluong"), app.getString("icon"));
                                    }
                                    //  Toast.makeText(getApplicationContext(), mDatabaseHelper.testTheLoai() + " == max the loai ", Toast.LENGTH_SHORT).show();
                                    mListTheLoai.clear();
                                    mListTheLoai.addAll(mDatabaseHelper.getListTheLoai());
                                    mTheLoaiAdapter.notifyDataSetChanged();
                                    break;
                                case "capnhat":
                                    JSONArray rootCapNhat = capnhat.getJSONArray("value");
                                    mDatabaseHelper.deleteCapNhat();
                                    JSONObject app = rootCapNhat.getJSONObject(0);
                                    mDatabaseHelper.insertCapNhat(app.getString("id"), app.getString("id"));
                                    // Toast.makeText(getApplicationContext(), mDatabaseHelper.testCapNhat() + " == test cap nhat ", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    break;

                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> values = new HashMap<>();
                values.put("capnhatid", mDatabaseHelper.getIdCapNhat());
                return values;
            }
        };
        requestQueue.add(stringRequest2);

        ThoiTiet thoiTiet = mDatabaseHelper.getThongTinThoiTiet(idThoiTiet);
        String url = "http://api.openweathermap.org/data/2.5/forecast?id=" + thoiTiet.getMaThoiTiet() + "&APPID=" + APIKEY + "&&units=metric";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    JSONArray listThoiTiet = root.getJSONArray("list");
                    int currentPos = 0;
                    for (int i = 0; i < listThoiTiet.length(); i++) {
                        JSONObject thoiTiet = listThoiTiet.getJSONObject(i);
                        String time = thoiTiet.getString("dt_txt");
                        // Toast.makeText(getApplicationContext(), todayFormated + "==" + time, Toast.LENGTH_SHORT).show();

                        if (DuLieu.compareDate(todayFormated, time)) {
                            if (i == 0) {
                                currentPos = 0;
                            } else {
                                currentPos = i - 1;
                            }
                            thoiTiet = listThoiTiet.getJSONObject(currentPos);
                            String nhietDo = thoiTiet.getJSONObject("main").getString("temp");
                            String trangThai = thoiTiet.getJSONArray("weather").getJSONObject(0).getString("main");
                            mTxtNhietDo.setText(Math.round(Double.parseDouble(nhietDo)) + "");
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);

        mTxtTinh.setText(thoiTiet.getTen());

        // Toast.makeText(this, mDatabaseHelper.getMaxCapNhatId() + "", Toast.LENGTH_SHORT).show();
    }

    private void addEvents() {
        /*mLayoutCaiDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get url of app on server
                String url = "http://phonecase.890m.com/files/case.zip";
                String fileName = DuLieu.getFileZipName(url);
                //Delete update file if exists
                File file = new File(Environment.getExternalStorageDirectory() + "/KiwiStore/download/" + fileName);
                if (file.exists()) {
                    file.delete();

                } else {
                    file.mkdirs();
                    file.delete();
                }
                final Uri uri = Uri.fromFile(file);

                final String destination = file.getAbsolutePath();

                //set downloadmanager
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setDescription(getString(R.string.hello_blank_fragment));
                request.setTitle(getString(R.string.app_name));

                //set destination
                request.setDestinationUri(uri);

                // get download service and enqueue file
                final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                final long downloadId = manager.enqueue(request);

                //set BroadcastReceiver to install app when .apk is downloaded
                BroadcastReceiver onComplete = new BroadcastReceiver() {
                    public void onReceive(Context ctxt, Intent intent) {
                        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setTitle("Đang cài đặt");
                        progressDialog.setMessage("Ứng dụng đang được cài đặt ...");
                        progressDialog.setIndeterminate(true);
                        progressDialog.show();
                        String apkFileName = unzip(destination);
                        progressDialog.dismiss();
                        DuLieu.installApp(MainActivity.this, apkFileName);
                        unregisterReceiver(this);
                    }
                };

                //register receiver for when .apk download is compete
                registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            }
        });*/
        mLayoutCauHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    /*Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);*/
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                /*String packageName = DuLieu.getPackageName("settings", MainActivity.this);
                if (packageName.equals("")) {

                } else {
                    Toast.makeText(MainActivity.this, packageName, Toast.LENGTH_SHORT).show();
                    openAppByLabelName(packageName);
                }*/
            }
        });


        mLayoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


       /* mLayoutLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://phonecase.890m.com/files/case.zip";
                //  String fileName = "f1.zip";
                String fileName = DuLieu.getFileZipName(url);
                //Delete update file if exists
                File file = new File(Environment.getExternalStorageDirectory() + "/KiwiStore/download/" + fileName);
                if (file.exists()) {
                    file.delete();
                } else {
                    file.mkdirs();
                    file.delete();
                }
                final Uri uri = Uri.fromFile(file);

                //get url of app on server
                destination = file.getAbsolutePath();
                //set downloadmanager
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setDescription(MainActivity.this.getString(R.string.hello_blank_fragment));
                request.setTitle(MainActivity.this.getString(R.string.app_name));

                //set destination
                request.setDestinationUri(uri);

                // get download service and enqueue file
                final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                final long downloadId = manager.enqueue(request);

                //set BroadcastReceiver to install app when .apk is downloaded
                BroadcastReceiver onComplete = new BroadcastReceiver() {
                    public void onReceive(Context ctxt, Intent intent) {
                        Toast.makeText(getApplicationContext(), "download finished !!!", Toast.LENGTH_SHORT).show();
                        String apkFileName = unzip(destination);
                        DuLieu.installApp(MainActivity.this, apkFileName);
                        unregisterReceiver(this);
                        Toast.makeText(MainActivity.this, "Cài đặt thành công", Toast.LENGTH_SHORT).show();
                    }
                };

                //register receiver for when .apk download is compete
                registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            }
        });*/
    }


    /* public void launchApp(String packageName) {
         Intent intent = new Intent();
         intent.setPackage(packageName);

         PackageManager pm = getPackageManager();
         List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
         Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));

         if(resolveInfos.size() > 0) {
             ResolveInfo launchable = resolveInfos.get(0);
             ActivityInfo activity = launchable.activityInfo;
             ComponentName name=new ComponentName(activity.applicationInfo.packageName,
                     activity.name);
             Intent i=new Intent(Intent.ACTION_MAIN);

             i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                     Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
             i.setComponent(name);

             startActivity(i);
         }
     }*/
    /*public void launchApp(String packageName) {
        Intent intent = new Intent();
        intent.setPackage(packageName);
        intent.addCategory("android.intent.category.LEANBACK_LAUNCHER");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }*/
    private void openAppByLabelName(String packageName) {
        Intent openApp = null;
        openApp = getPackageManager().getLaunchIntentForPackage(packageName);
        openApp.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(openApp);
    }

    /*
            Intent launchIntent = null;

            try{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    launchIntent = getPackageManager().getLeanbackLaunchIntentForPackage(packageName);
                }

            } catch (NoSuchMethodError e){
            }

            if (launchIntent == null) launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);

            if (launchIntent != null)  {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(launchIntent);
            } else {
                // failure message
            }*/
    private void setWidthAnhHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        mLayoutHeader.getLayoutParams().height = height * 7 / 50;
        mLayoutLogo.getLayoutParams().width = width * 2 / 9;
        mTxtSearch.getLayoutParams().width = width * 3 / 9;
        mButtonSearch.getLayoutParams().width = width * 20 / 900;
        mButtonSearch.getLayoutParams().height = height * 20 / 500;
        mLayoutTheLoai.getLayoutParams().width = width * 180 / 900;
        mLayoutLienQuan.getLayoutParams().width = width * 230 / 900;
        mAnhQuangCao.getLayoutParams().height = height * 150 / 500;

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, 0, height * 20 / 500);
        mUngDungChiTietFragment.setLayoutParams(layoutParams);

    }

    public boolean haveStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error", "You have permission");
                return true;
            } else {

                Log.e("Permission error", "You have asked for permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //you dont need to worry about these stuff below api level 23
            Log.e("Permission error", "You already have the permission");
            return true;
        }
    }

    public void addListMap() {
        listMap = new ArrayList<>();
        listMap.add(mTxtSearch);
        listMap.add(mButtonSearch);
        listMap.add(mLayoutCauHinh);
        listMap.get(0).setBackgroundResource(R.drawable.border_pick);

        for (TheLoai t : mListTheLoai) {
            listMap.add(mLayoutCauHinh);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (didindex < main) {
                    listMap.get(didindex).setBackgroundResource(R.drawable.none);
                    didindex = main;
                    mRecyclerViewTheLoai.getChildAt(0).callOnClick();
                } else if( didindex >= main && didindex < main - 1 + mListTheLoai.size()) {
                    didindex++;
                    mRecyclerViewTheLoai.getChildAt(didindex - main).callOnClick();
                } else if ( didindex > main - 1 + mListTheLoai.size() && didindex <= main - 4 + mListTheLoai.size() + mListUngDung.size()) {
                    mRecyclerViewUngDung.getChildAt(didindex - main - mListTheLoai.size()).setBackgroundResource(R.drawable.none);
                    didindex = didindex + 3;
                    mRecyclerViewUngDung.getChildAt(didindex - main - mListTheLoai.size()).setBackgroundResource(R.drawable.border_pick);
                }
                break;

            case KeyEvent.KEYCODE_DPAD_UP:
                if (didindex > main && didindex < main + mListTheLoai.size()) {
                    didindex--;
                    mRecyclerViewTheLoai.getChildAt(didindex - main).callOnClick();
                } else if (didindex == main) {
                    didindex = 0;
                    listMap.get(didindex).setBackgroundResource(R.drawable.border_pick);
                } else if (didindex > main + mListTheLoai.size() + 2 && didindex <= main - 1 + mListTheLoai.size() + mListUngDung.size()) {
                    mRecyclerViewUngDung.getChildAt(didindex - main - mListTheLoai.size()).setBackgroundResource(R.drawable.none);
                    didindex = didindex - 3;
                    mRecyclerViewUngDung.getChildAt(didindex - main - mListTheLoai.size()).setBackgroundResource(R.drawable.border_pick);
                }
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (didindex > 0 && didindex <= main) {
                    if (didindex != main) {
                        listMap.get(didindex).setBackgroundResource(R.drawable.none);
                    }
                    didindex--;
                    listMap.get(didindex).setBackgroundResource(R.drawable.border_pick);
                    if (didindex == 1) {
                        listMap.get(didindex).setBackgroundResource(R.color.colorPrimaryDark);
                    }
                } else if (didindex > main + mListTheLoai.size() && didindex <= main - 1 + mListTheLoai.size() + mListUngDung.size()) {
                    mRecyclerViewUngDung.getChildAt(didindex - main - mListTheLoai.size()).setBackgroundResource(R.drawable.none);
                    didindex--;
                    mRecyclerViewUngDung.getChildAt(didindex - main - mListTheLoai.size()).setBackgroundResource(R.drawable.border_pick);
                } /*else if (didindex == main + mListTheLoai.size()) {
                    mRecyclerViewUngDung.getChildAt(0).setBackgroundResource(R.drawable.none);
                    didindex = indexchoose;
                }*/
                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (didindex < main - 1) {
                    listMap.get(didindex).setBackgroundResource(R.drawable.none);
                    didindex++;
                    listMap.get(didindex).setBackgroundResource(R.drawable.border_pick);
                    if (didindex == 1) {
                        listMap.get(didindex).setBackgroundResource(R.color.colorPrimaryDark);
                    }
                } else if (didindex >= main && didindex < main + mListTheLoai.size()) {
                    indexchoose = didindex;
                    didindex = main + mListTheLoai.size();
                    mRecyclerViewUngDung.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
                    chooseUngDung = true;
                } else {
                    if (chooseUngDung == true) {
                        if (didindex >= main + mListTheLoai.size() && didindex < main - 1 + mListTheLoai.size() + mListUngDung.size()) {
                            mRecyclerViewUngDung.getChildAt(didindex - main - mListTheLoai.size()).setBackgroundResource(R.drawable.none);
                            didindex++;
                            mRecyclerViewUngDung.getChildAt(didindex - main - mListTheLoai.size()).setBackgroundResource(R.drawable.border_pick);
                        }
                    }
                }
                break;

            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
                if (didindex < main) {
                    listMap.get(didindex).callOnClick();
                } else if (didindex >= main + mListTheLoai.size() && didindex < main - 1 + mListTheLoai.size() + mListUngDung.size()) {
                    if (chooseUngDung == true)
                    mRecyclerViewUngDung.getChildAt(didindex - main - mListTheLoai.size()).callOnClick();
                }
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}

