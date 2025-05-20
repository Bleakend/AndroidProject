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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Quiz extends AppCompatActivity {

    private FirebaseFirestore database;
    private TextView tv_question;
    private TextView answer1;
    private TextView answer2;
    private TextView answer3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        FirebaseApp.initializeApp(this);
        database = FirebaseFirestore.getInstance();

        tv_question = (TextView) findViewById(R.id.label_question);
        answer1 = (TextView) findViewById(R.id.label_answer1);
        answer2 = (TextView) findViewById(R.id.label_answer2);
        answer3 = (TextView) findViewById(R.id.label_answer3);

        database.collection("questions")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        List<DocumentSnapshot> question_documents = querySnapshot.getDocuments();

                        if (question_documents.isEmpty()) {
                            return;
                        }
                        int random_index = new Random().nextInt(question_documents.size());
                        DocumentSnapshot random_question = question_documents.get(random_index);

                        String question_text = random_question.getString("question");
                        String answer1_text = random_question.getString("answer1");
                        String answer2_text = random_question.getString("answer2");
                        String correct_answer_text = random_question.getString("correct_answer");
                        String[] options = {answer1_text, answer2_text, correct_answer_text};
                        Question question = new Question(question_text, options, correct_answer_text);
                        tv_question.setText(question.get_question());
                        answer1.setText(question.get_options()[0]);
                        answer2.setText(question.get_options()[1]);
                        answer3.setText(question.get_options()[2]);

                    }
                });
    }
}
