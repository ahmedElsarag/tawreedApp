package com.example.ecommerce.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ecommerce.audiances.HomeActivity;
import com.example.ecommerce.audiances.MainActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.databinding.ActivityAdminMainBinding;

public class AdminMainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityAdminMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_main);

        binding.logout.setOnClickListener(this);
        binding.checkOrders.setOnClickListener(this);
        binding.maintainProduct.setOnClickListener(this);
        binding.checkNewProduct.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.logout: {
                Intent intent = new Intent(AdminMainActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.check_orders: {
                Intent intent = new Intent(AdminMainActivity.this, AdminOrdersActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.maintain_product: {
                Intent intent = new Intent(AdminMainActivity.this, HomeActivity.class);
                intent.putExtra("admin", "admin");
                startActivity(intent);
                break;
            }
            case R.id.check_new_product:{
                Intent intent = new Intent(AdminMainActivity.this, AdminCheckNewProducts.class);
                startActivity(intent);
            }
        }
    }
}
