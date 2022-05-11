package com.zomindianew.user.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zomindianew.R;
import com.zomindianew.bean.SearchViewBean;

import java.util.ArrayList;

public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.MyViewHolder> {

    private SearchViewAdapter.ItemInterFace itemClick;
    private Context context;
    private ArrayList<SearchViewBean> searchBeanArrayList;
    SharedPreferences sharedPreferences;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView name;
        CardView cardView;


        public MyViewHolder(View view) {
            super(view);
//            image = view.findViewById(R.id.image);
            name = view.findViewById(R.id.search_name);
//            cardView = view.findViewById(R.id.cardView);
//            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClick != null) {
                itemClick.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public void onItemClickMethod(SearchViewAdapter.ItemInterFace itemClick) {

        this.itemClick = itemClick;


    }

    public interface ItemInterFace {
        void onItemClick(View view, int position);
    }

    public SearchViewAdapter(Context context, ArrayList<SearchViewBean> searchBeanArrayList) {

        this.context = context;
        this.searchBeanArrayList = searchBeanArrayList;


    }

    @Override
    public SearchViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.card_view_adaptersearch, parent, false);

        return new SearchViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchViewAdapter.MyViewHolder holder, final int position) {

        holder.name.setText(searchBeanArrayList.get(position).getSvcategory_name());
        Log.e("ghghhgh","klllkl"+searchBeanArrayList.get(position).getSvcategory_name());
//        sharedPreferences = context.getSharedPreferences(Variables.pref_name, MODE_PRIVATE);

//
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString(Variables.CITYNAME, cityname.get(position).getCityname());
//                editor.commit();
//                context.startActivity(new Intent(context, WelComeLoginActivity.class));

            }
        });


    }


    @Override
    public int getItemCount() {
        return searchBeanArrayList.size();

    }

    public void updates(ArrayList<SearchViewBean> result) {

        searchBeanArrayList = new ArrayList<>();
        searchBeanArrayList.addAll(result);
        notifyDataSetChanged();

    }
}