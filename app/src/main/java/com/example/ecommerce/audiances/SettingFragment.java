package com.example.ecommerce.audiances;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.example.ecommerce.databinding.FragmentSettingBinding;
import com.example.ecommerce.prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {

    FragmentSettingBinding binding;
    private Uri imageUri;
    String myUrl = "";
    private StorageTask uploadTask;
    private static final int GALLERYPICK = 1;
    boolean checker = false;
    StorageReference storageReference;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSettingBinding.inflate(getLayoutInflater());

        storageReference = FirebaseStorage.getInstance().getReference().child("Profile pictures");
        binding.settingsProfileImage.setOnClickListener(this);
        binding.saveChanges.setOnClickListener(this);
        binding.ignoreChanges.setOnClickListener(this);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.settings_profile_image:{
                checker =true;
                // for fragment (DO NOT use `getActivity()`)
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .start(getContext(), this);
                break;
            }
            case R.id.save_changes:{
                if (checker){
                    userInfoSaved();
                }else {
                    updateOnlyUserInfo();
                }

                break;
            }
            case R.id.ignore_changes:{

                break;
            }
        }

    }
    private void userInfoSaved() {
        uploadImage();
    }
    private void updateOnlyUserInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("name", binding.settingsFullName.getText().toString());
        userMap. put("address", binding.settingsAddress.getText().toString());
        userMap. put("phoneOrder", binding.settingsPhoneNumber.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

        Toast.makeText(getActivity(), "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            binding.settingsProfileImage.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(getActivity(), "Error, Try Again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage()
    {

        if (imageUri != null)
        {
            final StorageReference fileRef = storageReference
                    .child(Prevalent.currentOnlineUser.getPhone() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                Uri downloadUrl = task.getResult();
                                myUrl = downloadUrl.toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap. put("name", binding.settingsFullName.getText().toString());
                                userMap. put("address", binding.settingsAddress.getText().toString());
                                userMap. put("phoneOrder", binding.settingsPhoneNumber.getText().toString());
                                userMap. put("image", myUrl);
                                ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

                                Toast.makeText(getActivity(), "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Error.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(getActivity(), "image is not selected.", Toast.LENGTH_SHORT).show();
        }
    }

}
