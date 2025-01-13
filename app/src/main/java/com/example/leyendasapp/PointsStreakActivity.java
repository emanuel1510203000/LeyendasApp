package com.example.leyendasapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PointsStreakActivity extends AppCompatActivity {

    private EditText leyendaNombreEditText;
    private EditText leyendaDescripcionEditText;
    private Button btnGuardarExploracion;

    private RecyclerView exploracionesRecyclerView;
    private ExploracionAdapter exploracionAdapter;
    private ArrayList<Leyenda> leyendasList = new ArrayList<>(); // Lista de exploraciones

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_streak);

        leyendaNombreEditText = findViewById(R.id.et_leyenda_nombre);
        leyendaDescripcionEditText = findViewById(R.id.et_leyenda_descripcion);
        btnGuardarExploracion = findViewById(R.id.btn_guardar_exploracion);
        exploracionesRecyclerView = findViewById(R.id.rv_exploraciones);

        // Configuración del RecyclerView
        exploracionesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        exploracionAdapter = new ExploracionAdapter(leyendasList, new ExploracionAdapter.OnExploracionListener() {
            @Override
            public void onEdit(int position) {
                editarLeyenda(position);
            }

            @Override
            public void onDelete(int position) {
                eliminarLeyenda(position);
            }
        });
        exploracionesRecyclerView.setAdapter(exploracionAdapter);

        // Guardar una nueva exploración
        btnGuardarExploracion.setOnClickListener(v -> {
            String nombre = leyendaNombreEditText.getText().toString();
            String descripcion = leyendaDescripcionEditText.getText().toString();

            if (!nombre.isEmpty() && !descripcion.isEmpty()) {
                Leyenda nuevaLeyenda = new Leyenda(nombre, descripcion);
                leyendasList.add(nuevaLeyenda);
                exploracionAdapter.notifyItemInserted(leyendasList.size() - 1); // Notifica al adaptador

                leyendaNombreEditText.setText("");
                leyendaDescripcionEditText.setText("");
                Toast.makeText(PointsStreakActivity.this, "Exploración guardada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PointsStreakActivity.this, "Por favor, ingrese todos los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para editar una exploración
    private void editarLeyenda(int position) {
        Leyenda leyenda = leyendasList.get(position);
        leyendaNombreEditText.setText(leyenda.getNombre());
        leyendaDescripcionEditText.setText(leyenda.getDescripcion());

        // Eliminar la leyenda de la lista para editarla
        leyendasList.remove(position);
        exploracionAdapter.notifyItemRemoved(position);
    }

    // Método para eliminar una exploración ya corregida
    private void eliminarLeyenda(int position) {
        leyendasList.remove(position);
        exploracionAdapter.notifyItemRemoved(position);
        Toast.makeText(this, "Exploración eliminada", Toast.LENGTH_SHORT).show();
    }
}
