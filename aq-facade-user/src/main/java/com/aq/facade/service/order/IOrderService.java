package com.aq.facade.service.order;

import com.aq.facade.dto.customer.VipPurchaseDTO;
import com.aq.facade.dto.order.AsyncOrderDTO;
import com.aq.facade.dto.order.UpdateOrderDTO;
import com.aq.facade.dto.order.UpdateVipOrderDTO;
import com.aq.util.result.Result;

/**
 * 
 * @ClassName: IOrderService
 * @Description: 订单相关服务接口
 * @author: lijie
 * @date: 2018年1月29日 下午2:43:39
 */
public interface IOrderService {
	/**
	 * @Creater: zhangxia
	 * @description：通过主订单号进行订单更新
	 * @methodName: updateOrderByMainOrderNo
	 * @params: [updateOrderDTO]
	 * @return: com.aq.util.result.Result
	 * @createTime: 14:20 2018-2-13
	 */
	Result updateOrderByMainOrderNo(UpdateOrderDTO updateOrderDTO);


	/**
	 * 订单异步回调（策略）
	 *
	 * @param asyncOrderDTO
	 * @return com.aq.util.result.Result
	 * @author 郑朋
	 * @create 2018/2/26 0026
	 */
	Result updateAsyncOrder(AsyncOrderDTO asyncOrderDTO);

	/**
	 * 订单异步回调（投顾）
	 *
	 * @param asyncOrderDTO
	 * @return com.aq.util.result.Result
	 * @author 郑朋
	 * @create 2018/3/07
	 */
	Result updateAsyncAdviserOrder(AsyncOrderDTO asyncOrderDTO);

	/**
	 * @Creater: zhangxia
	 * @description：通过主订单号获取所有子订单
	 * @methodName: getOrdersByMainOrderNo
	 * @params: [mainOrderNo]
	 * @return: com.aq.util.result.Result
	 * @createTime: 14:21 2018-2-13
	 */
	Result getOrdersByMainOrderNo(String mainOrderNo);
	/**
	 * 
	* @Title: addVipOrder  
	* @Description: 生成vip购买订单  
	* @param @param dto
	* @param @return    参数  
	* @return Result    返回类型  
	* @throws
	 */
	Result addVipOrder(VipPurchaseDTO dto);
	/**
	 * 
	* @Title: updateVipOrder  
	* @Description: 修改vip订单  
	* @param @param dto
	* @param @return    参数  
	* @return Result    返回类型  
	* @throws
	 */
	Result updateVipOrder(UpdateVipOrderDTO dto);
	/**
	 * 
	* @Title: getVipOrderInfo  
	* @Description: 根据订单号/订单ID查询订单信息 
	* @param @param obj
	* @param @return    参数  
	* @return Result    返回类型  
	* @throws
	 */
	Result getVipOrderInfo(Object obj);
}
