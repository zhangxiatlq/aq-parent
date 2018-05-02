package com.aq.util.bean;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName:  SpringUtil   
 * @Description:spring util   
 * @author: wq
 * @date:   2017/9/5
 */
@Component
public class SpringUtil implements ApplicationContextAware{

	public static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
		
	}

	/**
     * 获取applicationContext对象
     * @return
     */
    public ApplicationContext getApplicationContext(){
        return applicationContext;
    }
    /**
     * 
     * @Title: getObject   
     * @Description:根据bean的id来查找对象
     * @param: @param id
     * @param: @return      
     * @return: Object
     * @author：李杰      
     * @throws
     */
	public static Object getObject(String id) {
		
		return applicationContext.getBean(id);
	}
	/**
	 * 
	 * @Title: getBeanByClass   
	 * @Description:根据bean的class来查找对象
	 * @param: @param c
	 * @param: @return      
	 * @return: Object
	 * @author：李杰      
	 * @throws
	 */
	public static <T> T getBeanByClass(Class<T> c) {

		return applicationContext.getBean(c);
	}
	/**
	 * 
	* @Title: getBeanByName  
	* @Description: TODO 
	* @param: @param name
	* @param: @param c
	* @param: @return
	* @return T
	* @author lijie
	* @throws
	 */
	public static <T> T getBeanByName(String name, Class<T> c) {

		return applicationContext.getBean(name, c);
	}
}