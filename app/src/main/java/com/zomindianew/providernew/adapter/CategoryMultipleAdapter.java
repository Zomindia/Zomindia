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
import com.zomindianew.bean.CategoryBean;

import java.util.ArrayList;

public class CategoryMultipleAdapter extends RecyclerView.Adapter<CategoryMultipleAdapter.ViewHolder> {


    private Context context;
    private ArrayList<CategoryBean> categoryList;
    private CustomItemClickListener customClickListener;

    public CategoryMultipleAdapter(Context context, ArrayList<CategoryBean> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.row_item_category_list, null);
        CategoryMultipleAdapter.ViewHolder vh = new CategoryMultipleAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

//        if (categoryList.get(i).isSetSlected()) {
//            viewHolder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.green));
//        } else {
//            viewHolder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
//        }


        viewHolder.categoryTV.setText(categoryList.get(i).getCategory_name());


    }

    @Override
    public int getItemCount() {
        return categoryList.size();
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

    public void setOntemClickListener(CategoryMultipleAdapter.CustomItemClickListener mItemClick) {
        this.customClickListener = mItemClick;
    }
}

