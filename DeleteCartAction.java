package com.internousdev.orion.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import  com.internousdev.orion.dao.CartInfoDAO;
import  com.internousdev.orion.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class DeleteCartAction  extends ActionSupport implements SessionAware{

		private Map<String, Object> session;
		private int totalPrice;
		private Collection<String> checkList;
		private ArrayList<CartInfoDTO> cartInfoDTOList;

		public String execute() throws SQLException{
			String ret = ERROR;
			String userId = null;

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
			int count = 0;

			//cart.jspでチェックを入れた商品の削除メソッドの実行・削除した数をcountへ入れる

			for(String productId : checkList){
				count = count + cartInfoDAO.deleteCartInfo(userId, productId);
			}

			//正しく削除されている場合、カート情報の取得、合計金額を取得して再度カート画面へ

			if(count == checkList.size()) {
				cartInfoDTOList = cartInfoDAO.getUserCartInfo(userId);
				setTotalPrice(cartInfoDAO.getTotalPrice(userId));
				ret = SUCCESS;
			}

			return ret;
		}

		public void setSession(Map<String, Object> session) {
			this.session=session;
		}

		public ArrayList<CartInfoDTO> getCartInfoList() {
			return getCartInfoList();
		}

		public List<CartInfoDTO> getCartInfoDTOList() {
			return cartInfoDTOList;
		}

		public void setCartInfoDTOList(List<CartInfoDTO> cartInfoDTOList) {
			this.cartInfoDTOList = (ArrayList<CartInfoDTO>) cartInfoDTOList;
		}

		public int getTotalPrice() {
			return totalPrice;
		}

		public void setTotalPrice(int totalPrice) {
			this.totalPrice = totalPrice;
		}

		public Collection<String> getCheckList() {
			return checkList;
		}

		public void setCheckList(Collection<String> checkList) {
			this.checkList = checkList;
		}

		public Map<String, Object> getSession() {
			return session;
		}

		public void setCartInfoDTOList(ArrayList<CartInfoDTO> cartInfoDTOList) {
			this.cartInfoDTOList = cartInfoDTOList;
		}
	}