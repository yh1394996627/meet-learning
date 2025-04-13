package org.example.meetlearning.take;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutorService;

@Component
@Transactional
public class ScheduledTasks {

    @Autowired
    private ExecutorService taskExecutor;

    @Scheduled(cron = "0 * * * * ?")  // 每分钟执行一次
    public void executeForLoopWithThreadPool() {
        System.out.println("定时任务开始 | 线程: " + Thread.currentThread().getName());





        for (int i = 1; i <= 10; i++) {
            int finalI = i;
            taskExecutor.submit(() -> {
                System.out.println("处理数据: " + finalI + " | 线程: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        System.out.println("任务提交完成（异步执行中）");
    }
}
