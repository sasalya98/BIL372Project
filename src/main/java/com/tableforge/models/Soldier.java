package com.tableforge.models;

public class Soldier {
	private int soldier_id;
	private boolean is_alive;
	private String name;
	private String surname;
	private String birth_date;
	private String rank;
	private boolean veteran_status;
	private Integer division_id;

	public Soldier(int soldier_id, boolean is_alive, String name, String surname, String birth_date, String rank,
			boolean veteran_status, Integer division_id) {
		this.soldier_id = soldier_id;
		this.is_alive = is_alive;
		this.name = name;
		this.surname = surname;
		this.birth_date = birth_date;
		this.rank = rank;
		this.veteran_status = veteran_status;
		this.division_id = division_id;
	}

	public int getSoldier_id() {
		return soldier_id;
	}

	public void setSoldier_id(int soldier_id) {
		this.soldier_id = soldier_id;
	}

	public boolean isIs_alive() {
		return is_alive;
	}

	public void setIs_alive(boolean is_alive) {
		this.is_alive = is_alive;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getBirth_date() {
		return birth_date;
	}

	public void setBirth_date(String birth_date) {
		this.birth_date = birth_date;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public boolean isVeteran_status() {
		return veteran_status;
	}

	public void setVeteran_status(boolean veteran_status) {
		this.veteran_status = veteran_status;
	}

	public Integer getDivision_id() {
		return division_id;
	}

	public void setDivision_id(Integer division_id) {
		this.division_id = division_id;
	}

	@Override
	public String toString() {
		return "Soldier [soldier_id=" + soldier_id + ", is_alive=" + is_alive + ", name=" + name + ", surname="
				+ surname + ", birth_date=" + birth_date + ", rank=" + rank + ", veteran_status=" + veteran_status
				+ ", division_id=" + division_id + "]";
	}
	
	
	
}
