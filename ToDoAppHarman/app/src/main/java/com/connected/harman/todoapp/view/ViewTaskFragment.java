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
 *
 * Fragment to View Task Details
 */
public class ViewTaskFragment extends Fragment implements View.OnClickListener,View.OnFocusChangeListener{

    View mRootView;

    Button mBtnSave;
    Button mBtnDelete;

    EditText mEdtTaskName;
    EditText mEdtTaskDesc;
    TextView mTxtTaskDate;

    OnNavigationListener mOnNavigationListener;
    Task mTask;

    public ViewTaskFragment(){

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView=inflater.inflate(R.layout.fragment_view_task,null);
        
        mBtnDelete= (Button) mRootView.findViewById(R.id.btn_delete);
        mBtnSave= (Button) mRootView.findViewById(R.id.btn_save);

        mEdtTaskDesc= (EditText) mRootView.findViewById(R.id.edt_view_task_desc);
        mEdtTaskName= (EditText) mRootView.findViewById(R.id.edt_view_task_name);
        mTxtTaskDate= (TextView) mRootView.findViewById(R.id.txt_view_date);

        mBtnSave.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);

        mEdtTaskDesc.setOnFocusChangeListener(this);
        mEdtTaskName.setOnFocusChangeListener(this);

        showTaskDetails();

        //Change the title of the action bar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("View Task");

        return mRootView;
    }

    /**
     * Method to show Task details.
     */
    private void showTaskDetails() {
        Bundle bundle=getArguments();

        int taskId=bundle.getInt(AppConstants.BUNDLE_KEY_TASK_ID);
        if(taskId!=0) {
            mTask = DBHelper.getInstance(getActivity()).getTask(taskId);
        }

        if(mTask!=null){
            mEdtTaskName.setText(mTask.getTaskName());
            mEdtTaskDesc.setText(mTask.getTaskDescription());
            mTxtTaskDate.setText(AppUtils.convertLongToDate(mTask.getTaskDate()));
        }
    }

    /**
     * Method to handle navigation once the task is seleted of updated.
     */
    private void goBack(){
        mOnNavigationListener.onBackToParent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_delete:
                //Delete from database
                DBHelper.getInstance(getActivity()).deleteTask(mTask.getTaskId());
                Toast.makeText(getActivity(), R.string.task_deleted_success, Toast.LENGTH_SHORT).show();
                goBack();
                break;
            case R.id.btn_save:
                mTask.setTaskDescription(mEdtTaskDesc.getText().toString());
                mTask.setTaskName(mEdtTaskName.getText().toString());
                //Update the Task in database
                DBHelper.getInstance(getActivity()).updateTask(mTask);
                Toast.makeText(getActivity(), R.string.task_updated_success,Toast.LENGTH_SHORT).show();
                goBack();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v instanceof EditText){
            if(!hasFocus){
                AppUtils.hideSoftKeyboard(getActivity(),v);
            }
        }
    }
}
