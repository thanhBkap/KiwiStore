package com.store.kiwi.kiwistore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.store.kiwi.kiwistore.adapter.UngDungAdapter;
import com.store.kiwi.kiwistore.model.UngDung;

import java.util.List;


public class TheLoaiFragment extends Fragment {

    private List<UngDung> mListUngDung;
    private UngDungAdapter mUngDungAdapter;
    private RecyclerView mRecyclerViewUngDung;

    public TheLoaiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_the_loai, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*mListUngDung = new ArrayList<>();
        String icon = "";
        List<String> listAnh = new ArrayList<>();
        mListUngDung.add(new UngDung("1", "abc", true, icon, listAnh, "1"));
        mListUngDung.add(new UngDung("2", "abc2", true, icon, listAnh, "1"));
        mListUngDung.add(new UngDung("3", "a3", true, icon, listAnh, "2"));
        mListUngDung.add(new UngDung("4", "a4", true, icon, listAnh, "3"));

        mRecyclerViewUngDung = (RecyclerView) view.findViewById(R.id.list_ung_dung);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        mRecyclerViewUngDung.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mRecyclerViewUngDung.setHasFixedSize(true);
        mUngDungAdapter = new UngDungAdapter(getActivity(), mListUngDung);
        mRecyclerViewUngDung.setAdapter(mUngDungAdapter);*/
    }
}
