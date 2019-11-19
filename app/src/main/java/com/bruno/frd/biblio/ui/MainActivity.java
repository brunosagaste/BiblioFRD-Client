package com.bruno.frd.biblio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bruno.frd.biblio.R;
import com.bruno.frd.biblio.data.api.BiblioApi;
import com.bruno.frd.biblio.data.api.model.ApiError;
import com.bruno.frd.biblio.data.api.model.ApiMessageResponse;
import com.bruno.frd.biblio.data.api.model.ApiResponsePrestamos;
import com.bruno.frd.biblio.data.api.model.PrestamosDisplayList;
import com.bruno.frd.biblio.data.prefs.SessionPrefs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int STATUS_FILTER_DEFAULT_VALUE = 0;

    private static final int REQUEST_ADD_APPOINMENT = 1;

    private Retrofit mRestAdapter;
    private BiblioApi mBiblioApi;

    private RecyclerView mAppointmentsList;
    private PrestamosAdapter mPrestamosAdapter;
    private View mEmptyStateContainer;
    private Spinner mStatusFilterSpinner;
    private FloatingActionButton mFab;

    //Spinner mStatusFilterSpinner = (Spinner) findViewById(R.id.toolbar_spinner);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Redirección al Login
        if (!SessionPrefs.get(this).isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remover título de la action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mStatusFilterSpinner = (Spinner) findViewById(R.id.toolbar_spinner);
        mStatusFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Ejecutar filtro de citas médicas
                String status = parent.getItemAtPosition(position).toString();
                loadAppointments(status);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> statusFilterAdapter =
                new ArrayAdapter<>(
                        toolbar.getContext(),
                        android.R.layout.simple_spinner_item,
                        PrestamosDisplayList.STATES_VALUES);
        mStatusFilterSpinner.setAdapter(statusFilterAdapter);
        statusFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mAppointmentsList = (RecyclerView) findViewById(R.id.list_appointments);
        mPrestamosAdapter = new PrestamosAdapter(this, new ArrayList<PrestamosDisplayList>(0));
        mPrestamosAdapter.setOnItemClickListener(new PrestamosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PrestamosDisplayList clickedAppointment) {
                // TODO: Codificar acciones de click en items
            }

            @Override
            public void onRenewBook(PrestamosDisplayList copy) {
                renewBook(copy.getCopyid());
            }

        });

        //setContentView(R.layout.content_main);
        //TextView textView = (TextView) findViewById(R.id.user_id);
        //textView.setText(SessionPrefs.get(this).getID());

        Log.d("TokenApp", SessionPrefs.get(this).getToken());

        mAppointmentsList.setAdapter(mPrestamosAdapter);

        mEmptyStateContainer = findViewById(R.id.empty_state_container);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showAddAppointment();
            }
        });


        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Pedir al servidor información reciente
                mStatusFilterSpinner.setSelection(STATUS_FILTER_DEFAULT_VALUE);
                loadAppointments(getCurrentState());
            }
        });

        // Crear adaptador Retrofit
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(BiblioApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Crear conexión a la API de SaludMock
        mBiblioApi = mRestAdapter.create(BiblioApi.class);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_appointments, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SessionPrefs.get(this).logOut();
            startActivity(new Intent(this, LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAppointments(getCurrentState());
    }

    private String getCurrentState() {
        //Spinner mStatusFilterSpinner = (Spinner) findViewById(R.id.toolbar_spinner);
        String status = (String) mStatusFilterSpinner.getSelectedItem();
        return status;
    }

    public void loadAppointments(String rawStatus) {

        showLoadingIndicator(true);

        String token = SessionPrefs.get(this).getToken();

        String status = "";

        // Elegir valor del estado según la opción del spinner
        switch (rawStatus) {
            case "Renovable":
                status = "Renovable";
                break;
            case "Vencido":
                status = "Vencido";
                break;
            case "No renovable":
                status = "No renovable";
                break;
            default:
                status = "Todos";
        }

        // Construir mapa de parámetros
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("display", "list");
        parameters.put("status", status);

        // Realizar petición HTTP
        Call<ApiResponsePrestamos> call = mBiblioApi.getPrestamos(token, parameters);
        call.enqueue(new Callback<ApiResponsePrestamos>() {
            @Override
            public void onResponse(Call<ApiResponsePrestamos> call,
                                   Response<ApiResponsePrestamos> response) {
                if (!response.isSuccessful()) {
                    // Procesar error de API
                    String error = "Ha ocurrido un error. Contacte al administrador";
                    if (response.errorBody()
                            .contentType()
                            .subtype()
                            .equals("json")) {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());

                        error = apiError.getMessage();
                        Log.d(TAG, apiError.getDeveloperMessage());

                    } else {
                        // Reportar causas de error no relacionado con la API
                        try {
                            Log.d(TAG, response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    showLoadingIndicator(false);
                    showErrorMessage(error);
                    return;
                }

                List<PrestamosDisplayList> serverAppointments = response.body().getResults();
                Log.d(TAG, response.body().getResults().toString());

                if (serverAppointments.size() > 0) {
                    // Mostrar lista de citas médicas
                    showAppointments(serverAppointments);
                } else {
                    // Mostrar empty state
                    showNoAppointments();
                }

                showLoadingIndicator(false);

            }


            @Override
            public void onFailure(Call<ApiResponsePrestamos> call, Throwable t) {
                showLoadingIndicator(false);
                Log.d(TAG, "Petición rechazada:" + t.getMessage());
                showErrorMessage("Error de comunicación");
            }
        });
    }

    private void showLoadingIndicator(final boolean show) {
        final SwipeRefreshLayout refreshLayout =
                (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(show);
            }
        });
    }

    private void showErrorMessage(String error) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, error, Snackbar.LENGTH_LONG);
        snackbar.show();
        //Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        loadAppointments(getCurrentState());
    }

    private void showAppointments(List<PrestamosDisplayList> serverAppointments) {
        mPrestamosAdapter.swapItems(serverAppointments);

        mAppointmentsList.setVisibility(View.VISIBLE);
        mEmptyStateContainer.setVisibility(View.GONE);

    }

    private void showNoAppointments() {
        mAppointmentsList.setVisibility(View.GONE);
        mEmptyStateContainer.setVisibility(View.VISIBLE);
    }

    private void renewBook(int appointmentId) {
        // TODO: Mostrar estado de carga

        // Obtener token de usuario
        String token = SessionPrefs.get(this).getToken();

        // Preparar cuerpo de la petición
        HashMap<String, String> statusMap = new HashMap<>();
        statusMap.put("status", "Cancelada");

        // Enviar petición
        mBiblioApi.renewBook(appointmentId, token).enqueue(
                new Callback<ApiMessageResponse>() {
                    @Override
                    public void onResponse(Call<ApiMessageResponse> call,
                                           Response<ApiMessageResponse> response) {
                        if (!response.isSuccessful()) {
                            // Procesar error de API
                            String error = "Ha ocurrido un error. Contacte al administrador";
                            if (response.errorBody()
                                    .contentType()
                                    .subtype()
                                    .equals("json")) {
                                ApiError apiError = ApiError.fromResponseBody(response.errorBody());

                                error = apiError.getMessage();
                                Log.d(TAG, apiError.getDeveloperMessage());
                            } else {
                                try {
                                    // Reportar causas de error no relacionado con la API
                                    Log.d(TAG, response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            // TODO: Ocultar estado de carga
                            showErrorMessage(error);
                            return;
                        }

                        // Cancelación Exitosa
                        Log.d(TAG, response.body().getResults().getMessage());
                        String message = response.body().getResults().getMessage();
                        //Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

                        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);

                        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                        snackbar.show();
                        loadAppointments(getCurrentState());
                        // TODO: Ocultar estado de carga
                    }

                    @Override
                    public void onFailure(Call<ApiMessageResponse> call, Throwable t) {
                        // TODO: Ocultar estado de carga
                        Log.d(TAG, "Petición rechazada:" + t.getMessage());
                        showErrorMessage("Error de comunicación");
                    }
                }
        );
    }
}
