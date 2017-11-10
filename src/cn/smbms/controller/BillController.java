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

import cn.smbms.entity.Bill;
import cn.smbms.entity.Provider;
import cn.smbms.entity.User;
import cn.smbms.service.bill.BillService;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;

@Controller
@RequestMapping("/bill")
public class BillController {
	
	@Resource
	private BillService billService;
	@Resource
	private ProviderService providerService;

	@RequestMapping("/querylist.html")
	public String queryBillList(@RequestParam(value="queryProductName",required=false) String productName,@RequestParam(value="queryProviderId",required=false) Integer providerId,@RequestParam(value="queryIsPayment",required=false) Integer isPayment,@RequestParam(value="pageIndex",required=false) String pageIndex,Model model){
		
		int pageSize = Constants.pageSize;
		
    	//当前页码
    	int currentPageNo = 1;
	
		if(productName == null){
			productName = "";
		}
		
		if(providerId == null){
			providerId = -1;
		}
		
    	if(pageIndex != null){
    		try{
    			currentPageNo = Integer.valueOf(pageIndex);
    		}catch(NumberFormatException e){
    			return "redirect:/provider/syserror.html";
    		}
    	}	
    	//总数量（表）	
    	int totalCount	= billService.getbillCount(productName, providerId);
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
    	
    	List<Provider> providerList = providerService.getAll();
    	List<Bill> billList = billService.getBillList(productName, providerId,isPayment,(currentPageNo-1)*pageSize, pageSize);
		model.addAttribute("billList", billList);
		model.addAttribute("providerList", providerList);
		model.addAttribute("queryProductName", productName);
		model.addAttribute("queryProviderId", providerId);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("currentPageNo", currentPageNo);
		return "billlist";
	}
	
	@RequestMapping(value="/view/{id}",method=RequestMethod.GET)
	public String view(@PathVariable String id,Model model){
		Bill bill=billService.getBillById(id);
		model.addAttribute(bill);
		return "billview";
	}
	
	@RequestMapping(value="/billadd.html",method=RequestMethod.GET)
	public String addBill(@ModelAttribute("bill")Bill bill){
		return "billadd";
	}
	
	@RequestMapping(value="/billadd.html",method=RequestMethod.POST)
	public String addBillSave(Bill bill,HttpSession session){
		bill.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
		bill.setCreationDate(new Date());
		if(billService.add(bill)){
			return "redirect:/bill/querylist.html";
		}
		return "billadd";
	}
	
	@RequestMapping(value="/billmodify.html",method=RequestMethod.GET)
	public String getBillById(@RequestParam String id,Model model){
		Bill bill =billService.getBillById(id);
		model.addAttribute(bill);
		return "billmodify";
	}
	
	@RequestMapping(value="/billmodify.html",method=RequestMethod.POST)
	public String modifyBillSave(Bill bill,HttpSession session){
		bill.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
		bill.setModifyDate(new Date());
		if(billService.modify(bill)){
			return "redirect:/bill/querylist.html";
		}
		return "billmodify";
	}
	
	@RequestMapping(value = "/delete")
	@ResponseBody
	public String deleteUser(@RequestParam String id) {
		String delResult;
		if (billService.deleteBill(id))
			delResult = "true";
		else
			delResult = "false";
		return delResult;
	}
	
	@RequestMapping(value = "/getproname")
	@ResponseBody
	public String getProductName() {
		List<Provider> proList=providerService.getAll();
		return JSON.toJSONStringWithDateFormat(proList,"yyyy-MM-dd");
	}
}
