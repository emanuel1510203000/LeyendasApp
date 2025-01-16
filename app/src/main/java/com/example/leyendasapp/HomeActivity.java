package com.example.leyendasapp;

import android.content.Intent;
import android.content.SharedPreferences;
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

        //cards
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<LegendItem> items = new ArrayList<>();
        items.add(new LegendItem("BIENVENIDOS",
                "En esta aplicación encontrarás la ubicación de eventos paranormales ocurridos dependiendo de en donde te encuentres",
                "https://img.freepik.com/foto-gratis/joven-bruja-lampara-que-ilumina-camino-matorral-dia_23-2147902878.jpg?t=st=1736963404~exp=1736967004~hmac=396e17c512341f585d902700b1f3c9c5900995873916cb0098a15ffbcdb94a1a&w=740"));
        items.add(new LegendItem("MAPA DE LEYENDAS",
                "Para ingresar al mapa de leyendas deberás entrar en el menú ubicado en la esquina superior derecha de tu pantalla y seleccionar la opción 'mapas y gps' esto mostrará en pantalla las coordenadas y direcciones de eventos paranormales ocurridos al rededor de tu ubicación.",
                "https://img.freepik.com/foto-gratis/brujula-vista-superior-parte-superior-mapa-mundial_23-2148610390.jpg?t=st=1736979777~exp=1736983377~hmac=d794ebc473090617fe98e0f436b21b1f01bcf90f377f9efa8e63a689d1a7893c&w=740"));
        items.add(new LegendItem("BITÁCORA DE EXPLORACIÓN",
                "La bitacora es un registro en el cual podrás ingresar el lugar y una descripción de los lugares que vicites. Esta función se encuentra en el menú de la esquina superior izquierda con el nombre 'Bitácora de Exploraciones'",
                "https://img.freepik.com/foto-gratis/brujula-libro-mapas_23-2147793500.jpg?t=st=1736982603~exp=1736986203~hmac=8053b3e60d9216d07f6e0e4372439c3e896e023019aff0863b3eb41bd3cbc464&w=740"));
        items.add(new LegendItem("LA LLORONA",
                "La llorona es el fantasma de una mujer que suele aparecer en diversas partes de México, principalmente en Xochimilco, la cual grita de dolor buscando a sus hijos, debido al arrepentimiento que siente de haberlos ahogado en los canales para vengarse de la traición de su amante.",
                "https://st5.depositphotos.com/23188010/77443/i/600/depositphotos_774436526-stock-photo-spooky-halloween-concept-mixed-media.jpg"));
        items.add(new LegendItem("NAHUALES",
                "Un nahual es una criatura sobrenatural de las creencias mesoamericanas que tiene la capacidad de transformarse en un animal. La palabra nahual proviene del náhuatl nahualli, que significa 'lo que es mi vestidura'. En la mitología mesoamericana, los nahuales eran considerados brujos o hechiceros. Se creía que podían usar su nahual para curar a las personas y practicar magia.",
                "https://elcomercio.pe/resizer/v2/CRL2DPW7QJF63K5GSKRWHLY56Y.png?auth=1a1aee0e4f2e71b331906ed16f07780105ccc7d104ad2f27c4523f18e6af361b&width=1200&height=810&quality=90&smart=true"));
        items.add(new LegendItem("BRUJAS MEXICANAS",
                "A diferencia de las brujas representadas en los cuentos infantiles y películas, las brujas mexicanas no vuelan en escobas o usan sombreros puntiagudos, sino que se transforman en fuego o guajolotes al quitarse los pies y se roban a niños pequeños para chuparles la sangre.",
                "https://mxc.com.mx/wp-content/uploads/2024/08/brujas-mayas-jpeg.webp-1.jpeg"));

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
