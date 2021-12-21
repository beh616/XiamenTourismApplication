package com.example.xiamentourismapplication;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
//    Variable Declaration
    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String isLogin = "IsloggedIn";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";

    SessionManager(Context _context){
        context = _context;
        usersSession = context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        editor = usersSession.edit();
    }

    public void createLoginSession(String username, String email){
        editor.putBoolean(isLogin, true);
//         put user data in SharedPreference
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);

        editor.commit();
    }

    public HashMap<String, String> getUserDetailFromSession(){
        HashMap<String, String> userData = new HashMap<String, String>();
//        get data from SharedPreference and put inside the hash map
        userData.put(KEY_USERNAME, usersSession.getString(KEY_USERNAME, null));
        userData.put(KEY_EMAIL, usersSession.getString(KEY_EMAIL, null));

        return userData;
    }

    public boolean checkLogin(){
        if(usersSession.getBoolean(isLogin, true)){
            return true;
        }
        else{
            return false;
        }
    }

    public void logoutUser(){
//        sign out from the session
        editor.putBoolean(isLogin, false);
        editor.clear();
        editor.commit();
    }
}
