package com.ood.clean.waterball.teampathy.Framework.Retrofit.Repository;


public class ResponseModel<TEntity> {
    private int status;
    private String message;
    private TEntity data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TEntity getData() {
        return data;
    }

    public void setData(TEntity data) {
        this.data = data;
    }
}
