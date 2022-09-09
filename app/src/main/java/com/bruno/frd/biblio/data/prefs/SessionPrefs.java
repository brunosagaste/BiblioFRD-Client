package  com.bruno.frd.biblio.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.bruno.frd.biblio.data.api.model.User;

/**
 * Manejador de preferencias de la sesión del usuario
 */
public class SessionPrefs {

    public static final String PREFS_NAME = "BIBLIO_PREFS";
    public static final String PREF_USER_ID = "PREF_USER_ID";
    public static final String PREF_USER_NAME = "PREF_USER_NAME";
    public static final String PREF_USER_LASTNAME = "PREF_USER_LASTNAME";
    public static final String PREF_USER_ADDRESS = "PREF_USER_ADDRESS";
    public static final String PREF_USER_CITY = "PREF_USER_CITY";
    public static final String PREF_USER_PHONE = "PREF_USER_PHONE";
    public static final String PREF_USER_FILE = "PREF_USER_FILE";
    public static final String PREF_USER_DNI = "PREF_USER_DNI";
    public static final String PREF_USER_TOKEN = "PREF_USER_TOKEN";
    public static final String PREF_USER_MAIL = "PREF_USER_MAIL";

    private final SharedPreferences mPrefs;

    private boolean mIsLoggedIn = false;

    private static SessionPrefs INSTANCE;

    public static SessionPrefs get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SessionPrefs(context);
        }
        return INSTANCE;
    }

    private SessionPrefs(Context context) {
        mPrefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        mIsLoggedIn = !TextUtils.isEmpty(mPrefs.getString(PREF_USER_TOKEN, null));
    }

    public boolean isLoggedIn() {
        return mIsLoggedIn;
    }

    public void saveUser(User user) {
        if (user != null) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.clear();
            editor.putString(PREF_USER_ID, user.getId());
            editor.putString(PREF_USER_NAME, user.getName());
            editor.putString(PREF_USER_LASTNAME, user.getLastName());
            editor.putString(PREF_USER_ADDRESS, user.getAddress());
            editor.putString(PREF_USER_CITY, user.getCity());
            editor.putString(PREF_USER_PHONE, user.getPhone());
            editor.putString(PREF_USER_FILE, user.getFile());
            editor.putString(PREF_USER_DNI, user.getDni());
            editor.putString(PREF_USER_MAIL, user.getMail());
            editor.putString(PREF_USER_TOKEN, user.getToken());
            //Log.d("tokenPrefInside", affiliate.getToken());
            editor.apply();

            mIsLoggedIn = true;
        }
    }

    public void logOut(){
        mIsLoggedIn = false;
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.clear();
        editor.putString(PREF_USER_ID, null);
        editor.putString(PREF_USER_NAME, null);
        editor.putString(PREF_USER_LASTNAME, null);
        editor.putString(PREF_USER_ADDRESS, null);
        editor.putString(PREF_USER_CITY, null);
        editor.putString(PREF_USER_PHONE, null);
        editor.putString(PREF_USER_FILE, null);
        editor.putString(PREF_USER_DNI, null);
        editor.putString(PREF_USER_MAIL, null);
        editor.putString(PREF_USER_TOKEN, null);
        editor.apply();
    }

    public String getID(){
        return mPrefs.getString(PREF_USER_ID, null);
    }
    public String getToken(){
        return mPrefs.getString(PREF_USER_TOKEN, null);
    }
    public String getName(){
        return mPrefs.getString(PREF_USER_NAME, null);
    }
    public String getLastName(){
        return mPrefs.getString(PREF_USER_LASTNAME, null);
    }
    public String getAddress(){
        return mPrefs.getString(PREF_USER_ADDRESS, null);
    }
    public String getCity(){
        return mPrefs.getString(PREF_USER_CITY, null);
    }
    public String getPhone(){
        return mPrefs.getString(PREF_USER_PHONE, null);
    }
    public String getDni(){
        return mPrefs.getString(PREF_USER_DNI, null);
    }
    public String getMail(){
        return mPrefs.getString(PREF_USER_MAIL, null);
    }
    public String getFile(){
        return mPrefs.getString(PREF_USER_FILE, null);
    }

    public void setToken(String token) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_USER_TOKEN, token);
        editor.apply();
    }
}
