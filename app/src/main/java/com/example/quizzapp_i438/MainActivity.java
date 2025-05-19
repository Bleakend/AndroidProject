package com.example.quizzapp_i438;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private TextView textViewHello;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();


        textViewHello = findViewById(R.id.hello_world);
        textViewHello.setText("a");

        db.collection("questions").document("iDeq1rCcSaDUHilJEwmm")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d("Firestore", "Document snapshot received: " + documentSnapshot.getData());

                        if (documentSnapshot.exists()) {
                            String text = documentSnapshot.getString("question");
                            Log.d("Firestore", "Field 'question': " + text);

                            if (text != null) {
                                textViewHello.setText(text);
                            } else {
                                textViewHello.setText("No message found");
                                Log.w("Firestore", "Field 'question' is null");
                            }
                        } else {
                            textViewHello.setText("No data available");
                            Log.w("Firestore", "Document does not exist");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error getting document", e);
                        textViewHello.setText("Error loading message");
                    }
                });

    }
}
