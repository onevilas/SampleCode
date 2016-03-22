package com.connected.harman.todoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.connected.harman.todoapp.R;
import com.connected.harman.todoapp.entity.Task;
import com.connected.harman.todoapp.utils.AppUtils;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

/**
 * Created by Vilas on 2/20/2016.
 *
 * Adapter class to display all tasks in the list.
 */
public class TaskAdapter extends BaseAdapter{

    Context mContext;
    ArrayList<Task> mTaskList;
    LayoutInflater mLayoutInflater;

    public TaskAdapter(Context context,ArrayList<Task> list){
        mContext=context;
        mTaskList=list;
        mLayoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mTaskList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTaskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=mLayoutInflater.inflate(R.layout.task_item_layout,null);
            holder.txtTaskDate= (TextView) convertView.findViewById(R.id.txt_task_date);
            holder.txtTaskDescription= (TextView) convertView.findViewById(R.id.txt_task_description);
            holder.txtTaskName= (TextView) convertView.findViewById(R.id.txt_task_name);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        Task task=mTaskList.get(position);
        holder.txtTaskDate.setText(AppUtils.convertLongToDate(task.getTaskDate()));
        holder.txtTaskDescription.setText(task.getTaskDescription());
        holder.txtTaskName.setText(task.getTaskName());

        return convertView;
    }

    /**
     * ViewHolder class for each item.
     */
    private static class ViewHolder{
        TextView txtTaskDate;
        TextView txtTaskName;
        TextView txtTaskDescription;
    }
}
