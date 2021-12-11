package com.example.ecommerce.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Dialog;
import com.example.ecommerce.R;
import com.example.ecommerce.databinding.ProductItemBinding;
import com.example.ecommerce.model.Products;
import com.example.ecommerce.prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SellerProductAdapter extends RecyclerView.Adapter<SellerProductAdapter.ProductsViewHolder> {

    Context context;
    List<Products> list = new ArrayList<>();
    DatabaseReference databaseReference;


    public SellerProductAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<Products> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ProductItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.product_item, parent, false);
        return new ProductsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductsViewHolder holder, final int position) {

        holder.binding.productTitle.setText(list.get(position).getPname());
        holder.binding.productPrice.setText(list.get(position).getPrice() + " EGP");
        Picasso.with(context).load(list.get(position).getImage()).into(holder.binding.productImage);

        holder.binding.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog(context,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder {
        ProductItemBinding binding;

        public ProductsViewHolder(@NonNull ProductItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void deleteDialog(Context context,final int position) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("Delete")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        deleteItem(position);
                    }
                })
                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    public void deleteItem(final int position) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("products");
        databaseReference.child(list.get(position).getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, "product deleted", Toast.LENGTH_LONG).show();
                list.remove(position);
                notifyDataSetChanged();
            }
        });
    }

}

