package com.aq.util.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @项目：phshopping-facade-permission
 *
 * @描述：分页基础Bean
 *
 * @作者： Mr.chang
 *
 * @创建时间：2017年3月17日
 * @Copyright @2017 by Mr.chang
 */
@ApiModel
public class PageBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8440299431844004877L;
	@ApiModelProperty(value = "当前页数(默认为1)",required = true)
	private int pageNum=1;
	@ApiModelProperty(value = "每页条数(默认为20)",required = true)
	private int pageSize=20;

	public PageBean(int pageNum, int pageSize) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	public PageBean() {
	}

	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	
}
