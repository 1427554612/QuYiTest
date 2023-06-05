package com.zhangjun.quyi.test_task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.quyi.test_task.entity.TaskRunLog;
import com.zhangjun.quyi.test_task.mapper.TaskRunLogMapper;
import com.zhangjun.quyi.test_task.service.TaskRunLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-12-27
 */
@Service
public class TaskRunLogServiceImpl extends ServiceImpl<TaskRunLogMapper, TaskRunLog> implements TaskRunLogService {

}
