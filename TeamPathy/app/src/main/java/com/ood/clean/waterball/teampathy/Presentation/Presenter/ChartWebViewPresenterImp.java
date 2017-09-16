package com.ood.clean.waterball.teampathy.Presentation.Presenter;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.ood.clean.waterball.teampathy.Domain.Repository.WbsRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.DefaultObserver;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.ChartWebViewPresenter;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.inject.Inject;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

// chart presenter cannot have a scope, because Gantt and WBS will use this together.
public class ChartWebViewPresenterImp implements ChartWebViewPresenter {
    private WbsRepository wbsRepository;
    private ThreadingObservableFactory observerFactory;
    private ChartWebView chartWebView;
    private Context context;
    private Disposable disposable;

    @Inject
    public ChartWebViewPresenterImp(
            WbsRepository wbsRepository, ThreadingObservableFactory observerFactory, Context context) {
        //todo redesign this, should only the use case can access the repository layer
        this.wbsRepository = wbsRepository;
        this.observerFactory = observerFactory;
        this.context = context;
    }


    public void setChartWebView(ChartWebView chartWebView) {
        this.chartWebView = chartWebView;
    }

    @Override
    public void transformXmlToHtml(final String xslFilename) {
        disposable = observerFactory.create(Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                String html = transformXsl(xslFilename);
                e.onNext(html);
                e.onComplete();
            }
        })).subscribeWith(new DefaultObserver<String>() {
            @Override
            public void onNext(@NonNull String html) {
                chartWebView.onWebPageFinish(html);
            }
        });
    }

    private String transformXsl(String xslFilename) throws Exception {
        InputStream inputStream = openXslStream(xslFilename);
        String wbs = wbsRepository.getWbs();
        Log.d("myLog",wbs);
        return  getXmlTransformWithXsl(wbs,inputStream);
    }

    private InputStream openXslStream(String xslFileName) throws IOException {
        AssetManager assetManager = context.getAssets();
        return assetManager.open(xslFileName);
    }

    private String getXmlTransformWithXsl(String xml, InputStream xslIs) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(xslIs);
        Transformer transformer = factory.newTransformer(xslt);
        transformer.setOutputProperty(OutputKeys.METHOD, "html");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        StringReader reader = new StringReader(xml);
        StringWriter resultWriter = new StringWriter();
        transformer.transform(new StreamSource(reader), new StreamResult(resultWriter));
        return resultWriter.toString();
    }

    @Override
    public void onResume() {}

    @Override
    public void onDestroy() {
        if (disposable != null)
            disposable.dispose();
    }
}
