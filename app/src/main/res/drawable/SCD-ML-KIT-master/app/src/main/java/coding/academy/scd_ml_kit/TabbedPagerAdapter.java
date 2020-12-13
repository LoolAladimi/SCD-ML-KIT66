package coding.academy.scd_ml_kit;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabbedPagerAdapter extends FragmentStateAdapter {


    public TabbedPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public TabbedPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public TabbedPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position)
        {
            case 0 :
                return new CameraFragment() ;
            case 1 :
                return new GallaryFragment();
            case 2 :
                return new AboutFragment() ;
            default:
                return null ;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
