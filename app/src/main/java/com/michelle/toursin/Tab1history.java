package com.michelle.toursin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;;import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Tab1history extends Fragment{
    JsonData[] imageJsonData;
    private MyAdapter myAdapter;

    public class JsonData{
        String name;
        String url;
        String file;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.tab1history, container, false);

        parseJson();

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        myAdapter = new MyAdapter(this.getActivity(),imageJsonData);
        rv.setAdapter(myAdapter);

        return rootView;
    }

    void parseJson(){

        Gson gson = new Gson();
        String jsonData = readTxt(R.raw.pictures);
        imageJsonData = gson.fromJson(jsonData,JsonData[].class);
    }

    private String readTxt(int resource){
        InputStream inputStream = getResources().openRawResource(resource);
        String line;
        String output = "";
        try{BufferedReader reader = new BufferedReader(new BufferedReader(new InputStreamReader(inputStream,"UTF-8")));
            while((line=reader.readLine())!=null){
                output = output+line;}
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return output;
    }

}