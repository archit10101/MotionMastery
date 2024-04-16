package com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchScreenRecyclerview;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techniqueshoppebackendconnectionattempt1.CourseEnrolledDisplayed;
import com.example.techniqueshoppebackendconnectionattempt1.Fragments.CourseDetailsDialog;
import com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchFragment;
import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.CourseInfo;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.RetrofitDBConnector;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserEnrolledCourse;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserInfoSingleton;

import java.util.ArrayList;
import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {

    List<ChildModalClass> childModalClassList;

    Context context;

    public ChildAdapter(List<ChildModalClass> childModalClassList, Context context) {
        this.childModalClassList = childModalClassList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChildAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.child_rv_layout,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildAdapter.ViewHolder holder, int position) {
        holder.iv_child_image.setImageResource(childModalClassList.get(position).image); //Use path name to get image from s3
        holder.iv_author_image.setImageResource(childModalClassList.get(position).authorImage); //Use path name to get image from s3
        holder.iv_child_coursename.setText(childModalClassList.get(position).courseName);
        holder.iv_author_name.setText(childModalClassList.get(position).authorName);

    }

    @Override
    public int getItemCount() {
        return childModalClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView iv_child_image;
        TextView iv_child_coursename;
        TextView iv_author_name;
        ImageView iv_author_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_child_image = itemView.findViewById(R.id.rv_child_image);
            iv_child_coursename = itemView.findViewById(R.id.tv_course_name);
            iv_author_name = itemView.findViewById(R.id.tv_author_name);
            iv_author_image = itemView.findViewById(R.id.iv_author_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION) {
                ChildModalClass clickedItem = childModalClassList.get(position);

                RetrofitDBConnector rdbc = new RetrofitDBConnector();
                rdbc.getCourseByCourseID(clickedItem.id, new SearchFragment.CourseCallback() {

                    @Override
                    public void onSuccess(List<CourseInfo> courseData) {
                        RetrofitDBConnector retrofitDBConnector = new RetrofitDBConnector();
                        retrofitDBConnector.getEnrolledCourse(UserInfoSingleton.getInstance().getDataList().get(0).getUserID()+"", courseData.get(0).getCourseId()+"", new RetrofitDBConnector.EnrolledCourseCallback() {
                            @Override
                            public void onSuccess(UserEnrolledCourse enrolledCourse) {
                                // Handle successful retrieval of enrolled course
                                Log.d("Retrofit", "Enrolled course found");
                                Intent intent = new Intent(context, CourseEnrolledDisplayed.class);
                                intent.putExtra("COURSE_ID", courseData.get(0).getCourseId());
                                context.startActivity(intent);

                            }

                            @Override
                            public void onFailure(String error) {
                                // Handle failure
                                CourseDetailsDialog dialog = new CourseDetailsDialog(context, courseData.get(0));
                                dialog.show();                            }
                        });



                    }

                    @Override
                    public void onFailure(String errorMessage) {

                    }
                });

            }
        }
    }
    }
