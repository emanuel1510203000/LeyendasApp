package com.example.leyendasapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

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
                // Redirigir a la actividad de racha de puntos
                Intent intent = new Intent(HomeActivity.this, PointsStreakActivity.class);
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

        //cards
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<LegendItem> items = new ArrayList<>();
        items.add(new LegendItem("BIENVENIDOS",
                "En esta aplicación encontrarás la ubicación de eventos paranormales ocurridos dependiendo de en donde te encuentres",
                "https://img.freepik.com/foto-gratis/joven-bruja-lampara-que-ilumina-camino-matorral-dia_23-2147902878.jpg?t=st=1736963404~exp=1736967004~hmac=396e17c512341f585d902700b1f3c9c5900995873916cb0098a15ffbcdb94a1a&w=740"));
        items.add(new LegendItem("MANUAL DE USO",
                "Para ingresar al mapa de leyendas deberás entrar en el menú ubicado en la esquina superior derecha de tu pantalla y seleccionar la opción 'mapas y gps' esto mostrará en pantalla las coordenadas y direcciones de eventos paranormales ocurridos al rededor de tu ubicación",
                "https://st5.depositphotos.com/69697474/75728/v/600/depositphotos_757286416-stock-illustration-happy-ghost-exclamation-question-mark.jpg"));

        CardAdapter adapter = new CardAdapter(items, new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(LegendItem item) {
                showLegendDialog(item);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void showLegendDialog(LegendItem item) {
        new AlertDialog.Builder(this)
                .setTitle(item.getTitle())
                .setMessage(item.getDescription())
                .setPositiveButton("Cerrar", null)
                .show();
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
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(HomeActivity.this, Login.class);
        startActivity(intent);
        finish();
    }
}
