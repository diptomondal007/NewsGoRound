package com.example.dipto.newsgoround.FragmentsClasses;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dipto.newsgoround.R;

public class LiveScoreTab extends Fragment {

    View view;


    public LiveScoreTab(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.live_score_tab_layout  , container , false);


        return view;
    }
}
