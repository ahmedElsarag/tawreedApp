package com.example.ecommerce.audiances;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerce.adapter.CartAdapter;
import com.example.ecommerce.databinding.FragmentCartBinding;
import com.example.ecommerce.model.CartList;
import com.example.ecommerce.prevalent.Prevalent;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    FragmentCartBinding binding;
    CartAdapter adapter;
    List<CartList> list = new ArrayList<>();
    DatabaseReference databaseReference;
    int totalPrice = 0;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(getLayoutInflater());


        intiateRecyclerView();
        checkOrderState();
        getCartList();

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ConfirmOrderActivity.class);
                intent.putExtra("total",totalPrice);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }

    private void getCartList() {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View")
                .child(Prevalent.currentOnlineUser.getPhone()).child("Products");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                CartList cartObject = dataSnapshot.getValue(CartList.class);
                list.add(cartObject);
                totalPrice += Integer.valueOf(cartObject.getPrice())*Integer.valueOf(cartObject.getQuantity());
                adapter.setList(list);
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
        binding.cartRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        binding.cartRecycler.setHasFixedSize(true);
        adapter = new CartAdapter(getContext());
        binding.cartRecycler.setAdapter(adapter);
    }

    private void checkOrderState(){
        DatabaseReference orderReference;

        orderReference = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        orderReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped")){

                        binding.cartRecycler.setVisibility(View.GONE);
                        binding.shipmentImg.setVisibility(View.VISIBLE);
                        binding.shipmentText.setVisibility(View.VISIBLE);
                        binding.nextBtn.setVisibility(View.GONE);
                    }else if (shippingState.equals("not shiped")){

                        binding.cartRecycler.setVisibility(View.GONE);
                        binding.shipmentImg.setVisibility(View.VISIBLE);
                        binding.shipmentText.setVisibility(View.VISIBLE);
                        binding.nextBtn.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
