/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.dao;

import cn.edu.ahut.copydetector.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CourseDao {
    Course getCourseById(String cid);
    Course getCourseByName(String cname);
    List<Course> getCoursesByTno(String tno);
    List<Course> getAll();
    int countAll();
    void saveOne(Course course);
    void delById(String cid);
    void updateCourse(Course course);
}
