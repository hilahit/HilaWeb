package com.example.parkinson.features.questionnaire;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.parkinson.R;
import com.example.parkinson.features.main.MainActivity;
import com.example.parkinson.features.main.MainViewModel;
import com.example.parkinson.features.questionnaire.single_question.SingleQuestionFragment;
import com.example.parkinson.model.question_models.Questionnaire;

import javax.inject.Inject;

public class QuestionnaireFragment extends Fragment {

    @Inject
    MainViewModel mainViewModel;

    @Inject
    QuestionnaireViewModel questionnaireViewModel;

    ImageButton exitBtn;
    CardView nextBtn;
    CardView backBtn;
    CardView finishBtn;
    private int pageNumber;


    public QuestionnaireFragment() {
        super(R.layout.fragment_questionnaire);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        ((MainActivity) getActivity()).mainComponent.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initUi();
        initObservers();

        //getting Args from NavigationComponent
        Boolean isNewQuestionnaire = QuestionnaireFragmentArgs.fromBundle(getArguments()).getIsNewQuestionnaire();
        questionnaireViewModel.init(isNewQuestionnaire);
    }

    private void initViews(View view){
        exitBtn = view.findViewById(R.id.questionnaireExitBtn);
        backBtn = view.findViewById(R.id.questionnaireBackBtn);
        nextBtn = view.findViewById(R.id.questionnaireNextBtn);
        finishBtn = view.findViewById(R.id.questionnaireFinishBtn);
        viewPager = view.findViewById(R.id.questionnaireFragViewPager);
    }

    private void initUi() {
        exitBtn.setOnClickListener(v->{
            getActivity().onBackPressed();
        });
        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
        nextBtn.setOnClickListener(v -> {
            onNextPressed();
        });
        finishBtn.setOnClickListener(v -> {
            questionnaireViewModel.onFinishClick();
            getActivity().onBackPressed();
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0){
                    // first page
                    backBtn.setVisibility(View.GONE);
                    nextBtn.setVisibility(View.VISIBLE);
                    finishBtn.setVisibility(View.GONE);

                } else if (position == pageNumber - 1) {
                    // last page
                    backBtn.setVisibility(View.VISIBLE);
                    nextBtn.setVisibility(View.GONE);
                    finishBtn.setVisibility(View.VISIBLE);
                } else {
                    // any other page
                    backBtn.setVisibility(View.VISIBLE);
                    nextBtn.setVisibility(View.VISIBLE);
                    finishBtn.setVisibility(View.GONE);
                }
            }
        });

    }

    private void initObservers() {
        questionnaireViewModel.questionnaireDataEvent.observe(getViewLifecycleOwner(), questionnaire -> {
            if (questionnaire != null) {
                initViewPager(questionnaire);
            }
        });
        questionnaireViewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateLoadingScreen(isLoading);
        });

    }

    public void onNextPressed() {
        if (viewPager.getCurrentItem() < pageNumber) {
            // Allow next press only if the user is currently looking at the last page
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            getActivity().onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }


    /**
     * Instantiate a ViewPager and a PagerAdapter.
     * Only when questionnaire already has value
     *
     * @param questionnaire
     */
    private void initViewPager(Questionnaire questionnaire) {
        pageNumber = questionnaire.getQuestionList().size();

        pagerAdapter = new ScreenSlidePagerAdapter(getActivity());
        viewPager.setAdapter(pagerAdapter);
    }


    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous and next wizard steps.
     **/
    private ViewPager2 viewPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     **/
    private FragmentStateAdapter pagerAdapter;

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            return new SingleQuestionFragment(position);
        }

        @Override
        public int getItemCount() {
            return pageNumber;
        }
    }
}

