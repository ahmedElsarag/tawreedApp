package com.example.ecommerce.audiances;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerce.R;
import com.example.ecommerce.databinding.ActivityProductDetailsBinding;
import com.example.ecommerce.prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    ActivityProductDetailsBinding binding;
    String name, desc, price, category, imagesrc, pId;
    String saveCurrentDate, saveCurrentTime, number;
    private String state = "normal";
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_details);


        binding.toolbar.setTitle(" ");
        setSupportActionBar(binding.toolbar);

        binding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.collapsToolBar.setTitle("product details");
                    isShow = true;
                } else if (isShow) {
                    binding.collapsToolBar.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

        setValues();

        checkOrderState();

        binding.numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Toast.makeText(ProductDetailsActivity.this, "num = " + newValue, Toast.LENGTH_LONG).show();
                number = Integer.toString(newValue);
            }
        });

        binding.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (state.equals("placed") || state.equals("shiped")) {
                    Toast.makeText(ProductDetailsActivity.this, "you can't add more products until first order done", Toast.LENGTH_LONG)
                            .show();
                } else {
                    addTocart();
                }
            }
        });
    }

    private void setValues() {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        desc = intent.getStringExtra("desc");
        price = intent.getStringExtra("price");
        category = intent.getStringExtra("category");
        imagesrc = intent.getStringExtra("img");
        pId = intent.getStringExtra("pId");

        binding.name.setText(name);
        binding.description.setText(desc);
        binding.price.setText(price);
        binding.category.setText(category);
        Picasso.with(this).load(imagesrc).into(binding.img);
    }

    private void addTocart() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", pId);
        cartMap.put("pname", name);
        cartMap.put("price", price);
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", number);
        cartMap.put("discount", "");

        databaseReference.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products")
                .child(pId)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            databaseReference.child("Admin View").child(Prevalent.currentOnlineUser.getPhone()).child("Products")
                                    .child(pId)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                                Toast.makeText(ProductDetailsActivity.this, "product added to cart", Toast.LENGTH_LONG)
                                                        .show();
                                            else
                                                Toast.makeText(ProductDetailsActivity.this, "error adding to cart", Toast.LENGTH_LONG)
                                                        .show();
                                        }
                                    });
                        }
                    }
                });

    }

    private void checkOrderState() {
        DatabaseReference orderReference;

        orderReference = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        orderReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped")) {

                        state = "placed";
                    } else if (shippingState.equals("not shiped")) {
                        state = "shiped";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
