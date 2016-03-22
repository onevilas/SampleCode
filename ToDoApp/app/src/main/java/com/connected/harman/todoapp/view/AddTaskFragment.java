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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.connected.harman.todoapp.R;
import com.connected.harman.todoapp.callback.OnNavigationListener;
import com.connected.harman.todoapp.entity.Task;
import com.connected.harman.todoapp.persistance.DBHelper;
import com.connected.harman.todoapp.utils.AppConstants;
import com.connected.harman.todoapp.utils.AppUtils;

/**
 * Created by Vilas on 2/20/2016.
 * <p/>
 * Add Task Fragment
 */
public class AddTaskFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

    View mRootView;

    Button mBtnAddTask;

    EditText mEdtTaskName;
    EditText mEdtTaskDesc;

    TextView mTxtTaskDate;
    long currentDateTime;

    OnNavigationListener mOnNavigationListener;

    public AddTaskFragment() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(AppConstants.CURRENT_MILI_SECONDS, currentDateTime);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            currentDateTime = savedInstanceState.getLong(AppConstants.CURRENT_MILI_SECONDS);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnNavigationListener = (OnNavigationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnTaskClickedListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mOnNavigationListener = (OnNavigationListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTaskClickedListener");
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_add_task, null);

        mBtnAddTask = (Button) mRootView.findViewById(R.id.btn_add_task);
        mEdtTaskDesc = (EditText) mRootView.findViewById(R.id.edt_txt_task_desc);
        mEdtTaskName = (EditText) mRootView.findViewById(R.id.edt_txt_task_name);
        mTxtTaskDate = (TextView) mRootView.findViewById(R.id.txt_date);

        currentDateTime = AppUtils.getLongDate();
        mTxtTaskDate.setText(AppUtils.convertLongToDate(currentDateTime));

        mBtnAddTask.setOnClickListener(this);
        mEdtTaskName.setOnFocusChangeListener(this);
        mEdtTaskDesc.setOnFocusChangeListener(this);

        //Change the title of the action bar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Create Task");

        return mRootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_task:
                if (!validateFields()) {
                    AppUtils.showDialog(getActivity(), getString(R.string.dialog_header_alert), getString(R.string.error_please_enter_task_name));
                    return;
                }
                saveTask();
                mOnNavigationListener.onBackToParent();
                break;
        }
    }

    /**
     * Method to save Task in database
     */
    private void saveTask() {
        Task task = new Task();
        task.setTaskDescription(mEdtTaskDesc.getText().toString());
        task.setTaskDate(currentDateTime);
        task.setTaskName(mEdtTaskName.getText().toString());
        long insertTask = DBHelper.getInstance(getActivity()).insertTask(task);
        if (insertTask != -1) {
            Toast.makeText(getActivity(), R.string.task_added_success, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), R.string.error_inserting_task_to_db, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method to validate if the Task Name field is entered or not
     *
     * @return
     */
    private boolean validateFields() {
        if (mEdtTaskName.getText().toString().trim().length() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //We are using only edit text for input. On clicking any other part of the application, soft key board should be hidden.
        if (v instanceof EditText) {
            if (!hasFocus) {
                AppUtils.hideSoftKeyboard(getActivity(), v);
            }
        }
    }
}
