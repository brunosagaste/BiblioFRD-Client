package com.bruno.frd.biblio.ui;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.bruno.frd.biblio.R;
import com.bruno.frd.biblio.data.api.model.SearchDisplayList;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // El CollapsingToolbarLayout no es el de Android, sino el de esta biblioteca: https://github.com/opacapp/multiline-collapsingtoolbar
        // Permite agregar mas lineas
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);

        // Recibo los datos del item
        final SearchDisplayList clickedItem = (SearchDisplayList) getIntent().getExtras().getSerializable("DATA");

        // Ajusto el tamaño de fuente del título si este es muy largo
        if (clickedItem.getTitle().length() > 20) {
            if (clickedItem.getTitle().length() > 30) {
                toolBarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar40);
            }
            toolBarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar20);
        }
        // Coloco el título
        toolBarLayout.setTitle(clickedItem.getTitle());

        // Si, una lista scrolleable adentro de un ScrollView, una negrada pero a estas alturas muy tarde para cambiarlo. Además es mas rápido de implementar que un RecyclerView.
        ListView listview;
        listview = (ListView) findViewById(R.id.item_list);
        final ArrayList<Pair> item_data;
        item_data = new ArrayList<Pair>();
        Pair<String, String> item_tuple_copyid = new Pair<String, String>("Copias libres", Integer.toString(clickedItem.getCopyFree()));;
        item_data.add(item_tuple_copyid);
        Pair<String, String> item_tuple_author = new Pair<String, String>("Autor", clickedItem.getAuthor());;
        item_data.add(item_tuple_author);
        Pair<String, String> item_tuple_status = new Pair<String, String>("Estado", clickedItem.getStatus());;
        item_data.add(item_tuple_status);
        if (clickedItem.getTopic2() != "") {
            Pair<String, String> item_tuple_topic2 = new Pair<String, String>("Tema", clickedItem.getTopic2());
            item_data.add(item_tuple_topic2);
        }
        Pair<String, String> item_tuple_bibid = new Pair<String, String>("Número de libro", Integer.toString(clickedItem.getBibid()));;
        item_data.add(item_tuple_bibid);


        // Armamos el listado
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, item_data) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text1.setTypeface(ResourcesCompat.getFont(SearchItemActivity.this, R.font.roboto_light));

                // Cargamos los items en la lista
                text1.setText(item_data.get(position).first.toString());
                text2.setText(item_data.get(position).second.toString());
                return view;
            }
        };

        listview.setAdapter(adapter);

        // Hacemos transparente la barra de estado
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

    }

    private String formatDate(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

}