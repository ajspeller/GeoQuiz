package com.ajspeller.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.ajspeller.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.ajspeller.android.geoquiz.answer_shown";
    private static final String WAS_SHOWN = "WAS_SHOWN";
    private boolean mAnswerIsTrue;
    private boolean mWasAnswerShown;

    private TextView mAnswerTextView;
    private Button mShowAnswerButton;
    private TextView mBuildRelease;
    private TextView mBuildLevel;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean(WAS_SHOWN, false)) {
                ButtonAndTextViewReferences();
                mShowAnswerButton.performClick();
            } else {
                setAnswerShowResults(false);
            }
        }

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        ButtonAndTextViewReferences();

        mBuildRelease = (TextView) findViewById(R.id.build_release);
        mBuildRelease.setText("Build Release: " + Build.VERSION.RELEASE);

        mBuildLevel = (TextView) findViewById(R.id.build_level);
        mBuildLevel.setText("API Level " + Build.VERSION.SDK_INT);


    }

    private void ButtonAndTextViewReferences() {
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                mWasAnswerShown = true;
                setAnswerShowResults(true);
            }
        });
    }

    private void setAnswerShowResults(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(WAS_SHOWN, mWasAnswerShown);
    }
}
