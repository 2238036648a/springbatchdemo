package com.example.springbatch.readdb.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="sys_user_copy1")
public class UserCopyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_name")
    private String user_name;
    @Column(name = "sex")
    private String sex;
    @Column(name = "age")
    private Integer age;
    @Column(name = "address")
    private String address;
    @Column(name = "status")
    private Integer status;
    @Column(name = "create_time")
    private Date create_time;
    /*@Column(name = "update_time")
    private Date update_time;*/
    /*private Integer id;
    private String user_name;
    private String sex;
    private Integer age;
    private String address;
    private Integer status;
    private Date create_time;
    private Date update_time;*/

    public UserCopyEntity() {
    }



    public UserCopyEntity(Integer id, String user_name, String sex, Integer age, String address, Integer status, Date create_time) {
        this.id = id;
        this.user_name = user_name;
        this.sex = sex;
        this.age = age;
        this.address = address;
        this.status = status;
        this.create_time = create_time;

    }

}
