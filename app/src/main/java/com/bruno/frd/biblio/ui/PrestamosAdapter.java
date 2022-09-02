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

        void onItemClick(PrestamosDisplayList clickedItem);

        void onRenewBook(PrestamosDisplayList canceledItem);

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



    public void swapItems(List<PrestamosDisplayList> loans) {
        if (loans == null) {
            mItems = new ArrayList<>(0);
        } else {
            mItems = loans;
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PrestamosDisplayList loan = mItems.get(position);

        View statusIndicator = holder.statusIndicator;

        // estado: se colorea indicador según el estado
        switch (loan.getStatus()) {
            case "Renovable":
                // mostrar botón
                holder.renewButton.setVisibility(View.VISIBLE);
                statusIndicator.setBackgroundResource(R.color.vencidaStatus);
                break;
            case "Vencido":
                // ocultar botón
                holder.renewButton.setVisibility(View.GONE);
                statusIndicator.setBackgroundResource(R.color.porvencerStatus);
                break;
            case "No renovable":
                // mostrar botón
                holder.renewButton.setVisibility(View.GONE);
                statusIndicator.setBackgroundResource(R.color.vencidaStatus);
                break;
        }

        Date date = loan.getDueBackDt();

        String fDate = "Vencimiento\n" + formatDate(date);

        holder.date.setText(fDate);
        holder.title.setText(loan.getTitle());
        holder.author.setText(loan.getAuthor());
        holder.status.setText(loan.getStatus());
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
        public TextView title;
        public TextView author;
        public TextView status;
        public Button renewButton;
        public View statusIndicator;

        public ViewHolder(View itemView) {
            super(itemView);

            statusIndicator = itemView.findViewById(R.id.indicator_status);
            date = (TextView) itemView.findViewById(R.id.text_date);
            title = (TextView) itemView.findViewById(R.id.text_title);
            author = (TextView) itemView.findViewById(R.id.text_author);
            status = (TextView) itemView.findViewById(R.id.text_status);
            renewButton = (Button) itemView.findViewById(R.id.button_renew);

            renewButton.setOnClickListener(new View.OnClickListener() {
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
