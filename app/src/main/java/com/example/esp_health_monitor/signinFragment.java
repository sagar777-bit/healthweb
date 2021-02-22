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

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class signinFragment extends Fragment {
    private FrameLayout fragmnetlayout;
    private EditText email_text, password_text;
    private Button signin_button;
    private TextView create_account;
    private String Emailpattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mdatabase;
    private ProgressBar progressBar;

    public signinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        fragmnetlayout = getActivity().findViewById(R.id.register_frame);
        email_text = view.findViewById(R.id.email_text);
        password_text = view.findViewById(R.id.password_text);
        signin_button = view.findViewById(R.id.login_button);
        create_account = view.findViewById(R.id.create_account_text);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        signin_button.setAlpha(0.3f);
        signin_button.setEnabled(false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailandPassword();
            }
        });
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new signupFragment());
            }
        });
        email_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void checkInput() {
        if (!TextUtils.isEmpty(email_text.getText().toString()) && !TextUtils.isEmpty(password_text.getText().toString())) {
            signin_button.setEnabled(true);
            signin_button.setAlpha(1f);
        } else {
            signin_button.setEnabled(false);
            signin_button.setAlpha(0.3f);
        }
    }

    private void checkEmailandPassword() {
        if (email_text.getText().toString().matches(Emailpattern)) {
            if (password_text.getText().toString().length() >= 8) {
                signin_button.setEnabled(false);
                signin_button.setAlpha(0.3f);
                progressBar.setVisibility(View.VISIBLE);
                signin();
            } else {
                Toast.makeText(getContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "incorrect password", Toast.LENGTH_SHORT).show();
        }
    }

    private void signin() {
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email_text.getText().toString(), password_text.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mainActivity();
                    mdatabase = FirebaseDatabase.getInstance().getReference();


                } else {
                    signin_button.setEnabled(true);
                    signin_button.setAlpha(1f);
                    progressBar.setVisibility(View.INVISIBLE);
                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void mainActivity(){
        Intent intent = new Intent(getContext(),MainActivity.class);
        getContext().startActivity(intent);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(fragmnetlayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}
