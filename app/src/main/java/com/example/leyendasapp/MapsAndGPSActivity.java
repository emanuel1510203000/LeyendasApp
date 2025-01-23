package com.example.leyendasapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import android.Manifest;
import androidx.core.app.ActivityCompat;

public class MapsAndGPSActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private Button btnRegisterLocation;
    private Button btnLocateMe; // Agregamos el botón para regresar a la ubicación actual
    private GoogleMap googleMap;
    private LatLng selectedLocation; // Coordenadas seleccionadas
    private Marker marker;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_and_gps);

        mapView = findViewById(R.id.mapView);
        btnRegisterLocation = findViewById(R.id.btnRegisterLocation);
        btnLocateMe = findViewById(R.id.btnLocateMe); // Inicializamos el botón

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnRegisterLocation.setOnClickListener(v -> {
            if (selectedLocation == null) {
                Toast.makeText(this, "Selecciona la ubicación a registrar", Toast.LENGTH_SHORT).show();
            } else {
                // Abrir formulario y pasar las coordenadas seleccionadas
                Intent intent = new Intent(MapsAndGPSActivity.this, AddLocationActivity.class);
                intent.putExtra("latitude", selectedLocation.latitude);
                intent.putExtra("longitude", selectedLocation.longitude);
                startActivity(intent);
            }
        });

        // Acción para el botón "Ubicación"
        btnLocateMe.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15)); // Mover la cámara a la ubicación actual
                                } else {
                                    Toast.makeText(MapsAndGPSActivity.this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                // Solicitar permisos si no se han concedido
                ActivityCompat.requestPermissions(MapsAndGPSActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Verificar y obtener la ubicación del usuario
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Mostrar la ubicación en el mapa
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Mi ubicación actual"));
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                            }
                        }
                    });
        } else {
            // Solicitar permisos si no se han concedido
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }



        // ** Agregar un marcador personalizado
        LatLng lloronaLocation = new LatLng(19.2188716,-98.8097659);
        googleMap.addMarker(new MarkerOptions()
                .position(lloronaLocation)
                .title("Avistamiento de la Llorona")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ghost)));

        LatLng nahualLocation = new LatLng(19.2358895,-98.8443971);
        googleMap.addMarker(new MarkerOptions()
                .position(nahualLocation)
                .title("Un nahual que se perdio entre la milpa")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ghost)));

        LatLng amarreLocation = new LatLng(19.2123209,-98.7386217);
        googleMap.addMarker(new MarkerOptions()
                .position(amarreLocation)
                .title("Avistamiento de rituales de brujas")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ghost)));

        LatLng vozLocation = new LatLng(19.2112825,-98.7438324);
        googleMap.addMarker(new MarkerOptions()
                .position(vozLocation)
                .title("Se escucharon pasos al rededor del area de campamento")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ghost)));

        LatLng ovniLocation = new LatLng(19.1958644,-98.7365411);
        googleMap.addMarker(new MarkerOptions()
                .position(ovniLocation)
                .title("Avistamiento de esferas luminosas")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ghost)));


        // Habilitar clics en el mapa para seleccionar ubicaciones
        googleMap.setOnMapClickListener(latLng -> {
            selectedLocation = latLng; // Guardar la ubicación seleccionada

            // Eliminar el marcador anterior si existe
            if (marker != null) {
                marker.remove();
            }

            // Agregar un nuevo marcador
            marker = googleMap.addMarker(new MarkerOptions().position(latLng).title("Ubicación seleccionada"));
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
