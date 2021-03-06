package com.example.ecommerce.audiances;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerce.adapter.ProductsAdapter;
import com.example.ecommerce.databinding.FragmentMainBinding;
import com.example.ecommerce.model.Products;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    FragmentMainBinding binding;
    DatabaseReference productsRef;
    List<Products> list = new ArrayList<>();
    ProductsAdapter adapter;
    String type = "";

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(getLayoutInflater());

        productsRef = FirebaseDatabase.getInstance().getReference().child("products");
        Intent intent =getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!=null){
            type = intent.getStringExtra("admin");
        }
        intiateRecyclerView();
        readProducts();


        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void readProducts(){

        productsRef.orderByChild("productState").equalTo("approved").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Products products = dataSnapshot.getValue(Products.class);
                list.add(products);
                adapter.setNotes(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void intiateRecyclerView() {
        binding.productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false));
        binding.productRecyclerView.setHasFixedSize(true);
        binding.productRecyclerView.addItemDecoration(new ProductGridItemDecoration(24,16));
        adapter = new ProductsAdapter(getContext(),type);
        binding.productRecyclerView.setAdapter(adapter);
    }
}
