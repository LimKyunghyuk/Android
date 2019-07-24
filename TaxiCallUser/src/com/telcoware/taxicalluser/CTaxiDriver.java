package com.telcoware.taxicalluser;

import android.os.*;


public class CTaxiDriver implements Parcelable
{
	public static final int TAXI_PRIVATE	= 0;
	public static final int TAXI_DELUXE		= 1;
	public static final int TAXI_BUSINESS	= 2;
	
	public static final int TAXI_CASH	= 0;		/// 카드택시
	public static final int TAXI_CARD	= 1;		/// 현금택시
	
	private String	m_strName;
	private String	m_strLicense;
	private int		m_iDriverType;
	private int		m_iPaymentType;					/// 카드or현금 택시
	private String	m_strCompany;
	private String	m_strPhone;
	private String	m_strImgURL;					/// 택시 기사 사진URL
	private float	m_fGrade;						/// 택시 등급
	
	private int		m_latitude;
	private int		m_longitude;
	private float	m_distance;
	
	private String	m_strAddress;
	
	private CTaxi	m_taxi;
	
	public CTaxiDriver(String _name, String _taxi)
	{
		setName(_name);
		m_taxi = new CTaxi(_taxi);	
	}

	public CTaxiDriver(){}

	public void setName(String m_strName) {
		this.m_strName = m_strName;
	}

	public String getName() {
		return m_strName;
	}

	public void setLicense(String m_strLicense) {
		this.m_strLicense = m_strLicense;
	}

	public String getLicense() {
		return m_strLicense;
	}

	public void setType(int m_iType) {
		this.m_iDriverType = m_iType;
	}

	public int getType() {
		return m_iDriverType;
	}

	public void setCompany(String m_strCompany) {
		this.m_strCompany = m_strCompany;
	}

	public String getCompany() {
		return m_strCompany;
	}

	public void setLatitude(int m_latitude) {
		this.m_latitude = m_latitude;
	}

	public int getLatitude() {
		return m_latitude;
	}

	public void setLongitude(int m_longitude) {
		this.m_longitude = m_longitude;
	}

	public int getLongitude() {
		return m_longitude;
	}
	
	public String getTaxiName()
	{
		return m_taxi.getName();
	}

	public void setAddress(String m_strAddress) {
		this.m_strAddress = m_strAddress;
	}

	public String getAddress() {
		return m_strAddress;
	}

	public void setDistance(float m_distance) {
		this.m_distance = m_distance;
	}

	public float getDistance() {
		return m_distance;
	}

	public void setPhone(String m_strPhone) {
		this.m_strPhone = m_strPhone;
	}

	public String getPhone() {
		return m_strPhone;
	}
	
	public String getTaxiNumber()
	{
		return m_taxi.getNumber();
	}
	
	public void setTaxiNumber(String _number)
	{
		m_taxi.setNumber(_number);
	}
	
	public int getTaxiFuelType()
	{
		return m_taxi.getFuelType();
	}
	
	public void setTaxiFuelType(int _type)
	{
		m_taxi.setFuelType(_type);
	}
	
	public void setPaymentType(int m_iType) {				///
		this.m_iPaymentType = m_iType;
	}

	public int getPaymentType() {							///
		return m_iPaymentType;
	}
	
	public void setGrade(float m_fGrade) {					///
		this.m_fGrade = m_fGrade;
	}
	
	public float getGrade() {								///
		return m_fGrade;
	}

	public String getImgURL() {								/// 
		return m_strImgURL;
	}
	
	public void setImgURL(String m_strImgURL) {				/// 
		this.m_strImgURL = m_strImgURL;
	}

	
	@Override
	public int describeContents()
	{
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(m_strName);
		dest.writeString(m_strLicense);
		dest.writeString(m_strCompany);
		dest.writeString(getPhone());
		dest.writeString(m_strAddress);
		dest.writeString(m_strImgURL);							///
		
		dest.writeInt(m_latitude);
		dest.writeInt(m_longitude);
		dest.writeFloat(m_distance);
		dest.writeInt(m_iDriverType);
		dest.writeFloat(m_fGrade);								///
		dest.writeInt(m_iPaymentType);							///

		dest.writeSerializable(m_taxi);
	}
	
	public static final Parcelable.Creator<CTaxiDriver> CREATOR = new Creator<CTaxiDriver>()
	{
		@Override
		public CTaxiDriver createFromParcel(Parcel source)
		{
			CTaxiDriver driver = new CTaxiDriver();
			
			driver.m_strName = source.readString();
			driver.m_strLicense = source.readString();
			driver.m_strCompany = source.readString();
			driver.setPhone(source.readString());
			driver.m_strAddress = source.readString();
			driver.m_strImgURL = source.readString();			///
			
			driver.m_latitude = source.readInt();
			driver.m_longitude = source.readInt();
			driver.m_distance = source.readFloat();
			driver.m_iDriverType = source.readInt();
			driver.m_fGrade = source.readFloat();					///
			driver.m_iPaymentType = source.readInt();			///
		
			driver.m_taxi = (CTaxi)source.readSerializable();
			return driver;
		}

		@Override
		public CTaxiDriver[] newArray(int size)
		{
			return new CTaxiDriver[size];
		}
		
	};
}