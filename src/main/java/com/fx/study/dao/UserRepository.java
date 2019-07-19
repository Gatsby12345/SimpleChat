package com.fx.study.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fx.study.bean.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	//登录
	@Query(nativeQuery = true, value = "SELECT * FROM user WHERE account = :account AND password = :pwd")
	User login(String account,String pwd);
	
	@Query(nativeQuery = true, value = "SELECT id FROM user WHERE account = :account")
	Integer getUserIdByAccount(String account);
	
	@Query(nativeQuery = true, value = "SELECT * FROM user WHERE account = :account")
    User getUserById(String account);
	
	@Query(nativeQuery = true, value = "SELECT * FROM user WHERE id NOT IN (:notIdList) AND sex IN (:sexList) AND birthday >= :begin AND birthday <= :end AND "
			+ "(if(:str !='',account LIKE CONCAT('%',:str,'%') OR name LIKE CONCAT('%',:str,'%'),1=1))")
    List<User> getUserByIdParams(List<Integer> notIdList, List<Character> sexList,Date begin,Date end,String str);
	
	@Modifying
    @Transactional
	@Query(nativeQuery = true, value = "UPDATE user SET friends = :friends WHERE id = :id")
	int updateFriends(String friends,int id);
	
	@Modifying
    @Transactional
	@Query(nativeQuery = true, value = "UPDATE user SET headimage = :headimage WHERE id = :id")
	int updateImage(String headimage,int id);
	
}
