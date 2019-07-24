package com.telcoware.taxicalluser;

public class EstData {
	public String index;
	public String phone_number;
	public String license_number;
	public String svc_time;
	public String svc_estimation;
	public String svc_comment;
	

	EstData(String[] arr){

		this.index = arr[0];
		this.phone_number = arr[1];
		this.license_number = arr[2];
		this.svc_time = arr[3];
		this.svc_estimation = arr[4];
		this.svc_comment = arr[5];
	}
}
