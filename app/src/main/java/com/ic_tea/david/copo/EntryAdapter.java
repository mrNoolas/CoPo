/*
 * Copyright (c) 2015. This was programmed by IC-Tea (@sirNoolas - David Vonk): Do not copy!
 */

package com.ic_tea.david.copo;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ic_tea.david.copo.objects.ActionLog;

import java.util.ArrayList;

public class EntryAdapter extends BaseAdapter {
    private SparseBooleanArray selectedIds;
    private Context context;
    ArrayList<ActionLog> objects;
    DBHelper dbHelper;


    /**
     * @param objects contains the data to display
     */
    public EntryAdapter(Context context, ArrayList<ActionLog> objects) {
        this.objects = objects;
        selectedIds = new SparseBooleanArray();
        this.context = context;

        dbHelper = DBHelper.getInstance(context);
    }

    private static class ViewHolder {
        TextView title, project, date, dole; // dole -> date of last edit
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public ActionLog getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.display_entry_list_item, parent);
            holder.title = (TextView) convertView.findViewById(R.id.deli_title_text_view);
            holder.project = (TextView) convertView.findViewById(R.id.deli_project_text_view);
            holder.date = (TextView) convertView.findViewById(R.id.deli_date_text_view);
            holder.dole = (TextView) convertView.findViewById(R.id.deli_dole_text_view);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ActionLog item = objects.get(position);
        holder.title.setText(item.title);
        holder.project.setText(dbHelper.getProject(item.projectId).title);
        // Format date from 'DDMMYYYY' to 'DD-MM-YYYY'
        String date = item.date.substring(0, 2) + "-" + item.date.substring(2, 4) + "-" +
                item.date.substring(4);
        holder.date.setText(date);

        // Format date from 'DDMMYYYYHHMM' to 'HH:MM DD-MM-YYYY'
        String dole = item.DOLE.substring(8, 10) + ":" + item.DOLE.substring(10) + " " +
                item.DOLE.substring(6, 8) + "-" + item.DOLE.substring(4, 6) + "-" +
                item.DOLE.substring(0, 4);
        holder.dole.setText(dole);
        return convertView;
    }

    public void remove(ActionLog object) {
        objects.remove(object);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !selectedIds.get(position));
    }

    public void removeSelection(){
        selectedIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean selected) {
        if (selected) {
            selectedIds.put(position, selected);
        } else {
            selectedIds.delete(position);
        }
        notifyDataSetChanged();
    }

    public int getSelectionCount() {
        return selectedIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return selectedIds;
    }
}
