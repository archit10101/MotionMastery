package com.example.techniqueshoppebackendconnectionattempt1.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.CourseInfo;

import java.util.List;

public class CourseGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<CourseInfo> courseList;

    public CourseGridViewAdapter(Context context, List<CourseInfo> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public Object getItem(int position) {
        return courseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.child_rv_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.textCourseName = view.findViewById(R.id.tv_course_name);
            viewHolder.textCourseAuthor = view.findViewById(R.id.tv_author_name);
            viewHolder.authorImg = view.findViewById(R.id.iv_author_image);
            viewHolder.courseIMG = view.findViewById(R.id.rv_child_image);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // Bind data to the views
        CourseInfo course = courseList.get(position);
        viewHolder.textCourseName.setText(course.getCourseName());
        viewHolder.textCourseAuthor.setText(course.getCourseAuthorName());
        viewHolder.courseID = course.getCourseId();
        viewHolder.courseIMG.setImageResource(R.drawable.technique_logo);
        viewHolder.authorImg.setImageResource(R.drawable.technique_logo);
        return view;
    }

    private static class ViewHolder {
        TextView textCourseName;
        TextView textCourseAuthor;

        ImageView courseIMG;


        int courseID;
        ImageView authorImg;
    }
}
