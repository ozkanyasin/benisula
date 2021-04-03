package com.example.bitkilerinisula;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import com.example.bitkilerinisula.model.PlantModel;
import com.example.bitkilerinisula.repository.AppDatabase;
import com.example.bitkilerinisula.repository.PlantDao;

import java.util.ArrayList;
import java.util.List;

public class PlantListFragment extends Fragment {

    private RecyclerView recyclerView;

    private PlantsAdapter adapter;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.plant_list_fragment,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));

        AppDatabase db = Room.databaseBuilder(getContext(),
                AppDatabase.class, "ozkan").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        PlantDao plantDao = db.userDao();


        adapter = new PlantsAdapter(getContext(), getParentFragmentManager());
        recyclerView.setAdapter(adapter);

        List<PlantModel> data = plantDao.getAll();
        adapter.setList(data);



        return view;
    }



}
