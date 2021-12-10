package com.example.ecommerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Admin.EditProductActivity;
import com.example.ecommerce.audiances.ProductDetailsActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.databinding.ProductItemBinding;
import com.example.ecommerce.model.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>{

    Context context;
    List<Products> list = new ArrayList<>();
    private String type = "";


    public ProductsAdapter(Context context,String type) {
        this.context = context;
        this.type = type;
    }
    public void setNotes(List<Products> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ProductItemBinding binding = DataBindingUtil.inflate(layoutInflater,R.layout.product_item,parent,false);
        return new ProductsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductsViewHolder holder, final int position) {

            holder.binding.productTitle.setText(list.get(position).getPname());
            holder.binding.productPrice.setText(list.get(position).getPrice()+" EGP");
            Picasso.with(context).load(list.get(position).getImage()).into(holder.binding.productImage);

            holder.binding.itemCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (type.equals("admin")){

                        Intent intent = new Intent(context, EditProductActivity.class);
                        intent.putExtra("pId",list.get(position).getPid());
                        intent.putExtra("name",list.get(position).getPname());
                        intent.putExtra("price",list.get(position).getPrice());
                        intent.putExtra("desc",list.get(position).getDescription());
                        context.startActivity(intent);
                    }else {
                        Intent intent = new Intent(context, ProductDetailsActivity.class);
                        intent.putExtra("name",list.get(position).getPname());
                        intent.putExtra("desc",list.get(position).getDescription());
                        intent.putExtra("price",list.get(position).getPrice());
                        intent.putExtra("category",list.get(position).getCategory());
                        intent.putExtra("img",list.get(position).getImage());
                        intent.putExtra("pId",list.get(position).getPid());
                        context.startActivity(intent);
                    }

                }
            });
    }

    @Override
    public int getItemCount() {
        return  list.size();
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder{
        ProductItemBinding binding;
        public ProductsViewHolder(@NonNull ProductItemBinding binding ) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
