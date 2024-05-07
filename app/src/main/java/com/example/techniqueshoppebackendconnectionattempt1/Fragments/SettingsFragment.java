package com.example.techniqueshoppebackendconnectionattempt1.Fragments;

import static android.app.Activity.RESULT_OK;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.PresignedUrlResponse;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.RetrofitDBConnector;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserInfo;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserInfoSingleton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class SettingsFragment extends Fragment {
    private TextInputLayout layoutUsername, layoutPassword, layoutEmail, layoutFirstName, layoutLastName;
    private TextInputEditText editUsername, editPassword, editEmail, editFirstName, editLastName;
    private MaterialButton btnEdit, btnSave;

    private ImageView userImage;



    private MaterialButton uploadImage;

    private UserInfo.MyData user;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;


    private RetrofitDBConnector rdbc;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
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

        rdbc = new RetrofitDBConnector();

        setEditable(false);
        uploadImage.setOnClickListener(v -> {
            Log.d("d","HERE");
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                openImagePicker();
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
            Log.d("userpathSave",UserInfoSingleton.getInstance().getDataList().get(0).getUserImagePath()+"");
            updateViews();
            setEditable(false);
        });
        updateViews();

        rdbc.downloadFile(UserInfoSingleton.getInstance().getDataList().get(0).getUserImagePath(), new RetrofitDBConnector.DownloadCallback() {
            @Override
            public void onSuccess(String fileContent) {
                Log.d("url","m"+fileContent);
                Picasso.get()
                        .load(fileContent)
                        .placeholder(R.drawable.loading)
                        .transform(new CropCircleTransformation())
                        .into(userImage);

            }

            @Override
            public void onFailure(String error) {
                // Handle download failure
                Log.e("Download", "Download failed: " + error);
            }
        });

        return view;
    }
    private void openImagePicker() {
        Log.d("I am here","This is openImagePicker");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open image picker
                openImagePicker();
            } else {
                Toast.makeText(requireContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String uuid;
    private String urlPresigned;

    private File imageFile;

    private InputStream inputStream;

    private Uri resultUri;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK )
        {
            Log.d("here","here");
            Uri imageUri = data.getData();

            if (imageUri != null){
                Intent intent = new Intent(getActivity(),CropperActivity.class);
                intent.putExtra("DATA",imageUri.toString());
                startActivityForResult(intent,101001);
            }

        }else if (requestCode == 101001 && resultCode == -1 && data != null) {
            // Handle cropped image result

            String result = data.getStringExtra("RESULT");
            resultUri = null;
            if (result != null) {
                resultUri = Uri.parse(result);


            }

            Picasso.get()
                    .load(resultUri)
                    .placeholder(R.drawable.loading)
                    .transform(new CropCircleTransformation())
                    .into(userImage);
            inputStream = convertUriToInputStream(resultUri);
            if (inputStream != null){
                rdbc.getPresigned(new RetrofitDBConnector.UploadCallback() {
                    @Override
                    public void onUploadSuccess(PresignedUrlResponse uploadObject) throws IOException {
                        uuid = uploadObject.getUuid();
                        urlPresigned = uploadObject.getUrl();
                        Log.d("uuid",uuid);
                        Log.d("urlPresigned",urlPresigned);
//                        rdbc.uploadWithURI(inputStream, urlPresigned, "image/*");
                        rdbc.uploadFileNew(getContext(),urlPresigned,resultUri);
                        user.setUserImagePath(uuid);

                    }

                    @Override
                    public void onUploadFailure(String error) {
                        Log.d("imageError",error);
                    }

                });

            }

        }



    }
    private InputStream convertUriToInputStream(Uri uri) {
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}