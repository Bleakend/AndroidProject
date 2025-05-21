package com.example.quizzapp_i438;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class QuizFragment extends Fragment {
    public QuizFragment() {
        // Required empty public constructor
    }

    public static QuizFragment newInstance() {
        return new QuizFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Quiz activity = (Quiz) getActivity();
        
        Button submit_button = view.findViewById(R.id.button_submit_answer);
        RadioGroup radio_group = view.findViewById(R.id.answers_group);
        TextView question_number_label = activity.findViewById(R.id.question_number_label);

        submit_button.setOnClickListener(v -> {
            int selected_id = radio_group.getCheckedRadioButtonId();
            if (selected_id == -1){
                Toast.makeText(activity, "Please select an answer before submitting", Toast.LENGTH_LONG).show();
                return;
            }
            RadioButton selected_radio_button = view.findViewById(selected_id);
            String selected_answer = selected_radio_button.getText().toString();
            String correct_answer = activity.current_question.get_correct_answer();

            if (selected_answer.equals(correct_answer)) {
                activity.correct_answers += 1;
            }

            activity.question_index += 1;
            question_number_label.setText(String.format("Question %s of 5", String.valueOf(activity.question_index + 1)));
            if (activity.question_index == activity.question_list.size() - 1){
                submit_button.setText(R.string.submit_quiz_label);
            }

            if (activity.question_index == activity.question_list.size()) {
                question_number_label.setVisibility(View.INVISIBLE);
                Fragment resultFragment = QuizResultFragment.newInstance(activity.correct_answers);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, resultFragment)
                        .commit();
            } else {
                // Show next question
                activity.current_question = activity.question_list.get(activity.question_index);
                update_question(activity.current_question);
                radio_group.clearCheck();
            }

        });
    }

    public void update_question(Question question) {
        TextView questionLabel = getView().findViewById(R.id.label_question);
        RadioButton answer1 = getView().findViewById(R.id.radio_answer1);
        RadioButton answer2 = getView().findViewById(R.id.radio_answer2);
        RadioButton answer3 = getView().findViewById(R.id.radio_answer3);

        questionLabel.setText(question.get_question());

        String[] options = question.get_options();
        int start = new Random().nextInt(3);

        answer1.setText(options[start % 3]);
        answer2.setText(options[(start + 1) % 3]);
        answer3.setText(options[(start + 2) % 3]);
    }
}
