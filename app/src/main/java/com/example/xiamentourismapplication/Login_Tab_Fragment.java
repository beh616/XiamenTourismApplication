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

                if (txt_email.equals("") || txt_password.equals("")) {
                    Toast.makeText(getActivity(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else if (!database.checkEmail(txt_email)) {
                    Toast.makeText(getActivity(), "Email not exist in database", Toast.LENGTH_SHORT).show();
                } else if (database.authenticateUser(txt_email, txt_password)) {
                    Toast.makeText(getActivity(), "Sign in successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), NavigationActivity.class));
                } else {
                    Toast.makeText(getActivity(), "Sign in failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}