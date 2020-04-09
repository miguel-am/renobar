package com.pruebas.ades.renobar.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Restaurante implements Parcelable {

    private String nombre;
    private String direccion;
    private String kmts;
    private int imagen;

    public Restaurante(String nombre, String direccion, String kmts,int imagen) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.imagen = imagen;
        this.kmts=kmts;
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

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString ( this.nombre );
        dest.writeString ( this.direccion );
        dest.writeInt ( this.imagen );
    }

    protected Restaurante(Parcel in) {
        this.nombre = in.readString ();
        this.direccion = in.readString ();
        this.imagen = in.readInt ();
    }

    public static final Parcelable.Creator <Restaurante> CREATOR = new Parcelable.Creator <Restaurante> () {
        @Override
        public Restaurante createFromParcel(Parcel source) {
            return new Restaurante ( source );
        }

        @Override
        public Restaurante[] newArray(int size) {
            return new Restaurante[size];
        }
    };
}
