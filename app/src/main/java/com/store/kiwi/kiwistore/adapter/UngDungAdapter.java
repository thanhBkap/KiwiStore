package com.store.kiwi.kiwistore.adapter;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    RelativeLayout mLayouCaiDatUngDung;

    public UngDungAdapter(Context mContext, List<UngDung> mListUngDung, View mUngDungFragment, View mUngDungChiTietFragment,
                          List<String> listAnh, DanhSachAnhUngDungAdapter danhSachAnhUngDungAdapter, TextView txtTenUngDung,
                          TextView txtDacTa, TextView txtLuotCai, ImageView anhIcon, RelativeLayout layouCaiDatUngDung, TextView txtCaiDatUngDung) {
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
            holder.mCaiDat.setText("đã cài đặt");
            holder.mLayoutGoCaiDat.setVisibility(View.VISIBLE);
            holder.mChecked.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check));
        } else {
            holder.mCaiDat.setText("Cài đặt");
            holder.mChecked.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check));
            holder.mLayoutGoCaiDat.setVisibility(View.INVISIBLE);
        }
        holder.mTen.setText(ungDung.getName());
        Glide.with(mContext).load(ungDung.getIcon()).into(holder.mIcon);
        holder.mCardViewUngDung.setCardBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return mListUngDung.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIcon, mChecked;
        TextView mTen;
        TextView mCaiDat;
        RelativeLayout mLayoutCaiDat, mLayoutGoCaiDat;
        CardView mCardViewUngDung;

        public ViewHolder(View itemView) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(R.id.anh_ung_dung);
            mChecked = (ImageView) itemView.findViewById(R.id.anh_checked);
            mTen = (TextView) itemView.findViewById(R.id.txt_ten);
            mCaiDat = (TextView) itemView.findViewById(R.id.txt_cai_dat);
            mLayoutCaiDat = (RelativeLayout) itemView.findViewById(R.id.layout_cai_dat);
            mLayoutGoCaiDat = (RelativeLayout) itemView.findViewById(R.id.layout_go_cai_dat);
            mCardViewUngDung = (CardView) itemView.findViewById(R.id.card_view_ung_dung);
            final DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final UngDung checkedUngDung = mListUngDung.get(getPosition());
                    if (v.getId() == R.id.layout_go_cai_dat) {
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

                    } else if (v.getId() == R.id.layout_cai_dat) {
                        mUngDungFragment.setVisibility(View.GONE);
                        mUngDungChiTietFragment.setVisibility(View.VISIBLE);

                        mListAnh.clear();
                        mListAnh.addAll(databaseHelper.getListAnhChiTietUngDung(checkedUngDung));
                        mDanhSachAnhUngDungAdapter.notifyDataSetChanged();

                        Glide.with(mContext).load(checkedUngDung.getIcon()).into(mAnhIcon);
                        mTxtTenUngDung.setText(checkedUngDung.getName());
                        mTxtDacTa.setText(checkedUngDung.getDes());
                        mTxtLuotCai.setText("Đã có " + checkedUngDung.getLuotCai() + " lượt cài");
                        if (checkedUngDung.isInstalled()){
                            mTxtCaiDatUngDung.setText("Đã cài đặt");
                        }else{
                            mTxtCaiDatUngDung.setText("Cài ứng dụng");
                            mLayouCaiDatUngDung.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //get url of app on server
                                    // String url = "http://phonecase.890m.com/files/case.zip";
                                    if(DuLieu.checkInstalledApplication(checkedUngDung.getName(),mContext)){

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
                                            ProgressDialog progressDialog = new ProgressDialog(mContext);
                                            progressDialog.setTitle("Đang cài đặt");
                                            progressDialog.setMessage("Ứng dụng đang được cài đặt ...");
                                            progressDialog.setIndeterminate(true);
                                            progressDialog.show();
                                            String apkFileName = unzip(destination);
                                            progressDialog.dismiss();
                                            DuLieu.installApp(mContext, apkFileName);
                                            mLayouCaiDatUngDung.setClickable(true);
                                            List<UngDung> ungDungList=databaseHelper.getLissAppName();
                                            for (int i=0;i<ungDungList.size();i++){
                                                if (checkInstalledApplication(ungDungList.get(i).getName(),mContext)){
                                                    databaseHelper.updateApp(1,ungDungList.get(i).getId());
                                                }else{
                                                    databaseHelper.updateApp(0,ungDungList.get(i).getId());
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

                }
            };
            itemView.setOnClickListener(listener);
            mLayoutCaiDat.setOnClickListener(listener);
            mLayoutGoCaiDat.setOnClickListener(listener);
        }
    }
}
