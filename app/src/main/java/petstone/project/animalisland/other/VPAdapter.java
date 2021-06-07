package petstone.project.animalisland.other;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import petstone.project.animalisland.component.FreeRehomeComponent;
import petstone.project.animalisland.component.SellRehomeComponent;

public class VPAdapter extends FragmentPagerAdapter {

    private int tabCount;

    public VPAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FreeRehomeComponent free = new FreeRehomeComponent();
                return free;
            case 1:
                SellRehomeComponent sell = new SellRehomeComponent();
                return sell;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

