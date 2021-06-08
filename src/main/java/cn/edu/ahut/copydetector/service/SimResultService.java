/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.service;

import cn.edu.ahut.copydetector.util.PageBean;
import cn.edu.ahut.copydetector.entity.SimResult;


import java.util.List;

public interface SimResultService {
    List<SimResult> getAllSimresult();
    List<SimResult> getSimResultByUserName(String username);
    PageBean<SimResult> getPageResult(Integer page, Integer limit);
}
