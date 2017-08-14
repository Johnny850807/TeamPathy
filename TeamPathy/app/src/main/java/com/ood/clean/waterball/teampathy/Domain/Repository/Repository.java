package com.ood.clean.waterball.teampathy.Domain.Repository;


import java.util.List;

/*
* The base repository which maintains the methods about CRUD of entity.
 */
public interface Repository<Entity extends com.ood.clean.waterball.teampathy.Domain.Model.Entity> {
     Entity create(final Entity entity) throws Exception;
     Entity update(final Entity entity) throws Exception;
     Entity delete(final Entity entity) throws Exception;
     List<Entity> readList(final int page) throws Exception;
     Entity readDetails(final int id)throws Exception;
}
