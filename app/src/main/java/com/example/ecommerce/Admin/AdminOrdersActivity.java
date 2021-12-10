package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.ecommerce.R;
import com.example.ecommerce.adapter.OrderAdapter;
import com.example.ecommerce.databinding.ActivityAdminOrdersBinding;
import com.example.ecommerce.model.AdminOrders;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdminOrdersActivity extends AppCompatActivity {

    ActivityAdminOrdersBinding binding;
    OrderAdapter adapter;
    DatabaseReference databaseReference;
    List<AdminOrders>list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_orders);

        intiateRecyclerView();
        getCartList();
    }


    private void getCartList() {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AdminOrders adminOrders = dataSnapshot.getValue(AdminOrders.class);
                list.add(adminOrders);
                adapter.setOrders(list);
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
        binding.orderRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.orderRecycler.setHasFixedSize(true);
        adapter = new OrderAdapter(this);
        binding.orderRecycler.setAdapter(adapter);
    }
}
