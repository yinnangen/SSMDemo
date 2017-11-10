package cn.smbms.dao.provider;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.smbms.entity.Provider;

public interface ProviderMapper {
	
	/**
	 * 通过供应商名称、编码获取供应商列表-模糊查询-providerList
	 * @param proName
	 * @return
	 */
	public List<Provider> getProviderList(@Param("proName")String proName,@Param("proCode")String proCode,@Param("currentPageNo")int currentPageNo, @Param("pageSize")int pageSize);
	
	
	/**
	 * 通过条件查询-供应商表记录数
	 * @param proName
	 * @param proCode
	 * @return
	 */
	public int getproviderCount(@Param("proName")String proName,@Param("proCode")String proCode);
	
	
	
	/**
	 * 增加供应商
	 * @param provider
	 * @return
	 */
	public int add(Provider provider);
	
	/**
	 * 通过proId获取Provider
	 * @param id
	 * @return
	 */
	public Provider getProviderById(@Param("id") String id); 
	
	/**
	 * 修改用户信息
	 * @param user
	 * @return
	 */
	public int modify(Provider provider);
	
	/**
	 * 删除用户信息
	 * @param id
	 * @return
	 */
	public int deleteProById(@Param("id") String id);
	
	/**
	 * 查询全部用户
	 */
	public List<Provider> getAll();
}
