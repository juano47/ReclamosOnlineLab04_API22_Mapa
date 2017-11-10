package padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

import padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.dao.ReclamoDaoHTTP;
import padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.modelo.Reclamo;
import padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.modelo.TipoReclamo;


public class FormReclamo extends AppCompatActivity {

    EditText frmReclamoTitulo;
    EditText frmReclamoDetalle;
    Spinner frmReclamoSpinner;
    Button btnGuardar;
    Button btnelegirLugar;
    Button btnCancelar;
    Button btnEliminar;
    ReclamoDaoHTTP daoHTTP = new ReclamoDaoHTTP();
    List<TipoReclamo> tipoReclamos;
    ArrayAdapter adapterTiposReclamos;
    Integer req;
    Reclamo nuevoReclamo = new Reclamo();

    private GoogleMap mapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_reclamo);

        btnGuardar = (Button) findViewById(R.id.frmReclamoGuardar);
        btnelegirLugar = (Button) findViewById(R.id.elegirLugar);
        btnCancelar = (Button) findViewById(R.id.frmReclamoCancelar);
        btnEliminar = (Button) findViewById(R.id.frmReclamoEliminar);
        frmReclamoTitulo = (EditText) findViewById(R.id.frmReclamoTextReclamo);
        frmReclamoDetalle = (EditText) findViewById(R.id.frmReclamoTextDetReclamo);
        frmReclamoSpinner = (Spinner) findViewById(R.id.frmReclamoCmbTipo);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        tipoReclamos = daoHTTP.tiposReclamo();
        adapterTiposReclamos = new ArrayAdapter(FormReclamo.this, android.R.layout.simple_spinner_item, tipoReclamos);
        frmReclamoSpinner.setAdapter(adapterTiposReclamos);

        Intent i = getIntent();
         req = i.getIntExtra("REQUEST",-1);

        if( req != -1){
            if(req == MainActivity.EDITAR_RECLAMO){
                Reclamo r = (Reclamo)i.getParcelableExtra("RECLAMO");
                nuevoReclamo = r;
                frmReclamoTitulo.setText(r.getTitulo());
                frmReclamoDetalle.setText(r.getDetalle());
                Integer pos = 0;

                //Un for para esto? Java es crueldad
                for(TipoReclamo t: tipoReclamos){
                    if(t.getId() == r.getTipo().getId())
                        break;
                    pos++;
                }

                frmReclamoSpinner.setSelection(pos);

            }
        }

        btnelegirLugar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Intent abrirMapa = new Intent(FormReclamo.this, MapsActivity.class);
                startActivity(abrirMapa);

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultado = getIntent();
                resultado.putExtra("RESULTADO","Cancelado" );
                setResult(RESULT_CANCELED, resultado);
                finish();


            }
        });


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nuevoReclamo.setTitulo( frmReclamoTitulo.getText().toString());
                nuevoReclamo.setDetalle( frmReclamoDetalle.getText().toString());
                nuevoReclamo.setTipo( (TipoReclamo) frmReclamoSpinner.getSelectedItem());
                nuevoReclamo.setEstado( daoHTTP.getEstadoById(1));
                nuevoReclamo.setUbicacion(new LatLng(-180, 180));

                if(req != MainActivity.EDITAR_RECLAMO){
                nuevoReclamo.setFecha( new Date());
                nuevoReclamo.setId( daoHTTP.reclamos().get(daoHTTP.reclamos().size() - 1).getId() + 1);
                }


                Intent resultado = getIntent();
                resultado.putExtra("RECLAMO",nuevoReclamo );
                resultado.putExtra("RESULTADO","OK" );
                setResult(RESULT_OK, resultado);
                finish();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {
                //seguir aca
            }
        });
    }
}
