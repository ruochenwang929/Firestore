package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.firestore.databinding.ActivityMainBinding;
import com.example.firestore.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {

    private ActivityProfileBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        binding.userText.setText(user.getEmail());

        final Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
//        String email = intent.getStringExtra("email");
//        String text = intent.getStringExtra("text");
        String email = bundle.getString("email");
        String text = bundle.getString("text");

        DocumentReference docRef = db.collection("User").document(email);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String name = document.getData().get("Name").toString();
                        binding.nameText.setText(name);
                        //String email = document.getData().get("Email").toString();
                        binding.emailText.setText(email);
                        String address = document.getData().get("Address").toString();
                        binding.addressText.setText(address);

                        ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipData = clipboardManager.getPrimaryClip();
                        String str = clipData.getItemAt(0).getText().toString();
                        binding.text.setText(str);


                    } else {
                        Toast.makeText(Profile.this, "No such document", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Profile.this, "get failed with " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //将email传到edit profile
                Intent intent = new Intent(Profile.this, EditProfile.class);
                Bundle bundle1 = new Bundle();
//                intent.putExtra("email", email);
//                intent.putExtra("text", text);
                bundle1.putString("email", email);
                bundle1.putString("text", text);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, MainActivity.class));
            }
        });
    }
}