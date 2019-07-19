package com.fx.study.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fx.study.bean.FriendsReq;

@Repository
public interface FriendsReqRepositary extends JpaRepository<FriendsReq, Integer> {

	@Query(nativeQuery = true, value = "SELECT * FROM friendsreq WHERE userid = :id OR destid = :id")
	List<FriendsReq> GetAll(Integer id);
	
	@Query(nativeQuery = true, value = "SELECT * FROM friendsreq WHERE userid = :userid AND destid = :destid AND status = 0")
	List<FriendsReq> GetFRById(Integer userid,Integer destid);
	
	@Modifying
    @Transactional
	@Query(nativeQuery = true, value = "UPDATE friendsreq SET status = :status WHERE userid = :userid AND destid = :destid "
			+ "AND id IN (SELECT id FROM (SELECT MAX(id) AS id FROM friendsreq WHERE userid = :userid AND destid = :destid) f)")
	int update(Integer status,Integer userid,Integer destid);
}
