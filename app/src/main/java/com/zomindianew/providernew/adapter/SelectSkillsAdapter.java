package com.zomindianew.providernew.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zomindianew.R;

import com.zomindianew.bean.AddSkillBean;


import java.util.ArrayList;


public class SelectSkillsAdapter extends RecyclerView.Adapter<SelectSkillsAdapter.ViewHolder> {


    private Context context;
    private ArrayList<AddSkillBean> categoryList;
    private SelectSkillsAdapter.CustomItemClickListener customClickListener;

    public SelectSkillsAdapter(Context context, ArrayList<AddSkillBean> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public SelectSkillsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.row_item_skill_adapter, null);
        SelectSkillsAdapter.ViewHolder vh = new SelectSkillsAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectSkillsAdapter.ViewHolder viewHolder, int i) {

//        if (categoryList.get(i).isSetSlected()) {
//            viewHolder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.green));
//        } else {
//            viewHolder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
//        }


        viewHolder.categoryTV.setText(categoryList.get(i).getCategory_name());
        viewHolder.subCategoryTV.setText(categoryList.get(i).getSub_category_name());


    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView categoryTV;
        public TextView subCategoryTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryTV = itemView.findViewById(R.id.categoryTV);
            subCategoryTV = itemView.findViewById(R.id.subCategoryTV);


        }

        @Override
        public void onClick(View v) {
            if (customClickListener != null) {
                customClickListener.onItemClick(v, getAdapterPosition());
            }

        }
    }

    public interface CustomItemClickListener {
        void onItemClick(View view, int position);

    }

    public void setOntemClickListener(SelectSkillsAdapter.CustomItemClickListener mItemClick) {
        this.customClickListener = mItemClick;
    }
}

