package com.androidadvance.androidsurvey.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidadvance.androidsurvey.Answers;
import com.androidadvance.androidsurvey.R;
import com.androidadvance.androidsurvey.Survey;
import com.androidadvance.androidsurvey.SurveyActivity;
import com.androidadvance.androidsurvey.models.Question;
import com.androidadvance.androidsurvey.utils.SurveyViewUtils;

import java.util.List;

public class FragmentTextSimple extends QuestionAbstractFragment {

    private final static String TAG = Survey.LIBRARY_NAME + ":" +
            FragmentTextSimple.class.getSimpleName();

    private FragmentActivity mContext;
    private Button mContinueButton;
    private TextView mTitleTextView;
    private EditText mAnswerEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUserVisibleHint(false);
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_text_simple, container, false);

        mContinueButton = rootView.findViewById(R.id.button_continue);
        mTitleTextView = rootView.findViewById(R.id.textview_q_title);
        mAnswerEditText = rootView.findViewById(R.id.editText_answer);

        // Personalizing
        SurveyViewUtils
                .personalizeButton(getActivity(), Survey.KEY_CONTINUE_TEXT_RES, mContinueButton);

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();
        final Question question = (Question) getArguments().getSerializable("data");

        // Letting everyone knows when something horrible goes wrong.
        if (question == null) {
            Log.e(TAG, "A fragment without data was initialized, that shouldn't happen.");
            ((SurveyActivity) mContext).goToNext();
            return;
        }

        if (question.getRequired()) {
            mContinueButton.setVisibility(View.GONE);
            mAnswerEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 3) {
                        mContinueButton.setVisibility(View.VISIBLE);
                    } else {
                        mContinueButton.setVisibility(View.GONE);
                    }
                }
            });
        }

        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String answer = mAnswerEditText.getText().toString().trim();

                Answers.getInstance().put_answer(mTitleTextView.getText().toString(), answer);

                List<Integer> links = question.getLinks();
                if (links != null) {
                    int index = answer.isEmpty() ? 0 : 1;
                    int link = index < links.size() ? links.get(index) : -1;
                    ((SurveyActivity) mContext).goToQuestion(link, previousLink);
                    previousLink = link;
                } else
                    ((SurveyActivity) mContext).goToNext();
            }
        });

        mTitleTextView.setText(Html.fromHtml(question.getQuestionTitle()));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            SurveyViewUtils.showSoftInput(mAnswerEditText, mContext);
    }
}