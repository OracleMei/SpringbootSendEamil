package com.example.mail.entity;

/**
 * @description:
 * @author: meiyc
 * @createDate: 2020/4/3 22:29
 * @version: 1.0
 */
public class User {
    private String username;
    private int num;
    private Double salary;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}
