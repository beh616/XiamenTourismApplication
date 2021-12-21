package com.example.xiamentourismapplication;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class Login_Tab_Fragment extends Fragment {

    EditText email, password;
    Button login;
    DatabaseHelper database;

    public Login_Tab_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login__tab_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email = view.findViewById(R.id.et_email_signIn);
        password = view.findViewById(R.id.et_password_signIn);
        login = view.findViewById(R.id.btn_login);

        database = new DatabaseHelper(getContext());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String hashed_password = getMd5Hash(txt_password);
                if (txt_email.equals("") || txt_password.equals("")) {
                    Toast.makeText(getActivity(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else if (!database.checkEmail(txt_email)) {
                    Toast.makeText(getActivity(), "Email not exist in database", Toast.LENGTH_SHORT).show();
                } else if (database.authenticateUser(txt_email, hashed_password)) {
                    Toast.makeText(getActivity(), "Sign in successfully", Toast.LENGTH_SHORT).show();

//                  Create session
                    SessionManager sessionManager = new SessionManager(getContext());
//                    get username from database
                    String username = database.getUsername(txt_email);
                    sessionManager.createLoginSession(username,txt_email);
                    startActivity(new Intent(getActivity(), NavigationActivity.class));
                } else {
                    Toast.makeText(getActivity(), "Sign in failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

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