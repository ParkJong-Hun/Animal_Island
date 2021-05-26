package petstone.project.animalisland.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import petstone.project.animalisland.Free_Rehome;
import petstone.project.animalisland.R;
import petstone.project.animalisland.Sell_Rehome;
import petstone.project.animalisland.VPAdapter;
import petstone.project.animalisland.activity.MainActivity;


public class RehomeComponent extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rehome_component, container, false);

        tabLayout = view.findViewById(R.id.Rehome_tab);
        viewPager = view.findViewById(R.id.Rehome_main);

        VPAdapter pagerAdapter = new VPAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }
}

