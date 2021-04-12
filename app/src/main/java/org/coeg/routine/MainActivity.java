package org.coeg.routine;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
{
    private static final int TAB_COUNT = 4;

    private TextView    txtTitle;
    private View        vwBGTitle;
    private TabLayout   tlNavigation;
    private ViewPager   vpLayout;

    private final int[] tabIcons = {
            R.drawable.ic_dashboard,
            R.drawable.ic_routine_list,
            R.drawable.ic_analytic,
            R.drawable.ic_setting
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitView();

        setupViewPager(vpLayout);

        // CAUTION: WILL ALWAYS LOAD 4 OF THE PAGES INSTANTLY
        // MIGHT INTRODUCE HUGE PERFORMANCE IMPACT IN THE LONG RUN(?)
        vpLayout.setOffscreenPageLimit(4);

        tlNavigation.setupWithViewPager(vpLayout);
        setupTabIcons();

        InitListener();
    }

    /**
     * Initialize variable by findViewByID
     * and setup view properties
     */
    private void InitView()
    {
        txtTitle        = findViewById(R.id.txt_title);
        vwBGTitle       = findViewById(R.id.layout_bgTitle);
        tlNavigation    = findViewById(R.id.tl_navigation);
        vpLayout        = findViewById(R.id.vp_content);
    }

    /**
     * Initialize component listener
     */
    private void InitListener()
    {
        vpLayout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            float maxPositionOffset = 0;
            float scale = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 175, getResources().getDisplayMetrics());
            float scale2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90f, getResources().getDisplayMetrics());

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                if (maxPositionOffset < positionOffset)
                    maxPositionOffset = positionOffset;

                if (positionOffset > 0 && position < 1)
                {
                    ViewGroup.LayoutParams params = vwBGTitle.getLayoutParams();
                    params.height = (int)scale - (int)((int)scale2 * positionOffset);
                    vwBGTitle.setLayoutParams(params);
                }

                // TEMPORARY FIX :: MIGHT NOT OPTIMAL --CHANGED WHEN FOUND NEW SOLUTION
                // TO FORCE SET BACKGROUND TITLE TO APPROPRIATE HEIGHT SIZE
                if (position > 1)
                {
                    ViewGroup.LayoutParams params = vwBGTitle.getLayoutParams();
                    params.height = (int)scale - (int)((int)scale2 * maxPositionOffset);
                    vwBGTitle.setLayoutParams(params);
                }
            }

            @Override
            public void onPageSelected(int position)
            {
                switch(position){
                    case 0:
                        txtTitle.setText(getString(R.string.title_dashboard));
                        break;
                    case 1:
                        txtTitle.setText(getString(R.string.title_routine));
                        break;
                    case 2:
                        txtTitle.setText(getString(R.string.title_analytic));
                        break;
                    case 3:
                        txtTitle.setText(getString(R.string.title_setting));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        tlNavigation.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.green_500);
                ImageView imageView = (ImageView)tab.getCustomView().findViewById(R.id.icon);
                imageView.getBackground().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.gray_100);
                ImageView imageView = (ImageView)tab.getCustomView().findViewById(R.id.icon);
                imageView.getBackground().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    private void setupTabIcons()
    {
        View[] views = new View[TAB_COUNT];

        // Initiate each cusstom view for each tab navigation
        for (int i = 0; i < views.length; i++)
        {
            views[i] = getLayoutInflater().inflate(R.layout.customtab, null);
            views[i].findViewById(R.id.icon).setBackgroundResource(tabIcons[i]);
            Objects.requireNonNull(tlNavigation.getTabAt(i)).setCustomView(views[i]);
        }

        // Set the first tab color to green
        views[0].findViewById(R.id.icon).getBackground().setColorFilter(getApplicationContext().getColor(R.color.green_500), PorterDuff.Mode.SRC_IN);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new DashboardFragment(), "Dashboard");
        adapter.addFragment(new ListFragment(), "Routine List");
        adapter.addFragment(new AnalyticFragment(), "Analytics");
        adapter.addFragment(new SettingFragment(), "Settings");

        viewPager.setAdapter(adapter);
        viewPager.setPageMargin(200);
    }
}