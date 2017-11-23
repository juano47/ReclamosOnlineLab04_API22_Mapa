package padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.dao;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.modelo.Estado;
import padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.modelo.Reclamo;
import padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.modelo.TipoReclamo;


/**
 * Created by mdominguez on 26/10/17.
 */

public class ReclamoDaoHTTP implements ReclamoDao {

    private List<TipoReclamo> tiposReclamos = null;
    private List<Estado> tiposEstados = null;
    private List<Reclamo> listaReclamos = null;
    private String server;
    private MyGenericHTTPClient cliente;

    public ReclamoDaoHTTP(){
        //para emulador
        //server="http://10.0.2.2:3000";

        //para celular
        server="http://192.168.42.100:3000";
        //server = "http://192.168.0.107:3000"; //(nico)
        cliente = new MyGenericHTTPClient(server);
    }

    public ReclamoDaoHTTP(String server){
        this.server=server;
        cliente=new MyGenericHTTPClient(server);
    }


    @Override
    public List<Estado> estados() {
        if(tiposEstados!=null && tiposEstados.size()>0) return this.tiposEstados;
        else{
            String estadosJSON = cliente.getAll("estado");
            try {
                JSONArray arr = new JSONArray(estadosJSON);
                for(int i=0;i<arr.length();i++){
                    JSONObject unaFila = arr.getJSONObject(i);
                    tiposEstados.add(new Estado(unaFila.getInt("id"),unaFila.getString("tipo")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tiposEstados;
    }

    @Override
    public List<TipoReclamo> tiposReclamo() {
        tiposReclamos = new ArrayList<TipoReclamo>();
        if(tiposReclamos!=null && tiposReclamos.size()>0) return this.tiposReclamos;
        else{
            String estadosJSON = cliente.getAll("tipo");
            try {
                JSONArray arr = new JSONArray(estadosJSON);
                for(int i=0;i<arr.length();i++){
                    JSONObject unaFila = arr.getJSONObject(i);
                    tiposReclamos.add(new TipoReclamo(unaFila.getInt("id"),unaFila.getString("tipo")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tiposReclamos;
    }

    @Override
    public List<Reclamo> reclamos() {
        listaReclamos = new ArrayList<>();
        String reclamosJSON = cliente.getAll("reclamo");
        try {
            JSONArray arr = new JSONArray(reclamosJSON);
            for(int i=0;i<arr.length();i++){
                JSONObject unaFila = arr.getJSONObject(i);
                Reclamo recTmp = new Reclamo();
                recTmp.setId(unaFila.getInt("id"));
                recTmp.setTitulo(unaFila.getString("titulo"));
                recTmp.setDetalle(unaFila.getString("detalle"));
                recTmp.setTipo(this.getTipoReclamoById(unaFila.getInt("tipoId")));
                recTmp.setEstado(this.getEstadoById(unaFila.getInt("estadoId")));
                try{
                double lat = unaFila.getDouble("latitud");
                double longitud = unaFila.getDouble("longitud");
                LatLng ubicacion = new LatLng(lat,longitud);
                recTmp.setUbicacion(ubicacion);
                }
                catch (Exception ex)
                {
                    recTmp.setUbicacion(new LatLng(-31.636050, -60.701071));
                }
                listaReclamos.add(recTmp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listaReclamos;
    }

    @Override
    public Estado getEstadoById(Integer id){
        Estado objResult =new Estado(99,"no encontrado");
        if(this.tiposEstados!=null){
            for(Estado e:tiposEstados){
                if(e.getId()==id) return e;
            }
        }else{
            String estadoJSON = cliente.getById("estado",id);
            try {
                JSONObject unaFila = new JSONObject(estadoJSON);
                objResult = new Estado(unaFila.getInt("id"),unaFila.getString("tipo"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return objResult;
    }

    @Override
    public TipoReclamo getTipoReclamoById(Integer id){
        TipoReclamo objResult =new TipoReclamo(99,"NO ENCONTRADO");
        if(this.tiposEstados!=null){
            for(TipoReclamo e:tiposReclamos){
                if(e.getId()==id) return e;
            }
        }else{
            String estadoJSON = cliente.getById("tipo",id);
            try {
                JSONObject unaFila = new JSONObject(estadoJSON);
                objResult = new TipoReclamo(unaFila.getInt("id"),unaFila.getString("tipo"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return objResult;
    }

    @Override
    public void crear(Reclamo r) {
        cliente.post("reclamo",r.toJSON());
    }

    @Override
    public void actualizar(Reclamo r) {
        cliente.put("reclamo/" + r.getId().toString(),r.toJSON());
    }

    @Override
    public void borrar(Reclamo r) {
        cliente.delete("reclamo",r.getId());
    }
}
