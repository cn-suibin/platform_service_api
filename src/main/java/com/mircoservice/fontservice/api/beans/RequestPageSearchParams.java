package com.mircoservice.fontservice.api.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RequestPageSearchParams implements Serializable {

  /**
   * RequestPageSearchVo.
   */

  /**
	 * 
	 */
	private static final long serialVersionUID = 6082878596090083480L;
/**
   * 用户ID.
   **/
  private String userId;
  /**
   * 客户端系统.
   **/
  private String os;
  /**
   * 客户端版本.
   **/
  private String v;
  /**
   * 页面条数大小.
   **/
  private int pageSize;
  /**
   * 页码值.
   **/
  private int pageNumber;
  /**
   * 用户类型.
   **/
  private String type;
  /**
   * 用户认证相关.
   **/
  private String appKey;
  /**
   * 用户ID
   */
  private String consumerId;
  /**
   * 用户姓名
   */
  private String consumerName;
  private Map<String, Object> searchMap = new HashMap<String, Object>();

  public String getConsumerId() {
    return consumerId;
  }

  public void setConsumerId(String consumerId) {
    this.consumerId = consumerId;
  }

  public String getConsumerName() {
    return consumerName;
  }

  public void setConsumerName(String consumerName) {
    this.consumerName = consumerName;
  }

  public String getAppKey() {
    return appKey;
  }

  public void setAppKey(String appKey) {
    this.appKey = appKey;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getOs() {
    return os;
  }

  public void setOs(String os) {
    this.os = os;
  }

  public String getV() {
    return v;
  }

  public void setV(String v) {
    this.v = v;
  }


  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }

  public Map<String, Object> getSearchMap() {
    return searchMap;
  }

  public void setSearchMap(Map<String, Object> searchMap) {
    this.searchMap = searchMap;
  }

  @Override
  public String toString() {
	  
    return "RequestPageSearchParams{"
            + "userId='" + userId + '\''
            + ", os='" + os + '\''
            + ", v='" + v + '\''
            + ", pageSize=" + pageSize
            + ", pageNumber=" + pageNumber
            + ", type='" + type + '\''
            + ", appKey='" + appKey + '\''
            + ", searchMap=" + searchMap
            + '}';
  }
}
