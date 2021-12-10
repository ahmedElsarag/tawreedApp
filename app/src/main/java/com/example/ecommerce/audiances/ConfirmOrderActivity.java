package com.example.ecommerce.audiances;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.example.ecommerce.databinding.ActivityConfirmOrderBinding;
import com.example.ecommerce.prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmOrderActivity extends AppCompatActivity {

    ActivityConfirmOrderBinding binding;
    String name, phone, address, city;
    int totalPrice;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_order);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        totalPrice = intent.getIntExtra("total",0);

        binding.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = binding.name.getText().toString();
                phone = binding.mobileNumber.getText().toString();
                address = binding.address.getText().toString();
                city = binding.city.getText().toString();
                confirmOrder();
            }
        });

    }

    private void confirmOrder() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yy");
        String saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String saveCurrentTime = currentTime.format(calendar.getTime());

        final HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("totalPrice", totalPrice);
        orderMap.put("name", name);
        orderMap.put("phone", phone);
        orderMap.put("address", address);
        orderMap.put("city", city);
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("state","not shiped");
        databaseReference.child("Orders").child(Prevalent.currentOnlineUser.getPhone())
                .updateChildren(orderMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            databaseReference.child("Cart List").child("User View").child(Prevalent.currentOnlineUser.getPhone())
                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ConfirmOrderActivity.this,"done",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
    }
}
