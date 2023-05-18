package com.samar.location.homepage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HomeTabs_Adpater extends FragmentStateAdapter {

    public HomeTabs_Adpater(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @Override
    public Fragment createFragment(int position) {

        if (position == 1) {
            return new MapTab();
        }

        return new HouseListTab();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
