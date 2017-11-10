package padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.dao;

import java.util.List;

import padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.modelo.Estado;
import padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.modelo.Reclamo;
import padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.modelo.TipoReclamo;


/**
 * Created by mdominguez on 26/10/17.
 */

public interface ReclamoDao {
    public List<Estado> estados();
    public List<TipoReclamo> tiposReclamo();
    public List<Reclamo> reclamos();
    public Estado getEstadoById(Integer id);
    public TipoReclamo getTipoReclamoById(Integer id);

    public void crear(Reclamo r);
    public void actualizar(Reclamo r);
    public void borrar(Reclamo r);
    }
