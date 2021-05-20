/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class Course implements Serializable {
    private Integer id;
    private String course_id;
    private String course_name;
    private String course_grade;
    private String course_teacher;

    public Course(Integer id, String course_id, String course_name, String course_grade, String course_teacher) {
        this.id = id;
        this.course_id = course_id;
        this.course_name = course_name;
        this.course_grade = course_grade;
        this.course_teacher = course_teacher;
    }
}
