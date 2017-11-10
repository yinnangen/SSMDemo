package cn.smbms.service.provider;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.smbms.dao.provider.ProviderMapper;
import cn.smbms.entity.Provider;
@Service
public class ProviderServiceImpl implements ProviderService {
	@Resource
	private ProviderMapper providerMapper;

	@Override
	public List<Provider> getProviderList(String proName, String proCode,
			int currentPageNo, int pageSize) {
		return providerMapper.getProviderList(proName,proCode,currentPageNo,pageSize);
	}
	@Override
	public int getproviderCount(String proName, String proCode) {
		return providerMapper.getproviderCount(proName, proCode);
	}
	@Override
	public boolean add(Provider provider) {
		return providerMapper.add(provider) > 0 ;
	}
	
	@Override
	public Provider getProviderById(String id) {
		return providerMapper.getProviderById(id);
	}
	
	@Override
	public boolean modify(Provider provider) {
		return providerMapper.modify(provider) > 0;
	}
	@Override
	public boolean deleteProvider(String id) {
		return providerMapper.deleteProById(id)>0;
	}
	@Override
	public List<Provider> getAll() {
		return providerMapper.getAll();
	}


}
