package com.example.leyendasapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

public class MapsAndGpsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_and_gps);  // Asegúrate de que este layout exista

        // Configura la flecha de retroceso en el ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Muestra la flecha
            getSupportActionBar().setHomeButtonEnabled(true);  // Habilita el botón de retroceso
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Creamos la acción al presionar la flecha de retroceso
        if (item.getItemId() == android.R.id.home) {
            // Regresar a la actividad HomeActivity
            Intent intent = new Intent(MapsAndGpsActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();  // Cierra la actividad actual
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
