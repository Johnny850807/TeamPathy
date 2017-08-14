package com.ood.clean.waterball.teampathy;

import com.ood.clean.waterball.teampathy.Domain.Model.Entity;
import com.ood.clean.waterball.teampathy.Domain.Repository.Repository;

/**
 * Created by User on 2017/7/17.
 */

public class RepositoryTestHelper<T extends Entity> {
    private Repository<T> repository;
    private T firstInsert;
    private T updateInsert;

    public RepositoryTestHelper(Repository<T> repository,
                                T firstInsert,
                                T updateInsert) {
        this.repository = repository;
        this.firstInsert = firstInsert;
        this.updateInsert = updateInsert;
    }

    public int getCurrentSize() throws Exception {

        return repository.readList(0).size();
    }

    public void create100Entity() throws Exception {
        for ( int i = 301 ; i <= 400 ; i ++ )
        {
            T t = (T) firstInsert.getClass().getConstructor().newInstance();
            t.setId(i);
            repository.create(t);
        }
    }

    public void deleteFinal20Entity() throws Exception {
        for ( int i = 381 ; i <= 400 ; i ++ )
            repository.delete(repository.readDetails(i));
    }

    public void updateID400Entity() throws Exception {
        updateInsert.setId(400);
        repository.update(updateInsert);
    }

    public T get400sEntity() throws Exception {
        return repository.readDetails(400);
    }

}
