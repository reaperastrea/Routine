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

    public ListFragment() { }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        //Fetching data from database asynchronously
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
            try{
                handler = new RoutinesHandler(context[0]);
                routines.addAll(handler.getAllRoutines());
                
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