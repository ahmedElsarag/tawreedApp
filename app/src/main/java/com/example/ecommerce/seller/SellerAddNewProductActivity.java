package com.example.ecommerce.seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ecommerce.Dialog;
import com.example.ecommerce.R;
import com.example.ecommerce.databinding.ActivitySellerAddNewProductBinding;
import com.example.ecommerce.model.Products;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class SellerAddNewProductActivity extends AppCompatActivity {

    ActivitySellerAddNewProductBinding binding;
    private String categoryName, saveCurrentDate, saveCurrentTime, productRandomKey, downloadUrl;
    private String sName,sId,sPhone,sAddress,sEmail;
    private static final int GALLERYPICK = 1;
    private Uri imageUri;
    StorageReference storageImgeReference;
    DatabaseReference productsRef,sellerRef;
    List<Products> list = new ArrayList<>();
    Dialog dialog = new Dialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_seller_add_new_product);

        categoryName = getIntent().getExtras().get("category").toString();
        storageImgeReference = FirebaseStorage.getInstance().getReference().child("product images");
        productsRef = FirebaseDatabase.getInstance().getReference().child("products");
        sellerRef = FirebaseDatabase.getInstance().getReference().child("Sellers");

        binding.selectProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        binding.addNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.loadDialog(SellerAddNewProductActivity.this);
                storeProductInfo();
            }
        });

        sellerRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null){
                            sName = dataSnapshot.child("name").getValue().toString();
                            sAddress = dataSnapshot.child("address").getValue().toString();
                            sEmail = dataSnapshot.child("email").getValue().toString();
                            sPhone = dataSnapshot.child("phone").getValue().toString();
                            sId = dataSnapshot.child("sId").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void selectImage() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERYPICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERYPICK && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            binding.selectProductImage.setImageURI(imageUri);
        }
    }

    private void storeProductInfo() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat date = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = date.format(calendar.getTime());

        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = time.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = storageImgeReference.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SellerAddNewProductActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SellerAddNewProductActivity.this, "image uploaded successfully", Toast.LENGTH_LONG).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()) {
                            downloadUrl = task.getResult().toString();
                            Toast.makeText(SellerAddNewProductActivity.this, "done", Toast.LENGTH_LONG).show();
                            dialog.dismisDialog();
                            saveProductInfoToDatabase();
                        }
                    }
                });
            }
        });


    }

    private void saveProductInfoToDatabase() {


        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", binding.productDescription.getText().toString());
        productMap.put("image", downloadUrl);
        productMap.put("category", categoryName);
        productMap.put("price", binding.productPrice.getText().toString());
        productMap.put("pname", binding.productName.getText().toString());
        productMap.put("productState","not approved");
        productMap.put("sellerName",sName);
        productMap.put("sellerPhone",sPhone);
        productMap.put("sellerAddress",sAddress);
        productMap.put("sellerEmail",sEmail);
        productMap.put("sId",sId);

        productsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(SellerAddNewProductActivity.this, SellerDashboardActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(SellerAddNewProductActivity.this, "Product added successfully..", Toast.LENGTH_SHORT).show();
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(SellerAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
