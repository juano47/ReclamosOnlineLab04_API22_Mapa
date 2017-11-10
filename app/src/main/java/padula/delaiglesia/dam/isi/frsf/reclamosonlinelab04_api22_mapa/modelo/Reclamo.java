package padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.modelo;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by mdominguez on 26/10/17.
 */

public class Reclamo implements Parcelable {
    private Integer id;
    private String titulo;
    private String detalle;
    private Date fecha;
    private TipoReclamo tipo;
    private LatLng ubicacion;

    private Estado estado;


    // private foto
    // private audio
    public Reclamo() {
    }

    public Reclamo(Parcel in){
        this.id = in.readInt();
        this.titulo = in.readString();
        this.detalle = in.readString();
        this.fecha = (Date)in.readSerializable();
        this.estado = in.readParcelable(Estado.class.getClassLoader());
        this.tipo = in.readParcelable(TipoReclamo.class.getClassLoader());
        this.ubicacion = in.readParcelable(LatLng.class.getClassLoader());
    }

    public Reclamo(Integer id, String titulo, String detalle, Date fecha, TipoReclamo tipo, Estado estado) {
        this.id = id;
        this.titulo = titulo;
        this.detalle = detalle;
        this.fecha = fecha;
        this.tipo = tipo;
        this.estado = estado;
        this.setUbicacion(new LatLng(-180, 180)); //Por default hasta que arreglemos lo del mapa
    }

    public Reclamo(Integer id, String titulo, String detalle, Date fecha, TipoReclamo tipo, Estado estado, LatLng ubicacion) {
        this.id = id;
        this.titulo = titulo;
        this.detalle = detalle;
        this.fecha = fecha;
        this.tipo = tipo;
        this.estado = estado;
        this.setUbicacion(ubicacion);
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public TipoReclamo getTipo() {
        return tipo;
    }

    public void setTipo(TipoReclamo tipo) {
        this.tipo = tipo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }


    public LatLng getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(LatLng ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String toJSON() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("id",this.getId());
            obj.put("titulo",this.getTitulo());
            obj.put("detalle",this.getDetalle());
            obj.put("tipoId",this.getTipo().getId());
            obj.put("estadoId",this.getEstado().getId());
            obj.put("fecha",this.getFecha());
            obj.put("latitud",this.ubicacion.latitude);
            obj.put("longitud",this.ubicacion.longitude);

            return obj.toString();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.titulo);
        parcel.writeString(this.detalle);
        parcel.writeSerializable(this.fecha);
        parcel.writeParcelable(this.estado,i);
        parcel.writeParcelable(this.tipo,i);
        parcel.writeParcelable(this.ubicacion,i);
    }


    public static final Creator CREATOR = new Creator() {
        public Reclamo createFromParcel(Parcel in) {
            return new Reclamo(in);
        }

        public Reclamo[] newArray(int size) {
            return new Reclamo[size];
        }
    };
}

