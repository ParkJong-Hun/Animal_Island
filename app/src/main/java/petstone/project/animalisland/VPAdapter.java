package petstone.project.animalisland;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class VPAdapter extends FragmentStatePagerAdapter {

    private int tabCount;

    public VPAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Free_Rehome free = new Free_Rehome();
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