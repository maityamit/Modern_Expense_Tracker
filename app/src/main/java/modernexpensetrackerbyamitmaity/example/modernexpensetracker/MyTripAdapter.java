package modernexpensetrackerbyamitmaity.example.modernexpensetracker;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyTripAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public MyTripAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CurrentTripFragment homeFragment = new CurrentTripFragment();
                return homeFragment;
            case 1:
                FutureTripFragment sportFragment = new FutureTripFragment();
                return sportFragment;
            case 2:
                PastTripFragment movieFragment = new PastTripFragment();
                return movieFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
