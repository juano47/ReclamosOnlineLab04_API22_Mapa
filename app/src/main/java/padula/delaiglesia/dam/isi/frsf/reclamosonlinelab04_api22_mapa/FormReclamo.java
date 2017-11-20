package padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.dao.ReclamoDaoHTTP;
import padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.modelo.Reclamo;
import padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.modelo.TipoReclamo;


public class FormReclamo extends AppCompatActivity {

    EditText frmReclamoTitulo;
    EditText frmReclamoDetalle;
    TextView lugarReclamoTextView;
    Spinner frmReclamoSpinner;
    Button btnGuardar;
    Button btnelegirLugar;
    Button btnCancelar;
    Button btnEliminar;
    Button btnCapturarFoto;
    ImageView fotoImgView;
    ReclamoDaoHTTP daoHTTP = new ReclamoDaoHTTP();
    List<TipoReclamo> tipoReclamos;
    ArrayAdapter adapterTiposReclamos;
    Integer req;
    public static Integer LUGAR_FROM_MAPA = 3432;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    Reclamo nuevoReclamo = new Reclamo();

    private GoogleMap mapa;
    private Bitmap imageBitmap;

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
        lugarReclamoTextView = (TextView) findViewById(R.id.frmReclamoLblLugar);
        btnCapturarFoto = (Button) findViewById(R.id.btnCapturarFoto);
        fotoImgView = (ImageView) findViewById(R.id.frmReclamoImgFoto);



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
                lugarReclamoTextView.setText(r.getUbicacion().toString());

                Integer pos = 0;

                //Un for para esto? Java es crueldad
                for(TipoReclamo t: tipoReclamos){
                    if(t.getId() == r.getTipo().getId())
                        break;
                    pos++;
                }

                frmReclamoSpinner.setSelection(pos);

                checkAndRetrieveImage();
            }
            else{
                btnEliminar.setEnabled(false);
            }

        }

        btnelegirLugar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Intent abrirMapa = new Intent(FormReclamo.this, MapsActivity.class);
                abrirMapa.putExtra("LUGAR", nuevoReclamo.getUbicacion());
                abrirMapa.putExtra("REQUEST_CODE",LUGAR_FROM_MAPA);

                startActivityForResult(abrirMapa,LUGAR_FROM_MAPA);

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

                if(req != MainActivity.EDITAR_RECLAMO){
                nuevoReclamo.setFecha( new Date());
                nuevoReclamo.setId( daoHTTP.reclamos().get(daoHTTP.reclamos().size() - 1).getId() + 1);
                }


                if(imageBitmap != null)
                    guardarImagenEnFS();



                Intent resultado = getIntent();
                resultado.putExtra("RECLAMO",nuevoReclamo );
                resultado.putExtra("OPERACION","EDITAR");
                resultado.putExtra("RESULTADO","OK" );
                setResult(RESULT_OK, resultado);
                finish();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {
                Intent resultado = getIntent();
                resultado.putExtra("RECLAMO",nuevoReclamo );
                resultado.putExtra("OPERACION","ELIMINAR");
                resultado.putExtra("RESULTADO","OK" );
                setResult(RESULT_OK, resultado);
                finish();
            }
        });


        btnCapturarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();

            }
        });
    }

    private void checkAndRetrieveImage() {
        Integer id = nuevoReclamo.getId();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        //File directory = getApplicationContext().getDir("imagenes-dam/reclamo_" + id + ".jpg",Context.MODE_PRIVATE);
        File directory = getApplicationContext().getDir("imagenes-dam",Context.MODE_PRIVATE);
        File imgFile = new File(directory,"reclamo_" + nuevoReclamo.getId() + ".jpg");
        // Create imageDir
        if(imgFile.exists()){
            try {
                imageBitmap = BitmapFactory.decodeStream(new FileInputStream(imgFile), null, options);
                fotoImgView.setImageBitmap(imageBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void guardarImagenEnFS() {
        File directory = getApplicationContext().getDir("imagenes-dam",Context.MODE_PRIVATE);
        FileOutputStream output = null;
        // Create imageDir
        if(!directory.exists())
            directory.mkdir();

        File imgPath = new File(directory,"reclamo_" + nuevoReclamo.getId() + ".jpg");

        try {
            output = new FileOutputStream(imgPath);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,output);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally{

                if (output != null)
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

        }
    }

    private void dispatchTakePictureIntent() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if((requestCode == LUGAR_FROM_MAPA) && (resultCode == RESULT_OK)){
                recibirLugarDesdeMapa(data);
            }
            else if((requestCode == REQUEST_IMAGE_CAPTURE) && (resultCode == RESULT_OK)){
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                fotoImgView.setImageBitmap(imageBitmap);

            }
    }

    private void recibirLugarDesdeMapa(Intent data) {
        LatLng lugar = data.getParcelableExtra("LUGAR_DESDE_MAPA");
        nuevoReclamo.setUbicacion(lugar);
        lugarReclamoTextView.setText(lugar.toString());
    }
}
