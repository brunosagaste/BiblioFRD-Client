package  com.bruno.frd.biblio.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.bruno.frd.biblio.data.api.model.Socio;

/**
 * Manejador de preferencias de la sesión del usuario
 */
public class SessionPrefs {

    public static final String PREFS_NAME = "BIBLIO_PREFS";
    public static final String PREF_AFFILIATE_ID = "PREF_USER_ID";
    public static final String PREF_AFFILIATE_NAME = "PREF_USER_NAME";
    public static final String PREF_AFFILIATE_EMAIL = "PREF_AFFILIATE_EMAIL";
    public static final String PREF_AFFILIATE_GENDER = "PREF_AFFILIATE_GENDER";
    public static final String PREF_AFFILIATE_TOKEN = "PREF_AFFILIATE_TOKEN";

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

        mIsLoggedIn = !TextUtils.isEmpty(mPrefs.getString(PREF_AFFILIATE_TOKEN, null));
    }

    public boolean isLoggedIn() {
        return mIsLoggedIn;
    }

    public void saveAffiliate(Socio affiliate) {
        if (affiliate != null) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.clear();
            editor.putString(PREF_AFFILIATE_ID, affiliate.getId());
            editor.putString(PREF_AFFILIATE_NAME, affiliate.getName());
            editor.putString(PREF_AFFILIATE_EMAIL, affiliate.getEmail());
            editor.putString(PREF_AFFILIATE_TOKEN, "Bearer " + affiliate.getToken());
            Log.d("tokenPrefInside", affiliate.getToken());
            editor.apply();

            mIsLoggedIn = true;
        }
    }

    public void logOut(){
        mIsLoggedIn = false;
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.clear();
        editor.putString(PREF_AFFILIATE_ID, null);
        editor.putString(PREF_AFFILIATE_NAME, null);
        editor.putString(PREF_AFFILIATE_EMAIL, null);
        editor.putString(PREF_AFFILIATE_GENDER, null);
        editor.putString(PREF_AFFILIATE_TOKEN, null);
        editor.apply();
        editor.commit();
    }

    public String getID(){
        return mPrefs.getString(PREF_AFFILIATE_ID, null);
    }
    public String getToken(){
        return mPrefs.getString(PREF_AFFILIATE_TOKEN, null);
    }
    public String getName(){
        return mPrefs.getString(PREF_AFFILIATE_NAME, null);
    }
}
