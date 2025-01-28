package com.clipmk.region.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "region")
public class Region {
	@Id
	@Column(length = 10)
	private String code;
	@Column(length = 10)
	private String city;
	@Nullable
	@Column(length = 10)
	private String district;
	@Nullable
	@Column(length = 10)
	private String dong;
}
