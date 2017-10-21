package com.ood.clean.waterball.teampathy.Presentation.Interfaces;


import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;

public interface ChartWebViewPresenter extends LifetimePresenter{

    void transformXmlToHtml(String xslFilename);
    void transformXmlToHtml(final TaskItem taskRoot, final String xslFilename);

    public interface ChartWebView extends WbsConsolePresenter.WbsUpdatedListener {
        void onWebPageFinish(String html);
    }

}

