package cn.smbms.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;

import cn.smbms.entity.Role;
import cn.smbms.entity.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;

@Controller
@RequestMapping("/user")
public class UserController {
	private Logger logger = Logger.getLogger(UserController.class);
	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;

	@RequestMapping("/login.html")
	public String login() {
		logger.info("UserController welcome SpringMvcTest");
		return "login";
	}

	@RequestMapping(value = "/dologin.html")
	public String doLogin(@RequestParam String userCode,
			@RequestParam String userPassword, HttpSession session,
			HttpServletRequest request) {
		User user = userService.login(userCode, userPassword);
		if (user != null) {
			session.setAttribute(Constants.USER_SESSION, user);
			return "redirect:/user/main.html";
		} else {
			request.setAttribute("error", "用户名或密码不正确");
			return "login";
		}
	}

	@RequestMapping("/main.html")
	public String main(HttpSession session) {
		if (session.getAttribute(Constants.USER_SESSION) == null) {
			return "redirect:/user/syserror.html";
		}
		return "frame";
	}

	@RequestMapping("/logout.html")
	public String logout(HttpSession session) {
		// 清除session
		session.removeAttribute(Constants.USER_SESSION);
		return "login";
	}

	@RequestMapping(value = "/userlist.html")
	public String getUserList(
			Model model,
			@RequestParam(value = "queryname", required = false) String queryUserName,
			@RequestParam(value = "queryUserRole", required = false) String queryUserRole,
			@RequestParam(value = "pageIndex", required = false) String pageIndex) {
		int _queryUserRole = 0;
		List<User> userList = null;
		// 设置页面容量
		int pageSize = Constants.pageSize;
		// 当前页码
		int currentPageNo = 1;

		if (queryUserName == null) {
			queryUserName = "";
		}
		if (queryUserRole != null && !queryUserRole.equals("")) {
			_queryUserRole = Integer.parseInt(queryUserRole);
		}

		if (pageIndex != null) {
			try {
				currentPageNo = Integer.valueOf(pageIndex);
			} catch (NumberFormatException e) {
				return "redirect:/user/syserror.html";
			}
		}
		// 总数量（表）
		int totalCount = userService
				.getUserCount(queryUserName, _queryUserRole);
		// 总页数
		PageSupport pages = new PageSupport();
		pages.setCurrentPageNo(currentPageNo);
		pages.setPageSize(pageSize);
		pages.setTotalCount(totalCount);
		int totalPageCount = pages.getTotalPageCount();
		// 控制首页和尾页
		if (currentPageNo < 1) {
			currentPageNo = 1;
		} else if (currentPageNo > totalPageCount) {
			currentPageNo = totalPageCount;
		}
		userList = userService.getUserList(queryUserName, _queryUserRole,
				(currentPageNo-1)*pageSize, pageSize);
		model.addAttribute("userList", userList);
		List<Role> roleList = null;
		roleList = roleService.getRoleList();
		model.addAttribute("roleList", roleList);
		model.addAttribute("queryUserName", queryUserName);
		model.addAttribute("queryUserRole", queryUserRole);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("currentPageNo", currentPageNo);
		return "userlist";
	}

	@RequestMapping("/syserror.html")
	public String sysError() {
		return "syserror";
	}

	@RequestMapping(value = "/useradd.html", method = RequestMethod.GET)
	public String addUser(@ModelAttribute("user") User user) {
		return "useradd";
	}

