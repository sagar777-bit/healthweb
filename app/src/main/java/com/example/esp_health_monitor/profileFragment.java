package com.example.esp_health_monitor;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
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
public class profileFragment extends Fragment {
    private DatabaseReference databaseReference;
    private ImageView profile_image,edit_image,edit_name,edit_phone,edit_birth_date,edit_wifi,edit_height,edit_weight,edit_blood_group;
    private TextView nametxt,nametxt1,phonetxt,birthtxt,wifitxt,heighttxt,weighttxt,blood_groptxt,file_numbers;
    private Button uploadpdf,changebtn,logout;
    private Dialog dialog;
    private EditText changetxt;
    private Uri uri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String key,name,phone,birth,wifi,height,weight,bloodgroup;
    private ProgressBar progressBar;
    private boolean changeWIFI=false;



    public profileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        MainActivity.CURRENTFRAGMNET="PROFILE";
        changeWIFI=false;
        databaseReference = FirebaseDatabase.getInstance().getReference();
      storage=FirebaseStorage.getInstance();
      storageReference = storage.getReference();
        profile_image = view.findViewById(R.id.profile_image);
        edit_image = view.findViewById(R.id.edit_image);
        nametxt1 = view.findViewById(R.id.nametxt1);
        nametxt = view.findViewById(R.id.nametxt);
        edit_name = view.findViewById(R.id.name_edit);
        phonetxt = view.findViewById(R.id.phonetxt);
        edit_phone = view.findViewById(R.id.phone_edit);
        birthtxt = view.findViewById(R.id.birth_datetxt);
        edit_birth_date = view.findViewById(R.id.birth_date_edit);
        wifitxt = view.findViewById(R.id.wifitxt);
        edit_wifi = view.findViewById(R.id.wifi_edit);
        heighttxt = view.findViewById(R.id.heighttxt);
        edit_height = view.findViewById(R.id.height_edit);
        weighttxt = view.findViewById(R.id.weighttxt);
        edit_weight = view.findViewById(R.id.weight_edit);
        blood_groptxt = view.findViewById(R.id.blood_grouptxt);
        edit_blood_group = view.findViewById(R.id.blood_group_edit);
        progressBar = view.findViewById(R.id.progressBar_uploadfiles);
        uploadpdf = view.findViewById(R.id.pdf_button);
        file_numbers = view.findViewById(R.id.file_numbers);
        logout = view.findViewById(R.id.button_logout);
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.profile_edit_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        changetxt = dialog.findViewById(R.id.change_txt);
        changebtn = dialog.findViewById(R.id.button_change);
        progressBar.setVisibility(View.VISIBLE);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
               getActivity().finish();
            }
        });

        final DatabaseReference databaseReference1 = databaseReference.child("hitman").child(FirebaseAuth.getInstance().getUid()).child("personal");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nametxt.setText(snapshot.child("EID").getValue().toString());
                nametxt1.setText("Name : "+snapshot.child("name").getValue().toString());
                phonetxt.setText("Phone : "+snapshot.child("phone").getValue().toString());
                birthtxt.setText("Birth Date : "+snapshot.child("birth_date").getValue().toString());
                wifitxt.setText("WIFI Pass : "+snapshot.child("wifi").getValue().toString());
                heighttxt.setText("Height : "+snapshot.child("height").getValue().toString()+" cm");
                weighttxt.setText("Weight : "+snapshot.child("weight").getValue().toString()+" kg");
                blood_groptxt.setText("Blood Group : "+snapshot.child("blood_group").getValue().toString());

                file_numbers.setText((int) snapshot.child("docs").getChildrenCount()+" Files Uploaded");

                Glide.with(getContext()).load(snapshot.child("profile_image").getValue().toString()).placeholder(R.drawable.ic_person_black_24dp).into(profile_image);
            name=snapshot.child("name").getValue().toString();
            phone=snapshot.child("phone").getValue().toString();
            birth=snapshot.child("birth_date").getValue().toString();
            wifi=snapshot.child("wifi").getValue().toString();
            height=snapshot.child("height").getValue().toString();
            weight=snapshot.child("weight").getValue().toString();
            bloodgroup=snapshot.child("blood_group").getValue().toString();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });
        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit(name);
                key="name";
            }
        });
        edit_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit(phone);
                key="phone";
            }
        });
        edit_birth_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit(birth);
                key="birth_date";
            }
        });
        edit_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit(wifi);
                key = "wifi";
                changeWIFI=true;
            }
        });
        edit_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit(weight);
                key="weight";
            }
        });
        edit_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit(height);
                key="height";
            }
        });
        edit_blood_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit(bloodgroup);
                key = "blood_group";
            }
        });
        uploadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,2);
            }
        });

        changebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Map<String,Object>data1=new HashMap<>();
                data1.put(key,changetxt.getText().toString());
                databaseReference.child("hitman/"+FirebaseAuth.getInstance().getUid()+"/personal").updateChildren(data1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), key+" updated", Toast.LENGTH_SHORT).show();
                        if (changeWIFI){
                            Map<String,Object>map=new HashMap<>();
                            map.put("uid",FirebaseAuth.getInstance().getUid());
                            FirebaseFirestore.getInstance().collection("users_data").document(changetxt.getText().toString()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                    }else {

                                    }


                                }
                            });
                        }
                        dialog.dismiss();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data !=null && data.getData() !=null){
            uri=data.getData();
            profile_image.setImageURI(uri);
            progressBar.setVisibility(View.VISIBLE);
            MainActivity.bottomNavigationView.setVisibility(View.INVISIBLE);

             final StorageReference sr = storageReference.child("profile_image/"+name+"/"+name+".jpg");
            sr.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map<String,Object>data = new HashMap<>();
                            String url = uri.toString();
                            data.put("profile_image",url);
                            databaseReference.child("hitman").child(FirebaseAuth.getInstance().getUid()).child("personal").updateChildren(data);
                            Toast.makeText(getContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed uploading", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                }
            });

        }
        if(requestCode==2 && resultCode==RESULT_OK && data !=null && data.getData() !=null){
            uri=data.getData();
            profile_image.setImageURI(uri);
            progressBar.setVisibility(View.VISIBLE);
            MainActivity.bottomNavigationView.setVisibility(View.INVISIBLE);



            final StorageReference sr = storageReference.child("profile_image/"+name+"/"+ System.currentTimeMillis()+".pdf");
            sr.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map<String,Object>data = new HashMap<>();
                            String url = uri.toString();
                            data.put(""+System.currentTimeMillis(),url);
                            databaseReference.child("hitman").child(FirebaseAuth.getInstance().getUid()).child("personal/docs").updateChildren(data);
                            Toast.makeText(getContext(), "Document Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed uploading", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                }
            });

        }
    }
    private void edit(String string){
        dialog.show();
        changetxt.setText(string);

    }

}
