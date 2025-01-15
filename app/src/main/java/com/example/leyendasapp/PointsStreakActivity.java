package com.example.leyendasapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PointsStreakActivity extends AppCompatActivity {

    private EditText etLeyendaNombre, etLeyendaDescripcion;
    private Button btnGuardarExploracion;
    private RecyclerView rvExploraciones;
    private ExploracionAdapter exploracionAdapter;
    private List<Leyenda> leyendaList;
    private SharedPreferences sharedPreferences;

    // Agregar un campo para el índice de edición
    private int editingPosition = -1; // -1 indica que no estamos editando ninguna leyenda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_streak);

        // Inicializar componentes de UI
        etLeyendaNombre = findViewById(R.id.et_leyenda_nombre);
        etLeyendaDescripcion = findViewById(R.id.et_leyenda_descripcion);
        btnGuardarExploracion = findViewById(R.id.btn_guardar_exploracion);
        rvExploraciones = findViewById(R.id.rv_exploraciones);

        // Configurar RecyclerView
        leyendaList = new ArrayList<>();
        exploracionAdapter = new ExploracionAdapter(leyendaList, new ExploracionAdapter.OnExploracionListener() {
            @Override
            public void onEdit(int position) {
                Leyenda leyenda = leyendaList.get(position);
                etLeyendaNombre.setText(leyenda.getNombre());
                etLeyendaDescripcion.setText(leyenda.getDescripcion());

                // Establecer el índice de edición
                editingPosition = position;
            }

            @Override
            public void onDelete(int position) {
                leyendaList.remove(position);
                exploracionAdapter.notifyItemRemoved(position);
                guardarLeyendas(); // Guardar después de eliminar
            }
        });
        rvExploraciones.setLayoutManager(new LinearLayoutManager(this));
        rvExploraciones.setAdapter(exploracionAdapter);

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("LeyendasData", MODE_PRIVATE);

        // Cargar leyendas guardadas
        cargarLeyendas();

        // Configurar el botón de guardar
        btnGuardarExploracion.setOnClickListener(v -> {
            String nombre = etLeyendaNombre.getText().toString();
            String descripcion = etLeyendaDescripcion.getText().toString();
            if (!nombre.isEmpty() && !descripcion.isEmpty()) {
                if (editingPosition == -1) {
                    // Si no estamos editando, agregamos una nueva leyenda
                    Leyenda leyenda = new Leyenda(nombre, descripcion);
                    leyendaList.add(leyenda);
                    exploracionAdapter.notifyItemInserted(leyendaList.size() - 1);
                } else {
                    // Si estamos editando, actualizamos la leyenda existente
                    Leyenda leyenda = leyendaList.get(editingPosition);
                    leyenda.setNombre(nombre);
                    leyenda.setDescripcion(descripcion);
                    exploracionAdapter.notifyItemChanged(editingPosition); // Notificar que la leyenda ha sido modificada
                }

                guardarLeyendas(); // Guardar después de agregar o editar
                etLeyendaNombre.setText("");
                etLeyendaDescripcion.setText("");
                editingPosition = -1; // Restablecer el índice de edición
            }
        });
    }

    private void guardarLeyendas() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Limpiar datos anteriores
        for (int i = 0; i < leyendaList.size(); i++) {
            Leyenda leyenda = leyendaList.get(i);
            editor.putString("leyenda_" + i + "_nombre", leyenda.getNombre());
            editor.putString("leyenda_" + i + "_descripcion", leyenda.getDescripcion());
        }
        editor.apply();
    }

    private void cargarLeyendas() {
        leyendaList.clear();
        int index = 0;
        while (sharedPreferences.contains("leyenda_" + index + "_nombre")) {
            String nombre = sharedPreferences.getString("leyenda_" + index + "_nombre", "");
            String descripcion = sharedPreferences.getString("leyenda_" + index + "_descripcion", "");
            leyendaList.add(new Leyenda(nombre, descripcion));
            index++;
        }
        exploracionAdapter.notifyDataSetChanged();
    }
}
