package com.road.bean;

/**
 * ���ݴ��ݵ�����
 * @author zhou
 *
 * @param <T>
 */
@SuppressWarnings({"serial", "rawtypes"})
public class Info<T> extends SuperBean{
	
	private T data;
	private Class origin;  // ��Դ
	
	public Info() {
		super();
		// TODO Auto-generated constructor stub
	}

	public T getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
	
	public Class getOrigin() {
		return origin;
	}
	
	public void setOrigin(Class origin) {
		this.origin = origin;
	}
	
	
	
}
