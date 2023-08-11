package com.zhangjun.quyi.script_java.web.service;

import java.util.Map;

public interface JaninoService {

    Object run(Map<String, Object> script) throws Exception;
}
