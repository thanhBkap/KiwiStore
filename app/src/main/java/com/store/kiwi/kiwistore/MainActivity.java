package com.store.kiwi.kiwistore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.store.kiwi.kiwistore.adapter.TheLoaiAdapter;
import com.store.kiwi.kiwistore.model.TheLoai;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<TheLoai> mListTheLoai;
    private RecyclerView mRecyclerViewTheLoai;
    private TheLoaiAdapter mTheLoaiAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addControls() {
        mListTheLoai=new ArrayList<>();
        mListTheLoai.add(new TheLoai("tất cả","750",R.drawable.ic_play,"1",true));
        mListTheLoai.add(new TheLoai("giải trí","200",R.drawable.game,"2",false));
        mListTheLoai.add(new TheLoai("trò chơi","300",R.drawable.ic_play,"3",false));
        mListTheLoai.add(new TheLoai("giáo dục và sức khỏe","250",R.drawable.game,"4",false));

        mRecyclerViewTheLoai = (RecyclerView) findViewById(R.id.list_the_loai);
        mRecyclerViewTheLoai.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerViewTheLoai.setHasFixedSize(false);
        mTheLoaiAdapter = new TheLoaiAdapter(this,mListTheLoai);
        mRecyclerViewTheLoai.setAdapter(mTheLoaiAdapter);
        /*List<ApplicationInfo> listApplicationInfo = getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        String label = (String) getPackageManager().getApplicationLabel(listApplicationInfo.get(2));
        Drawable icon = getPackageManager().getApplicationIcon(listApplicationInfo.get(2));
        txtTest.setText(label);
        img.setImageDrawable(icon);*/

        /*for (int i = 0; i < listApplicationInfo.size(); i++) {
            // Toast.makeText(getApplicationContext(),"ứng dụng số "+i+"/"+listApplicationInfo.size()+" "+getPackageManager().getApplicationLabel(listApplicationInfo.get(i)),Toast.LENGTH_SHORT).show();
            if (getPackageManager().getApplicationLabel(listApplicationInfo.get(i)).toString().trim().toLowerCase().equals("youtube")) {
                pname=getPackageManager().getInstalledPackages(PackageManager.GET_META_DATA).get(i).packageName;
                Toast.makeText(getApplicationContext(), "Ok-" +pname, Toast.LENGTH_LONG).show();
                break;
            }
        }
        Intent openApp=getPackageManager().getLaunchIntentForPackage(pname);
        openApp.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(openApp);*/
        /*Map<String, String> today = LunarCalendar.getTodayInfo();
        Toast.makeText(this, "Ngày dương: " +"Thứ "+today.get("thu")+" "+ today.get("daySolar") + "/" + today.get("monthSolar") + "/" + today.get("yearSolar")
                + "\nNgày âm: " + today.get("dayLunar") + "/" + today.get("monthLunar") + "/" + today.get("yearLunar")
                +"\nNăm "+today.get("can")+" "+today.get("chi"), Toast.LENGTH_LONG).show();*/


    }

    private void addEvents() {

    }
}
