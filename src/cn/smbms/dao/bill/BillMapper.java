package cn.smbms.dao.bill;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.smbms.entity.Bill;

public interface BillMapper {
	
	/**
	 * 通过供应商Id、商品名称获取订单列表-模糊查询
	 * @param productName 商品名称
	 * @param providerId 供应商Id
	 * @return
	 */
	public List<Bill> getBillList(@Param("productName")String productName,@Param("providerId")Integer providerId,@Param("isPayment")Integer isPayment,@Param("currentPageNo")int currentPageNo, @Param("pageSize")int pageSize);
	
	
	/**
	 * 通过条件查询-订单记录数
	 * @param productName
	 * @param providerId
	 * @return
	 */
	public int getbillCount(@Param("productName")String productName,@Param("providerId")Integer providerId);
	
	
	
	/**
	 * 增加订单
	 * @param bill
	 * @return
	 */
	public int add(Bill bill);
	
	/**
	 * 通过id获取bill
	 * @param id
	 * @return
	 */
	public Bill getBillById(@Param("id") String id); 
	
	/**
	 * 修改订单
	 * @param user
	 * @return
	 */
	public int modify(Bill bill);
	
	/**
	 * 删除订单
	 * @param id
	 * @return
	 */
	public int deleteBillById(@Param("id") String id);
	
}
