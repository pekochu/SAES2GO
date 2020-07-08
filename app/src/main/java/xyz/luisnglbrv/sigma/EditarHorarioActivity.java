package xyz.luisnglbrv.sigma;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import xyz.luisnglbrv.sigma.db.Horario_OpenHelper;
import xyz.luisnglbrv.sigma.db.Horario_Script;

public class EditarHorarioActivity extends AppCompatActivity {

    private SimpleCursorAdapter adapter;
    private EditText salon, edificio;
    //private TextView tvLunes1, tvLunes2, tvMartes1, tvMartes2, tvMiercoles1, tvMiercoles2;
    private TextInputLayout mFloatLabelEdificio, mFloatLabelSalon;
    private FloatingActionButton actualizar;
    private String edificioSelection, salonSelection;
    //String lunes, martes, miercoles, jueves, viernes, sabado;
    private Spinner spinner;
    private TextView texto;
    private Preferencias datos;
    private Calendar calendario;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datos = new Preferencias(this);
        setTheme(datos.getStyle());
        setContentView(R.layout.activity_editar_horario);

        Locale locale = new Locale("es", "MX");
        sdf = new SimpleDateFormat("HH:mm", locale);
        calendario = Calendar.getInstance();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.horario_offline_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        /*
        tvLunes1 = (TextView) findViewById(R.id.editar_lunes_0);
        tvLunes2 = (TextView) findViewById(R.id.editar_lunes_1);
        tvMartes1 = (TextView) findViewById(R.id.editar_martes_0);
        tvMartes2 = (TextView) findViewById(R.id.editar_martes_1);
        tvMiercoles1 = (TextView) findViewById(R.id.editar_miercoles_0);
        tvMiercoles2 = (TextView) findViewById(R.id.editar_miercoles_1);
        */
        spinner = (Spinner) findViewById(R.id.materia_spinner);
        salon = (EditText) findViewById(R.id.editar_Salon);
        edificio = (EditText) findViewById(R.id.editar_Edificio);
        actualizar = (FloatingActionButton) findViewById(R.id.editar_fab);
        mFloatLabelEdificio = (TextInputLayout) findViewById(R.id.float_label_edificio);
        mFloatLabelSalon = (TextInputLayout) findViewById(R.id.float_label_salon);
        texto = (TextView) findViewById(R.id.editar_textView);
        texto.setTextColor(datos.getColorSelected());

        adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_item,//Layout simple
                Horario_OpenHelper.getInstance(EditarHorarioActivity.this).obtenerHorario(),
                new String[]{Horario_Script.ColumnEntradas.MATERIA},
                new int[]{android.R.id.text1},
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursor c1 = (Cursor) parent.getItemAtPosition(position);
                edificioSelection = c1.getString(
                        c1.getColumnIndex(Horario_Script.ColumnEntradas.EDIFICIO));
                salonSelection = c1.getString(
                        c1.getColumnIndex(Horario_Script.ColumnEntradas.SALON));
                salon.setText(salonSelection.substring(7));
                edificio.setText(edificioSelection.substring(10));
                /*
                if(c1.getString(c1.getColumnIndex(Horario_Script.ColumnEntradas.LUNES)).equals("")){
                    lunes = "--:-- - --:--";
                }else{
                    lunes = c1.getString(c1.getColumnIndex(Horario_Script.ColumnEntradas.LUNES));
                }
                tvLunes1.setText(lunes.substring(0, 5));
                tvLunes2.setText(lunes.substring(8, 13));
                if(c1.getString(c1.getColumnIndex(Horario_Script.ColumnEntradas.MARTES)).equals("")){
                    martes = "--:-- - --:--";
                }else{
                    martes = c1.getString(c1.getColumnIndex(Horario_Script.ColumnEntradas.MARTES));
                }
                tvMartes1.setText(lunes.substring(0, 5));
                tvMartes2.setText(lunes.substring(8, 13));
                */
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Nada
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizar(v);
            }
        });
        /*
        tvLunes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = 0, minute = 0;
                try {
                    hour = Integer.valueOf(lunes.substring(0, 2));
                    minute = Integer.valueOf(lunes.substring(3, 5));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                new TimePickerDialog(EditarHorarioActivity.this, timeLunes1, hour, minute, true).show();
            }
        });

        tvLunes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = 0, minute = 0;
                try {
                    hour = Integer.valueOf(lunes.substring(8, 10));
                    minute = Integer.valueOf(lunes.substring(11, 13));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                new TimePickerDialog(EditarHorarioActivity.this, timeLunes2, hour, minute, true).show();
            }
        });
        */
    }

    public void actualizar(View view){
        mFloatLabelEdificio.setError(null);
        mFloatLabelSalon.setError(null);

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(edificio.getText().toString())) {
            mFloatLabelEdificio.setError(getString(R.string.error_campo_requerido));
            if(focusView == null)
                focusView = mFloatLabelEdificio;
            cancel = true;
        }
        if (TextUtils.isEmpty(salon.getText().toString())) {
            mFloatLabelSalon.setError(getString(R.string.error_campo_requerido));
            if(focusView == null)
                focusView = mFloatLabelSalon;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            Cursor selection = (Cursor) spinner.getSelectedItem();
            Horario_OpenHelper.getInstance(EditarHorarioActivity.this).
                    actualizarHorario("EDIFICIO: "+edificio.getText().toString(),
                            "SALÓN: "+salon.getText().toString(),
                            selection.getString(selection.getColumnIndex(Horario_Script.
                                    ColumnEntradas.MATERIA)));
            Snackbar.make(view, "¡Actualizado!", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(EditarHorarioActivity.this, HorarioOffline.class));
        EditarHorarioActivity.this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home){
            startActivity(new Intent(EditarHorarioActivity.this, HorarioOffline.class));
            EditarHorarioActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
    /*
    TimePickerDialog.OnTimeSetListener timeLunes1 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute){
            tvLunes1.setText(hour+":"+minute);
        }
    };

    TimePickerDialog.OnTimeSetListener timeLunes2 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute){
            tvLunes2.setText(hour+":"+minute);
        }
    };
    */

}
