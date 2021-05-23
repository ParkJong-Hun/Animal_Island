package petstone.project.animalisland;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class VPAdapter extends FragmentPagerAdapter {

    private int tabCount;

    public VPAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        Free_Rehome free = new Free_Rehome();

        switch (position) {
            case 0:

                return free;
            case 1:
                Sell_Rehome sell = new Sell_Rehome();
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

