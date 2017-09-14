package com.androidadvance.androidsurvey.adapters;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.androidadvance.androidsurvey.Survey;
import com.androidadvance.androidsurvey.fragment.FragmentCheckboxes;
import com.androidadvance.androidsurvey.fragment.FragmentEnd;
import com.androidadvance.androidsurvey.fragment.FragmentMultiline;
import com.androidadvance.androidsurvey.fragment.FragmentNumber;
import com.androidadvance.androidsurvey.fragment.FragmentRadioboxes;
import com.androidadvance.androidsurvey.fragment.FragmentStart;
import com.androidadvance.androidsurvey.fragment.FragmentTextSimple;
import com.androidadvance.androidsurvey.fragment.QuestionAbstractFragment;
import com.androidadvance.androidsurvey.models.Question;
import com.androidadvance.androidsurvey.models.SurveyProperties;

import java.util.List;

/**
 * The adapter that will fill our pager and make everything look beautiful. The Count is always
 * returned as 1 more than the actual number of slides, that is because the EndFragment should
 * always come no matter what.
 *
 * @author Rafael miranda
 */
public class SurveyFragmentAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = Survey.LIBRARY_NAME + ":" +
            SurveyFragmentAdapter.class.getSimpleName();

    private final List<Question> mQuestions;
    private final SurveyProperties mSurveyProperties;
    private final List<Integer> mFragmentOrder;

    /*
     * A list of the already created fragments and their position. It changes whenever a new one
     * gets added or deleted from the order list so that the pager can make a new request.
     * <br><br>
     * The key should be the position and value is the hashcode of the fragment in this
     * position, both should be unique.
     * private final SparseIntArray mCreatedFragmentHashes;
     */

    /**
     * Instantiates a new fragment adapter that handles creating fragments and giving them to the
     * view pager. For that we need all the questions to be displayed, as well as the properties
     * (so that the start and end may be created)
     *
     * @param manager          for instantiating the super
     * @param questions        All the questions that MIGHT be displayed, this list should contain
     *                         everything received.
     * @param surveyProperties the properties containing the start and end screen
     * @param fragmentOrder    the initial order in which everything is displayed. -1 means start
     *                         screen everything else should be an index to be found in the questions
     *                         list given.
     */
    public SurveyFragmentAdapter(FragmentManager manager, List<Question> questions,
                                 SurveyProperties surveyProperties, List<Integer> fragmentOrder) {
        super(manager);
        mQuestions = questions;
        mFragmentOrder = fragmentOrder;
        mSurveyProperties = surveyProperties;
    }

    /**
     * Instantiates and gets the item to be displayed in this position.
     *
     * @param position where the item is in our order list
     * @return the instantiated fragment
     */
    @Override
    public Fragment getItem(int position) {
        int selectedFragment;
        Fragment fragment;

        // that means end fragment
        if (position >= mFragmentOrder.size()) {
            fragment = new FragmentEnd();
            Bundle sBundle = new Bundle();
            sBundle.putSerializable(Survey.KEY_SURVEY_PROPERTIES, mSurveyProperties);
            fragment.setArguments(sBundle);
        } else if ((selectedFragment = mFragmentOrder.get(position)) == -1) {
            // if -1 then we know we should load the start fragment(this is not always present)
            fragment = new FragmentStart();
            Bundle sBundle = new Bundle();
            sBundle.putSerializable(Survey.KEY_SURVEY_PROPERTIES, mSurveyProperties);
            fragment.setArguments(sBundle);
        } else {
            // selecting which question fragment should be loaded.
            Question question = mQuestions.get(selectedFragment);
            switch (question.getQuestionType()) {
                case "String":
                    fragment = new FragmentTextSimple();
                    break;
                case "Checkboxes":
                    fragment = new FragmentCheckboxes();
                    break;
                case "Radioboxes":
                    fragment = new FragmentRadioboxes();
                    break;
                case "Number":
                    fragment = new FragmentNumber();
                    break;
                case "StringMultiline":
                    fragment = new FragmentMultiline();
                    break;
                default:
                    throw new UnsupportedOperationException("Question type not known: " +
                            question.getQuestionType());
            }

            // check if some connection has already been made by this question
            if (question.getLinks() != null && position + 1 < mFragmentOrder.size())
                ((QuestionAbstractFragment) fragment)
                        .setPreviousLink(mFragmentOrder.get(position + 1));

            // putting all the extra data needed to fill the question.
            Bundle arguments = new Bundle();
            arguments.putSerializable("data", question);
            fragment.setArguments(arguments);
        }

        return fragment;
    }

    /**
     * We need to make sure the object is still as the same position as before, if not then we tell
     * the pager that.
     *
     * @param object the object that we need to verify the position of.
     * @return the current position of this object or POSITION_NONE if it can't be found.
     */
    @Override
    public int getItemPosition(Object object) {
        // todo: Optimize. There can be duplicates and the list saved contain only numbers.
        if (mSurveyProperties.getConditionalLinking())
            return POSITION_NONE;
        else return POSITION_UNCHANGED;
    }

    /**
     * Returns the number of fragments that can be displayed. This number represents the start
     * fragment(if any), all the questions and the end fragment. <br>
     * Note that the end fragment is always there and can't therefore be deleted from the list.
     *
     * @return how many views can be displayed by this adapter.
     */
    @Override
    public int getCount() {
        return this.mFragmentOrder.size() + 1;
    }

    /**
     * Adds a single question to the end of the list and notifies the change.
     *
     * @param questionIndex the index of the question to be added.
     */
    public void add(@NonNull Integer questionIndex) {
        mFragmentOrder.add(questionIndex);
        notifyDataSetChanged();
    }

    /**
     * Adds a single question to the given place in the list and notifies the change.
     *
     * @param questionIndex the index of the question to be added.
     */
    public void add(int index, @NonNull Integer questionIndex) {
        mFragmentOrder.add(index, questionIndex);
        notifyDataSetChanged();
    }

    /**
     * Removes the item in the given position also notifies.
     *
     * @param index The position which should be removed, getCount is off by one as the last
     *              element cannot be changed.
     */
    public void remove(int index) {
        if (index < mFragmentOrder.size()) {
            mFragmentOrder.remove(index);
            notifyDataSetChanged();
        } else {
            Log.e(TAG, "Tried removing something out of bounds, getCount is off by one!");
        }
    }

    /**
     * Removes every item that comes after the given index.
     * @param index exclusive, everything after this index will be deleted. Ignores the FragmentEnd.
     *              As it stays fixed no matter what.
     */
    public void clearAfter(int index) {
        int nextItem = index + 1;
        while (nextItem < mFragmentOrder.size())
            mFragmentOrder.remove(nextItem);
        notifyDataSetChanged();
    }
}
