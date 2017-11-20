package padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;

import padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.dao.ReclamoDao;
import padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.dao.ReclamoDaoHTTP;
import padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.modelo.Reclamo;


public class MainActivity extends AppCompatActivity {
    private ReclamoDao daoReclamo;
    private ListView listViewReclamos;
    private List<Reclamo> listaReclamos;
    private ReclamoAdapter adapter;
    private Button btnNuevoReclamo;
    private Button btnVisualizarMapa;
    public static int NUEVO_RECLAMO =1;
    public static int EDITAR_RECLAMO = 2;
    public static int VER_RECLAMOS_MAPA = 987;
    private Intent intentForReclamo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        daoReclamo = new ReclamoDaoHTTP();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewReclamos = (ListView) findViewById(R.id.mainListaReclamos);
        listaReclamos = new ArrayList<>();
        adapter = new ReclamoAdapter(this, listaReclamos);
        new ReclamoAdapter(MainActivity.this, listaReclamos);
        listViewReclamos.setAdapter(adapter);

        listViewReclamos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Reclamo r = (Reclamo) listViewReclamos.getItemAtPosition(pos);
                Intent i = new Intent(MainActivity.this,FormReclamo.class);
                i.putExtra("RECLAMO",r);
                i.putExtra("REQUEST",EDITAR_RECLAMO);
                startActivityForResult(i,EDITAR_RECLAMO);
                return true;
            }
        });

        Runnable r = new Runnable() {
            @Override
            public void run() {
                List<Reclamo> rec = daoReclamo.reclamos();
                listaReclamos.clear();
                listaReclamos.addAll(rec);
                runOnUiThread(new Runnable() {
                    public void run() {

                        adapter.notifyDataSetChanged();

                    }
                });
            }
        };
        Thread t = new Thread(r);
        t.start();
        btnNuevoReclamo = (Button) findViewById(R.id.btnNuevoReclamo);
        btnNuevoReclamo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                intentForReclamo = new Intent(MainActivity.this,FormReclamo.class);
                intentForReclamo.putExtra("REQUEST", NUEVO_RECLAMO);
                startActivityForResult(intentForReclamo, NUEVO_RECLAMO);
            }
        });


        btnVisualizarMapa = (Button) findViewById(R.id.btnVerReclamos);

        btnVisualizarMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Reclamo> reclamoArrayList = new ArrayList<Reclamo>();
                reclamoArrayList.addAll(listaReclamos);
                Intent verEnMapaIntent = new Intent(MainActivity.this,MapsActivity.class);
                verEnMapaIntent.putExtra("REQUEST_CODE",VER_RECLAMOS_MAPA);
                verEnMapaIntent.putParcelableArrayListExtra("RECLAMOS",reclamoArrayList);
                startActivityForResult(verEnMapaIntent,VER_RECLAMOS_MAPA);//Pensandolo bien no deberia ser "ForResult" ya que no
                                                                            //queremos obtener nada de la actividad en este modo
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        if(resultCode == RESULT_OK){
            String opResult = "";
            Reclamo r = (Reclamo) data.getParcelableExtra("RECLAMO");
            if(requestCode == NUEVO_RECLAMO){
                daoReclamo.crear(r);
                opResult = "Reclamo creado";
            }
            else if (requestCode == EDITAR_RECLAMO) {
                //
                String op = data.getStringExtra("OPERACION");
                if(op.equals("EDITAR")){
                daoReclamo.actualizar(r);
                    opResult = "Reclamo modificado";
                }
                else{

            //Eliminar reclamo
                    daoReclamo.borrar(r);
                    opResult = "Reclamo eliminado";
            }

        }
        else if(requestCode == VER_RECLAMOS_MAPA){
                //
            }


            Runnable runnable = new UpdateDataSetRunnable(opResult);


            Thread t = new Thread(runnable);
            t.start();
    }
    }


    private class UpdateDataSetRunnable implements Runnable{

        private String _msg;
        UpdateDataSetRunnable(String msg){
            _msg = msg;
        }
        @Override
        public void run() {
            List<Reclamo> rec = daoReclamo.reclamos();
            listaReclamos.clear();
            listaReclamos.addAll(rec);
            runOnUiThread(new Runnable() {
                public void run() {

                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this,_msg,Toast.LENGTH_LONG).show();

                }
            });
        }


    }
}
