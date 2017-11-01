package com.ood.clean.waterball.teampathy.Presentation.UI.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.WbsConsolePresenterImp;
import com.ood.clean.waterball.teampathy.Presentation.UI.Dialog.ProjectCaseoverDialogFragment;
import com.ood.clean.waterball.teampathy.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskAnalyticsFragment extends BaseFragment{
    private String CONSOLE;
    private String WBS;
    private String GANTT;
    private String REVIEW_TASK;
    private String[] TASK_ANALYSIS_SECTIONS;

    @BindView(R.id.tablayout) TabLayout tabLayout;
    @BindView(R.id.viewpager) ViewPager viewPager;
    ProjectCaseoverDialogFragment caseoverDialogFragment = new ProjectCaseoverDialogFragment();
    MyFragmentPageAdapter adapter;
    @Inject Member member;
    @Inject Project project;
    @Inject WbsConsolePresenterImp wbsConsolePresenterImp;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_analytics,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
        MyApp.getWbsComponent(getActivity()).inject(this);
        caseoverDialogFragment.setBaseView(getBaseView());
        initStringResources();
        setHasOptionsMenu(!project.isCaseclosed());
        initiateViewPager();
    }

    private void initStringResources(){
        CONSOLE = getString(R.string.console);
        WBS = getString(R.string.wbs);
        GANTT = getString(R.string.gantt);
        REVIEW_TASK = getString(R.string.task_review_section);
        TASK_ANALYSIS_SECTIONS = getTaskAnalysisSectionsByMemberAndProjectState();
    }

    private String[] getTaskAnalysisSectionsByMemberAndProjectState(){
        // if the project has done the caseover or the member is not a manager, the operation section should be hidden.
        if (project.isCaseclosed() || member.isNotManager())
            return new String[]{WBS, GANTT};
        else if (member.isManager()) // if the member is a manager,
            return new String[]{CONSOLE, WBS, GANTT, REVIEW_TASK};

        throw new IllegalStateException("No matched state.");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.caseover_on_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.caseclosed)
            showAlertDialogFragment(caseoverDialogFragment);
        return true;
    }

    private void initiateViewPager(){
        adapter = new MyFragmentPageAdapter(getChildFragmentManager());
        viewPager.setOffscreenPageLimit(adapter.getCount());
        tabLayout.setupWithViewPager(viewPager);
        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("myLog", "onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("myLog", "onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("myLog", "onPageScrollStateChanged");
            }
        };
        viewPager.addOnPageChangeListener(onPageChangeListener);
        viewPager.setAdapter(adapter);
    }



    private class MyFragmentPageAdapter extends FragmentPagerAdapter {


        public MyFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            super.getPageTitle(position);
            return TASK_ANALYSIS_SECTIONS[position];
        }

        @Override
        public Fragment getItem(int position) {
            /*
              The Wbs console, wbs chart and the gantt chart fragments should be added as a WbsUpdatedListener
              into the presenter in order to ensure all the fragments would be updated
              at the same time whenever the wbs updated.
             */
            String section = TASK_ANALYSIS_SECTIONS[position];

            if (section.equals(CONSOLE))
            {
                WbsConsoleFragment consolefragment = new WbsConsoleFragment();
                wbsConsolePresenterImp.putWbsUpdatedListener("console", consolefragment);
                return consolefragment;
            }
            else if (section.equals(WBS))
            {
                ChartWebViewFragment wbsfragment = ChartWebViewFragment.newInstance(
                        ChartWebViewFragment.XslType.WBS);
                wbsConsolePresenterImp.putWbsUpdatedListener("wbs", wbsfragment);
                return wbsfragment;
            }
            else if (section.equals(GANTT))
            {
                ChartWebViewFragment ganttfragment = ChartWebViewFragment.newInstance(
                        ChartWebViewFragment.XslType.GanttChart);
                wbsConsolePresenterImp.putWbsUpdatedListener("gantt", ganttfragment);
                return ganttfragment;
            }
            else if (section.equals(REVIEW_TASK))
                return new TaskPendingFragment();
            else
                throw new IllegalStateException("No matched section name: got " + section);
        }

        @Override
        public int getCount() {
            return TASK_ANALYSIS_SECTIONS.length;
        }
    }

}
