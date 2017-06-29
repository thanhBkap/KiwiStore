package com.store.kiwi.kiwistore.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.store.kiwi.kiwistore.R;

import java.util.List;

/**
 * Created by admin on 5/29/2017.
 */

public class DanhSachAnhUngDungAdapter extends RecyclerView.Adapter<DanhSachAnhUngDungAdapter.ViewHolder> {
    Context mContext;
    List<String> mListAnh;

    public DanhSachAnhUngDungAdapter(Context context, List<String> listAnh) {
        mContext = context;
        mListAnh = listAnh;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_don_vi_danh_sach_anh, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext).load(mListAnh.get(position)).into(holder.mAnhChiTiet);
        if (position == 2) {
            //holder.mAnhChiTiet.setScaleX(2);
            //holder.mAnhChiTiet.setScaleY(2);
        }
    }

    @Override
    public int getItemCount() {
        return mListAnh.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mAnhChiTiet;

        public ViewHolder(View itemView) {
            super(itemView);
            mAnhChiTiet = (ImageView) itemView.findViewById(R.id.anh_chi_tiet_ung_dung);
        }
    }
}
