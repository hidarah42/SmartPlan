package com.example.hidarah42.smartplan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fertilize extends Fragment {


    public Fertilize(){}

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View page = inflater.inflate(R.layout.fragment_fertilize,container,false);
        return page;
    }
}
