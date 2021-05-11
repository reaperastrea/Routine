package org.coeg.routine.activities;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
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

import org.coeg.routine.backend.Days;
import org.coeg.routine.backend.History;
import org.coeg.routine.backend.PreferencesStorage;
import org.coeg.routine.backend.Routine;
import org.coeg.routine.backend.RoutinesHandler;
import org.coeg.routine.fragments.AnalyticFragment;
import org.coeg.routine.fragments.DashboardFragment;
import org.coeg.routine.fragments.ListFragment;
import org.coeg.routine.R;
import org.coeg.routine.fragments.SettingFragment;
import org.coeg.routine.adapters.ViewPagerAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity
{
    private static final int TAB_COUNT = 4;

    private TextView    txtTitle;
    private View        vwBGTitle;
    private TabLayout   tlNavigation;
    private ViewPager   vpLayout;

    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private RoutinesHandler handler;

    //Testing database input
    public static int counter = 0;
    List<History> query;

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

        //Insert data dummy for testing purposes
        new InsertDummyData().execute(this);

        InitView();

        setupViewPager(vpLayout);

        // CAUTION: WILL ALWAYS LOAD 4 OF THE PAGES INSTANTLY
        // MIGHT INTRODUCE HUGE PERFORMANCE IMPACT IN THE LONG RUN(?)
        //Set to 1
        vpLayout.setOffscreenPageLimit(1);

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
        views[0].findViewById(R.id.icon).getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.green_500), PorterDuff.Mode.SRC_IN);
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

    //Inputting dummy data
    //todo: delete this whole function once everything is done
    private class InsertDummyData  extends AsyncTask<Context, Integer, Integer> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Integer doInBackground(Context... context) {
            try{
                handler = new RoutinesHandler(context[0]);
                handler.deleteAllRoutines();

                Routine[] routines = {
                        new Routine(),
                        new Routine(),
                        new Routine(),
                        new Routine()
                };
                routines[0].setId(1);
                routines[0].setName("Talk to senpai");
                routines[0].setTime(formatter.parse("14:00:00"));
                routines[0].setActive(true);
                routines[0].setDays(new Days[] {
                        Days.Monday
                });
                routines[1].setId(2);
                routines[1].setName("Get bath");
                routines[1].setTime(formatter.parse("15:00:00"));
                routines[1].setActive(true);
                routines[1].setDays(new Days[] {
                        Days.Monday,
                        Days.Tuesday,
                        Days.Thursday
                });
                routines[2].setId(3);
                routines[2].setName("Nap time");
                routines[2].setTime(formatter.parse("16:00:00"));
                routines[2].setActive(true);
                routines[2].setDays(new Days[] {
                        Days.Monday,
                        Days.Tuesday,
                        Days.Wednesday,
                        Days.Thursday,
                        Days.Friday,
                        Days.Saturday,
                        Days.Sunday
                });
                routines[3].setId(4);
                routines[3].setName("Weapons check-up");
                routines[3].setTime(formatter.parse("17:00:00"));
                routines[3].setActive(true);
                routines[3].setDays(new Days[] {
                        Days.Tuesday,
                        Days.Wednesday,
                        Days.Friday
                });

                handler.addRoutine(routines);

                History[] histories = {
                        new History(),
                        new History(),
                        new History(),
                        new History()
                };
                histories[0].setId(1);
                histories[0].setRoutineId(1);
                histories[0].setTime(formatter.parse("14:00:00"));
                histories[0].setDate(dateFormatter.parse("2021-05-11"));

                histories[1].setId(2);
                histories[1].setRoutineId(2);
                histories[1].setTime(formatter.parse("15:01:00"));
                histories[1].setDate(dateFormatter.parse("2021-05-11"));

                histories[2].setId(3);
                histories[2].setRoutineId(3);
                histories[2].setTime(formatter.parse("16:20:00"));
                histories[2].setDate(dateFormatter.parse("2021-05-11"));

                histories[3].setId(4);
                histories[3].setRoutineId(4);
                histories[3].setTime(formatter.parse("17:19:59"));
                histories[3].setDate(dateFormatter.parse("2021-05-11"));

                handler.addHistory(histories);
            } catch (Exception e) {
                e.printStackTrace();
            } return 0;
        }
    }
}