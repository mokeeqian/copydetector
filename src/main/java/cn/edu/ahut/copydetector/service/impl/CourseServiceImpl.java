/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.service.impl;

import cn.edu.ahut.copydetector.dao.CourseDao;
import cn.edu.ahut.copydetector.entity.Course;
import cn.edu.ahut.copydetector.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private CourseDao courseDao;

    public CourseServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Override
    public Course getCourseById(String cid) {
        return courseDao.getCourseById(cid);
    }

    @Override
    public Course getCourseByName(String cname) {
        return courseDao.getCourseByName(cname);
    }

    @Override
    public List<Course> getCoursesByTno(String tno) {
        return courseDao.getCoursesByTno(tno);
    }

    @Override
    public List<Course> getAll() {
        return courseDao.getAll();
    }

    @Override
    public void saveCourse(Course course) {
        courseDao.saveOne(course);
    }

    @Override
    public void delCourseById(String cid) {
        courseDao.delById(cid);
    }

    @Override
    public void updateCourse(Course course) {
        courseDao.updateCourse(course);
    }
}
