package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.ecommerce.R;
import com.example.ecommerce.adapter.OrderProductAdapter;
import com.example.ecommerce.databinding.ActivityAdminOrdersProductBinding;
import com.example.ecommerce.model.CartList;
import com.example.ecommerce.prevalent.Prevalent;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdminOrdersProductActivity extends AppCompatActivity {

    ActivityAdminOrdersProductBinding binding;
    String pId;
    OrderProductAdapter adapter;
    DatabaseReference databaseReference;
    List<CartList> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_orders_product);

        Intent intent = getIntent();
        pId = intent.getStringExtra("pId");

        intiateRecyclerView();
        getProducts();

    }

    private void getProducts() {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View")
                .child(pId).child("Products");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                CartList cartObject = dataSnapshot.getValue(CartList.class);
                list.add(cartObject);
                adapter.setOrderProducts(list);
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
        binding.orderProductRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.orderProductRecycler.setHasFixedSize(true);
        adapter = new OrderProductAdapter(this);
        binding.orderProductRecycler.setAdapter(adapter);
    }
}
