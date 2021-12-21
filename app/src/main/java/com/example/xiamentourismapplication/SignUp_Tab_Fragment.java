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

public class SignUp_Tab_Fragment extends Fragment {

    EditText username, email, password, repassword;
    Button signUp;
    DatabaseHelper database;

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

        database = new DatabaseHelper(getContext());

//        email set when entering

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_repass = repassword.getText().toString();

                if(txt_username.equals("") || txt_email.equals("") || txt_password.equals("") || txt_repass.equals("")){
                    Toast.makeText(getActivity(), "Please Enter all the fields",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!txt_password.equals(txt_repass))
                    {
                        Toast.makeText(getActivity(), "The passwords not match", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String hashed_password = getMd5Hash(txt_password);
                        if(database.createUser(txt_username, txt_email, hashed_password))
                        {
                            Toast.makeText(getActivity(), "Sign up successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), NavigationActivity.class));
                        }
                        else{
                            Toast.makeText(getActivity(), "Sign up failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
//    hash password with MD5 algorithm
    public String getMd5Hash(String password){
        String md5 = "MD5";
        try {
            MessageDigest digest = MessageDigest.getInstance(md5);
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder hashedPassword = new StringBuilder();

            for(byte adigest: messageDigest){
                String h = Integer.toHexString(0xFF & adigest);
                while (h.length() < 2){
                    h = "0" + h;
                }
                hashedPassword.append(h);

            }
            return hashedPassword.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";

    }
}