package com.example.firebase;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    private TextView txtTemperatura;
    private EditText edtHumedad;


    private DatabaseReference humedadRef, presionRef, velocidadRef, temperaturaRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        txtTemperatura = findViewById(R.id.valor_Temperatura);
        edtHumedad = findViewById(R.id.setvalor_humedad);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        humedadRef = database.getReference("sensores/humedad");
        presionRef = database.getReference("sensores/presion");
        velocidadRef = database.getReference("sensores/velocidad");
        temperaturaRef = database.getReference("sensores/temperatura");


        temperaturaRef.addValueEventListener(setListener(txtTemperatura, "°C"));
        humedadRef.addValueEventListener(setListener(findViewById(R.id.valor_water), "%"));
        presionRef.addValueEventListener(setListener(findViewById(R.id.valor_presion), "hPa"));
        velocidadRef.addValueEventListener(setListener(findViewById(R.id.valor_velocidad), "km/h"));
    }

    // Método para crear un listener genérico
    private ValueEventListener setListener(TextView txt, String unidad) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Object value = snapshot.getValue();
                if (value != null) {
                    txt.setText(value.toString() + " " + unidad);
                } else {
                    txt.setText("-- " + unidad);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                txt.setText("Error");
            }
        };
    }

    // Método del botón "Set" de humedad
    public void clickBotonHumedad(View view) {
        temperaturaRef.setValue(Float.parseFloat(edtHumedad.getText().toString()));
    }

    // Método del botón de presión (ejemplo)
    public void clickBotonpresion(View view) {
        presionRef.setValue(1013); // aquí puedes poner un valor de prueba
        Toast.makeText(this, "Presión enviada", Toast.LENGTH_SHORT).show();
    }
}
