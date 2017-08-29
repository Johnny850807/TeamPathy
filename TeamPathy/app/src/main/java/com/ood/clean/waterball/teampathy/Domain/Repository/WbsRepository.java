package com.ood.clean.waterball.teampathy.Domain.Repository;


import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.WbsCommand;

public interface WbsRepository {
     String getWbs() throws Exception;
     <Data extends TaskItem> String executeWbsCommand(WbsCommand<Data> command) throws Exception;
}
