package com.connected.harman.todoapp.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.connected.harman.todoapp.R;
import com.connected.harman.todoapp.adapter.TaskAdapter;
import com.connected.harman.todoapp.callback.OnTaskClickedListener;
import com.connected.harman.todoapp.entity.Task;
import com.connected.harman.todoapp.persistance.DBHelper;

import java.util.ArrayList;

/**
 * Created by Vilas on 2/20/2016.
 *
 * Fragment class to display list of Tasks
 */
public class TaskListFragment extends Fragment implements AdapterView.OnItemClickListener {

    View mRootView;
    ListView mLstTask;
    TaskAdapter mTaskAdapter;
    ArrayList<Task> mTaskList;
    OnTaskClickedListener mOnTaskClickedListener;

    public TaskListFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mOnTaskClickedListener = (OnTaskClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTaskClickedListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnTaskClickedListener = (OnTaskClickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnTaskClickedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_task_list, null);
        mLstTask = (ListView) mRootView.findViewById(R.id.lst_view_task_list);
        mLstTask.setOnItemClickListener(this);

        //Get all the tasks from database
        mTaskList = DBHelper.getInstance(getActivity()).getAllTasks();

        if (mTaskList != null) {
            mTaskAdapter = new TaskAdapter(getActivity(), mTaskList);
        }
        mLstTask.setAdapter(mTaskAdapter);

        //Change the title of the action bar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Tasks");

        return mRootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Open Task details when you click on a Task
        mOnTaskClickedListener.taskOpened(mTaskList.get(position).getTaskId());
    }
}
