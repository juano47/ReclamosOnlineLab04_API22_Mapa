package padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.modelo.Reclamo;

/**
 * Created by martdominguez on 26/10/17.
 */

public class ReclamoAdapter extends ArrayAdapter<Reclamo> {

    private LayoutInflater layoutInflater;

    public ReclamoAdapter(Context ctx, List<Reclamo> lista) {
        super(ctx, 0, lista);
        layoutInflater = LayoutInflater.from(ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View filaNueva = convertView;
        if (filaNueva == null)
            filaNueva = layoutInflater.inflate(R.layout.fila_reclamo, parent, false);
        TextView tvNombre = (TextView) filaNueva.findViewById(R.id.lvReclamoTitulo);
        TextView tvTipoReclamo = (TextView) filaNueva.findViewById(R.id.lvReclamoTipo);
        TextView tvEstado = (TextView) filaNueva.findViewById(R.id.lvReclamoEstado);
        Reclamo reclamo = getItem(position);
        tvNombre.setText(reclamo.getTitulo());
        tvTipoReclamo.setText(reclamo.getTipo().getTipo());
        tvEstado.setText(reclamo.getEstado().getTipo());
        return filaNueva;
    }
}