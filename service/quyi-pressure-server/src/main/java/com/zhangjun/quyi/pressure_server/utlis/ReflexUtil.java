package com.zhangjun.quyi.pressure_server.utlis;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class ReflexUtil {

    /**
     * 字节码文件列表
     */
    public static List<String> apiClassList = new ArrayList<>();
    private static String classPackagePath = "com.zhangjun.quyi.pressure_server.service.api";   // 需要加载的包

    static {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        String packagePath = null;
        try {
            Enumeration<URL> resources = contextClassLoader.getResources(classPackagePath.replaceAll("\\.","/"));
            URL url = null;
            while (resources.hasMoreElements()){
                url = resources.nextElement();
            }
            packagePath = url.getPath().substring(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Arrays.stream(new File(packagePath).listFiles()).forEach( classFile ->{
            String path = classFile.getPath();
            String className = classPackagePath + "." + path.substring(path.lastIndexOf("\\")+1,path.lastIndexOf("."));
            apiClassList.add(className);
        });


    }
    /**
     * 反射执行方法
     * @param apiName
     */
//    public static void reflexRunApi(String apiName){
//        i = new AtomicInteger(0);
//        apiRunVo = ThreadPoolUtil.startPool(requestParamEntity.getRequestNumber(), apiName, requestType, () -> {
//            ThreadRunVo threadRunVo = PressureApi.userCodeApi(requestParamEntity, paramsList.get(i.getAndIncrement()));
//            ThreadPoolUtil.countDownLatch.countDown();
//            return threadRunVo;
//        });
//        apiRunVoList.add(apiRunVo);
//
//        apiClassList.stream().forEach(apiClass ->{
//            Class<?> apiJavaClass = null;
//            try {
//                apiJavaClass = Class.forName(apiClass);
//                for (Method declaredMethod : apiJavaClass.getDeclaredMethods()) {
//                    if (declaredMethod.getName().equals(apiName)){
//                        System.out.println("匹配到对应方法");
//                        declaredMethod.invoke(apiJavaClass.newInstance()).
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//        });
//    }

//    public static void main(String[] args) {
//        ReflexUtil.reflexRunApi("registerApi");
//    }
}
