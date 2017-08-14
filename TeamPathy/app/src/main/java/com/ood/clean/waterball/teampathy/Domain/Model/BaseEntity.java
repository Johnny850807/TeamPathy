package com.ood.clean.waterball.teampathy.Domain.Model;

/**
 * the base entity which is unique by the id.
 */
public abstract class BaseEntity implements Entity {
    protected static int entityAmount = 0;
    protected int id;

    public BaseEntity() {
        id = genId();
    }

    @Override
    public boolean equals(Object obj) {
        Entity idObj = (Entity) obj;
        return getId() == idObj.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    protected int genId(){
        return entityAmount ++;
    }

}
