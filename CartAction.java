package com.internousdev.orion.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import  com.internousdev.orion.dao.CartInfoDAO;
import  com.internousdev.orion.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class CartAction  extends ActionSupport implements SessionAware{

	private Map<String, Object> session;
	private String userId;
	private int totalPrice;
	private ArrayList<CartInfoDTO> cartInfoDTOList;

	public String execute() throws SQLException{

		//３０分以上経ってセッションが空の時はタイムアウトを返す
		if(session.isEmpty()) {
			return "sessionTimeout";
		}

		//ログイン・未ログイン時の判別
		if(session.containsKey("loginUserId")) {
			userId = String.valueOf(session.get("loginUserId"));
		} else if(session.containsKey("tempId")){
			userId = String.valueOf(session.get("tempId"));
		}

		CartInfoDAO cartInfoDAO = new CartInfoDAO();

		//ユーザーIDに紐づいたカート情報から取得（参考:mypage.action）
			cartInfoDTOList = cartInfoDAO.getUserCartInfo(userId);
			totalPrice = cartInfoDAO.getTotalPrice(userId);
			String ret = SUCCESS;
		return ret;
	}

	public void setSession(Map<String, Object> session) {
		this.session=session;
	}

	public List<CartInfoDTO> getCartInfoDTOList() {
		return cartInfoDTOList;
	}

	public void setCartInfoDTOList(List<CartInfoDTO> cartInfoDTOList) {
		this.cartInfoDTOList = (ArrayList<CartInfoDTO>) cartInfoDTOList;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
}