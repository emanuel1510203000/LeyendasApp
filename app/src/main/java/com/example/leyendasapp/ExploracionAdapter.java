package com.example.leyendasapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExploracionAdapter extends RecyclerView.Adapter<ExploracionAdapter.ExploracionViewHolder> {

    private List<Leyenda> leyendaList;
    private OnExploracionListener listener;

    // Interfaz para los eventos de editar y eliminar
    public interface OnExploracionListener {
        void onEdit(int position);
        void onDelete(int position);
    }

    public ExploracionAdapter(List<Leyenda> leyendaList, OnExploracionListener listener) {
        this.leyendaList = leyendaList;
        this.listener = listener;
    }

    @Override
    public ExploracionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leyenda, parent, false);
        return new ExploracionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExploracionViewHolder holder, int position) {
        Leyenda leyenda = leyendaList.get(position);
        holder.nombreTextView.setText(leyenda.getNombre());
        holder.descripcionTextView.setText(leyenda.getDescripcion());

        // Setetear de botones
        holder.btnEditar.setOnClickListener(v -> listener.onEdit(position));
        holder.btnEliminar.setOnClickListener(v -> listener.onDelete(position));
    }

    @Override
    public int getItemCount() {
        return leyendaList.size();
    }

    public static class ExploracionViewHolder extends RecyclerView.ViewHolder {

        public TextView nombreTextView;
        public TextView descripcionTextView;
        public Button btnEditar;
        public Button btnEliminar;

        public ExploracionViewHolder(View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.tv_leyenda_nombre);
            descripcionTextView = itemView.findViewById(R.id.tv_leyenda_descripcion);
            btnEditar = itemView.findViewById(R.id.btn_editar);
            btnEliminar = itemView.findViewById(R.id.btn_eliminar);
        }
    }
}
