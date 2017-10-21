package com.ood.clean.waterball.teampathy.Presentation.Presenter;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskXmlTranslator;
import com.ood.clean.waterball.teampathy.Domain.Repository.WbsRepository;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Base.DefaultObserver;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.ChartWebViewPresenter;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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
    private TaskXmlTranslator taskXmlTranslator;
    private ChartWebView chartWebView;
    private Context context;
    private List<Disposable> disposables = new ArrayList<>();

    @Inject
    public ChartWebViewPresenterImp(
            WbsRepository wbsRepository, ThreadingObservableFactory observerFactory, TaskXmlTranslator taskXmlTranslator, Context context) {
        //todo redesign this, should only the use case can access the repository layer
        this.wbsRepository = wbsRepository;
        this.observerFactory = observerFactory;
        this.taskXmlTranslator = taskXmlTranslator;
        this.context = context;
    }


    public void setChartWebView(ChartWebView chartWebView) {
        this.chartWebView = chartWebView;
    }

    @Override
    public void transformXmlToHtml(final String xslFilename) {
        disposables.add(createWbsTransformationUsecase(new WbsSupplier() {
            @Override
            public String get() throws Exception {
                return wbsRepository.getWbs();
            }
        }, xslFilename));
    }

    @Override
    public void transformXmlToHtml(final TaskItem taskRoot, final String xslFilename) {
        disposables.add(createWbsTransformationUsecase(new WbsSupplier() {
            @Override
            public String get() throws Exception {
                return taskXmlTranslator.taskToXml(taskRoot);
            }
        }, xslFilename));
    }

    public interface WbsSupplier{
        String get() throws Exception;
    }

    /**
     * Since there's only an async task for getting the html of the chart, we handle the usecase inside the presenter.
     * @param getWbs the method that get the project wbs as a string.
     * @param xslFilename the xsl file that transforms the wbs xml.
     * @return the disposable usecase that handles the transforming task.
     */
    private Disposable createWbsTransformationUsecase(final WbsSupplier getWbs, final String xslFilename){
        return observerFactory.create(Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                try{
                    String wbs = getWbs.get();
                    String html = transformXsl(wbs, xslFilename);
                    e.onNext(html);
                    e.onComplete();
                }catch (Exception err){
                    e.onError(err);
                }
            }
        })).subscribeWith(new DefaultObserver<String>() {
            @Override
            public void onNext(@NonNull String html) {
                chartWebView.onWebPageFinish(html);
            }
        });
    }


    private String transformXsl(String wbs, String xslFilename) throws Exception {
        InputStream inputStream = openXslStream(xslFilename);
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
        for (Disposable disposable : disposables)
            disposable.dispose();
    }
}
