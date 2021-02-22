package com.example.esp_health_monitor;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class signupFragment extends Fragment {
    private EditText email, name, password, confirm_password;
    private Button create_button;
    private TextView create_txt;
    private FrameLayout frameLayout;
    private String Emailpattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mdatabase;
    private ProgressBar progressBar;

    public signupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        frameLayout = getActivity().findViewById(R.id.register_frame);
        email = view.findViewById(R.id.register_email);
        name = view.findViewById(R.id.register_name);
        password = view.findViewById(R.id.redister_password1);
        confirm_password = view.findViewById(R.id.register_password2);
        create_button = view.findViewById(R.id.create_button);
        create_txt = view.findViewById(R.id.createtxt);
        progressBar = view.findViewById(R.id.create_progress);
        progressBar.setVisibility(View.INVISIBLE);
        create_button.setEnabled(false);
        create_button.setAlpha(0.3f);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
            }
        });
        create_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new signinFragment());
            }
        });
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString())) {
            create_button.setEnabled(true);
            create_button.setAlpha(1f);
        } else {
            create_button.setEnabled(false);
            create_button.setAlpha(0.3f);
        }
    }

    private void create() {
        if (!TextUtils.isEmpty(email.getText().toString()) && email.getText().toString().matches(Emailpattern)) {
            if (!TextUtils.isEmpty(name.getText().toString())) {
                if (!TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_password.getText().toString()) && password.getText().toString().length() >= 8) {
                    if (password.getText().toString().equals(confirm_password.getText().toString())) {
                        progressBar.setVisibility(View.VISIBLE);
                        create_button.setEnabled(false);
                        create_button.setAlpha(0.3f);
                        firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("name", name.getText().toString());
                                    data.put("email", email.getText().toString());
                                    data.put("uid", firebaseAuth.getUid());
                                    mdatabase = FirebaseDatabase.getInstance().getReference();
                                    mdatabase.child("hitman").child(firebaseAuth.getUid()).child("personal").setValue(data);


                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    getContext().startActivity(intent);
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    create_button.setEnabled(true);
                                    create_button.setAlpha(1f);
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Password Not Matching", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "enter password >=8 ", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Enter Name", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}
