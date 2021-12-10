package com.example.ecommerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerce.Admin.AdminOrdersProductActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.databinding.OrdersItemBinding;
import com.example.ecommerce.model.AdminOrders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{

    Context context;
    List<AdminOrders> list = new ArrayList<>();
    DatabaseReference databaseReference;


    public OrderAdapter(Context context) {
        this.context = context;
    }

    public void setOrders(List<AdminOrders> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        OrdersItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.orders_item,parent,false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, final int position) {

        String totalPrice = String.valueOf(list.get(position).getTotalPrice());
        holder.binding.orderName.setText(list.get(position).getName());
        holder.binding.orderPhone.setText(list.get(position).getPhone());
        holder.binding.orderAddress.setText(list.get(position).getAddress()+", "+list.get(position).getCity());
        holder.binding.orderTotalPrice.setText(totalPrice+"EGP");

        holder.binding.orderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminOrdersProductActivity.class);
                intent.putExtra("pId",list.get(position).getPhone());
                context.startActivity(intent);
            }
        });

        holder.binding.orderItemRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders");
                databaseReference.child(list.get(position).getPhone()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "error"+ list.get(position).getPhone(), Toast.LENGTH_SHORT).show();

                            list.remove(position);
                            notifyDataSetChanged();
                        } else
                            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return  list.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{
        OrdersItemBinding binding;
        public OrderViewHolder(@NonNull OrdersItemBinding binding ) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
