package com.example.esp_health_monitor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class details_formFragment extends Fragment {
    private EditText EID,phone,wifi,birth_date,age,height,bood_group,weight;
    private Button submit_form,upload_image_button;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar_data;
    private FrameLayout frameLayout;
    private Uri image_url;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ImageView imageView;
    public details_formFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details_form, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        MainActivity.CURRENTFRAGMNET="DETAIL";
        EID = view.findViewById(R.id.EID);
        phone = view.findViewById(R.id.phone);
        wifi = view.findViewById(R.id.wifi);
        birth_date = view.findViewById(R.id.birth_date);
        age = view.findViewById(R.id.age);
        height = view.findViewById(R.id.height);
        weight = view.findViewById(R.id.weight);
        bood_group = view.findViewById(R.id.bood_group);
        submit_form = view.findViewById(R.id.submit_form_button);
        progressBar_data = view.findViewById(R.id.progressBar_form);
        upload_image_button = view.findViewById(R.id.upload_image_button);
        imageView = view.findViewById(R.id.profileimageView);
        frameLayout = getActivity().findViewById(R.id.main_fram_layout);
        progressBar_data.setVisibility(View.INVISIBLE);
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();




        submit_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
        upload_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }


        });


        return view;
    }
    private  void submitForm(){
        if(!TextUtils.isEmpty(EID.getText().toString())){
            if(!TextUtils.isEmpty(phone.getText().toString())){
                if(!TextUtils.isEmpty(wifi.getText().toString())){
                    if(!TextUtils.isEmpty(birth_date.getText().toString())){
                        if(!TextUtils.isEmpty(age.getText().toString())){
                            if(!TextUtils.isEmpty(height.getText().toString())){
                                if(!TextUtils.isEmpty(weight.getText().toString())){
                                    if(!TextUtils.isEmpty(bood_group.getText().toString())){
                                        if(image_url !=null){

                                            progressBar_data.setVisibility(View.VISIBLE);
                                        submit_form.setEnabled(false);
                                        submit_form.setAlpha(0.3f);
                                            upload();

                                        }else {
                                            Toast.makeText(getContext(), "Select Profile Image", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Toast.makeText(getContext(), "Blood Group Required", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(getContext(), "Weight Required", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getContext(), "Height Required", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getContext(), "Age Required", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getContext(), "Birth Date Required", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "WIFI password Required", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getContext(), "Phone Required", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getContext(), "EID Required", Toast.LENGTH_SHORT).show();
        }
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
    private void uploadImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data !=null && data.getData() !=null){
            image_url=data.getData();
           imageView.setImageURI(image_url);

        }
    }
    private void upload(){
        final String random_string= UUID.randomUUID().toString();
        final StorageReference sr = storageReference.child("profile_image/"+random_string);
        sr.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url = uri.toString();
                    MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                    Map<String,Object>form_data = new HashMap<>();
                    final Map<String,Object>users = new HashMap<>();
                    form_data.put("EID",EID.getText().toString());
                    form_data.put("phone",phone.getText().toString());
                    form_data.put("wifi",wifi.getText().toString());
                    form_data.put("birth_date",birth_date.getText().toString());
                    form_data.put("age",age.getText().toString());
                    form_data.put("height",height.getText().toString());
                    form_data.put("weight",weight.getText().toString());
                    form_data.put("blood_group",bood_group.getText().toString());
                    form_data.put("profile_image",url);
                    form_data.put("token",FirebaseInstanceId.getInstance().getToken());
                    users.put("uid",FirebaseAuth.getInstance().getUid());
                    users.put("token",FirebaseInstanceId.getInstance().getToken());
                    databaseReference = FirebaseDatabase.getInstance().getReference("hitman").child(firebaseAuth.getUid());
                    databaseReference.child("personal").updateChildren(form_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                DatabaseReference databaseReference_user = FirebaseDatabase.getInstance().getReference("users");
                                databaseReference_user.child(wifi.getText().toString()).setValue(users);

                                            Toast.makeText(getContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                            progressBar_data.setVisibility(View.INVISIBLE);
                                            setFragment(new homeFragment());


                            }else {
                                progressBar_data.setVisibility(View.INVISIBLE);
                                submit_form.setEnabled(true);
                                submit_form.setAlpha(1f);
                                String error = task.getException().getMessage();
                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Image Uploading Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
