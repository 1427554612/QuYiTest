package com.zhangjun.quyi.script_java.web.service.impl;

import com.zhangjun.quyi.script_java.web.service.JaninoService;
import org.codehaus.commons.compiler.IScriptEvaluator;
import org.codehaus.janino.ScriptEvaluator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JaninoServiceImpl implements JaninoService {

    @Override
    public Object run(Map<String, Object> script) throws Exception{
        String sp = (String)script.get("script");
        ScriptEvaluator se = new ScriptEvaluator();
        if(sp.contains("return")) {
            se.setReturnType(Object.class);
            se.cook(sp);
            return se.evaluate(null);
        };
        se.cook(sp);
        se.evaluate(null);
        return null;
    }
}
