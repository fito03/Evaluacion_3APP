package com.example.evaluacion_3app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private Handler handler;
    private Button btngrabar, btnlistar;
    private EditText etNombre, etDescripcion, etRut;
    private TextView textHora, textFecha;
    private Runnable actualizarHora;
    private Spinner spLaboratorio;
    private DatabaseHelper databaseHelper;
    private boolean loop = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        btngrabar = (Button) findViewById(R.id.btngrabar);
        btnlistar = (Button) findViewById(R.id.btnlistar);

        textHora = findViewById(R.id.textHora);
        textFecha = findViewById(R.id.textFecha);

        spLaboratorio = (Spinner) findViewById(R.id.spinner_options);
        etNombre = (EditText) findViewById(R.id.nombrePersona);
        etDescripcion = (EditText) findViewById(R.id.incidente);
        etRut = (EditText) findViewById(R.id.rut);

        handler = new Handler(Looper.getMainLooper());


        actualizarHora = new Runnable() {
            @Override
            public void run() {
                updateCurrentTime();
                handler.postDelayed(this, 1000); //
            }
        };

        btngrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validar_grabar(etNombre, etDescripcion, etRut);
            }
        });

        btnlistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListarActivity.class);
                startActivity(intent);
            }
        });
        super.onResume();
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() > 0){
            sm.registerListener((SensorEventListener) this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
        }
    }
    private String pad(int i){
        if (i < 10)
            return "0" + i;
        return String.valueOf(i);
    }
    private void updateCurrentTime() {
        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("KK:mm:ss", Locale.getDefault());
        String formattedTime = sdf.format(currentTime);

        SimpleDateFormat sdfFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedFecha = sdfFecha.format(currentTime);

        textHora.setText("Hora actual: " + formattedTime);
        textFecha.setText("Fecha: "+ formattedFecha);
    }
    private void Validar_grabar(EditText etNombre, EditText etDescripcion, EditText etRut){
        String nombre = etNombre.getText().toString();
        String descripcion = etDescripcion.getText().toString();
        String rut = etRut.getText().toString();


        if(TextUtils.isEmpty(nombre)){
            etNombre.setError("Ingrese nombre");
            etNombre.requestFocus();
        }else if(TextUtils.isEmpty(descripcion)){
            etDescripcion.setError("Ingrese descripcion");
            etDescripcion.requestFocus();
        }else if(validarRUT(rut) == false){
            etRut.setError("Ingrese rut valido");
            etRut.requestFocus();
        }else if(TextUtils.isEmpty(rut)){
            etRut.setError("Ingrese rut");
            etRut.requestFocus();
        }else {
            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String currentDate = dateFormat.format(calendar.getTime());

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String currentTime = timeFormat.format(calendar.getTime());

            databaseHelper.addIncidenteDetail(nombre, rut, spLaboratorio.getSelectedItemPosition() , descripcion, currentDate, currentTime);
            etNombre.setText("");
            etDescripcion.setText("");
            etRut.setText("");
            spLaboratorio.setSelection(0);
            Toast.makeText(this, "Incidente agregado", Toast.LENGTH_SHORT).show();
        }
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
    @Override
    protected void onResume(){
        super.onResume();
        handler.post(actualizarHora);
    }
    @Override
    protected void onPause(){
        super.onPause();
        handler.removeCallbacks(actualizarHora);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float y = event.values[1];
            if (y > 8.5 || y < -8.5){
                if (loop) {
                    Log.d("test", "giro");
                    Validar_grabar(etNombre, etDescripcion, etRut);
                    loop = false;
                }
            }else {
                loop = true;
            }

        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}