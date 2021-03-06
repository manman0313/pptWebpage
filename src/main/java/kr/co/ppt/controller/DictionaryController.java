package kr.co.ppt.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import kr.co.ppt.serviceImpl.DictionaryServiceImpl;
import kr.co.ppt.util.SHA_ENC;

@Controller
@RequestMapping("/dictionary")
public class DictionaryController {
	@Autowired
	DictionaryServiceImpl dService;
	public static Map<String,JSONArray> userReqCheckDic = new HashMap<>();
	
	
	//=======================Connect to MongoDB================================//
	@RequestMapping("/mongo/selectOpiDic.json")
	@ResponseBody
	public String selectOpiDicMongo(String comName,String opinion, String newsCode){
		JSONObject obj = dService.selectOpiDicMongo(comName, opinion, newsCode);
		return obj.toJSONString();
	}
	
	@RequestMapping("/mongo/selectProDic.json")
	@ResponseBody
	public String selectProDicMongo(String comName, String newsCode){
		return  dService.selectProDicMongo(comName, newsCode).toJSONString();
	}
	
	@RequestMapping("/mongo/selectPro2Dic.json")
	@ResponseBody
	public String selectPro2Dic(String comName, String newsCode){
		return  dService.selectPro2DicMongo(comName, newsCode).toJSONString();
	}
	
	@RequestMapping("/mongo/selectTFIDF.json")
	@ResponseBody
	public String selectTFIDFMongo(String comName,String newsCode, String anaCode){
		return dService.selectTFIDFMongo(comName,newsCode,anaCode).toJSONString();
	}
	
	@RequestMapping("/mongo/selectUserDic.json")
	@ResponseBody
	public String selectUserDic (int userNo, String dicName, String dicInclude){
		boolean b = true;
		if(dicInclude != null){
			if(dicInclude.equals("true"))
				b = true;
			else if(dicInclude.equals("false"))
				b = false;
		}
		JSONArray arr = dService.selectUserDic(userNo, dicName, b);
		return arr.toJSONString();
	}
	
	@RequestMapping(value = "/mongo/insertUserDic.json", method=RequestMethod.POST)
	@ResponseBody
	public String insertUserDic (int userNo, String comName,String newsCode, String anaCode, String dicName,int reliability, String userDic){
		userDic = userDic.substring(1, userDic.length()-1).replaceAll("\"", "");
		String[] userReqArr = userDic.split(",");
		JSONArray dicArr = new JSONArray();
		for(String term : userReqArr){
			JSONObject obj = new JSONObject();
			obj.put("term", term);
			dicArr.add(obj);
		}
		System.out.println(comName);
		Document document = new Document();
		document.append("userNo", userNo);
		document.append("comName", comName);
		document.append("newsCode", newsCode);
		document.append("anaCode", anaCode);
		document.append("dicName", dicName);
		document.append("reliability", reliability);
		document.append("dictionary", dicArr);
		dService.insertUserDic(document);
		return document.toJson();
		//return dService.selectTFIDFMongo(comName,newsCode,anaCode).toJSONString();
	}
	
