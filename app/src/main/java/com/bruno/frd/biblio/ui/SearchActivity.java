package com.bruno.frd.biblio.ui;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bruno.frd.biblio.R;
import com.bruno.frd.biblio.data.api.BiblioApi;
import com.bruno.frd.biblio.data.api.model.ApiError;
import com.bruno.frd.biblio.data.api.model.ApiSearchResponse;
import com.bruno.frd.biblio.data.api.model.SearchDisplayList;
import com.bruno.frd.biblio.data.prefs.SessionPrefs;
import com.danimahardhika.cafebar.CafeBar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    private Retrofit mRestAdapter;
    private BiblioApi mBiblioApi;

    private RecyclerView mSearchList;
    private SearchAdapter mSearchAdapter;
    private View mEmptyStateContainer;

    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SearchView mSearchView = findViewById(R.id.search_view);
        mSearchView.setFocusable(true);
        //mSearchView.requestFocusFromTouch();
        //mSearchView.requestFocus();
        mSearchView.onActionViewExpanded();
        mSearchView.setIconified(false);

        //Creo el listado de resultados
        loadRecyclerView();

        final TextView searchText = (TextView) mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard(getActivity(searchText.getContext()));
                    if (!searchText.getText().toString().isEmpty()) {
                        loadSearch(searchText.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_search_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Pedir al servidor información reciente
                if (searchText.getText().toString().isEmpty()) {
                    showLoadingIndicator(false);
                } else {
                    Log.d("String de busqueda", searchText.getText().toString());
                    loadSearch(searchText.getText().toString());
                }
            }
        });

        // Crear adaptador Retrofit
        Gson gson = new GsonBuilder()
                //.setDateFormat("yyyy-MM-dd")
                .create();
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(BiblioApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Crear conexión a la API de SaludMock
        mBiblioApi = mRestAdapter.create(BiblioApi.class);

    }


    private void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }



    @Override
    public void onBackPressed() {
        showMainScreen();
    }


    private void showMainScreen() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }


    public void loadSearch(String searchText) {

        showLoadingIndicator(true);
        String token = SessionPrefs.get(this).getToken();
        String id = SessionPrefs.get(this).getID();

        // Realizar petición HTTP
        Call<ApiSearchResponse> call = mBiblioApi.getSearch(searchText, token, id);
        call.enqueue(new Callback<ApiSearchResponse>() {
            @Override
            public void onResponse(Call<ApiSearchResponse> call,
                                   Response<ApiSearchResponse> response) {
                try {
                    if (!response.isSuccessful()) {
                        // Procesar error de API
                        String error = "Ha ocurrido un error. Contacte al administrador.";
                        String deverror = null;
                        if (response.errorBody()
                                .contentType()
                                .subtype()
                                .equals("json")) {
                            ApiError apiError = ApiError.fromResponseBody(response.errorBody());

                            error = apiError.getMessage();
                            deverror = apiError.getDeveloperMessage();
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
                        if (Objects.equals(deverror, "wrongtoken")) {
                            showMainScreen();
                        }
                        showErrorMessage(error);
                        return;
                    }

                    List<SearchDisplayList> serverResults = response.body().getResults();
                    Log.d(TAG, response.body().getResults().toString());

                    if (serverResults.size() > 0) {
                        // Mostrar lista de citas médicas
                        showResults(serverResults);
                    } else {
                        // Mostrar empty state
                        showNoResults();
                    }

                    showLoadingIndicator(false);
                    mSearchList.requestFocus();
                }
                catch(Exception e) {
                    showLoadingIndicator(false);
                    showNoResults();
                    showErrorMessage("HTTP Error: " + String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ApiSearchResponse> call, Throwable t) {
                showLoadingIndicator(false);
                Log.d(TAG, "Petición rechazada:" + t.getMessage());
                showErrorMessage("No hay conexión con el servidor");
            }
        });
    }


    private void showErrorMessage(String error) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_search);
        CafeBar.builder(coordinatorLayout.getContext())
                .floating(true)
                .content(error)
                .to(coordinatorLayout)
                .neutralText("Aceptar")
                .duration(CafeBar.Duration.LONG)
                .show();
    }

    private void showResults(List<SearchDisplayList> serverResult) {
        mSearchAdapter.swapItems(serverResult);
        mSearchList.setVisibility(View.VISIBLE);
        mEmptyStateContainer.setVisibility(View.GONE);
    }

    private void showNoResults() {
        mSearchList.setVisibility(View.GONE);
        mEmptyStateContainer.setVisibility(View.VISIBLE);
    }

    private void showLoadingIndicator(final boolean show) {
        final SwipeRefreshLayout refreshLayout =
                (SwipeRefreshLayout) findViewById(R.id.refresh_search_layout);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(show);
            }
        });
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public Activity getActivity(Context context) {
        if (context == null) {
            return null;
        }
        else if (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            } else {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }
        return null;
    }


    public void loadRecyclerView(){
        mSearchList = (RecyclerView) findViewById(R.id.list_search);
        mSearchAdapter = new SearchAdapter(this, new ArrayList<SearchDisplayList>(0));
        mSearchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SearchDisplayList clickedItem) {
                Intent item_intent = new Intent(SearchActivity.this, SearchItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA",clickedItem);
                item_intent.putExtras(bundle);
                startActivity(item_intent);
            }

        });

        mSearchList.setAdapter(mSearchAdapter);
        mEmptyStateContainer = findViewById(R.id.empty_state_container_search);
        //Vuelo la lupa al arrancar
        mEmptyStateContainer.setVisibility(View.GONE);
    }


}


