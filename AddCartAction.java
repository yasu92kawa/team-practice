package com.internousdev.orion.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import  com.internousdev.orion.dao.CartInfoDAO;
import com.internousdev.orion.dao.ProductInfoDAO;
import  com.internousdev.orion.dto.CartInfoDTO;
import com.internousdev.orion.dto.ProductInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class AddCartAction  extends ActionSupport implements SessionAware{

	private Map<String, Object> session;
	private int productId;
	private int productCount;
	private int productPrice;
	private int totalPrice;
	private ArrayList<CartInfoDTO> cartInfoDTOList;

	public String execute() throws SQLException{

		String ret = ERROR;

		//３０分以上経ってセッションが空の時はタイムアウトを返す
		if(session.isEmpty()) {
			return "sessionTimeout";
		}

		//ログイン・未ログイン時の判別
		String userId = null;

		if(session.containsKey("loginUserId")) {
			userId = String.valueOf(session.get("loginUserId"));
		} else if(session.containsKey("tempId")){
			userId = String.valueOf(session.get("tempId"));
		}

		CartInfoDAO cartInfoDAO = new CartInfoDAO();
		int count =0;
		ProductInfoDAO productInfoDAO = new ProductInfoDAO();
		ProductInfoDTO productInfoDTO = productInfoDAO.getProductInfoByProductId(productId);

		if(productCount<1){
			return ret;
		}

		//新規商品が既にカートにあるかチェック
		if(cartInfoDAO.existCartInfo(userId, productId)){
			//存在する場合は、商品の個数を更新する。
			count = cartInfoDAO.updateCartCount(userId, productId, productCount);
		}else{
			//存在しない場合は商品を登録する(productPriceはJSPから取得）（参考:buyConfirmAction）
			count = cartInfoDAO.addCartInfo(userId, productId, productCount, productInfoDTO.getPrice());
		}

		//ユーザーIDに紐づいたカート情報から取得（参考:mypage.action）
		if(count>0){
			cartInfoDTOList = cartInfoDAO.getUserCartInfo(userId);
			totalPrice = cartInfoDAO.getTotalPrice(userId);
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

	public int getProductCount() {
		return productCount;
	}

	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}
}
