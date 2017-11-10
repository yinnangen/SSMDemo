package cn.smbms.service.bill;

import java.util.List;

import cn.smbms.entity.Bill;

public interface BillService {

	/**
	 * 通过供应商Id、商品名称获取供应商列表-模糊查询-providerList
	 * @param productName 商品名称
	 * @param providerId 供应商Id
	 * @return
	 */
	public List<Bill> getBillList(String productName,Integer providerId,Integer isPayment,int currentPageNo,int pageSize);

	
	/**
	 * 通过条件查询-供应商表记录数
	 * @param productName
	 * @param providerId
	 * @return
	 */
	public int getbillCount(String productName,Integer providerId);
	
	/**
	 * 增加订单
	 * @param bill
	 * @return
	 */
	public boolean add(Bill bill);
	
	/**
	 * 通过id获取Bill
	 * @param id
	 * @return
	 */
	public Bill getBillById(String id);
	
	/**
	 * 修改订单
	 * @param bill
	 * @return
	 */
	public boolean modify(Bill bill);
	
	/**
	 * 根据id删除订单
	 * @param id
	 * @return
	 */
	public boolean deleteBill(String id);
}
