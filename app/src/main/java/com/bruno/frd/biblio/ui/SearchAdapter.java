package com.bruno.frd.biblio.ui;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bruno.frd.biblio.R;
import com.bruno.frd.biblio.data.api.model.SearchDisplayList;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<SearchDisplayList> mItems;

    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    interface OnItemClickListener {

        void onItemClick(SearchDisplayList clickedAppointment);

        //void onRenewBook(SearchDisplayList canceledAppointment);

    }

    public SearchAdapter(Context context, List<SearchDisplayList> items) {
        mItems = items;
        mContext = context;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }



    public void swapItems(List<SearchDisplayList> results) {
        if (results == null) {
            mItems = new ArrayList<>(0);
        } else {
            mItems = results;
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchDisplayList book = mItems.get(position);

        View statusIndicator = holder.statusIndicator;

        // estado: se colorea indicador según el estado
        /*switch (appointment.getStatus()) {
            case "ND":
                // ocultar botón
                //holder.cancelButton.setVisibility(View.GONE);
                statusIndicator.setBackgroundResource(R.color.porvencerStatus);
                break;
            case "D":
                // mostrar botón
                //holder.cancelButton.setVisibility(View.GONE);
                statusIndicator.setBackgroundResource(R.color.vencidaStatus);
                break;
        }*/

        String cText;
        if (book.getCopyFree() == 1) {
            cText = " copia\ndisponible";
        } else {
            cText = " copias\ndisponibles";
        }
        String fCopies = String.valueOf(book.getCopyFree()) + cText;

        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        holder.copyFree.setText(fCopies);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.search_item_list, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView author;
        public TextView topic2;
        public TextView bibid;
        public TextView copyFree;
        public View statusIndicator;

        public ViewHolder(View itemView) {
            super(itemView);

            statusIndicator = itemView.findViewById(R.id.indicator_book_status);
            //topic2 = (TextView) itemView.findViewById(R.id.book_info);
            //bibid = (TextView) itemView.findViewById(R.id.text_medical_service);
            title = (TextView) itemView.findViewById(R.id.book_name);
            author = (TextView) itemView.findViewById(R.id.author_name);
            copyFree = (TextView) itemView.findViewById(R.id.book_info);

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