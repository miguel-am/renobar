package com.pruebas.ades.renobar.AdaptadorLista;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pruebas.ades.renobar.R;
import com.pruebas.ades.renobar.Model.Restaurante;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListaAdapter extends BaseAdapter {

    private List <Restaurante> restaurante;
    private Context contexto;
    private TextView nombre, direccion, kmts;
    private Uri url;
    private ImageView imagen;

    public ListaAdapter(List <Restaurante> restaurante, Context contexto) {
        this.restaurante = restaurante;
        this.contexto = contexto;

    }

    @Override
    public int getCount() {
        return restaurante.size ();
    }

    @Override
    public Object getItem(int position) {
        return restaurante.get ( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView == null){

            LayoutInflater inflater=( LayoutInflater )contexto.getSystemService ( Context.LAYOUT_INFLATER_SERVICE );
            convertView=inflater.inflate ( R.layout.activity_adapter_view,null );
            nombre=convertView.findViewById ( R.id.tvNomRestaurante );
            direccion=convertView.findViewById ( R.id.tvDireRestaurante );
            kmts=convertView.findViewById ( R.id.tvDistKmts );
            imagen=convertView.findViewById ( R.id.imgAdapter );

            nombre.setText ( restaurante.get ( position ).getNombre () );
            direccion.setText ( restaurante.get ( position ).getDireccion () );
            kmts.setText ( restaurante.get ( position ).getKmts () );

            kmts.setCompoundDrawablesWithIntrinsicBounds (0,0, R.drawable.ic_place_white_24dp,0 );
            Picasso.get ().load ( restaurante.get ( position ).getImagen () ).into(imagen);


        }

        return convertView;
    }



}
