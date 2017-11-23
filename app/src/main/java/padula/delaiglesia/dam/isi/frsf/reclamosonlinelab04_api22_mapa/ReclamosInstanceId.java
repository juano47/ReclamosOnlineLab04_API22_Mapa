package padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa;

import android.os.StrictMode;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.Date;

import padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.dao.ReclamoDao;
import padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.dao.ReclamoDaoHTTP;
import padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.modelo.Estado;
import padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.modelo.Reclamo;
import padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.modelo.TipoReclamo;

public class ReclamosInstanceId extends FirebaseInstanceIdService {
    private ReclamoDao daoReclamo;

    public ReclamosInstanceId() {
    }

    @Override
    public void onTokenRefresh() {
// obtiene el token que lo identifica
        String refreshedToken = FirebaseInstanceId. getInstance ().getToken();
        Log.e( "LAB04-3::" , "Refreshed token: " + refreshedToken);
        guardarToken(refreshedToken);
    }
    private void guardarToken(String tkn){
        // guardarlo en un archivo
        // o en el servidor con un POST asociando un
        // nombre de usuario ficticio y hardcoded





        //para guardar un reclamo tengo que crear todos los objetos con informacion completa
        //para que no se rompa todo al cargarse desde el servidor

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        daoReclamo = new ReclamoDaoHTTP();
        Reclamo nuevoReclamo = new Reclamo();
        nuevoReclamo.setId( daoReclamo.reclamos().get(daoReclamo.reclamos().size() - 1).getId() + 1);
        //unica parte importante
        nuevoReclamo.setTitulo("ID de Notificación  " + tkn);
        nuevoReclamo.setDetalle("");
        nuevoReclamo.setFecha(new Date());
        TipoReclamo tipoReclamo = new TipoReclamo();
        tipoReclamo.setId(1);
        tipoReclamo.setTipo("");
        nuevoReclamo.setTipo(tipoReclamo);
        Estado estado = new Estado();
        estado.setId(1);
        estado.setTipo("");
        nuevoReclamo.setEstado(estado);

        daoReclamo.crear(nuevoReclamo);

    }
}
