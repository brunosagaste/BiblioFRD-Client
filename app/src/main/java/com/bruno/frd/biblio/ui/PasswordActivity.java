package com.bruno.frd.biblio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bruno.frd.biblio.R;
import com.bruno.frd.biblio.data.api.BiblioApi;
import com.bruno.frd.biblio.data.api.model.ApiError;
import com.bruno.frd.biblio.data.api.model.ApiMessageResponse;
import com.bruno.frd.biblio.data.api.model.PasswordBody;
import com.bruno.frd.biblio.data.api.model.PasswordResponse;
import com.bruno.frd.biblio.data.prefs.SessionPrefs;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PasswordActivity extends AppCompatActivity {
    public static final String TAG = PasswordActivity.class.getSimpleName();
    private Retrofit mRestAdapter;
    private BiblioApi mBiblioApi;
    EditText oldpw;
    EditText newpw;
    EditText confpw;
    TextInputLayout oldpw_layout;
    TextInputLayout newpw_layout;
    TextInputLayout confpw_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.pw_toolbar);
        mActionBarToolbar.setTitle("Cambio de contraseña");
        setSupportActionBar(mActionBarToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Creamos el adaptador de Retrofit
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(BiblioApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Creamos conexión a la API de la app
        mBiblioApi = mRestAdapter.create(BiblioApi.class);

        oldpw_layout = findViewById(R.id.float_label_actual_password);
        newpw_layout = findViewById(R.id.float_label_new_password);
        confpw_layout = findViewById(R.id.float_label_confirm_password);
        oldpw = findViewById(R.id.oldpw);
        newpw = findViewById(R.id.new_pw);
        confpw = findViewById(R.id.confirm_pw);
        Button mPassButton = findViewById(R.id.password_change_button);
        mPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPasswordRequest(oldpw.getText().toString(), newpw.getText().toString(), confpw.getText().toString());
            }
        });
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        showProfileScreen();
        return true;
    }

    private void showProfileScreen() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void sendPasswordRequest(String oldpw, final String newpw, final String confirmpw) {

        String token = SessionPrefs.get(this).getToken();
        String id = SessionPrefs.get(this).getID();
        PasswordBody pwbody = new PasswordBody(oldpw, newpw, confirmpw);
        // Realizar petición HTTP
        Call<PasswordResponse> call = mBiblioApi.sendPassword(token, id, pwbody);
        call.enqueue(new Callback<PasswordResponse>() {
            @Override
            public void onResponse(Call<PasswordResponse> call,
                                   Response<PasswordResponse> response) {
                try {
                    if (!response.isSuccessful()) {
                        // Procesar error de API
                        String error = "Ha ocurrido un error. Contacte al administrador";
                        String field = null;
                        if (response.errorBody()
                                .contentType()
                                .subtype()
                                .equals("json")) {
                            ApiError apiError = ApiError.fromResponseBody(response.errorBody());

                            error = apiError.getMessage();
                            field = apiError.getDeveloperMessage();
                            if (field.equals("oldpw")) {
                                oldpw_layout.setError(error);
                                oldpw_layout.requestFocus();
                                newpw_layout.setErrorEnabled(false);
                                confpw_layout.setErrorEnabled(false);
                            }
                            if (field.equals("newpw")) {
                                newpw_layout.setError(error);
                                newpw_layout.requestFocus();
                                oldpw_layout.setErrorEnabled(false);
                                confpw_layout.setErrorEnabled(false);
                            }
                            if (field.equals("confpw")) {
                                confpw_layout.setError(error);
                                confpw_layout.requestFocus();
                                oldpw_layout.setErrorEnabled(false);
                                newpw_layout.setErrorEnabled(false);
                            }
                            Log.d(TAG, error);
                        } else {
                            // Reportar causas de error no relacionado con la API
                            try {
                                Log.d(TAG, response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (Objects.equals(field, "wrongtoken")) {
                            showProfileScreen();
                        }
                        return;
                    }

                    Log.d(TAG + " PassToken ", response.body().getToken());
                    Log.d(TAG, response.body().getMessage());
                    SessionPrefs.get(PasswordActivity.this).setToken(response.body().getToken());
                    Intent item_intent = new Intent(PasswordActivity.this, ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DATA", response.body().getMessage());
                    item_intent.putExtras(bundle);
                    startActivity(item_intent);
                }
                catch(Exception e) {
                    Intent item_intent = new Intent(PasswordActivity.this, ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DATA", "HTTP Error: " + String.valueOf(response.code()));
                    item_intent.putExtras(bundle);
                    startActivity(item_intent);
                }
            }

            @Override
            public void onFailure(Call<PasswordResponse> call, Throwable t) {
                Log.d(TAG, "Petición rechazada:" + t.getMessage());
                //showErrorMessage("No hay conexión a internet");
            }
        });
    }
}