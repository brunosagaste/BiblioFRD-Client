package com.bruno.frd.biblio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;

import com.bruno.frd.biblio.R;
import com.bruno.frd.biblio.data.prefs.SessionPrefs;
import com.danimahardhika.cafebar.CafeBar;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        String name = SessionPrefs.get(this).getName() + " " + SessionPrefs.get(this).getLastName();

        if (name.length() > 20) {
            if (name.length() > 30) {
                toolBarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar40);
            }
            toolBarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar20);
        }

        //toolBarLayout.setTitle(name);
        initCollapsingToolbar(name);

        // Si, una lista scrolleable adentro de un ScrollView, una negrada pero a estas alturas muy tarde para cambiarlo. Además es mas rápido de implementar que un RecyclerView.
        ListView listview;
        listview = (ListView) findViewById(R.id.item_list);
        final ArrayList<Pair> user_data;
        user_data = new ArrayList<Pair>();
        Pair<String, String> item_tuple_file = new Pair<String, String>("Legajo", SessionPrefs.get(this).getFile());
        user_data.add(item_tuple_file);
        Pair<String, String> item_tuple_dni = new Pair<String, String>("DNI", SessionPrefs.get(this).getDni());
        user_data.add(item_tuple_dni);
        Pair<String, String> item_tuple_mail = new Pair<String, String>("Correo", SessionPrefs.get(this).getMail());
        user_data.add(item_tuple_mail);
        Pair<String, String> item_tuple_address = new Pair<String, String>("Dirección", SessionPrefs.get(this).getAddress());
        user_data.add(item_tuple_address);
        Pair<String, String> item_tuple_city = new Pair<String, String>("Ciudad", SessionPrefs.get(this).getCity());
        user_data.add(item_tuple_city);
        Pair<String, String> item_tuple_phone = new Pair<String, String>("Teléfono", SessionPrefs.get(this).getPhone());
        user_data.add(item_tuple_phone);

        // Armamos el listado
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, user_data) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text1.setTypeface(ResourcesCompat.getFont(ProfileActivity.this, R.font.roboto_light));

                // Cargamos los items en la lista
                text1.setText(user_data.get(position).first.toString());
                text2.setText(user_data.get(position).second.toString());
                return view;
            }
        };

        listview.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, PasswordActivity.class));
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        if (getIntent().getExtras() != null) {
            final String pw_result_message = (String) getIntent().getExtras().getSerializable("DATA");
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.profile_coordinator);
            CafeBar.builder(coordinatorLayout.getContext())
                    .floating(true)
                    .content(pw_result_message)
                    .to(coordinatorLayout)
                    .neutralText("Aceptar")
                    .duration(CafeBar.Duration.LONG)
                    .show();
            getIntent().removeExtra("DATA");
        }
    }

    @Override
    public void onBackPressed() {
        showMainScreen();
    }

    private void showMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        showMainScreen();
        return true;
    }

    private void initCollapsingToolbar(final String name) {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(name);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(name);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(name);
                    isShow = false;
                }
            }
        });
    }
}