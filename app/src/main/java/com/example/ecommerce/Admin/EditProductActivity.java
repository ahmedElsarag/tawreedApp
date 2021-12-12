package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ecommerce.Dialog;
import com.example.ecommerce.R;
import com.example.ecommerce.databinding.ActivityEditProductBinding;
import com.example.ecommerce.seller.SellerProductCategoryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditProductActivity extends AppCompatActivity implements View.OnClickListener {

    String id,name,price,desc;
    ActivityEditProductBinding binding;
    DatabaseReference databaseReference;
    Dialog dialog = new Dialog();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_product);

        setValues();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("products").child(id);

        binding.applyChangesBtn.setOnClickListener(this);
        binding.deleteProduct.setOnClickListener(this);

    }

    private void setValues(){
        Intent intent = getIntent();
        id = intent.getStringExtra("pId");
        name = intent.getStringExtra("name");
        price = intent.getStringExtra("price");
        desc = intent.getStringExtra("desc");

        binding.editProductName.setText(name);
        binding.editProductPrice.setText(price);
        binding.editProductDesc.setText(desc);
    }
    private void updateChanges(){
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("description", binding.editProductDesc.getText().toString());
        productMap.put("price", binding.editProductPrice.getText().toString());
        productMap.put("pname", binding.editProductName.getText().toString());

        databaseReference.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(EditProductActivity.this,"product updated",Toast.LENGTH_SHORT).show();
                    dialog.dismisDialog();
                    Intent intent = new Intent(EditProductActivity.this, AdminMainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    private void deleteProduct() {
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EditProductActivity.this,"product deleted",Toast.LENGTH_LONG).show();
                dialog.dismisDialog();
                Intent intent = new Intent(EditProductActivity.this, AdminMainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.apply_changes_btn:{
                dialog.loadDialog(EditProductActivity.this);
                updateChanges();

                break;
            } case R.id.delete_product:{
                dialog.loadDialog(EditProductActivity.this);
                deleteProduct();
                break;
            }
        }

    }

}
