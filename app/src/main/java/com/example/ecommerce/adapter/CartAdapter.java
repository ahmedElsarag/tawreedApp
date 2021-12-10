package com.example.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.databinding.CartItemBinding;
import com.example.ecommerce.model.CartList;
import com.example.ecommerce.model.Products;
import com.example.ecommerce.prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    List<CartList> list = new ArrayList<>();
    DatabaseReference databaseReference;


    public CartAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<CartList> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        CartItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.cart_item, parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {

        holder.binding.cartItemName.setText(list.get(position).getPname());
        holder.binding.cartItemPrice.setText(list.get(position).getPrice() + " EGP");
        holder.binding.cartItemNumber.setText("X" + list.get(position).getQuantity());
//        Picasso.with(context).load(list.get(position).getImage()).into(holder.binding.cartItemImg);

        holder.binding.cartItemRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart List");
                databaseReference.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products")
                        .child(list.get(position).getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            databaseReference.child("Admin View").child(Prevalent.currentOnlineUser.getPhone()).child("Products")
                                    .child(list.get(position).getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        list.remove(position);
                                        notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        CartItemBinding binding;

        public CartViewHolder(@NonNull CartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}