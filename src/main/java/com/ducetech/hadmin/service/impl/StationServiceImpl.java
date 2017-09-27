package com.ducetech.hadmin.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ducetech.hadmin.dao.IStationDao;
import com.ducetech.hadmin.dao.support.IBaseDao;
import com.ducetech.hadmin.entity.Station;
import com.ducetech.hadmin.service.IStationService;
import com.ducetech.hadmin.service.support.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author lisx
 * @since 2017-8-1
 */
@Service
public class StationServiceImpl extends BaseServiceImpl<Station, Integer>
		implements IStationService {

	@Autowired
	private IStationDao stationDao;

	@Override
	public IBaseDao<Station, Integer> getBaseDao() {
		return this.stationDao;
	}


    @Override
    public JSONArray tree(int ifUse) {
        Sort sort = new Sort(Direction.ASC,  "id");
        List<Station> all = stationDao.findAll(sort);
        JSONArray array = new JSONArray();
        JSONObject obj;
        Station node;
        obj = createRoot("站点数据");
        array.add(obj);
        for(int i=0;i<all.size();i++){
            node = all.get(i);
            obj = new JSONObject();
            obj.put("id", node.getNodeCode());
            obj.put("pId",node.getNodeCode().substring(0, node.getNodeCode().length()-3));
            obj.put("name", node.getNodeName());
            array.add(obj);
        }
        return array;
    }

//    @Override
//    public Station findByNodeCode(String nodeCode) {
//        return stationDao.findByNodeCode(nodeCode);
//    }
    @Override
    public Station findByNodeName(String nodeName){
	    return stationDao.findByNodeName(nodeName);
    }
//    @Override
//    public List <Station> findByStationArea(int nodeLength){
//        return stationDao.findByStationArea(nodeLength);
//    }
    public static JSONObject createRoot(String name){
        JSONObject obj = new JSONObject();
        obj.put("id", "000");
        obj.put("pId", "-1");
        obj.put("name", name);
        return obj;
    }
//    public List<Station> querySubNodesByCode(String parentCode){
//        List<Station> all = stationDao.findAll();
//        return stationDao.querySubNodesByCode(parentCode,3);
//    }
//    public List<Station> querySubNodesByCode(String parentCode, int nodeLength){
//        return stationDao.querySubNodesByCode(parentCode,nodeLength);
//    }
    @Override
    public Station saveOrUpdate(Station station) {
        return stationDao.saveAndFlush(station);
    }
}
