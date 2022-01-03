package com.example.xiamentourismapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UpdateEmail extends Fragment {

    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "email";

    private String id;
    private String email;

    public UpdateEmail() {
        // Required empty public constructor
    }

    public static UpdateEmail newInstance(String id, String email) {
        UpdateEmail fragment = new UpdateEmail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, id);
        args.putString(ARG_PARAM2, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_PARAM1);
            email = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_email, container, false);
        EditText currentEmail = view.findViewById(R.id.et_email_emailUpdate);
        currentEmail.setText(email);

        ImageView back = view.findViewById(R.id.btn_back_email);
        UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper(getContext());
        EditText password = view.findViewById(R.id.et_password_emailUpdate);
        EditText newEmail = view.findViewById(R.id.et_newEmail_emailUpdate);
        Button updateEmail = view.findViewById(R.id.btn_updateEmail);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        updateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_password = password.getText().toString();
                String txt_newEmail = newEmail.getText().toString();
                final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(txt_newEmail);

                if (txt_password.equals("") || txt_newEmail.equals(""))
                {
                    Toast.makeText(getContext(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }
                else if(txt_newEmail.equals(email))
                {
                    Toast.makeText(getContext(), "Please enter a new email", Toast.LENGTH_SHORT).show();
                }
                else if(!matcher.matches()){
                    Toast.makeText(getContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
                }
                else{
                    String hashedPassword = new Md5Hash().getMd5Hash(txt_password);
                    boolean result = userDatabaseHelper.authenticateUser(email, hashedPassword);
                    if(result){
                        boolean emailExisted = userDatabaseHelper.checkEmail(txt_newEmail);
                        if(!emailExisted){
                            int updateResult = userDatabaseHelper.updateEmail(id, txt_newEmail);
                            if(updateResult > 0){
                                Toast.makeText(getContext(), "The email updated", Toast.LENGTH_LONG).show();
                                SessionManager sessionManager = new SessionManager(getContext());
                                sessionManager.updateEmail(txt_newEmail);
                                getParentFragmentManager().popBackStack();
                            }
                        }
                        else {
                            Toast.makeText(getContext(), "Email has been used before",Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(getContext(), "The password incorrect", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


        return view;
    }
}