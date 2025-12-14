package an4.com.example.btl_android;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class TabbedActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager;

    private final String TAG = "TabActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tabbed);

        InitUIItems();
        Log.d(TAG,"Tab activity launched!");



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void InitUIItems() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        ArrayList<String> tabTitle = new ArrayList<>();
        tabTitle.add("Danh sách Học Phần");
        tabTitle.add("Danh sách đã đăng ký");
        viewPager.setAdapter(new ViewPagerAdapter(this,2));

        new TabLayoutMediator(tabLayout, viewPager, (tab, i) -> tab.setText(tabTitle.get(i))).attach();
    }


}