	@RequestMapping(value = "/mongo/deleteUserDic.json", method=RequestMethod.GET)
	@ResponseBody
	public String deleteUserDic (int userNo, String dicName){
		dService.deleteUserDic(userNo,dicName);
		return "삭제 되었습니다.";
	}
	
	
	/*
	 * anaCode
	 * opi1,2 -> OPI_DIC
	 * pro1 -> PRO_DIC
	 * pro2 -> PRO2_DIC
	 * fit&meg1,2 -> filtered tfidf
	*/
	@RequestMapping("/getDictionary.json")
	@ResponseBody
	public String getDictionary(String id, String name, String anaCode, String newsCode){
		id = SHA_ENC.SHA256_Encrypt(id); // id 암호화하여 고유 키로 사용
		int size=0;
		System.out.println("id : " + id);
		System.out.println("name : " + name);
		System.out.println("anaCode : " + anaCode);
		System.out.println("newsCode : " + newsCode);
		JSONArray proDic = new JSONArray();
		switch(anaCode){
			case "opi1":
			case "opi2":
				JSONArray opiDic = new JSONArray();
				JSONObject posDic = dService.selectOpiDicMongo(name, "pos", newsCode);
				JSONObject negDic = dService.selectOpiDicMongo(name, "neg", newsCode);
				opiDic.add(posDic);
				opiDic.add(negDic);
				userReqCheckDic.put(id, opiDic);
				size = posDic.size() + negDic.size();
				System.out.println("opiDic(" + opiDic.toJSONString().length() + ") 호출");
				break;
			case "pro1":
				proDic = dService.selectProDicMongo(name, newsCode);
				userReqCheckDic.put(id, proDic);
				size = proDic.size();
				System.out.println("pro1Dic(" + proDic.toJSONString().length() + ") 호출");
				break;
			case "pro2":
				proDic = dService.selectPro2DicMongo(name, newsCode);
				userReqCheckDic.put(id, proDic);
				size = proDic.size();
				System.out.println("pro2Dic(" + proDic.toJSONString().length() + ") 호출");
				break;
			case "fit1":
				JSONArray fit1DicArr = new JSONArray();
				proDic = dService.selectProDicMongo(name, newsCode);
				JSONObject fit1 = dService.selectTFIDFMongo(name,newsCode,anaCode);
				for(int i=0; i<proDic.size(); i++){
					JSONObject proDicObj = (JSONObject)proDic.get(i);
					String term = (String)proDicObj.get("word");
					if(fit1.containsKey(term)){
						fit1DicArr.add(proDicObj);
					}
				}
				size = fit1DicArr.size();
				userReqCheckDic.put(id, fit1DicArr);
				System.out.println("fit1Dic(" + fit1DicArr.toJSONString().length() + ") 호출");
				break;
			case "fit2":
				JSONArray fit2DicArr = new JSONArray();
				proDic = dService.selectPro2DicMongo(name, newsCode);
				JSONObject fit2 = dService.selectTFIDFMongo(name,newsCode,anaCode);
				for(int i=0; i<proDic.size(); i++){
					JSONObject pro2DicObj = (JSONObject)proDic.get(i);
					String term = (String)pro2DicObj.get("word");
					if(fit2.containsKey(term)){
						fit2DicArr.add(pro2DicObj);
					}
				}
				size = fit2DicArr.size();
				userReqCheckDic.put(id, fit2DicArr);
				System.out.println("fit2Dic(" + fit2DicArr.toJSONString().length() + ") 호출");
				break;
			case "meg1":
				JSONArray meg1DicArr = new JSONArray();
				JSONObject posDicObj1 = dService.selectOpiDicMongo(name, "pos", newsCode);
				JSONObject negDicObj1 = dService.selectOpiDicMongo(name, "neg", newsCode);
				JSONObject meg1 = dService.selectTFIDFMongo(name,newsCode,anaCode);
				Set<String> meg1TermSet = new HashSet<>();
				Iterator meg1Iter = meg1.keySet().iterator();
				while(meg1Iter.hasNext()){
					String key = (String)meg1Iter.next();
					if(posDicObj1.containsKey(key) || negDicObj1.containsKey(key)){
						meg1TermSet.add(key);	
					}
				}
				proDic = dService.selectProDicMongo(name, newsCode);
				for(int i=0; i<proDic.size(); i++){
					JSONObject proDicObj = (JSONObject)proDic.get(i);
					String term = (String)proDicObj.get("word");
					if(meg1TermSet.contains(term)){
						meg1DicArr.add(proDicObj);
					}
				}
				size = meg1DicArr.size();
				userReqCheckDic.put(id, meg1DicArr);
				System.out.println("meg1Dic(" + meg1DicArr.toJSONString().length() + ") 호출");
				break;
			case "meg2":
				JSONArray meg2DicArr = new JSONArray();
				JSONObject posDicObj2 = dService.selectOpiDicMongo(name, "pos", newsCode);
				JSONObject negDicObj2 = dService.selectOpiDicMongo(name, "neg", newsCode);
				JSONObject meg2 = dService.selectTFIDFMongo(name,newsCode,anaCode);
				Set<String> meg2TermSet = new HashSet<>();
				Iterator meg2Iter = meg2.keySet().iterator();
				while(meg2Iter.hasNext()){
					String key = (String)meg2Iter.next();
					if(posDicObj2.containsKey(key) || negDicObj2.containsKey(key)){
						meg2TermSet.add(key);	
					}
				}
				proDic = dService.selectPro2DicMongo(name, newsCode);
				for(int i=0; i<proDic.size(); i++){
					JSONObject proDicObj = (JSONObject)proDic.get(i);
					String term = (String)proDicObj.get("word");
					if(meg2TermSet.contains(term)){
						meg2DicArr.add(proDicObj);
					}
				}
				size = meg2DicArr.size();
				userReqCheckDic.put(id, meg2DicArr);
				System.out.println("meg2Dic(" + meg2DicArr.toJSONString().length() + ") 호출");
				break;
		}
		JSONObject obj = new JSONObject();
		obj.put("id",id);
		obj.put("prevDic", userReqCheckDic.get(id));
		obj.put("size", size);
		return obj.toJSONString();
	}
	
