package com.example.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerce.R;
import com.example.ecommerce.databinding.CartItemBinding;
import com.example.ecommerce.model.CartList;

import java.util.ArrayList;
import java.util.List;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.OrderViewHolder>{

    Context context;
    List<CartList> list = new ArrayList<>();


    public OrderProductAdapter(Context context) {
        this.context = context;
    }

    public void setOrderProducts(List<CartList> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        CartItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.cart_item,parent,false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, final int position) {

        holder.binding.cartItemName.setText(list.get(position).getPname());
        holder.binding.cartItemPrice.setText(list.get(position).getPrice() + " EGP");
        holder.binding.cartItemNumber.setText("X" + list.get(position).getQuantity());

    }

    @Override
    public int getItemCount() {
        return  list.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{
        CartItemBinding binding;
        public OrderViewHolder(@NonNull CartItemBinding binding ) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
