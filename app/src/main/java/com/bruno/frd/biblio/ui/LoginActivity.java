package  com.bruno.frd.biblio.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bruno.frd.biblio.R;
import com.bruno.frd.biblio.data.api.BiblioApi;
import com.bruno.frd.biblio.data.api.model.ApiError;
import com.bruno.frd.biblio.data.api.model.LoginBody;
import com.bruno.frd.biblio.data.api.model.User;
import com.bruno.frd.biblio.data.prefs.SessionPrefs;
import com.danimahardhika.cafebar.CafeBar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Screen de login para usuario.
 */
public class LoginActivity extends AppCompatActivity {

    private Retrofit mRestAdapter;
    private BiblioApi mSaludMockApi;

    // UI references.
    private ImageView mLogoView;
    private EditText mUserIdView;
    private EditText mPasswordView;
    private TextInputLayout mFloatLabelUserId;
    private TextInputLayout mFloatLabelPassword;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Crear adaptador Retrofit
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(BiblioApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear conexión a la API de BiblioFRD
        mSaludMockApi = mRestAdapter.create(BiblioApi.class);

        mLogoView = (ImageView) findViewById(R.id.image_logo);
        mUserIdView = (EditText) findViewById(R.id.user_id);
        mPasswordView = (EditText) findViewById(R.id.password);
        mFloatLabelUserId = (TextInputLayout) findViewById(R.id.float_label_user_id);
        mFloatLabelPassword = (TextInputLayout) findViewById(R.id.float_label_password);
        Button mSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        // Setup
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.user_id || id == EditorInfo.IME_NULL) {
                    if (!isOnline()) {
                        showLoginError(getString(R.string.error_network));
                        return false;
                    }
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOnline()) {
                    showLoginError(getString(R.string.error_network));
                    return;
                }
                attemptLogin();

            }
        });
    }

    private void attemptLogin() {

        // Reset errors.
        mFloatLabelUserId.setError(null);
        mFloatLabelPassword.setError(null);

        // Store values at the time of the login attempt.
        String userId = mUserIdView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mFloatLabelPassword.setError(getString(R.string.error_field_required));
            focusView = mFloatLabelPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mFloatLabelPassword.setError(getString(R.string.error_invalid_password));
            focusView = mFloatLabelPassword;
            cancel = true;
        }

        // Verificar si el ID tiene contenido.
        if (TextUtils.isEmpty(userId)) {
            mFloatLabelUserId.setError(getString(R.string.error_field_required));
            focusView = mFloatLabelUserId;
            cancel = true;
        } else if (!isUserIdValid(userId)) {
            mFloatLabelUserId.setError(getString(R.string.error_invalid_user_id));
            focusView = mFloatLabelUserId;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Mostrar el indicador de carga y luego iniciar la petición asíncrona.
            showProgress(true);

            Call<User> loginCall = mSaludMockApi.login(new LoginBody(userId, password));
            loginCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    // Mostrar progreso
                    showProgress(false);

                    try {
                        // Procesar errores
                        if (!response.isSuccessful()) {
                            String error = "Ha ocurrido un error. Contacte al administrador";
                            if (response.errorBody()
                                    .contentType()
                                    .subtype()
                                    .equals("json")) {
                                ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                                mFloatLabelUserId.setError(apiError.getMessage());
                                mFloatLabelUserId.requestFocus();

                                error = apiError.getMessage();
                                Log.d("LoginActivity", apiError.getDeveloperMessage());
                            } else {
                                try {
                                    // Reportar causas de error no relacionado con la API
                                    Log.d("LoginActivity", response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            //showLoginError(error);
                            return;
                        }
                        // Guardar usuario en preferencias
                        SessionPrefs.get(LoginActivity.this).saveUser(response.body());
                        // Log.d("tokenResponse", response.body().getToken());
                        //Log.d("tokenPref", SessionPrefs.get(LoginActivity.this).getToken());

                        // Ir a préstamos
                        showMainScreen();
                    }
                    catch(Exception e) {
                        showLoginError("HTTP Error: " + String.valueOf(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    showProgress(false);
                    showLoginError(t.getMessage());
                }
            });
        }
    }

    private boolean isUserIdValid(String userId) {
        return userId.length() > 0;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 0;
    }

    private void showProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

        int visibility = show ? View.GONE : View.VISIBLE;
        mLogoView.setVisibility(visibility);
        mLoginFormView.setVisibility(visibility);
    }

    private void showMainScreen() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    private void showLoginError(String error) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        CafeBar.builder(linearLayout.getContext())
                .floating(true)
                .content(error)
                .to(linearLayout)
                .neutralText("Aceptar")
                .duration(CafeBar.Duration.LONG)
                .show();
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

}

