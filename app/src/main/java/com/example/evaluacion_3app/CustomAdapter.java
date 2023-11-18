package com.example.evaluacion_3app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Model> modelArrayList;

    public CustomAdapter (Context context, ArrayList<Model> modelArrayList){

        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @Override
    public int getCount(){
        return modelArrayList.size();
    }

    @Override
    public Object getItem(int position){
        return modelArrayList.get(position);
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView ==  null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item, null, true);

            holder.tvlab = (TextView) convertView.findViewById(R.id.lab);
            holder.tvnombre = (TextView) convertView.findViewById(R.id.nombre);
            holder.tvdesc = (TextView) convertView.findViewById(R.id.desc);
            holder.tvrut = (TextView) convertView.findViewById(R.id.rut);
            holder.tvfecha = (TextView) convertView.findViewById(R.id.fecha);
            holder.tvhora = (TextView) convertView.findViewById(R.id.hora);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvlab.setText("Laboratorio: " + modelArrayList.get(position).getNombreLaboratorio());
        holder.tvnombre.setText("Nombre: " + modelArrayList.get(position).getNombre());
        holder.tvdesc.setText("Descripcion: " + modelArrayList.get(position).getDescripcion());
        holder.tvrut.setText("Rut: " + modelArrayList.get(position).getRut());
        holder.tvfecha.setText("Fecha: " + modelArrayList.get(position).getFecha());
        holder.tvhora.setText("Hora: " + modelArrayList.get(position).getHora());

        return convertView;
    }
    private class ViewHolder{
        protected TextView tvlab, tvnombre, tvdesc, tvrut, tvfecha, tvhora;
    }
}
