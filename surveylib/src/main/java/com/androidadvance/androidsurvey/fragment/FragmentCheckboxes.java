package com.androidadvance.androidsurvey.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidadvance.androidsurvey.Answers;
import com.androidadvance.androidsurvey.R;
import com.androidadvance.androidsurvey.Survey;
import com.androidadvance.androidsurvey.SurveyActivity;
import com.androidadvance.androidsurvey.models.Question;
import com.androidadvance.androidsurvey.utils.SurveyViewUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentCheckboxes extends QuestionAbstractFragment {

    private static final String TAG = Survey.LIBRARY_NAME + ":"
            + FragmentCheckboxes.class.getSimpleName();

    private Question mQuestion;
    private FragmentActivity mContext;
    private Button mContinueButton;
    private TextView mTitleTextView;
    private LinearLayout linearLayout_checkboxes;

    private final ArrayList<CheckBox> mCheckBoxes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_checkboxes, container, false);

        mContinueButton = rootView.findViewById(R.id.button_continue);
        mTitleTextView = rootView.findViewById(R.id.textview_q_title);
        linearLayout_checkboxes = rootView.findViewById(R.id.linearLayout_checkboxes);

        // Personalizing
        SurveyViewUtils
                .personalizeButton(getActivity(), Survey.KEY_CONTINUE_TEXT_RES, mContinueButton);

        return rootView;
    }

    private void collect_data() {

        //----- collection & validation for is_required
        StringBuilder answers = new StringBuilder();
        boolean atLeastOneChecked = false;
        for (CheckBox cb : mCheckBoxes) {
            if (cb.isChecked()) {
                atLeastOneChecked = true;
                answers.append(cb.getText().toString());
                answers.append(", ");
            }
        }

        if (answers.length() > 2) {
            // removing the last ", "
            answers.delete(answers.length() - 2, answers.length());
            Answers.getInstance().put_answer(mTitleTextView.getText().toString(),
                    answers.toString());
        }


        if (mQuestion.getRequired()) {
            if (atLeastOneChecked) {
                mContinueButton.setVisibility(View.VISIBLE);
            } else {
                mContinueButton.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mContext = getActivity();
        mQuestion = (Question) getArguments().getSerializable("data");

        // Letting everyone knows when something horrible goes wrong.
        if (mQuestion == null) {
            Log.e(TAG, "A fragment without data was initialized, that shouldn't happen.");
            ((SurveyActivity) mContext).goToNext();
            return;
        }

        mTitleTextView.setText(mQuestion.getQuestionTitle());

        if (mQuestion.getRequired()) {
            mContinueButton.setVisibility(View.GONE);
        }

        List<String> choices = mQuestion.getChoices();

        if (choices.isEmpty()) {
            Log.e(Survey.LIBRARY_NAME, "Checkbox with no choices: "
                    + mTitleTextView.getText().toString());
            ((SurveyActivity) mContext).goToNext();
        }

        if (mQuestion.getRandomChoices()) {
            Collections.shuffle(choices);
        }

        for (String choice : choices) {
            CheckBox checkBox = new CheckBox(mContext);
            checkBox.setText(Html.fromHtml(choice));
            checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout_checkboxes.addView(checkBox);
            mCheckBoxes.add(checkBox);


            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    collect_data();
                }
            });
        }

        // todo: Solve onBackPressed problem, when we get back to this question and then try linking again then too many fragments get added.
        // todo: Make selecting checkboxes add new questions to the stack instead of moving to the first link.
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> links = mQuestion.getLinks();
                if (links != null) {
                    for (int i = 0; i < mCheckBoxes.size(); i++) {
                        if (mCheckBoxes.get(i).isChecked()) {
                            int link = i >= 0 && i < links.size() ? links.get(i) : -1;
                            ((SurveyActivity) mContext).goToQuestion(link, previousLink);
                            previousLink = link;
                            break;
                        }
                    }
                } else
                    ((SurveyActivity) mContext).goToNext();
            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            SurveyViewUtils.hideSoftInput(mContext);
    }


}