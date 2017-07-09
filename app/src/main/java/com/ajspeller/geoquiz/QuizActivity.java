package com.ajspeller.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String QA0 = "QA0";
    private static final String QA1 = "QA1";
    private static final String QA2 = "QA2";
    private static final String QA3 = "QA3";
    private static final String QA4 = "QA4";
    private static final String QA5 = "QA5";
    private static final String IS_CHEATER = "IS_CHEATER";
    private Button mTrueButton;
    private Button mFalseButton;
    private TextView mQuestionTextView;
    private int mCurrentIndex = 0;
    private int mCorrectAnswers = 0;
    private int mIncorrectAnswers = 0;
    private Button mCheatButton;
    private boolean mIsCheater;
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private boolean questionAnswered[] = {false, false, false, false, false, false};
    private boolean cheated[] = {false, false, false, false, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageButton mNextButton;
        ImageButton mPrevButton;

        // if the activity has been rebuilt then restore the values
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            questionAnswered[0] = savedInstanceState.getBoolean(QA0, false);
            questionAnswered[1] = savedInstanceState.getBoolean(QA1, false);
            questionAnswered[2] = savedInstanceState.getBoolean(QA2, false);
            questionAnswered[3] = savedInstanceState.getBoolean(QA3, false);
            questionAnswered[4] = savedInstanceState.getBoolean(QA4, false);
            questionAnswered[5] = savedInstanceState.getBoolean(QA5, false);
            mIsCheater = savedInstanceState.getBoolean(IS_CHEATER, false);
        }

        Log.d(TAG, "onCreate(Bundle) called");

        setContentView(R.layout.activity_quiz);

        // wire up the question and get the first question
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        // Wire up the true button and show toast when clicked
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        // Wire up the false button and show toast when clicked
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        // Wire up the next button, and get the next question when button clicked
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = cheated[mCurrentIndex];
                updateQuestion();
            }
        });

        // Wire up the cheat button
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start CheatActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        // Wire up the next button, and get the next question when button clicked
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex - 1);
                if (mCurrentIndex < 0) {
                    mCurrentIndex = mQuestionBank.length - 1;
                }
                mIsCheater = cheated[mCurrentIndex];
                updateQuestion();
            }
        });

        // display a question
        updateQuestion();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(QA0, questionAnswered[0]);
        savedInstanceState.putBoolean(QA1, questionAnswered[1]);
        savedInstanceState.putBoolean(QA2, questionAnswered[2]);
        savedInstanceState.putBoolean(QA3, questionAnswered[3]);
        savedInstanceState.putBoolean(QA4, questionAnswered[4]);
        savedInstanceState.putBoolean(QA5, questionAnswered[5]);
        savedInstanceState.putBoolean(IS_CHEATER, mIsCheater);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            cheated[mCurrentIndex] = mIsCheater;
        }
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

        if (!questionAnswered[mCurrentIndex]) {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        } else {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        }
    }

    private void checkAnswer(boolean userPressed) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        float score;

        int messageResId;

        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressed == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mCorrectAnswers++;
            } else {
                messageResId = R.string.incorrect_toast;
                mIncorrectAnswers++;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

        questionAnswered[mCurrentIndex] = true;
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);

        if (mCorrectAnswers + mIncorrectAnswers == mQuestionBank.length) {
            score = (float) mCorrectAnswers / mQuestionBank.length;
            Toast.makeText(this, String.valueOf(score * 100) + "%", Toast.LENGTH_LONG).show();
        }
    }


}
