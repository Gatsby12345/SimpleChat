package com.fx.study.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fx.study.bean.Friends;
import com.fx.study.bean.User;
import com.fx.study.bean.UserStatus;
import com.fx.study.dao.FriendsReqRepositary;
import com.fx.study.dao.UserRepository;
import com.fx.study.server.MsgHandler;
import com.fx.study.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


@Controller
public class UserController {

	@Autowired
	private UserRepository ur;
	
	@Autowired
	private FriendsReqRepositary frr;
	
	@Autowired
	private Gson gson;
	
	@ResponseBody
	@RequestMapping(value = "/regist",method = RequestMethod.POST)
	public String regist(@RequestBody User user)
	{
		String result;
		Integer userByAccount = ur.getUserIdByAccount(user.getAccount());
		if(userByAccount != null)
		{
			result = "该账号已存在！";
		}
		else
		{
			User newuser =ur.save(user);
			if(newuser != null)
			{
				result = newuser.getId().toString();
			}
			else
			{
				result = "注册失败！";
			}
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/login/{account}/{pwd}")
	public String login(@PathVariable String account,@PathVariable String pwd)
	{
		User login = ur.login(account, pwd);
		if(login == null)
		{
			return "登录失败！";
		}
		else
		{
			return gson.toJson(login);
		}
	}
	
	@ResponseBody
	@RequestMapping("/getUsersByUserid/{idArray}")
	public String getUsersByUserid(@PathVariable String idArray)
	{
		String[] idStr = idArray.split(",");
		List<Integer> intList = new ArrayList<Integer>();
		for(String str : idStr)
		{
			intList.add(Integer.valueOf(str));
		}
		List<User> userListByIds = ur.findAllById(intList);
		String json = gson.toJson(userListByIds);
		return json;
	}
	
	@ResponseBody
	@RequestMapping(value = "/searchusers",method = RequestMethod.POST)
	public String getUsersBySearchStr(@RequestBody Map<String,String> map)
	{
		List<Character> sexList = new ArrayList<Character>();
		String sex = map.get("sex");
		if("性别".equals(sex) || "不限".equals(sex))
		{
			sexList.add('男');
			sexList.add('女');
		}
		else if("男".equals(sex))
		{
			sexList.add('男');
		}
		else
		{
			sexList.add('女');
		}
		Date[] date = Util.GetDateTime(map.get("age"));
		String notids = map.get("notids");
		Type type = new TypeToken<List<Integer>>(){}.getType();
		List<Integer> notIdList = gson.fromJson(notids, type);
		notIdList.add(Integer.valueOf(map.get("id")));
		List<User> userByIdParams = ur.getUserByIdParams(notIdList, sexList, date[0], date[1], map.get("searchStr"));
		List<UserStatus> usersStatus = new ArrayList<UserStatus>();
		for(User user : userByIdParams)
		{
			UserStatus us = new UserStatus(user);
			if(Boolean.valueOf(map.get("online")))
			{
				if(MsgHandler.id_session.containsKey(user.getId()))
					us.setOnline(true);
				else
					continue;
			}
			else
			{
				if(MsgHandler.id_session.containsKey(user.getId()))
					us.setOnline(true);
				else
					us.setOnline(false);
			}
			usersStatus.add(us);
		}
		return gson.toJson(usersStatus);
	}
	
	@Transactional(rollbackFor = Exception.class) //3个update操作 ，需要加上事务  当有异常捕获机制时事务会失效  所以要加上异常判断
	@ResponseBody
	@RequestMapping(value = "/addfriends", method = RequestMethod.POST)
	public String changeUserFriends(@RequestBody Map<String,String> map)
	{
		try {
			Integer id = Integer.valueOf(map.get("id"));
			Integer destid = Integer.valueOf(map.get("destid"));
			List<Integer> list = new ArrayList<Integer>();
			list.add(id);
			list.add(destid);
			String f1;
			String f2;
			List<User> userListByIds = ur.findAllById(list);
			if(id < destid)
			{
				f1 = userListByIds.get(0).getFriends();
				f2 = userListByIds.get(1).getFriends();
			}
			else
			{
				f1 = userListByIds.get(1).getFriends();
				f2 = userListByIds.get(0).getFriends();
			}
			Type type = new TypeToken<List<Friends>>(){}.getType();
			List<Friends> friends1 = gson.fromJson(f1, type);
			for(Friends u : friends1)
			{
				System.out.println("u1:"+u.getName());
			}
			List<Friends> friends2 = gson.fromJson(f2, type);
			for(Friends u : friends2)
			{
				System.out.println("u2:"+u.getName());
			}
			friends1.forEach(fr -> {
			if(fr.getName().equals(map.get("thisgroup"))) 
			{
				if("".equals(fr.getMembers())) 
					fr.setMembers(destid.toString());
				else
					fr.setMembers(fr.getMembers() + "," + destid);
			}
			});
			friends2.forEach(fr -> {
			if(fr.getName().equals(map.get("thatgroup"))) 
				if("".equals(fr.getMembers())) 
					fr.setMembers(id.toString());
				else
					fr.setMembers(fr.getMembers() + "," + id);
			});
			ur.updateFriends(gson.toJson(friends1), id);
			ur.updateFriends(gson.toJson(friends2), destid);
			frr.update(1, destid, id);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//加注解@Transactional和try catch捕获异常会让注解失效 所以要加上这句
			e.printStackTrace();
			return "添加失败！";
		}
		
		return "添加成功！";
	}
	
	@ResponseBody
	@RequestMapping(value = "/addgroup",method = RequestMethod.POST)
	public String addGroup(@RequestBody Map<String,String> map)
	{
		try {
			List<Integer> list = new ArrayList<Integer>();
			Integer id = Integer.valueOf(map.get("id"));
			list.add(id);
			List<User> userListByIds = ur.findAllById(list);
			String f1 = userListByIds.get(0).getFriends();
			Type type = new TypeToken<List<Friends>>(){}.getType();
			List<Friends> friends1 = gson.fromJson(f1, type);
			friends1.add(new Friends(map.get("groupname"),""));
			ur.updateFriends(gson.toJson(friends1), id);
		} catch (Exception e) {
			e.printStackTrace();
			return "添加失败！";
		}
		return "添加成功！";
	}
	
	@ResponseBody
	@RequestMapping(value = "/deletegroup",method = RequestMethod.POST)
	public String deleteGroup(@RequestBody Map<String,String> map)
	{
		try {
			List<Integer> list = new ArrayList<Integer>();
			Integer id = Integer.valueOf(map.get("id"));
			list.add(id);
			List<User> userListByIds = ur.findAllById(list);
			String f1 = userListByIds.get(0).getFriends();
			Type type = new TypeToken<List<Friends>>(){}.getType();
			List<Friends> friends1 = gson.fromJson(f1, type);
			for(Friends fr : friends1)
			{
				if(fr.getName().equals(map.get("groupname")))
				{
					friends1.remove(fr);
					break;
				}
			}
			ur.updateFriends(gson.toJson(friends1), id);
		} catch (Exception e) {
			e.printStackTrace();
			return "删除失败！";
		}
		return "删除成功！";
	}
	
	@ResponseBody
	@RequestMapping(value = "/updategroup",method = RequestMethod.POST)
	public String updateGroup(@RequestBody Map<String,String> map)
	{
		try {
			List<Integer> list = new ArrayList<Integer>();
			Integer id = Integer.valueOf(map.get("id"));
			list.add(id);
			List<User> userListByIds = ur.findAllById(list);
			String f1 = userListByIds.get(0).getFriends();
			Type type = new TypeToken<List<Friends>>(){}.getType();
			List<Friends> friends1 = gson.fromJson(f1, type);
			friends1.forEach(fr -> {if(fr.getName().equals(map.get("oldname"))) fr.setName(map.get("newname"));});
			ur.updateFriends(gson.toJson(friends1), id);
		} catch (Exception e) {
			e.printStackTrace();
			return "修改失败！";
		}
		return "修改成功！";
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateimage",method = RequestMethod.POST)
	public String updateImage(@RequestBody Map<String,String> map)
	{
		try {
			Integer id = Integer.valueOf(map.get("id"));
			ur.updateImage(map.get("image"), id);
		} catch (Exception e) {
			e.printStackTrace();
			return "修改失败！";
		}
		return "修改成功！";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getfriendsbyid/{id}")
	public String getFriendsById(@PathVariable Integer id)
	{
		try {
			return ur.findById(id).get().getFriends();
		} catch (Exception e) {
			e.printStackTrace();
			return "获取失败！";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getuserbyaccount/{account}")
	public String getUserById(@PathVariable String account)
	{
		try {
			return gson.toJson(ur.getUserById(account));
		} catch (Exception e) {
			e.printStackTrace();
			return "获取失败！";
		}
	}
	
	@RequestMapping("/index")
	public String index()
	{
		return "index";
	}
}
	
