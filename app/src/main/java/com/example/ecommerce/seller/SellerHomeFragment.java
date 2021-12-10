package com.example.ecommerce.seller;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerce.audiances.ProductGridItemDecoration;
import com.example.ecommerce.adapter.SellerProductAdapter;
import com.example.ecommerce.databinding.FragmentSellerHomeBinding;
import com.example.ecommerce.model.Products;
import com.google.firebase.auth.FirebaseAuth;
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
public class SellerHomeFragment extends Fragment {

    FragmentSellerHomeBinding binding;
    List<Products> list = new ArrayList<>();
    SellerProductAdapter adapter;
    DatabaseReference databaseReference;
    public SellerHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSellerHomeBinding.inflate(getLayoutInflater());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("products");

        intiateRecyclerView();
        readProducts();
        return binding.getRoot();
    }


    private void readProducts(){

        databaseReference.orderByChild("sId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Products products = dataSnapshot.getValue(Products.class);
                list.add(products);
                adapter.setList(list);
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(),"lengnth :"+list.size(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
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
        binding.sellerRecycler.setLayoutManager(new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false));
        binding.sellerRecycler.setHasFixedSize(true);
        binding.sellerRecycler.addItemDecoration(new ProductGridItemDecoration(24,16));
        adapter = new SellerProductAdapter(getActivity());
        binding.sellerRecycler.setAdapter(adapter);
    }
}
