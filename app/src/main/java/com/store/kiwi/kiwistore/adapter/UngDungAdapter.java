package com.store.kiwi.kiwistore.adapter;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.store.kiwi.kiwistore.R;
import com.store.kiwi.kiwistore.database.DatabaseHelper;
import com.store.kiwi.kiwistore.model.UngDung;
import com.store.kiwi.kiwistore.xuly.DuLieu;

import java.io.File;
import java.util.List;

import static com.store.kiwi.kiwistore.xuly.DuLieu.checkInstalledApplication;
import static com.store.kiwi.kiwistore.xuly.DuLieu.unzip;

/**
 * Created by admin on 5/25/2017.
 */

public class UngDungAdapter extends RecyclerView.Adapter<UngDungAdapter.ViewHolder> {
    private Context mContext;
    private List<UngDung> mListUngDung;
    private View mUngDungFragment, mUngDungChiTietFragment;
    private List<String> mListAnh;
    private DanhSachAnhUngDungAdapter mDanhSachAnhUngDungAdapter;
    private TextView mTxtTenUngDung, mTxtDacTa, mTxtLuotCai, mTxtCaiDatUngDung;
    private ImageView mAnhIcon;
    private TextView mTxtVersion;
    private RatingBar mRatingApp;
    private RelativeLayout mLayouCaiDatUngDung;
    private List<View> mListMap;
    private ProgressDialog progressDialog;
    private ImageView mImageChecked;

    public UngDungAdapter(Context mContext, List<UngDung> mListUngDung, View mUngDungFragment, View mUngDungChiTietFragment,
                          List<String> listAnh, DanhSachAnhUngDungAdapter danhSachAnhUngDungAdapter, TextView txtTenUngDung,
                          TextView txtDacTa, TextView txtLuotCai, ImageView anhIcon, RelativeLayout layouCaiDatUngDung, TextView txtCaiDatUngDung,
                          TextView txtVersion, RatingBar rating, List<View> listMap, ImageView imageChecked) {
        this.mContext = mContext;
        this.mListUngDung = mListUngDung;
        this.mUngDungFragment = mUngDungFragment;
        this.mUngDungChiTietFragment = mUngDungChiTietFragment;
        mListAnh = listAnh;
        mDanhSachAnhUngDungAdapter = danhSachAnhUngDungAdapter;
        mTxtTenUngDung = txtTenUngDung;
        mTxtDacTa = txtDacTa;
        mTxtLuotCai = txtLuotCai;
        mAnhIcon = anhIcon;
        mLayouCaiDatUngDung = layouCaiDatUngDung;
        mTxtCaiDatUngDung = txtCaiDatUngDung;
        mTxtVersion = txtVersion;
        mRatingApp = rating;
        mListMap = listMap;
        mImageChecked = imageChecked;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_don_vi_ung_dung, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UngDung ungDung = mListUngDung.get(position);
        if (ungDung.isInstalled()) {
            holder.mCaiDat.setText("Ứng dụng đã được cài đặt");
            holder.mCaiDat.setTextColor(mContext.getResources().getColor(R.color.light_green));
            holder.mChecked.setImageResource(R.mipmap.ic_checked);
            //mLayouCaiDatUngDung.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bo_vien_cai_dat));
        } else {
            holder.mCaiDat.setText("Chưa cài ứng dụng");
            holder.mChecked.setImageResource(R.mipmap.ic_nonchecked);
            holder.mCaiDat.setTextColor(mContext.getResources().getColor(R.color.orange));
            //mLayouCaiDatUngDung.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bo_vien_go_cai_dat));
        }
        holder.mVersionName.setText("Ver: " + ungDung.getVersion());
        holder.mTen.setText(ungDung.getName());
        Glide.with(mContext).load(ungDung.getIcon()).into(holder.mIcon);
    }

