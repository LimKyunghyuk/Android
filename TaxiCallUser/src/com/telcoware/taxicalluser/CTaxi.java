package com.telcoware.taxicalluser;

import java.io.*;

public class CTaxi implements Serializable
{
	private static final long serialVersionUID = -4262430952836148042L;
	
	public static final int LPG		= 0;
	public static final int DISEL	= 1;
	
	private String	m_strName;
	private String	m_strNumber;
	private int		m_iModel;
	private int		m_iFuelType;
	
	public CTaxi(String _name)
	{
		setName(_name);
	}

	public void setName(String m_strName) {
		this.m_strName = m_strName;
	}

	public String getName() {
		return m_strName;
	}

	public void setNumber(String m_strNumber) {
		this.m_strNumber = m_strNumber;
	}

	public String getNumber() {
		return m_strNumber;
	}

	public void setModel(int m_iModel) {
		this.m_iModel = m_iModel;
	}

	public int getModel() {
		return m_iModel;
	}

	public void setFuelType(int m_iFuelType) {
		this.m_iFuelType = m_iFuelType;
	}

	public int getFuelType() {
		return m_iFuelType;
	}
}
