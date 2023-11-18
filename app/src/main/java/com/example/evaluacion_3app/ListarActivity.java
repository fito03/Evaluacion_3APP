package com.example.evaluacion_3app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListarActivity extends AppCompatActivity{

    private ListView listView;
    private ArrayList<Model> modelArrayList;
    private CustomAdapter customAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar);

        listView = (ListView) findViewById(R.id.lv);

        databaseHelper = new DatabaseHelper(this);

        modelArrayList = databaseHelper.getAllIncidente();

        customAdapter = new CustomAdapter(this, modelArrayList);
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new  Intent(getApplicationContext(), ActualizarActivity.class);
                intent.putExtra("incidente", modelArrayList.get(position));
                startActivity(intent);
            }
        });
    }
}
