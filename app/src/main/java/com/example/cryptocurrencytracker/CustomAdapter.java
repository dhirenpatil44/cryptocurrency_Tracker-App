package com.example.cryptocurrencytracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CurrencyModel> currencyModelArrayList;
    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public CustomAdapter(Context context, ArrayList<CurrencyModel> currencyModelArrayList) {
        this.context = context;
        this.currencyModelArrayList = currencyModelArrayList;
    }

    public void filterArrayList(ArrayList<CurrencyModel> currencyModels){
        currencyModelArrayList = currencyModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.currency_item,parent,false);
        return new CustomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  CustomAdapter.ViewHolder holder, int position) {
        CurrencyModel currencyModel = currencyModelArrayList.get(position);

        holder.tvName.setText(currencyModel.getName());
        holder.tvSymbol.setText(currencyModel.getSymbol());
        holder.tvPrice.setText("$ "+ decimalFormat.format(currencyModel.getPrice()));
    }

    @Override
    public int getItemCount() {
        return currencyModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSymbol, tvName, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvSymbol = itemView.findViewById(R.id.tvSymbol);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
