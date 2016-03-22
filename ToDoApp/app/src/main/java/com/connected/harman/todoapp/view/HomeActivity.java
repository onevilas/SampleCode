package com.connected.harman.todoapp.view;

import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.connected.harman.todoapp.R;
import com.connected.harman.todoapp.callback.OnNavigationListener;
import com.connected.harman.todoapp.callback.OnTaskClickedListener;
import com.connected.harman.todoapp.persistance.DBHelper;
import com.connected.harman.todoapp.utils.AppConstants;

/**
 * Activity class to handle Task operation.
 */
public class HomeActivity extends AppCompatActivity implements OnTaskClickedListener,
        OnNavigationListener, View.OnClickListener {

    private static final String TAG = "HomeActivity";

    Button mBtnAddTask;
    boolean isExitPressedOnce = false;
    boolean isFabVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setStatusBarColorForPostLollipop();
        mBtnAddTask= (Button) findViewById(R.id.btn_add_task);
        mBtnAddTask.setOnClickListener(this);
        if (savedInstanceState == null) {
            showTaskListFragment();
        }
    }

    private void setStatusBarColorForPostLollipop() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public void taskOpened(int taskId) {
        hideFab();
        ViewTaskFragment viewTaskFragment = new ViewTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.BUNDLE_KEY_TASK_ID, taskId);
        viewTaskFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.fragment_container, viewTaskFragment, "viewTaskFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackToParent() {
        showTaskListFragment();
        showFab();
    }

    /**
     * Method to show fragment which contains list of Tasks.
     */
    private void showTaskListFragment() {
        TaskListFragment taskListFragment = new TaskListFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, taskListFragment)
                .commit();
        showFab();
    }


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            if (isExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.isExitPressedOnce = true;
            Toast.makeText(this, R.string.please_click_back_to_exit, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    isExitPressedOnce = false;
                }
            }, 2000);
        } else {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            showFab();
        }

    }

    /**
     * Method to show Floating Action button
     */
    private void showFab() {
        //Show Floating Action Button
        mBtnAddTask.setVisibility(View.VISIBLE);
        isFabVisible = true;
    }

    /**
     * Method to hide Floating Action button
     */
    private void hideFab() {
        //Hide Floating Action Button
        mBtnAddTask.setVisibility(View.GONE);
        isFabVisible = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Close the database connection when activity destroyed.
        DBHelper.getInstance(this).closeDB();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add_task) {
            hideFab();
            AddTaskFragment addTaskFragment = new AddTaskFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, addTaskFragment, "addTaskFragment")
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AppConstants.IS_BACK_PRESSED, isExitPressedOnce);
        outState.putBoolean(AppConstants.IS_FAB_VISIBLE, isFabVisible);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isExitPressedOnce = savedInstanceState.getBoolean(AppConstants.IS_BACK_PRESSED);
        isFabVisible = savedInstanceState.getBoolean(AppConstants.IS_FAB_VISIBLE);
        Log.i(TAG, "Inside onRestoreInstanceState isExitPressedOnce=" + isExitPressedOnce + " ,isFabVisible=" + isFabVisible);
        if (isFabVisible) {
            showFab();
        } else {
            hideFab();
        }
    }
}
