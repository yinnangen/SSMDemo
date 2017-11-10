package cn.smbms.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.smbms.entity.Provider;
import cn.smbms.entity.Role;
import cn.smbms.entity.User;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;

@Controller
@RequestMapping("/provider")
public class ProviderController {
	
	@Resource
	private ProviderService providerService;

	@RequestMapping(value="/providerlist.html")
	public String getProviderList(Model model,
							@RequestParam(value="queryProName",required=false) String queryProName,
							@RequestParam(value="queryProCode",required=false) String queryProCode,
							@RequestParam(value="pageIndex",required=false) String pageIndex){
		List<Provider> providerList = null;
		//设置页面容量
    	int pageSize = Constants.pageSize;
    	//当前页码
    	int currentPageNo = 1;
	
		if(queryProName == null){
			queryProName = "";
		}
		
		if(queryProCode == null){
			queryProCode = "";
		}
		
    	if(pageIndex != null){
    		try{
    			currentPageNo = Integer.valueOf(pageIndex);
    		}catch(NumberFormatException e){
    			return "redirect:/provider/syserror.html";
    		}
    	}	
    	//总数量（表）	
    	int totalCount	= providerService.getproviderCount(queryProName,queryProCode);
    	//总页数
    	PageSupport pages=new PageSupport();
    	pages.setCurrentPageNo(currentPageNo);
    	pages.setPageSize(pageSize);
    	pages.setTotalCount(totalCount);
    	int totalPageCount = pages.getTotalPageCount();
    	//控制首页和尾页
    	if(currentPageNo < 1){
    		currentPageNo = 1;
    	}else if(currentPageNo > totalPageCount){
    		currentPageNo = totalPageCount;
    	}
    	providerList = providerService.getProviderList(queryProName, queryProCode, (currentPageNo-1)*pageSize, pageSize);
		model.addAttribute("providerList", providerList);
		model.addAttribute("queryProName", queryProName);
		model.addAttribute("queryProCode", queryProCode);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("currentPageNo", currentPageNo);
		return "providerlist";
	}
	
	@RequestMapping(value="/provideradd.html",method=RequestMethod.GET)
	public String addProvider(@ModelAttribute("provider")Provider provider){
		return "provideradd";
	}
	
	@RequestMapping(value="/provideradd.html",method=RequestMethod.POST)
	public String addProviderSave(@Valid Provider provider,BindingResult bindingResult,HttpSession session){
		if(bindingResult.hasErrors()){
			return "provideradd";
		}
		provider.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
		provider.setCreationDate(new Date());
		if(providerService.add(provider)){
			return "redirect:/provider/providerlist.html";
		}
		return "provideradd";
	}
	
	@RequestMapping(value="/providermodify.html",method=RequestMethod.GET)
	public String getUserById(@RequestParam String id,Model model){
		Provider provider =providerService.getProviderById(id);
		model.addAttribute(provider);
		return "providermodify";
	}
	
	@RequestMapping(value="/providermodify.html",method=RequestMethod.POST)
	public String modifyUserSave(Provider provider,HttpSession session){
		provider.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
		provider.setModifyDate(new Date());
		if(providerService.modify(provider)){
			return "redirect:/provider/providerlist.html";
		}
		return "providermodify";
	}
	
	@RequestMapping(value="/view/{id}",method=RequestMethod.GET)
	public String view(@PathVariable String id,Model model){
		Provider provider=providerService.getProviderById(id);
		model.addAttribute(provider);
		return "providerview";
	}
	
	@RequestMapping("/syserror.html")
	public String sysError(){
		return "syserror";
	}
	
	@RequestMapping(value="/view")
	@ResponseBody
	public String view(@RequestParam String id){
		String cjson="";
		if(id==null||"".equals(id)){
			return "nodata";
		}else{
			try {
				 Provider provider=providerService.getProviderById(id);
				 cjson=JSON.toJSONStringWithDateFormat(provider,"yyyy-MM-dd");
			} catch (Exception e) {
				e.printStackTrace();
				return "failed";
			}
			return cjson;
		}
	}
	
	@RequestMapping(value = "/delete")
	@ResponseBody
	public String deleteUser(@RequestParam String proid) {
		String delResult;
		if (providerService.deleteProvider(proid))
			delResult = "true";
		else
			delResult = "false";
		return delResult;
	}
}
