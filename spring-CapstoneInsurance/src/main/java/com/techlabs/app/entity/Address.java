//package com.techlabs.app.entity;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import lombok.Data;
//
//@Entity
//@Table(name = "addresses")
//@Data
//public class Address {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long addressId;
//
//    private String address;
//
//    private long pincode;
//
//    @ManyToOne
//    @JoinColumn(name = "state_id", nullable = false)    
//    private State state;
//
//	public Address(long addressId, String address, long pincode, State state) {
//		super();
//		this.addressId = addressId;
//		this.address = address;
//		this.pincode = pincode;
//		this.state = state;
//	}
//	
//	
//
//	public Address() {
//	
//	}
//
//
//
//	public long getAddressId() {
//		return addressId;
//	}
//
//	public void setAddressId(long addressId) {
//		this.addressId = addressId;
//	}
//
//	public String getAddress() {
//		return address;
//	}
//
//	public void setAddress(String address) {
//		this.address = address;
//	}
//
//	public long getPincode() {
//		return pincode;
//	}
//
//	public void setPincode(long pincode) {
//		this.pincode = pincode;
//	}
//
//	public State getState() {
//		return state;
//	}
//
//	public void setState(State state) {
//		this.state = state;
//	}
//
//	@Override
//	public String toString() {
//		return "Address [addressId=" + addressId + ", address=" + address + ", pincode=" + pincode + ", state=" + state
//				+ "]";
//	}
//
//  
//}

