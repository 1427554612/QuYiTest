package com.zhangjun.quyi.test_task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangjun.quyi.test_task.entity.TaskCase;
import com.zhangjun.quyi.test_task.mapper.TaskCaseMapper;
import com.zhangjun.quyi.test_task.service.TaskCaseService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-12-23
 */
@Service
public class TaskCaseServiceImpl extends ServiceImpl<TaskCaseMapper, TaskCase> implements TaskCaseService {

}
