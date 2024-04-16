package com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchScreenRecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techniqueshoppebackendconnectionattempt1.R;

import java.util.List;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ViewHolder> {

    List<ParentModalClass> parentModalClassList;

    Context context;

    public ParentAdapter(List<ParentModalClass> parentModalClassList, Context context) {
        this.parentModalClassList = parentModalClassList;
        this.context = context;
    }

    @NonNull
    @Override
    public ParentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.parent_rv_layout,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentAdapter.ViewHolder holder, int position) {
        holder.tv_parent_title.setText(parentModalClassList.get(position).title);
        ChildAdapter childAdapter;
        childAdapter = new ChildAdapter(parentModalClassList.get(position).childModalClassList,context);
        holder.rv_child.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        holder.rv_child.setAdapter(childAdapter);
        childAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return parentModalClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView rv_child;

        TextView tv_parent_title;

        TextView noCoursesView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rv_child = itemView.findViewById(R.id.rv_child);
            tv_parent_title = itemView.findViewById(R.id.tv_parent_title);
            noCoursesView = itemView.findViewById(R.id.noCourses);
        }
    }
}
