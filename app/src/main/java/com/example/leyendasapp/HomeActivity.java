package com.example.leyendasapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TextView userEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Configurar DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);

        // Configurar el NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        userEmailTextView = headerView.findViewById(R.id.tv_user_email);

        // Obtener el usuario actual y mostrar su email
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            userEmailTextView.setText(email != null ? email : "Correo no disponible");
        }

        // Configurar el menú lateral
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_logout) {
                cerrarSesion();
                return true;
            } else if (item.getItemId() == R.id.action_settings) {
                // Redirigir a la actividad de racha de puntos y pasar el correo del usuario
                if (currentUser != null) {
                    String email = currentUser.getEmail();
                    Intent intent = new Intent(HomeActivity.this, PointsStreakActivity.class);
                    intent.putExtra("USER_EMAIL", email); // Pasa el correo del usuario a la siguiente actividad
                    startActivity(intent);
                }
                return true;
            } else if (item.getItemId() == R.id.action_maps_gps) {
                // Redirigir a la actividad de Mapas y GPS
                Intent intent = new Intent(HomeActivity.this, MapsAndGpsActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        // Agregar el ActionBarDrawerToggle para el icono de hamburguesa
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Configurar ActionBar para mostrar el ícono de hamburguesa
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false); // Ocultar título
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Manejar el ícono de menú (hamburguesa o flecha de retroceso)
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cerrarSesion() {
        // Limpiar los datos de la sesión guardados en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LeyendasData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();  // Eliminar todos los datos guardados
        editor.apply();   // Aplicar los cambios

        // Cerrar sesión en Firebase
        FirebaseAuth.getInstance().signOut();

        // Redirigir al Login
        Intent intent = new Intent(HomeActivity.this, Login.class);
        startActivity(intent);
        finish();
    }
}
