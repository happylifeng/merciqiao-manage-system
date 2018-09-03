package com.carloan.quartz.job;

import com.carloan.apimodel.biz.RuleCodeConstant;
import com.carloan.apimodel.workflow.common.CarFlowConst;
import com.carloan.common.redisTemplate.service.RedisService;
import com.carloan.feign.info.Jbpm4ServiceFeign;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by zhangyl on 2018/8/6
 * 定价--自动派单
 */
@DisallowConcurrentExecution
@Component("com.carloan.quartz.job.DispatchPriceTaskJob")
public class DispatchPriceTaskJob extends AbstractTaskJob implements Job, Serializable {
    private static final long serialVersionUID = 4006701892973955822L;

    private Jbpm4ServiceFeign jbpm4ServiceFeign;
    private RedisService redisService;
    @Override
    public void execute(JobExecutionContext context) {
        jbpm4ServiceFeign = super.getBean(context, Jbpm4ServiceFeign.class);
        redisService = super.getBean(context, RedisService.class);
        try {
            jbpm4ServiceFeign.dispatchJob(CarFlowConst.CAR_TASKTYPE_DINGJIA, CarFlowConst.PRICE);
        } finally {
            //清除redis缓存
            redisService.delete(KEY + ":" + RuleCodeConstant.PRICE_RULE);
        }
    }
}