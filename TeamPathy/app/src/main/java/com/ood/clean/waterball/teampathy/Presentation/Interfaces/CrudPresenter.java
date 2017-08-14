package com.ood.clean.waterball.teampathy.Presentation.Interfaces;


public interface CrudPresenter<Entity> extends LifetimePresenter{
    void loadEntities(int page);
    void create(Entity entity);
    void update(Entity entity);
    void delete(Entity entity);

    public interface CrudView<Entity>{
        void loadEntity(Entity entity);
        void onCreateFinishNotify(Entity entity);
        void onDeleteFinishNotify(Entity entity);
        void onUpdateFinishNotify(Entity entity);
        void onLoadFinishNotify();
        void onPageIndexOutOfBound();
        void onOperationTimeout(Throwable err);
    }


}
