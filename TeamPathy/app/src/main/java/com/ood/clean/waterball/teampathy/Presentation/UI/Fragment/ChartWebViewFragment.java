package com.ood.clean.waterball.teampathy.Presentation.UI.Fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.ChartWebViewPresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.ChartWebViewPresenterImp;
import com.ood.clean.waterball.teampathy.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartWebViewFragment extends BaseFragment implements ChartWebViewPresenter.ChartWebView{
    private static final String TAG = "Chart";
    private static final String XSLFILENAME_PARAM = "xsl parameter";

    @BindView(R.id.webview) WebView webview;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    @Inject Project project;
    @Inject ChartWebViewPresenterImp presenterImp;

    private String xslFilename;

    @Override
    public void onUpdateTasksFinish(TaskItem taskRoot) {
        presenterImp.transformXmlToHtml(taskRoot, xslFilename);
    }

    enum XslType {
        WBS,GanttChart;

        public String toFileName() {
            return name() + ".xsl";
        }
    }

    public static ChartWebViewFragment newInstance(XslType xslXslType) {
        ChartWebViewFragment fragment = new ChartWebViewFragment();
        Bundle args = new Bundle();
        args.putString(XSLFILENAME_PARAM, xslXslType.toFileName());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        xslFilename = getArguments().getString(XSLFILENAME_PARAM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart_webview,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
        MyApp.getWbsComponent(getActivity()).inject(this);
        setupWebView();
        transformToWebPage();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
    }

    private void transformToWebPage(){
        presenterImp.setChartWebView(this);
        presenterImp.transformXmlToHtml(xslFilename);
    }

    @Override
    public void onWebPageFinish(String html) {
        webview.loadData(html,"text/html; charset=utf-8", "utf-8");
        progressBar.setVisibility(View.GONE);
        webview.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenterImp.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenterImp.onDestroy();
    }

}
