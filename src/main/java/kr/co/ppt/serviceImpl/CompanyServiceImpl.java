package kr.co.ppt.serviceImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.conversions.Bson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.client.model.Filters;

import kr.co.ppt.daoImpl.CompanyDAOImpl;
import kr.co.ppt.vo.CompanyVO;
import kr.co.ppt.vo.RTAVO;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@Service
public class CompanyServiceImpl {
	@Autowired
	CompanyDAOImpl cDAO;
	
	//=======================Connect to MongoDB================================//
	public JSONArray selectStockByName(String comName){
		Bson query = Filters.eq("comName",comName);
		String data = cDAO.selectStock(query).toJson();
		JSONParser parser = new JSONParser();
		JSONArray arr = null;
		try {
			arr = (JSONArray)((JSONObject) parser.parse(data)).get("quote");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arr;
	}
	
	public JSONArray selectStockByCode(String comCode){
		Bson query = Filters.eq("comCode",comCode);
		String data = cDAO.selectStock(query).toJson();
		JSONParser parser = new JSONParser();
		JSONArray arr = null;
		try {
			arr = (JSONArray)((JSONObject) parser.parse(data)).get("quote");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arr;
	}
	
	//=======================Connect to Oracle================================//
	public void insertCompany(String comName, String comCode, String type){
		Map<Object, Object> dataMap = new HashMap<Object, Object>();
		dataMap.put("comName", comName);
		dataMap.put("comCode", comCode);
		dataMap.put("stockType", type);
		cDAO.insertCompany(dataMap);
	}
	
	public CompanyVO selectCom(CompanyVO companyVO){
		return cDAO.selectCom(companyVO);
	}
	
	public List<CompanyVO> selectComList(){
		return cDAO.selectComList();
	}
	
	public JSONArray selectRTA(String name, String newsCode){
		JSONArray arr = new JSONArray();
		for(RTAVO rta : cDAO.selectRTA(name)){
			Map<Object,Object> map = new HashMap<>();
			if(newsCode != null && !newsCode.equals(rta.getNewsCode())){
				continue;
			}else{
				map.put("no", rta.getNo());
				map.put("comName", rta.getComName());
				map.put("anaCode", rta.getAnaCode());
				map.put("newsCode", rta.getNewsCode());
				map.put("todayFluc", rta.getTodayFluc());
				map.put("tomorrowFluc", rta.getTomorrowFluc());
				map.put("regDate", rta.getRegDate());
			}
			JSONObject obj = new JSONObject(map);
			arr.add(obj);
		}
		return arr;
	}
}