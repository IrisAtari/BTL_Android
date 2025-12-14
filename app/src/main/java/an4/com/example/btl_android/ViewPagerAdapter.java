package an4.com.example.btl_android;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private final int totalTabNumber;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, int totalTabNumber) {
        super(fragmentActivity);
        this.totalTabNumber = totalTabNumber;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
        CourseListFragment fragment = new CourseListFragment();
        Bundle args = new Bundle();
        //args.putString("title", totalTabNumber.get(position));
        fragment.setArguments(args);
        return fragment;
        }
        else {
            RegisterFragment fragment = new RegisterFragment();
            Bundle args = new Bundle();
            //args.putString("title", totalTabNumber.get(position));
            fragment.setArguments(args);
            return fragment;
        }
    }

    @Override
    public int getItemCount() {
        return totalTabNumber;
    }
}
