package cn.smbms.service.user;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.smbms.dao.user.UserMapper;
import cn.smbms.entity.User;

/**
 * service层捕获异常，进行事务处理
 * 事务处理：调用不同dao的多个方法，必须使用同一个connection（connection作为参数传递）
 * 事务完成之后，需要在service层进行connection的关闭，在dao层关闭（PreparedStatement和ResultSet对象）
 * @author Administrator
 *
 */
@Service
public class UserServiceImpl implements UserService{
	@Resource
	private UserMapper userMapper;

	@Override
	public User login(String userCode, String password) {
		try {
			return userMapper.getUserByCodeAndPwd(userCode, password);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<User> getUserList(String queryUserName, int queryUserRole,
			int currentPageNo, int pageSize) {
		return userMapper.getUserList(queryUserName,queryUserRole,currentPageNo,pageSize);
	}

	@Override
	public int getUserCount(String queryUserName, int queryUserRole) {
		return userMapper.getUserCount(queryUserName,queryUserRole);
	}

	@Override
	public boolean add(User user) {
		return userMapper.add(user)>0;
	}

	@Override
	public User selectUserCodeExist(String userCode) {
		return userMapper.getLoginUser(userCode);
	}

	@Override
	public boolean deleteUserById(Integer id) {
		return userMapper.deleteUserById(id)>0;
	}

	@Override
	public User getUserById(String id) {
		return userMapper.getUserById(id);
	}

	@Override
	public boolean modify(User user) {
		return userMapper.modify(user)>0;
	}

	@Override
	public boolean updatePwd(int id, String pwd) {
		return userMapper.updatePwd(id, pwd)>0;
	}

}
