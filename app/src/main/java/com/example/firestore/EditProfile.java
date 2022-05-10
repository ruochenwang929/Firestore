package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.firestore.databinding.ActivityMainBinding;
import com.example.firestore.databinding.EditProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private EditProfileBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = EditProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        final Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        binding.emailText.setText(email);

        DocumentReference docRef = db.collection("User").document(email);

//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        String email = document.getData().get("Email").toString();
//                        binding.emailText.setText(email);
//                    } else {
//                        Toast.makeText(EditProfile.this, "No such document", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(EditProfile.this, "get failed with " + task.getException(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = binding.nameText.getText().toString();
                String address = binding.addressText.getText().toString();
                String email = binding.emailText.getText().toString();

                Map<String, Object> profile = new HashMap<>();

                profile.put("Name", name);
                profile.put("Email", email);
                profile.put("Address", address);

                db.collection("User").document(email)
                        .set(profile)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                System.out.println("success+++++++");
                                Toast.makeText(EditProfile.this, "Save successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditProfile.this, Profile.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("Error--------");
                                Toast.makeText(EditProfile.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfile.this, Profile.class));
            }
        });
    }
}