	@RequestMapping(value = "/useradd.html", method = RequestMethod.POST)
	public String addUserSave(
			@Valid User user,
			BindingResult bindingResult,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "a_idPicPath", required = false) MultipartFile attach) {
		if (bindingResult.hasErrors()) {
			return "useradd";
		}
		String idPicPath = null;
		if (!attach.isEmpty()) {
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			String oldFilename = attach.getOriginalFilename();
			String prefix = FilenameUtils.getExtension(oldFilename);
			int filesize = 500000;
			if (attach.getSize() > filesize) {
				return "useradd";
			} else if (prefix.equalsIgnoreCase("jpg")
					|| prefix.equalsIgnoreCase("png")
					|| prefix.equalsIgnoreCase("jpeg")
					|| prefix.equalsIgnoreCase("pneg")) {
				String fileName = System.currentTimeMillis()
						+ RandomUtils.nextInt(1000000) + "_Personal.jpg";
				File targetFile = new File(path, fileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
				idPicPath = path + File.separator + fileName;
			} else {
				return "useradd";
			}
		}
		user.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION))
				.getId());
		user.setCreationDate(new Date());
		user.setIdPicPath(idPicPath);
		if (userService.add(user)) {
			return "redirect:/user/userlist.html";
		}
		return "useradd";
	}

	@RequestMapping(value = "/usermodify.html", method = RequestMethod.GET)
	public String getUserById(@RequestParam String uid, Model model) {
		User user = userService.getUserById(uid);
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		 String birthday=sdf.format(user.getBirthday());
		 model.addAttribute("birthday",birthday);
		model.addAttribute(user);
		return "usermodify";
	}

	@RequestMapping(value = "/usermodify.html", method = RequestMethod.POST)
	public String modifyUserSave(User user, HttpSession session) {
		user.setModifyBy(((User) session.getAttribute(Constants.USER_SESSION))
				.getId());
		user.setModifyDate(new Date());
		if (userService.modify(user)) {
			return "redirect:/user/userlist.html";
		}
		return "usermodify";
	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable String id, Model model) {
		User user = userService.getUserById(id);
		model.addAttribute(user);
		return "userview";
	}

	@RequestMapping(value = "/ucexist.html")
	@ResponseBody
	public Object userCodeIsExit(@RequestParam String userCode) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if (StringUtils.isNullOrEmpty(userCode)) {
			resultMap.put("userCode", "exist");
		} else {
			User user = userService.selectUserCodeExist(userCode);
			if (user != null)
				resultMap.put("userCode", "exist");
			else
				resultMap.put("userCode", "noexist");
		}
		return JSONArray.toJSONString(resultMap);
	}

	@RequestMapping(value = "/view")
	@ResponseBody
	public String view(@RequestParam String id) {
		String cjson = "";
		User user = null;
		if (id == null || "".equals(id)) {
			return "nodata";
		} else {
			try {
				user = userService.getUserById(id);
				cjson = JSON.toJSONStringWithDateFormat(user, "yyyy-MM-dd");
				// cjson=JSON.toJSONString(user);
			} catch (Exception e) {
				e.printStackTrace();
				return "failed";
			}
			return cjson;
		}
	}

	@RequestMapping(value = "/getByCode/{userCode}")
	@ResponseBody
	public String getByCode(@PathVariable String userCode) {
		User user = userService.selectUserCodeExist(userCode);
		if (user != null) {
			return JSON.toJSONStringWithDateFormat(user, "yyyy-MM-dd");
		} else {
			return "false";
		}
	}

	@RequestMapping(value = "/getData")
	@ResponseBody
	public String getData() {
		List<User> user = userService.getUserList(null, 0, 1, 5);
		return JSON.toJSONStringWithDateFormat(user, "yyyy-MM-dd");
	}

	@RequestMapping(value = "/delete")
	@ResponseBody
	public String deleteUser(@RequestParam String id) {
		String flag;
		if (userService.deleteUserById(Integer.valueOf(id)))
			flag = "true";
		else
			flag = "false";
		return flag;
	}
	
	@RequestMapping(value="/pwdmodify.html",method=RequestMethod.GET)
	public String pwdmodify(){
		return "pwdmodify";
	}
	
	@RequestMapping(value="/pwdmodify.html",method=RequestMethod.POST)
	public String pwdmodifySave(@RequestParam String newpassword,HttpSession session){
		User user = (User)session.getAttribute(Constants.USER_SESSION);
		int id =user.getId();
		boolean flag=userService.updatePwd(id, newpassword);
		if(flag){
			return "redirect:/user/login.html";
		}
		return "pwdmodify";
	}
	
	/**
	 * 验证旧密码是否正确
	 * @param oldpassword
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getpwd")
	@ResponseBody
	public String checkpwd(@RequestParam String oldpassword,HttpSession session) {
		String msg=null;
		User user = (User)session.getAttribute(Constants.USER_SESSION);
		String pwd=user.getUserPassword();
		if(oldpassword==null||oldpassword.equals("")){
			msg="error";
		}else if(!oldpassword.equals(pwd)){
			msg="false";
		}else if(pwd==null||pwd.equals("")){
			msg="sessionerror";
		}else{
			msg="true";
		}
		return msg;
	}
	
	@RequestMapping(value = "/getrolename")
	@ResponseBody
	public String getRoleName(){
		List<Role> roleList = roleService.getRoleList();
		return JSON.toJSONStringWithDateFormat(roleList,"yyyy-MM-dd");
	}
}
