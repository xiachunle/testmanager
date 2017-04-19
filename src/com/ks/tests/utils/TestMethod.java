package com.ks.tests.utils;

public class TestMethod {

	 public static void main(String[] args) {    
		 TestMethod myLocalDemo = new TestMethod();    
           System.out.println("path=="+myLocalDemo.getXmlPath());    
       }    
           
     /**  
        * 获取WEB-INF目录下面server.xml文件的路径  
        * @return  
        */    
       public String getXmlPath()    
       {    
           //file:/D:/JavaWeb/.metadata/.me_tcat/webapps/TestBeanUtils/WEB-INF/classes/     
           String path=Thread.currentThread().getContextClassLoader().getResource("").toString();    
           path=path.replace('/', '\\'); // 将/换成\    
           path=path.replace("file:", ""); //去掉file:    
           path=path.replace("classes\\", ""); //去掉class\    
           path=path.substring(1); //去掉第一个\,如 \D:\JavaWeb...    
           path+="web.xml";    
           //System.out.println(path);    
           return path;    
       }    

}