	@RequestMapping(value="/validation.json", method=RequestMethod.POST)
	@ResponseBody
	public String getValidation(String key, String userReq){
		if(key == null || userReqCheckDic.isEmpty() || !userReqCheckDic.containsKey(key) || userReq == null){
			return "";
		}else{
			//return value
			JSONArray usefulTermArr = new JSONArray();
			JSONArray uselessTermArr = new JSONArray();

			//request value
			Set<String> userTermSet = new HashSet<>();
			userReq = userReq.substring(1, userReq.length()-1).replaceAll("\"", "");
			String[] userReqArr = userReq.split(",");
			for(int i=0; i<userReqArr.length; i++){
				userTermSet.add(userReqArr[i]);
			}
			JSONArray checkArr = userReqCheckDic.get(key);
			if(checkArr.size() == 2){//OPI_DIC
				JSONObject posDic = (JSONObject)checkArr.get(0);
				JSONObject negDic = (JSONObject)checkArr.get(1);
				Iterator<String> userTermIter = userTermSet.iterator();
				while(userTermIter.hasNext()){
					String userTerm = userTermIter.next();
					if(posDic.containsKey(userTerm)){
						JSONObject termObject = new JSONObject();
						termObject.put("key", userTerm);
						termObject.put("value", Double.parseDouble((String)posDic.get(userTerm)));
						termObject.put("opinion", "inc");
						usefulTermArr.add(termObject);
					}else if(negDic.containsKey(userTerm)){
						JSONObject termObject = new JSONObject();
						termObject.put("key", userTerm);
						termObject.put("value", Double.parseDouble((String)negDic.get(userTerm)));
						termObject.put("opinion", "dec");
						usefulTermArr.add(termObject);
					}else{
						JSONObject termObject = new JSONObject();
						termObject.put("key", userTerm);
						termObject.put("value", 1);
						termObject.put("opinion", "none");
						uselessTermArr.add(termObject);
					}
				}
			}else{//Others
				for(int i=0; i<checkArr.size(); i++){
					JSONObject prevDic = (JSONObject)checkArr.get(i);
					String term = (String)prevDic.get("word");
					if(userTermSet.contains(term)){
						String opinion = "";
						double incValue = Double.parseDouble((String)prevDic.get("inc"));
						double decValue = Double.parseDouble((String)prevDic.get("dec"));
						double equValue = Double.parseDouble((String)prevDic.get("equ"));
						double value = Double.max(Double.max(incValue, decValue), equValue);
						if(value == incValue)
							opinion = "inc";
						else if(value == decValue)
							opinion = "dec";
						else
							opinion = "equ";
						JSONObject termObject = new JSONObject();
						termObject.put("key", term);
						termObject.put("value", value);
						termObject.put("opinion", opinion);
						usefulTermArr.add(termObject);
						userTermSet.remove(term);
					}
				}
				Iterator<String> userTermIter = userTermSet.iterator();
				while(userTermIter.hasNext()){
					String userTerm = userTermIter.next();
					JSONObject termObject = new JSONObject();
					termObject.put("key", userTerm);
					termObject.put("value", 1);
					termObject.put("opinion", "none");
					uselessTermArr.add(termObject);
				}
			}
			JSONObject resultObj = new JSONObject(); // [{usefulTerms : [{key:term,value:num}]}, {uselessTerms:[{key:term,value:num}]}
			resultObj.put("usefulTerms",usefulTermArr);
			resultObj.put("uselessTerms",uselessTermArr);
			return resultObj.toJSONString();
		}
	}
}
