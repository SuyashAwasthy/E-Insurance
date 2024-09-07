package com.techlabs.app.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "key_value_store")
public class KeyValueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Change "key" to "settingKey" to avoid reserved keyword conflict
    @Column(name = "setting_key")
    private String settingKey;

    private String value;

   
}
