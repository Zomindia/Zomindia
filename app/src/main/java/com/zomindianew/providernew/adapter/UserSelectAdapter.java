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

import java.util.ArrayList;

public class UserSelectAdapter extends RecyclerView.Adapter<UserSelectAdapter.ViewHolder> {


    private Context context;
    private ArrayList<String> arrayList;
    private CustomItemClickListener customClickListener;

    public UserSelectAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.row_item_category_list, null);
        UserSelectAdapter.ViewHolder vh = new UserSelectAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.categoryTV.setText(arrayList.get(i));


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
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

    public void setOntemClickListener(UserSelectAdapter.CustomItemClickListener mItemClick) {
        this.customClickListener = mItemClick;
    }
}

