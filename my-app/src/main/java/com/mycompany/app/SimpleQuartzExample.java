
package com.mycompany.app;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class SimpleQuartzExample {

    public static class HelloJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Hello Quartz! – " + new java.util.Date());
        }
    }

    public static void main(String[] args) {
        try {
            // 1. Create a Scheduler
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            // 2. Define the job and tie it to our HelloJob class
            JobDetail job = JobBuilder.newJob(HelloJob.class)
                    .withIdentity("helloJob", "group1")
                    .build();

            // 3. Create a trigger that fires now, and then every 5 seconds
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("helloTrigger", "group1")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(5)
                            .repeatForever())
                    .build();

            // 4. Schedule the job with the trigger
            scheduler.scheduleJob(job, trigger);

            // 5. Start the scheduler
            scheduler.start();

            // Let it run for, say, 20 seconds
            Thread.sleep(20000);

            // 6. Shutdown the scheduler gracefully
            scheduler.shutdown(true);

        } catch (SchedulerException | InterruptedException se) {
            se.printStackTrace();
        }
    }
}
