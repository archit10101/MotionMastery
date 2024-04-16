package com.example.techniqueshoppebackendconnectionattempt1.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.CourseInfo;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.RetrofitDBConnector;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserInfoSingleton;

public class CourseDetailsDialog extends Dialog {

    public CourseDetailsDialog(@NonNull Context context, @NonNull CourseInfo course) {
        super(context);
        setContentView(R.layout.dialog_course_details);

        ImageView xButton = findViewById(R.id.image_close);
        ImageView image = findViewById(R.id.CourseImg);
        TextView textCourseName = findViewById(R.id.text_course_name);
        TextView textCourseDescription = findViewById(R.id.text_course_description);
        Button enrollButton = findViewById(R.id.button_enroll);

        ImageView authorImg = findViewById(R.id.authorImg);

        TextView authorText = findViewById(R.id.authorName);

        authorText.setText(course.getCourseAuthorName());

        image.setImageResource(R.drawable.technique_logo);
        textCourseName.setText(course.getCourseName());
        textCourseDescription.setText(course.getCourseDescription());
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitDBConnector retrofitDBConnector = new RetrofitDBConnector();
                retrofitDBConnector.enrollCourse(""+UserInfoSingleton.getInstance().getDataList().get(0).getUserID(), ""+course.getCourseId(), new RetrofitDBConnector.EnrollCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d("Retrofit", "User enrolled in course successfully");
                    }

                    @Override
                    public void onFailure(String error) {
                        Log.e("Retrofit", "Failed to enroll user in course: " + error);
                    }
                });
                dismiss();
            }
        });
    }
}
