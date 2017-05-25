package com.store.kiwi.kiwistore.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.store.kiwi.kiwistore.R;
import com.store.kiwi.kiwistore.model.TheLoai;

import java.util.List;

/**
 * Created by admin on 5/25/2017.
 */

public class TheLoaiAdapter extends RecyclerView.Adapter<TheLoaiAdapter.ViewHolder> {
    Context mContext;
    List<TheLoai> mListTheLoai;


    public TheLoaiAdapter(Context context, List<TheLoai> listTheLoai) {
        mContext = context;
        mListTheLoai = listTheLoai;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_don_vi_the_loai, null);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TheLoai theLoai=mListTheLoai.get(position);
        Drawable icon = mContext.getResources().getDrawable(theLoai.getIcon());
        holder.mIcon.setImageDrawable(icon);
        holder.mTenVaSoLuong.setText(theLoai.getTen()+" ("+theLoai.getSoLuong()+")");
        holder.mCardView.setCardElevation(0);

        if (theLoai.isChecked()){
            holder.mCardView.setRadius(15);
            holder.mCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.light_green));
        }else{
            holder.mCardView.setRadius(0);
            holder.mCardView.setCardBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return mListTheLoai.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIcon;
        TextView mTenVaSoLuong;
        CardView mCardView;
        ViewHolder( View itemView) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(R.id.anh_the_loai);
            mTenVaSoLuong = (TextView) itemView.findViewById(R.id.txtTenVaSoLuong);
            mCardView= (CardView) itemView.findViewById(R.id.card_view_the_loai);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,getPosition()+"",Toast.LENGTH_LONG).show();
                    for (int i=0;i<getItemCount();i++){
                        if (mListTheLoai.get(i).isChecked()){
                            mListTheLoai.get(i).setChecked(false);
                            break;
                        }
                    }
                    mListTheLoai.get(getPosition()).setChecked(true);
                    notifyDataSetChanged();
                }
            });
        }

    }
}

