package com.pruebas.ades.renobar.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Restaurante implements Parcelable {

    private String nombre;
    private String direccion;
    private String kmts;
    private String imagen;
    private String url;

    public Restaurante(String nombre, String direccion, String kmts,String imagen,String url) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.imagen = imagen;
        this.kmts=kmts;
        this.url=url;
    }

    protected Restaurante(Parcel in) {
        nombre = in.readString ();
        direccion = in.readString ();
        kmts = in.readString ();
        imagen = in.readString ();
        url=in.readString();
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString ( nombre );
        dest.writeString ( direccion );
        dest.writeString ( kmts );
        dest.writeString ( imagen );
        dest.writeString ( url );
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator <Restaurante> CREATOR = new Creator <Restaurante> () {
        @Override
        public Restaurante createFromParcel(Parcel in) {
            return new Restaurante ( in );
        }

        @Override
        public Restaurante[] newArray(int size) {
            return new Restaurante[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKmts() {
        return kmts;
    }

    public void setKmts(String kmts) {
        this.kmts = kmts;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }



}
