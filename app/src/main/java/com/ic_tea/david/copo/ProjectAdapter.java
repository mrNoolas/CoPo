package com.ic_tea.david.copo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ic_tea.david.copo.objects.ActionLog;
import com.ic_tea.david.copo.objects.Project;

import java.util.ArrayList;

/**
 * Created by david on 6/19/16.
 */
public class ProjectAdapter extends BaseExpandableListAdapter {
    private Context context;
    private DBHelper dbHelper;
    private ArrayList<Project> projects;
    Button logButton, portfolioButton, editButton, shareButton, removeButton;

    public ProjectAdapter(Context context, ArrayList<Project> projects) {
        this.context = context;
        this.projects = projects;

        dbHelper = DBHelper.getInstance(context);
    }

    private static class GroupViewHolder {
        TextView title, hoursSpent;
    }

    private static class ChildViewHolder {
        TextView description;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.display_project_parent, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.dpp_title);
            holder.hoursSpent = (TextView) convertView.findViewById(R.id.dpp_hours_spent_text_view);

            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }

        Project item = projects.get(groupPosition);
        holder.title.setText(item.title);
        holder.hoursSpent.setText(Double.toString(dbHelper.getHoursSpentOnProject(item.id)));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder holder;

        // It seems to recycle the views a bit awkwardly... therefore remake child every time
        //if (convertView == null) {
        holder = new ChildViewHolder();
        convertView = LayoutInflater.from(context).inflate(R.layout.display_project_child, parent, false);
        holder.description = (TextView) convertView.findViewById(R.id.dpc_descr_text_view);
        convertView.setTag(holder);

        logButton = (Button) convertView.findViewById(R.id.dpc_log_button);
        portfolioButton = (Button) convertView.findViewById(R.id.dpc_portfolio_button);
        editButton = (Button) convertView.findViewById(R.id.dpc_edit_button);
        shareButton = (Button) convertView.findViewById(R.id.dpc_share_button);
        removeButton = (Button) convertView.findViewById(R.id.dpc_delete_button);

        Project item = projects.get(groupPosition);
        // store in every button for easy onClick-listening and handling
        logButton.setTag(item.id);
        portfolioButton.setTag(item.id);
        editButton.setTag(item.id);
        shareButton.setTag(groupPosition);
        removeButton.setTag(item.id);

        //} else {
        //    holder = (ChildViewHolder) convertView.getTag();
        //}

        holder.description.setText(item.descr);

        return convertView;
    }

    @Override
    public int getGroupCount() {
        return projects.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1; // there is always one child per parent
    }

    @Override
    public Project getGroup(int groupPosition) {
        return projects.get(groupPosition);
    }

    @Override
    public Project getChild(int groupPosition, int childPosition) {
        return projects.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
