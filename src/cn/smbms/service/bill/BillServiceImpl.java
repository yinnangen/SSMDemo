package cn.smbms.service.bill;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.smbms.dao.bill.BillMapper;
import cn.smbms.dao.provider.ProviderMapper;
import cn.smbms.entity.Bill;
import cn.smbms.entity.Provider;
@Service
public class BillServiceImpl implements BillService {
	@Resource
	private BillMapper billMapper;
	
	@Override
	public List<Bill> getBillList(String productName, Integer providerId,Integer isPayment,
			int currentPageNo, int pageSize) {
		return billMapper.getBillList(productName, providerId,isPayment,currentPageNo, pageSize);
	}

	@Override
	public int getbillCount(String productName, Integer providerId) {
		return billMapper.getbillCount(productName, providerId);
	}

	@Override
	public boolean add(Bill bill) {
		return billMapper.add(bill)>0;
	}

	@Override
	public Bill getBillById(String id) {
		return billMapper.getBillById(id);
	}

	@Override
	public boolean modify(Bill bill) {
		return billMapper.modify(bill)>0;
	}

	@Override
	public boolean deleteBill(String id) {
		return billMapper.deleteBillById(id)>0;
	}


}
