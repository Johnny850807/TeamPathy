package com.ood.clean.waterball.teampathy.Domain.DI.Module;


import com.ood.clean.waterball.teampathy.Domain.Exception.ConverterFactory.ExceptionConverter;
import com.ood.clean.waterball.teampathy.Domain.Exception.ConverterFactory.ExceptionConverterImp;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskXmlTranslator;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskXmlTranslatorImp;
import com.ood.clean.waterball.teampathy.Threading.JobThread;
import com.ood.clean.waterball.teampathy.Threading.PostExecutionThread;
import com.ood.clean.waterball.teampathy.Threading.ThreadExecutor;
import com.ood.clean.waterball.teampathy.Threading.ThreadingObservableFactory;
import com.ood.clean.waterball.teampathy.Threading.UIThread;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    @Provides @Singleton
    public ThreadExecutor provideThreadExecutor(){
        return new JobThread();
    }

    @Provides @Singleton
    public PostExecutionThread providePostExecutionThread(){
        return new UIThread();
    }

    @Provides @Singleton
    public TaskXmlTranslator provideXmlTranslator(){
        return new TaskXmlTranslatorImp();
    }

    @Provides @Singleton
    public ThreadingObservableFactory provideThreadingObserverFactory(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread){
        return new ThreadingObservableFactory(threadExecutor,postExecutionThread);
    }

    @Provides @Singleton
    public ExceptionConverter provideExceptionConverter(){
        return new ExceptionConverterImp();
    }
}
