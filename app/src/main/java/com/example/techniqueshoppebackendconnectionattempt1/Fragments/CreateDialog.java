package com.example.techniqueshoppebackendconnectionattempt1.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.CourseInfo;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.RetrofitDBConnector;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserInfoSingleton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class CreateDialog extends Dialog implements View.OnClickListener {

    private Context context;

    public CreateDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_create_course);

        ImageView imageClose = findViewById(R.id.image_close);
        imageClose.setOnClickListener(this);

        Button create = findViewById(R.id.button_create_course);
        create.setOnClickListener(this);

        Button upload = findViewById(R.id.button_upload_image);
        upload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.image_close) {
            dismiss();
        }else if (v.getId() == R.id.button_create_course) {

            createCourse();
        }else if (v.getId() == R.id.button_upload_image) {

        }

    }

    private void createCourse() {
        RetrofitDBConnector rdbc = new RetrofitDBConnector();
        TextInputEditText editCourseName = findViewById(R.id.edit_course_name);
        TextInputEditText editCourseDescription = findViewById(R.id.edit_course_description);

        String courseName = editCourseName.getText().toString();
        String courseDescription = editCourseDescription.getText().toString();

        if (!courseName.isEmpty() && !courseDescription.isEmpty()) {
            int authorId = UserInfoSingleton.getInstance().getDataList().get(0).getUserID();
            Log.d("id",authorId+"");
            String authorName = UserInfoSingleton.getInstance().getDataList().get(0).getUserName();
            // For course image path, you can set default or handle it as needed
            String courseImgPath = "default_image_path";
            CourseInfo courseInfo = new CourseInfo(0, courseName, courseDescription, authorName, authorId, courseImgPath, courseImgPath);

            rdbc.postNewCourse(courseInfo, new CourseCallback() {
                @Override
                public void onSuccess(List<CourseInfo> courses) {
                    dismiss();
                    Toast.makeText(context, "Course created successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(context, "Failed to create course: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "Please enter all the information", Toast.LENGTH_SHORT).show();
        }
    }

    public interface CourseCallback {
        void onSuccess(List<CourseInfo> courses);
        void onFailure(String error);
    }
}
