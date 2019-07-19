package com.fx.study.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fx.study.bean.FriendsReq;
import com.fx.study.dao.FriendsReqRepositary;
import com.google.gson.Gson;

@RequestMapping("/friendsreq")
@Controller
public class FriednsReqController {

	@Autowired
	private FriendsReqRepositary frp;
	
	@Autowired
	private Gson gson;
	
	@ResponseBody
	@RequestMapping("/getall/{id}")
	public String GetAll(@PathVariable Integer id)
	{
		List<FriendsReq> getAll = frp.GetAll(id);
		return gson.toJson(getAll);
	}
}
