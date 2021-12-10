package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.ecommerce.audiances.ProductGridItemDecoration;
import com.example.ecommerce.R;
import com.example.ecommerce.adapter.CheckProductAdapter;
import com.example.ecommerce.databinding.ActivityAdminCheckNewProductsBinding;
import com.example.ecommerce.model.Products;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdminCheckNewProducts extends AppCompatActivity {

    ActivityAdminCheckNewProductsBinding binding;
    CheckProductAdapter adapter;
    DatabaseReference databaseReference;
    List<Products> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_check_new_products);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("products");

        intiateRecyclerView();
        readProducts();
    }


    private void readProducts(){

        databaseReference.orderByChild("productState").equalTo("not approved").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Products products = dataSnapshot.getValue(Products.class);
                Toast.makeText(AdminCheckNewProducts.this,products.getPname(),Toast.LENGTH_LONG).show();
                list.add(products);
                adapter.setList(list);
                adapter.notifyDataSetChanged();
                Toast.makeText(AdminCheckNewProducts.this,"lengnth :"+list.size(),Toast.LENGTH_LONG).show();
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
        binding.checkProductRecycler.setLayoutManager(new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false));
        binding.checkProductRecycler.setHasFixedSize(true);
        binding.checkProductRecycler.addItemDecoration(new ProductGridItemDecoration(24,16));
        adapter = new CheckProductAdapter(this);
        binding.checkProductRecycler.setAdapter(adapter);
    }
}
