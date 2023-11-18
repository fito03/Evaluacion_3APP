package com.example.evaluacion_3app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ActualizarActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private Model model;
    private Button btnActualizar, btnEliminar;
    private EditText etNombre, etDescripcion, etRut;
    private Spinner spLaboratorio;
    private TextView textHora, textFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actualizar);

        Intent intent = getIntent();
        model = (Model) intent.getSerializableExtra("incidente");

        databaseHelper = new DatabaseHelper(this);

        btnActualizar = (Button) findViewById(R.id.btnActualizar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);

        etNombre = (EditText) findViewById(R.id.nombrePersona);
        etDescripcion = (EditText) findViewById(R.id.incidente);
        etRut = (EditText) findViewById(R.id.rut);
        spLaboratorio = (Spinner) findViewById(R.id.spinner_options);
        textFecha = findViewById(R.id.textFecha);
        textHora = findViewById(R.id.textHora);

        etNombre.setText(model.getNombre());
        etDescripcion.setText(model.getDescripcion());
        etRut.setText(model.getRut());
        spLaboratorio.setSelection(model.getLaboratorio());

        textHora.setText("Hora: "+model.getHora());
        textFecha.setText("Fecha: "+model.getFecha());

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etNombre.getText().toString())){
                    etNombre.setError("Ingrese nombre");
                    return;
                }
                if (TextUtils.isEmpty(etRut.getText().toString())){
                    etRut.setError("Ingrese rut");
                    return;
                }
                if (!validarRUT(etRut.getText().toString())){
                    etRut.setError("Ingrese rut valido");
                    return;
                }
                if (TextUtils.isEmpty(etDescripcion.getText().toString())){
                    etDescripcion.setError("Ingrese descripcion");
                    return;
                }
                Log.d("test", "onClick: "+spLaboratorio.getSelectedItem());




                databaseHelper.updateIncidente(model.getId(), etNombre.getText().toString(), etRut.getText().toString(), spLaboratorio.getSelectedItemPosition(), etDescripcion.getText().toString());
                Toast.makeText(getApplicationContext(), "Actualizacion exitosa", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            public boolean validarRUT(String rut){
                boolean validacion = false;
                try{
                    rut =  rut.toUpperCase();
                    rut = rut.replace(".", "");
                    rut = rut.replace("-", "");
                    int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));
                    char dv = rut.charAt(rut.length() - 1);
                    int m = 0, s = 1;
                    for(; rutAux != 0; rutAux /= 10){
                        s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
                    }
                    if(dv == (char) (s != 0 ? s + 47 : 75)){
                        validacion = true;
                    }
                }catch (java.lang.NumberFormatException e){
                }catch (Exception e){}
                return validacion;
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteIncidente(model.getId());
                Toast.makeText(getApplicationContext(), "Eliminacion exitosa", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
