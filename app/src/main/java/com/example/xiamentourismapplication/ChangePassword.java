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

public class ChangePassword extends Fragment {

    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "email";

    private String id;
    private String email;

    public ChangePassword() {
        // Required empty public constructor
    }

    public static ChangePassword newInstance(String id, String email) {
        ChangePassword fragment = new ChangePassword();
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
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        ImageView back = view.findViewById(R.id.btn_back_password);
        EditText password = view.findViewById(R.id.et_password_changePassword);
        EditText newPassword = view.findViewById(R.id.et_newPassword_changePassword);
        EditText reNewPassword = view.findViewById(R.id.et_reNewPassword_changePassword);

        Button changePassword = view.findViewById(R.id.btn_changePassword);
        UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper(getContext());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_password = password.getText().toString();
                String txt_newPassword = newPassword.getText().toString();
                String txt_reNewPassword = reNewPassword.getText().toString();

                if(txt_password.equals("") || txt_newPassword.equals("") || txt_reNewPassword.equals(""))
                {
                    Toast.makeText(getContext(), "Please enter all the field", Toast.LENGTH_SHORT).show();
                }
                else if(!txt_newPassword.equals(txt_reNewPassword))
                {
                    Toast.makeText(getContext(), "Passwords are not match", Toast.LENGTH_SHORT).show();
                }
                else{
                    String hashedPassword = new Md5Hash().getMd5Hash(txt_password);
                    boolean result = userDatabaseHelper.authenticateUser(email, hashedPassword);
                    if(result)
                    {
                        String hashedNewPassword = new Md5Hash().getMd5Hash(txt_newPassword);
                        int updateResult = userDatabaseHelper.changePassword(id, hashedNewPassword);
                        if(updateResult > 0)
                        {
                            Toast.makeText(getContext(), "Password updated", Toast.LENGTH_SHORT).show();
                            getParentFragmentManager().popBackStack();
                        }
                    }
                    else{
                        Toast.makeText(getContext(), "Password incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }
}