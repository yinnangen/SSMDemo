package cn.smbms.dao.user;

import java.sql.Connection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.smbms.entity.User;

public interface UserMapper {
	
	/**
	 * 增加用户信息
	 * @param user
	 * @return
	 */
	public int add(User user);
	
	/**
	 * 通过userCode获取User
	 * @param userCode
	 * @return
	 */
	public User getLoginUser(@Param("userCode") String userCode);
	
	/**
	 * 通过userCode获取User
	 * @param userCode
	 * @param password
	 * @return
	 */
	public User getUserByCodeAndPwd(@Param("userCode") String userCode,@Param("password")String password);

	/**
	 * 通过条件查询-userList
	 * @param userName
	 * @param userRole
	 * @return
	 */
	public List<User> getUserList(@Param("userName") String userName,@Param("userRole") int userRole,@Param("currentPageNo") int currentPageNo,@Param("pageSize") int pageSize);
	/**
	 * 通过条件查询-用户表记录数
	 * @param userName
	 * @param userRole
	 * @return
	 */
	public int getUserCount(@Param("userName")String userName,@Param("userRole")int userRole);
	
	/**
	 * 通过id删除user
	 * @param id
	 * @return
	 */
	public int deleteUserById(@Param("id")Integer id); 
	
	
	/**
	 * 通过userId获取user
	 * @param id
	 * @return
	 */
	public User getUserById(@Param("id")String id); 
	
	/**
	 * 修改用户信息
	 * @param user
	 * @return
	 */
	public int modify(User user);
	
	
	/**
	 * 修改当前用户密码
	 * @param id
	 * @param pwd
	 * @return
	 */
	public int updatePwd(@Param("id")int id,@Param("userPassword")String userPassword);
	
	
}
