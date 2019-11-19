package com.bruno.frd.biblio.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bruno.frd.biblio.R;
import com.bruno.frd.biblio.data.api.model.PrestamosDisplayList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrestamosAdapter extends RecyclerView.Adapter<PrestamosAdapter.ViewHolder> {

    private List<PrestamosDisplayList> mItems;

    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    interface OnItemClickListener {

        void onItemClick(PrestamosDisplayList clickedAppointment);

        void onRenewBook(PrestamosDisplayList canceledAppointment);

    }

    public PrestamosAdapter(Context context, List<PrestamosDisplayList> items) {
        mItems = items;
        mContext = context;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }



    public void swapItems(List<PrestamosDisplayList> appointments) {
        if (appointments == null) {
            mItems = new ArrayList<>(0);
        } else {
            mItems = appointments;
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PrestamosDisplayList appointment = mItems.get(position);

        View statusIndicator = holder.statusIndicator;

        // estado: se colorea indicador según el estado
        switch (appointment.getStatus()) {
            case "Renovable":
                // mostrar botón
                holder.cancelButton.setVisibility(View.VISIBLE);
                statusIndicator.setBackgroundResource(R.color.vencidaStatus);
                break;
            case "Vencido":
                // ocultar botón
                holder.cancelButton.setVisibility(View.GONE);
                statusIndicator.setBackgroundResource(R.color.porvencerStatus);
                break;
            case "No renovable":
                // mostrar botón
                holder.cancelButton.setVisibility(View.GONE);
                statusIndicator.setBackgroundResource(R.color.vencidaStatus);
                break;
        }

        Date date = appointment.getDueBackDt();

        String fDate = "Vencimiento\n" + formatDate(date);

        holder.date.setText(fDate);
        holder.service.setText(appointment.getTitle());
        holder.doctor.setText(appointment.getAuthor());
        holder.medicalCenter.setText(appointment.getStatus());
    }

    private String formatDate(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public PrestamosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.main_item_list, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView date;
        public TextView service;
        public TextView doctor;
        public TextView medicalCenter;
        public Button cancelButton;
        public View statusIndicator;

        public ViewHolder(View itemView) {
            super(itemView);

            statusIndicator = itemView.findViewById(R.id.indicator_appointment_status);
            date = (TextView) itemView.findViewById(R.id.text_appointment_date);
            service = (TextView) itemView.findViewById(R.id.text_medical_service);
            doctor = (TextView) itemView.findViewById(R.id.text_doctor_name);
            medicalCenter = (TextView) itemView.findViewById(R.id.text_medical_center);
            cancelButton = (Button) itemView.findViewById(R.id.button_cancel_appointment);

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mOnItemClickListener.onRenewBook(mItems.get(position));
                    }
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                mOnItemClickListener.onItemClick(mItems.get(position));
            }
        }

    }
}
