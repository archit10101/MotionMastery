package com.example.techniqueshoppebackendconnectionattempt1.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.RetrofitDBConnector;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserInfo;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserInfoSingleton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SettingsFragment extends Fragment {
    private TextInputLayout layoutUsername, layoutPassword, layoutEmail, layoutFirstName, layoutLastName;
    private TextInputEditText editUsername, editPassword, editEmail, editFirstName, editLastName;
    private MaterialButton btnEdit, btnSave;

    private ImageView userImage;

    private MaterialButton uploadImage;

    private UserInfo.MyData user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);


        user = UserInfoSingleton.getInstance().getDataList().get(0);

        layoutUsername = view.findViewById(R.id.username_layout);
        userImage = view.findViewById(R.id.userImage);
        uploadImage = view.findViewById(R.id.button_upload_image);
        layoutPassword = view.findViewById(R.id.password_layout);
        layoutEmail = view.findViewById(R.id.email_layout);
        layoutFirstName = view.findViewById(R.id.first_name_layout);
        layoutLastName = view.findViewById(R.id.last_name_layout);
        editUsername = view.findViewById(R.id.edit_username);
        editPassword = view.findViewById(R.id.edit_password);
        editEmail = view.findViewById(R.id.edit_email);
        editFirstName = view.findViewById(R.id.edit_first_name);
        editLastName = view.findViewById(R.id.edit_last_name);
        btnEdit = view.findViewById(R.id.btn_edit);
        btnSave = view.findViewById(R.id.btn_save);


        setEditable(false);

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),user.getUserImagePath(),Toast.LENGTH_SHORT).show();
            }
        });

        btnEdit.setOnClickListener(v -> {
            setEditable(true);

        });

        btnSave.setOnClickListener(v -> {
            RetrofitDBConnector rdbc = new RetrofitDBConnector();
            user.setUserName(editUsername.getText().toString());
            user.setUserEmail(editEmail.getText().toString());
            user.setUserPassword(editPassword.getText().toString());
            user.setFirstName(editFirstName.getText().toString());
            user.setLastName(editLastName.getText().toString());
            rdbc.updateUserData(""+user.getUserID(),user);
            updateViews();
            setEditable(false);
        });
        updateViews();
        return view;
    }

    private void updateViews() {
        editUsername.setText(user.getUserName());
        editPassword.setText(user.getUserPassword());
        editEmail.setText(user.getUserEmail());
        editFirstName.setText(user.getFirstName());
        editLastName.setText(user.getLastName());
    }

    private void setEditable(boolean isEditable) {
        editUsername.setEnabled(isEditable);
        editPassword.setEnabled(isEditable);
        editEmail.setEnabled(isEditable);
        editFirstName.setEnabled(isEditable);
        editLastName.setEnabled(isEditable);

        // Show/hide save button based on edit mode
        if (isEditable) {
            uploadImage.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
        } else {
            uploadImage.setVisibility(View.GONE);

            btnEdit.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.GONE);
        }
    }
}