    @Override
    public int getItemCount() {
        return mListUngDung.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIcon, mChecked;
        TextView mTen, mCaiDat, mVersionName;
        CardView mCardViewUngDung;

        public ViewHolder(final View itemView) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(R.id.anh_ung_dung);
            mChecked = (ImageView) itemView.findViewById(R.id.img_checked);
            mTen = (TextView) itemView.findViewById(R.id.txt_ten);
            mCaiDat = (TextView) itemView.findViewById(R.id.txt_cai_dat);
            mVersionName = (TextView) itemView.findViewById(R.id.txt_version);
            mCardViewUngDung = (CardView) itemView.findViewById(R.id.card_view_ung_dung);
            final DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final UngDung checkedUngDung = mListUngDung.get(getPosition());
                    /*if (v.getId() == R.id.layout_go_cai_dat) {
                        String url = checkedUngDung.getLinkCai();
                        String fileName = DuLieu.getFileZipName(url);
                        //Delete update file if exists
                        File file = new File(Environment.getExternalStorageDirectory() + "/KiwiStore/download/" + fileName);
                        if (file.exists()) {
                            file.delete();
                        }
                        //  DuLieu.uninstallApp("com.att.attcase", mContext);
                        DuLieu.uninstallApp(DuLieu.getPackageName(checkedUngDung.getName(), mContext), mContext);
                        List<UngDung> ungDungList=databaseHelper.getLissAppName();
                        for (int i=0;i<ungDungList.size();i++){
                            if (checkInstalledApplication(ungDungList.get(i).getName(),mContext)){
                                databaseHelper.updateApp(1,ungDungList.get(i).getId());
                            }else{
                                databaseHelper.updateApp(0,ungDungList.get(i).getId());
                            }
                        }
                        //DuLieu.uninstallApp(DuLieu.getPackageName(mListUngDung.get(getPosition()).getName(),mContext), mContext);

                    } else*/
                        mUngDungFragment.setVisibility(View.GONE);
                        mUngDungChiTietFragment.setVisibility(View.VISIBLE);
                        /*for (int i = (mListMap.size() - getItemCount()); i < mListMap.size(); i++) {
                            mListMap.remove(i);
                        }*/
                        ImageView adImage = (ImageView) mUngDungChiTietFragment.findViewById(R.id.anh_quang_cao);
                        //mListMap.add(adImage);
                        //  mListMap.add(mUngDungChiTietFragment.findViewById())
                        mListAnh.clear();
                        mListAnh.addAll(databaseHelper.getListAnhChiTietUngDung(checkedUngDung));
                        mDanhSachAnhUngDungAdapter.notifyDataSetChanged();

                        Glide.with(mContext).load(checkedUngDung.getIcon()).into(mAnhIcon);
                        mTxtTenUngDung.setText(checkedUngDung.getName());
                        mRatingApp.setRating(Float.parseFloat(checkedUngDung.getRating()));
                        mTxtVersion.setText(" Phiên bản: " + checkedUngDung.getVersion());
                        mTxtDacTa.setText(checkedUngDung.getDes());
                        mTxtLuotCai.setText("Đã có " + checkedUngDung.getLuotCai() + " lượt cài");
                        if (checkedUngDung.isInstalled() && checkedUngDung.getUpdate().equals("0")) {
                            mTxtCaiDatUngDung.setText("Đã cài đặt");
                            mLayouCaiDatUngDung.setBackgroundDrawable(
                                    mContext.getResources().getDrawable(R.drawable.bo_vien_cai_dat));
                            mLayouCaiDatUngDung.setClickable(false);
                            mImageChecked.setBackgroundResource(R.mipmap.ic_checked);
                        } else {
                            mLayouCaiDatUngDung.setClickable(true);
                            if (checkedUngDung.isInstalled()) {
                                mTxtCaiDatUngDung.setText("Cập nhật");
                                mImageChecked.setBackgroundResource(R.mipmap.ic_checked);
                                mLayouCaiDatUngDung.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bo_vien_cai_dat));
                            } else {
                                mTxtCaiDatUngDung.setText("Cài ứng dụng");
                                mImageChecked.setBackgroundResource(R.mipmap.ic_nonchecked);
                                mLayouCaiDatUngDung.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bo_vien_cai_dat));
                            }
                            //mLayouCaiDatUngDung.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bo_vien_cai_dat));
                            mLayouCaiDatUngDung.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //get url of app on server
                                    // String url = "http://phonecase.890m.com/files/case.zip";
                                    if (DuLieu.checkInstalledApplication(checkedUngDung.getName(), mContext)) {

                                    }
                                    mLayouCaiDatUngDung.setClickable(false);
                                    String url = checkedUngDung.getLinkCai();
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
                                    progressDialog = new ProgressDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                                    progressDialog.setTitle(checkedUngDung.getName());
                                    progressDialog.setMessage("Ứng dụng đang được tải về ...");
                                    progressDialog.setIndeterminate(false);
                                    progressDialog.setMax(100);
                                    progressDialog.setProgress(10);
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                    progressDialog.show();

                                    //set downloadmanager
                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                                    request.setDescription(mContext.getString(R.string.hello_blank_fragment));
                                    request.setTitle(mContext.getString(R.string.app_name));

                                    //set destination
                                    request.setDestinationUri(uri);
                                    request.setVisibleInDownloadsUi(true);
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                                    request.setShowRunningNotification(true);
                                    // get download service and enqueue file
                                    final DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                                    final long downloadId = manager.enqueue(request);
                                    final Handler handler = new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            super.handleMessage(msg);
                                        }
                                    };
                                    final DownloadManager.Query q = new DownloadManager.Query();
                                    q.setFilterById(downloadId);
                                    final Thread thread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Cursor cursor = manager.query(q);
                                            cursor.moveToFirst();
                                            long bytes_downloaded = cursor.getInt(cursor
                                                    .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                            long bytes_total = cursor.getInt(cursor
                                                    .getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                                            cursor.close();
                                            if (bytes_downloaded == bytes_total) {
                                                progressDialog.setProgress(100);
                                                handler.removeCallbacks(this);
                                            } else {
                                                handler.postDelayed(this, 200);
                                                progressDialog.setProgress((int) (bytes_downloaded * 100 / bytes_total));
                                            }
                                        }
                                    });
                                    thread.start();

                                    //set BroadcastReceiver to install app when .apk is downloaded
                                    BroadcastReceiver onComplete = new BroadcastReceiver() {
                                        public void onReceive(Context ctxt, Intent intent) {
                                            progressDialog.setProgress(100);
                                            Runnable runnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    progressDialog.dismiss();
                                                }
                                            };
                                            handler.postDelayed(runnable, 1000);
                                            String apkFileName = unzip(destination);
                                            DuLieu.installApp(mContext, apkFileName);
                                            mLayouCaiDatUngDung.setClickable(true);
                                            List<UngDung> ungDungList = databaseHelper.getLissAppName();
                                            for (int i = 0; i < ungDungList.size(); i++) {
                                                if (checkInstalledApplication(ungDungList.get(i).getName(), mContext)) {
                                                    databaseHelper.updateApp(1, ungDungList.get(i).getId());
                                                } else {
                                                    databaseHelper.updateApp(0, ungDungList.get(i).getId());
                                                }
                                            }
                                            mContext.unregisterReceiver(this);
                                        }
                                    };

                                    //register receiver for when .apk download is compete
                                    mContext.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                                }
                            });
                        }

                        //   Toast.makeText(mContext, mListAnh.size()+"ảnh , ", Toast.LENGTH_LONG).show();
                        /*if (mCaiDat.getText().toString().trim().length() < 9) {
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
                            request.setDescription(mContext.getString(R.string.hello_blank_fragment));
                            request.setTitle(mContext.getString(R.string.app_name));

                            //set destination
                            request.setDestinationUri(uri);

                            // get download service and enqueue file
                            final DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                            final long downloadId = manager.enqueue(request);

                            //set BroadcastReceiver to install app when .apk is downloaded
                            BroadcastReceiver onComplete = new BroadcastReceiver() {
                                public void onReceive(Context ctxt, Intent intent) {

                                    Toast.makeText(mContext, "download finished !!!", Toast.LENGTH_SHORT).show();
                                    String apkFileName = unzip(destination);
                                    DuLieu.installApp(mContext, apkFileName);
                                    mContext.unregisterReceiver(this);
                                }
                            };

                            //register receiver for when .apk download is compete
                            mContext.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                        } else {

                        }*/

                }
            };
            itemView.setOnClickListener(listener);
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"ok",Toast.LENGTH_SHORT).show();
                }
            });*/
           // mCaiDat.setOnClickListener(listener);
        }
    }
}
