package com.ducetech.hadmin.service.impl;

import com.ducetech.hadmin.dao.ITrainDao;
import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.Train;
import com.ducetech.hadmin.service.ITrainService;
import com.ducetech.hadmin.service.support.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 文章服务实现类
 * </p>
 *
 * @author lisx
 * @since 2016-12-28
 */
@Service
public class TrainServiceImpl extends BaseServiceImpl<Train, Integer> implements ITrainService {
	@Autowired
	private ITrainDao trainDao;
	@Override
	public IBaseDao<Train, Integer> getBaseDao() {
		return this.trainDao;
	}

	public void saveOrUpdate(Train train) {
		if(train.getId() != null){
			Train dbUser = find(train.getId());
            train.setUpdateTime(new Date());
            dbUser.setFileName(train.getFileName());
            dbUser.setFileSize(train.getFileSize());
			update(dbUser);
		}else{
			train.setCreateTime(new Date());
			save(train);
		}
	}

	@Override
	public void delete(Integer id) {
		super.delete(id);
	}



}
