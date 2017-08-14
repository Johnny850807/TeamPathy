package com.ood.clean.waterball.teampathy.Presentation.Interfaces;

/**
 * Created by AndroidWork on 2017/8/3.
 */

public interface ChartWebViewPresenter extends LifetimePresenter{

    void transformXmlToHtml(String xslFilename);

    public interface ChartWebView{
        void onWebPageFinish(String html);
    }

}

