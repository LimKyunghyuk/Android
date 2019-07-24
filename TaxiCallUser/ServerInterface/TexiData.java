package kr.priv.test;

public class TexiData {

	public String license_number;
	public String car_number;
	public String phone_number;
	public String name;
	public String type;
	public String company;
	public String model;
	public String years;
	public String card;
	public String grade;
	public String location_x;
	public String location_y;
	public String status;
	public String customer_number;

	TexiData(String[] arr){

		this.license_number = arr[0];
		this.car_number = arr[1];
		this.phone_number = arr[2];
		this.name = arr[3];
		this.type = arr[4];
		this.company = arr[5];
		this.model = arr[6];
		this.years = arr[7];
		this.card = arr[8];
		this.grade = arr[9];
		this.location_x = arr[10];
		this.location_y = arr[11];
		this.status = arr[12];
		this.customer_number = arr[13];
	}

}
