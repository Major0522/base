package com.easyget.terminal.base.task;

import org.apache.log4j.Logger;

import com.easyget.terminal.base.entity.Substance;
import com.easyget.terminal.base.service.CabinetService;
import com.easyget.terminal.base.service.LogAutoAnalysisService;
import com.easyget.terminal.base.service.SubstanceService;
import com.easyget.terminal.base.util.ApplicationUtil;
import com.easyget.terminal.base.util.AudioUtils;
import com.easyget.terminal.base.util.Const;
import com.easyget.terminal.base.view.control.MessageDialog;
import com.easyget.terminal.base.view.control.MessageDialog.MessageType;
import com.easyget.terminal.base.view.control.Progress;
import com.easyget.terminal.base.view.control.TakeDialog;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class TakeSubstanceTask {

    private static Logger logger = Logger.getLogger(TakeSubstanceTask.class);

    private static Timeline cellOpenTimeline;

    private static void startCellAlarm() {
        if (cellOpenTimeline == null) {
            cellOpenTimeline = new Timeline();
        }
        cellOpenTimeline.setCycleCount(Animation.INDEFINITE);
        cellOpenTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AudioUtils.getInstance().playCloseCell();
            }
        }));
        cellOpenTimeline.play();
    }

    private static void stopCellAlarm() {
        if (cellOpenTimeline != null) {
            cellOpenTimeline.stop();
            cellOpenTimeline = null;
        }
    }

    public static void take(final Substance substance) {
        logger.info("取走物品，substanceId：" + substance.getSubstanceId());

        final int slaveId = substance.getSlaveId();
        final int cellId = substance.getCellId();

        final Service<Boolean> service = new Service<Boolean>() {
            @Override
            protected Task<Boolean> createTask() {
                return new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        logger.info("打开格口[" + slaveId + "," + cellId + "]");
                        return CabinetService.getInstance().openCell(slaveId, cellId, Const.CELL_ACTION_TAKE);
                    }
                };
            }
        };
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Progress.getInstance().stop();
                final boolean ret = service.valueProperty().get();
                if (ret) {
                    logger.info("打开格口[" + slaveId + "," + cellId + "]:成功");

                    TakeDialog.getInstance().showDialog(60, null);
                    ApplicationUtil.setUsing(60);

                    SubstanceService.getInstance().take(substance);
                    startCellAlarm();
                    waitCellClose(slaveId, cellId);
                } else {
                    /**将领取快件或者样品失败过程记录到数据库中****/
                    LogAutoAnalysisService.getInstance().RecordTakeFailed(substance, slaveId, cellId);
                    /**将领取快件或者样品失败过程记录到数据库中****/
                    logger.info("打开格口[" + slaveId + "," + cellId + "]失败");
                    AudioUtils.getInstance().playOpenCellFial();
                    MessageDialog.getInstance().show(MessageType.INFO, "打开格口失败\n请联系客服4008882311处理", null);
                }
            }
        });
        service.start();
    }

    private static void waitCellClose(final int slaveId, final int cellId) {
        logger.info("监听格口[" + slaveId + "," + cellId + "]关闭");

        final Service<Boolean> service = new Service<Boolean>() {
            @Override
            protected Task<Boolean> createTask() {
                return new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        return CabinetService.getInstance().listenClosed(slaveId, cellId, Const.CELL_ACTION_TAKE);
                    }
                };
            }
        };
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                TakeDialog.getInstance().hide();
                stopCellAlarm();
                final boolean ret = service.valueProperty().get();
                logger.info("格口[" + slaveId + "," + cellId + "]关闭" + (ret ? "成功" : "失败"));
            }
        });
        service.setOnFailed(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent event) {
                TakeDialog.getInstance().hide();

                stopCellAlarm();
            }
        });
        service.start();
    }
}