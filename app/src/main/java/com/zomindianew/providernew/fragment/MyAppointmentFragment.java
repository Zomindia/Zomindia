package com.zomindianew.providernew.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zomindianew.R;
import com.zomindianew.providernew.adapter.ViewPagerAdapter;


@SuppressLint("ValidFragment")
public class MyAppointmentFragment extends Fragment {
    private ViewPager viewpager;
    private TabLayout tabLayout;
    String typeStr;

    @SuppressLint("ValidFragment")
    public MyAppointmentFragment(String past) {
        this.typeStr = past;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        init(view);

        return view;
    }

    public void init(View view) {
        viewpager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tab_layout);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        //adapter.addFrag(new OnGoingFragment(), "ON GOING");
        adapter.addFrag(new UpComingFragment(), "UPCOMING");
        //adapter.addFrag(new PastFragment(), "HISTORY");
        viewpager.setAdapter(adapter);
        /*if (typeStr.equals("past")) {
            viewpager.setCurrentItem(2);
        } else if (typeStr.equals("upcoming")) {
            viewpager.setCurrentItem(1);
        } else {
            viewpager.setCurrentItem(0);
        }
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.setTabTextColors(getContext().getResources().getColor(R.color.colorAccent), getContext().getResources().getColor(R.color.white));
*/

    }
}
