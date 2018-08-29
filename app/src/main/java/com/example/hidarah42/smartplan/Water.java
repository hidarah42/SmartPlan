package com.example.hidarah42.smartplan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Water extends Fragment {

    public Water(){}

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View Page = inflater.inflate(R.layout.fragment_water,container,false);
        return Page;
    }
}
