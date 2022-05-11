package com.zomindianew.providernew.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zomindianew.R;
import com.zomindianew.bean.SubCategoryBean;

import java.util.ArrayList;

public class SubCategoryMultipleAdapter extends RecyclerView.Adapter<SubCategoryMultipleAdapter.ViewHolder> {


    private Context context;
    private ArrayList<SubCategoryBean> subCategoryBeanArrayList;
    private CustomItemClickListener customClickListener;

    public SubCategoryMultipleAdapter(Context context, ArrayList<SubCategoryBean> subCategoryBeanArrayList) {
        this.context = context;
        this.subCategoryBeanArrayList = subCategoryBeanArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.row_item_category_list, null);
        SubCategoryMultipleAdapter.ViewHolder vh = new SubCategoryMultipleAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        if (subCategoryBeanArrayList.get(i).isSetSlected()) {
            viewHolder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.green));
        } else {
            viewHolder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        }


        viewHolder.categoryTV.setText(subCategoryBeanArrayList.get(i).getCategory_name());


    }

    @Override
    public int getItemCount() {
        return subCategoryBeanArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView categoryTV;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linearLayout);
            categoryTV = itemView.findViewById(R.id.categoryTV);
            categoryTV.setOnClickListener(this);


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

    public void setOntemClickListener(SubCategoryMultipleAdapter.CustomItemClickListener mItemClick) {
        this.customClickListener = mItemClick;
    }
}

