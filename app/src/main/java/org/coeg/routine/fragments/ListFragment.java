package org.coeg.routine.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.coeg.routine.R;
import org.coeg.routine.activities.AddRoutineActivity;
import org.coeg.routine.adapters.RoutineListAdapter;
import org.coeg.routine.backend.Routine;
import org.coeg.routine.backend.RoutinesHandler;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class ListFragment extends Fragment
{
    ImageButton         btnAddRoutine;
    RecyclerView        rvRoutineList;
    RoutineListAdapter  mAdapter;

    private static LinkedList<Routine> routines = new LinkedList<>();
    private RoutinesHandler handler;

    //private Boolean isFetched = false;

    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    List<Routine> query;

    public static int counter = 0;

    public ListFragment() { }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        //fetchDatabase();
        //dbFetching.fetchingDB(this.getContext());
        new DBAsync().execute(this.getContext());
        InitView(view);
        InitListener();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    /**
     * Initialize variable by findViewByID
     * and setup view properties
     * @param view fragment view
     */
    private void InitView(View view)
    {
        btnAddRoutine = view.findViewById(R.id.btn_addRoutine);
        rvRoutineList = view.findViewById(R.id.rv_routineList);
        mAdapter = new RoutineListAdapter(this.getContext(), routines);
        rvRoutineList.setAdapter(mAdapter);
        rvRoutineList.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    /**
     * Initialize component listener
     */
    private void InitListener()
    {
        // Open add routine activity
        btnAddRoutine.setOnClickListener(v -> {
            startActivityForResult(new Intent(getActivity(), AddRoutineActivity.class), 1);
        });
    }

    private class DBAsync  extends AsyncTask<Context, Integer, LinkedList<Routine>> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected LinkedList<Routine> doInBackground(Context... context) {
            /*Looper.prepare();
            Looper.loop();
            final Handler handlerr = new Handler(Looper.myLooper());
            handlerr.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms

                }
            }, 10000);
            Looper.myLooper().quitSafely();*/

            try{
                handler = new RoutinesHandler(context[0]);
                routines.addAll(handler.getAllRoutines());

                Routine[] routinez = {
                        new Routine(),
                        new Routine(),
                        new Routine(),
                        new Routine()
                };
                routinez[0].setId(1);
                routinez[0].setName("Talk to senpai");
                routinez[0].setTime(formatter.parse("14:00:00"));
                routinez[0].setActive(true);
                routinez[1].setId(2);
                routinez[1].setName("Get bath");
                routinez[1].setTime(formatter.parse("15:00:00"));
                routinez[1].setActive(true);
                routinez[2].setId(3);
                routinez[2].setName("Nap time");
                routinez[2].setTime(formatter.parse("16:00:00"));
                routinez[2].setActive(true);
                routinez[3].setId(4);
                routinez[3].setName("Weapons check-up");
                routinez[3].setTime(formatter.parse("17:00:00"));
                routinez[3].setActive(true);

                handler.addRoutine(routinez);

                //Test database output
                query = handler.getAllRoutines();
                if(query.get(0).getName().equals(routinez[0].getName())){
                    counter++;
                }if(query.get(1).getName().equals(routinez[1].getName())){
                    counter++;
                }if(query.get(2).getName().equals(routinez[2].getName())){
                    counter++;
                }if(query.get(3).getName().equals(routinez[3].getName())){
                    counter++;
                }

                if(routines.get(0).getName().equals(routinez[0].getName())){
                    counter++;
                }if(routines.get(1).getName().equals(routinez[1].getName())){
                    counter++;
                }if(routines.get(2).getName().equals(routinez[2].getName())){
                    counter++;
                }if(routines.get(3).getName().equals(routinez[3].getName())){
                    counter++;
                }

                //isFetched = true;
                mAdapter.notifyItemInserted(0);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routines;
        }

        @Override
        protected void onPostExecute(LinkedList<Routine> routine) {

        }
    }
}