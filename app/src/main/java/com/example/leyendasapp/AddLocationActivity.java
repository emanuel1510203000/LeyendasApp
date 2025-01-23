package com.example.leyendasapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddLocationActivity extends AppCompatActivity {

    private EditText userEmail, userName, location, paranormalEvent, creature, description, comments, duration, latitudeField, longitudeField;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        userEmail = findViewById(R.id.userEmail);
        userName = findViewById(R.id.userName);
        location = findViewById(R.id.location);
        paranormalEvent = findViewById(R.id.paranormalEvent);
        creature = findViewById(R.id.creature);
        description = findViewById(R.id.description);
        comments = findViewById(R.id.comments);
        duration = findViewById(R.id.duration);
        latitudeField = findViewById(R.id.latitude);
        longitudeField = findViewById(R.id.longitude);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Obtener las coordenadas del intent
        double latitude = getIntent().getDoubleExtra("latitude", 0);
        double longitude = getIntent().getDoubleExtra("longitude", 0);

        // Llenar los campos de latitud y longitud automáticamente
        latitudeField.setText(String.valueOf(latitude));
        longitudeField.setText(String.valueOf(longitude));

        btnSubmit.setOnClickListener(v -> saveLocationData());
    }

    private void saveLocationData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("locations").child(userId);

            Map<String, Object> locationData = new HashMap<>();
            locationData.put("userEmail", userEmail.getText().toString());
            locationData.put("userName", userName.getText().toString());
            locationData.put("location", location.getText().toString());
            locationData.put("paranormalEvent", paranormalEvent.getText().toString());
            locationData.put("creature", creature.getText().toString());
            locationData.put("description", description.getText().toString());
            locationData.put("comments", comments.getText().toString());
            locationData.put("duration", duration.getText().toString());
            locationData.put("latitude", latitudeField.getText().toString());
            locationData.put("longitude", longitudeField.getText().toString());

            databaseReference.push().setValue(locationData).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    finish(); // Cerrar la actividad
                } else {
                    // Manejar errores
                }
            });
        }
    }
}
