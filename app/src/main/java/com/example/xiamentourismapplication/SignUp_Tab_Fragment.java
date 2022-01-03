package com.example.xiamentourismapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp_Tab_Fragment extends Fragment {

    EditText username, email, password, repassword;
    Button signUp;
    UserDatabaseHelper database;

    public SignUp_Tab_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up__tab_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username = view.findViewById(R.id.et_username_signUp);
        email = view.findViewById(R.id.et_email_signUp);
        password = view.findViewById(R.id.et_password_signUp);
        repassword = view.findViewById(R.id.et_repass_signUp);
        signUp = view.findViewById(R.id.btn_signUp);

        database = new UserDatabaseHelper(getContext());

//        email set when entering

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_repass = repassword.getText().toString();
                final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(txt_email);

                if(txt_username.equals("") || txt_email.equals("") || txt_password.equals("") || txt_repass.equals("")){
                    Toast.makeText(getActivity(), "Please Enter all the fields",Toast.LENGTH_SHORT).show();
                }
                else if(!matcher.matches())
                {
                    Toast.makeText(getActivity(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
                }
                else if(!txt_password.equals(txt_repass))
                {
                    Toast.makeText(getActivity(), "The passwords not match", Toast.LENGTH_SHORT).show();
                }
                else if(database.checkEmail(txt_email)) {
                    Toast.makeText(getActivity(), "The email have been used before", Toast.LENGTH_SHORT).show();
                }
                else{
                        String hashed_password = new Md5Hash().getMd5Hash(txt_password);
                        if(database.createUser(txt_username, txt_email, hashed_password))
                        {
                            SessionManager sessionManager = new SessionManager(getContext());
                            Toast.makeText(getActivity(), "Sign up successfully", Toast.LENGTH_SHORT).show();
                            int id = database.getUserId(txt_email);
                            sessionManager.createLoginSession(id,txt_username,txt_email);
                            startActivity(new Intent(getActivity(), NavigationActivity.class));
                            getActivity().finish();
                        }
                        else{
                            Toast.makeText(getActivity(), "Sign up failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        });
    }
}