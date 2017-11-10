package padula.delaiglesia.dam.isi.frsf.reclamosonlinelab04_api22_mapa.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mdominguez on 26/10/17.
 */

public class TipoReclamo implements Parcelable{
    private Integer id;
    private String tipo;

    public TipoReclamo() {

    }

    public TipoReclamo(Parcel in){
        this.id = in.readInt();
        this.tipo = in.readString();
    }

    public TipoReclamo(Integer id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return tipo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.id);
            parcel.writeString(this.tipo);
    }


    public static final Creator CREATOR = new Creator() {
        public TipoReclamo createFromParcel(Parcel in) {
            return new TipoReclamo(in);
        }

        public TipoReclamo[] newArray(int size) {
            return new TipoReclamo[size];
        }
    };
    /*public String toString() {
        return "TipoReclamo{" +
                "tipo='" + tipo + '\'' +
                '}';
    }*/
}
