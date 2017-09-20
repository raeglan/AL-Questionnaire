package de.alfingo.al_questionnaire.fragment;

import android.support.v4.app.Fragment;

import de.alfingo.al_questionnaire.SurveyActivity;

/**
 * @author Rafael Miranda
 * @version 0.1
 * @since 14.09.2017
 */
public abstract class QuestionAbstractFragment extends Fragment {

    /**
     * The previous connection this question had. Used for seeing if anything changed.
     */
    protected int previousLink = SurveyActivity.LINK_NOT_SET;

    /**
     * Sets the previous link this object made.
     *
     * @param link the link that was made by the last choice this object made.
     */
    public void setPreviousLink(int link) {
        previousLink = link;
    }

}
