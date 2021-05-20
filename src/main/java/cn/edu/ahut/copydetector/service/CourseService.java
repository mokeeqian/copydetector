/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.service;

import cn.edu.ahut.copydetector.entity.Course;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CourseService {
    Course getCourseById(String cid);
    Course getCourseByName(String cname);
    List<Course> getCoursesByTno(String tno);
    List<Course> getAll();

    void saveCourse(Course course);
    void delCourseById(String cid);
    void updateCourse(Course course);
}
