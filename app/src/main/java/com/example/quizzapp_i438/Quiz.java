package com.example.quizzapp_i438;

import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Quiz extends AppCompatActivity {
    private Fragment fragment;
    private FragmentContainerView fragment_container;
    private FirebaseFirestore database;
    public int question_index = 0;
    public int correct_answers = 0;
    public ArrayList<Question> question_list = new ArrayList<>();
    public Question current_question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        fragment_container = findViewById(R.id.fragment_container);

        FirebaseApp.initializeApp(this);
        database = FirebaseFirestore.getInstance();

        database.collection("questions")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        List<DocumentSnapshot> question_documents = querySnapshot.getDocuments();
                        Collections.shuffle(question_documents);
                        for (int i = 0;i < 5;i++) {
                            DocumentSnapshot random_question = question_documents.get(i);

                            String question_text = random_question.getString("question");
                            String answer1_text = random_question.getString("answer1");
                            String answer2_text = random_question.getString("answer2");
                            String correct_answer_text = random_question.getString("correct_answer");
                            String[] options = {answer1_text, answer2_text, correct_answer_text};
                            question_list.add(new Question(question_text, options, correct_answer_text));
                        }
                        current_question = question_list.get(0);
                        ((QuizFragment) fragment).update_question(question_list.get(0));
                        fragment_container.setVisibility(FragmentContainerView.VISIBLE);
                    }
                });
    }
}
