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
import android.widget.TextView;

import java.util.ArrayList;

public class EntryAdapter extends ArrayAdapter<String> {
    private SparseBooleanArray selectedIds;
    ArrayList<String> objects;
    LayoutInflater inflater;
    Context context;

    /**
     * @param objects contains the data to display
     */
    public EntryAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);

        this.objects = objects;
        selectedIds = new SparseBooleanArray();
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    private static class ViewHolder {
        TextView item;
    }

    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.entry_item, null);
            holder.item = (TextView) view.findViewById(R.id.item_text_view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.item.setText(objects.get(position));
        return view;
    }

    @Override
    public void remove(String object) {
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
