package com.connected.harman.todoapp.callback;

import com.connected.harman.todoapp.entity.Task;

/**
 * Created by Vilas on 2/20/2016.
 *
 * Interface to handle communication between activity and fragment
 */
public interface OnTaskClickedListener {

    void taskOpened(int taskId);
}
