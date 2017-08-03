package com.ducetech.hadmin.service.impl;

import com.ducetech.hadmin.dao.IArticleSortDao;
import com.ducetech.hadmin.dao.ILearnDao;
import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.ArticleSort;
import com.ducetech.hadmin.entity.Learn;
import com.ducetech.hadmin.service.IArticleSortService;
import com.ducetech.hadmin.service.ILearnService;
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
public class LearnServiceImpl extends BaseServiceImpl<Learn, Integer> implements ILearnService {
	@Autowired
	private ILearnDao learnDao;
	@Override
	public IBaseDao<Learn, Integer> getBaseDao() {
		return this.learnDao;
	}

	@Override
	public void saveOrUpdate(Learn learn) {
		if(learn.getId() != null){
			Learn dbUser = find(learn.getId());
            learn.setUpdateTime(new Date());
            dbUser.setFileName(learn.getFileName());
            dbUser.setFileSize(learn.getFileSize());
			update(dbUser);
		}else{
			learn.setCreateTime(new Date());
			save(learn);
		}
	}

	@Override
	public void delete(Integer id) {
		super.delete(id);
	}



}
