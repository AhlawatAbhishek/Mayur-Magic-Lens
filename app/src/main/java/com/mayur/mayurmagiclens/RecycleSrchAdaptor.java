package com.mayur.mayurmagiclens;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleSrchAdaptor extends RecyclerView.Adapter<RecycleSrchAdaptor.ViewHolder> {
    private Context context;
    private ArrayList<RecycleSearchModel> searchModels;

    public RecycleSrchAdaptor(Context context, ArrayList<RecycleSearchModel> searchModels) {
        this.context = context;
        this.searchModels = searchModels;
    }

    @NonNull
    @Override
    public RecycleSrchAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.srch_res_recyc_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleSrchAdaptor.ViewHolder holder, int position) {
        RecycleSearchModel recycleSearchModel = searchModels.get(position);
        holder.titleTxtView.setText(recycleSearchModel.getrTitle());
        holder.desTextView.setText(recycleSearchModel.getrSnippet());
        holder.sinppetTxtView.setText(recycleSearchModel.getLink());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(recycleSearchModel.getLink()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTxtView, sinppetTxtView, desTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxtView = itemView.findViewById(R.id.RecTxtViewTitle);
            sinppetTxtView = itemView.findViewById(R.id.RecTxtViewSnippet);
            desTextView = itemView.findViewById(R.id.RecTxtViewDes);

        }
    }
}
