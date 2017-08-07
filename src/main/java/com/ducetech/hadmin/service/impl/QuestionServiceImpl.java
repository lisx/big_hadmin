package com.ducetech.hadmin.service.impl;

import com.ducetech.hadmin.dao.IQuestionDao;
import com.ducetech.hadmin.dao.ITrainDao;
import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.Question;
import com.ducetech.hadmin.entity.Train;
import com.ducetech.hadmin.service.IQuestionService;
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
public class QuestionServiceImpl extends BaseServiceImpl<Question, Integer> implements IQuestionService {
	@Autowired
	private IQuestionDao trainDao;
	@Override
	public IBaseDao<Question, Integer> getBaseDao() {
		return this.trainDao;
	}

	public void saveOrUpdate(Question train) {
		if(train.getId() != null){
            Question dbUser = find(train.getId());
            train.setUpdateTime(new Date()+"");
			update(dbUser);
		}else{
			train.setCreateTime(new Date()+"");
			save(train);
		}
	}

	@Override
	public void delete(Integer id) {
		super.delete(id);
	}



}
