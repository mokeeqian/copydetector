/*
 * Copyright (c) mokeeqian 2021.
 * Bugs and suggestions please contact me via mokeeqian@gmail.com
 */

package cn.edu.ahut.copydetector.service.impl;

import cn.edu.ahut.copydetector.dao.SimResultDao;
import cn.edu.ahut.copydetector.entity.SimResult;
import cn.edu.ahut.copydetector.service.SimResultService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimResultServiceImpl implements SimResultService {
    private SimResultDao simResultDao;

    public SimResultServiceImpl(SimResultDao simResultDao) {
        this.simResultDao = simResultDao;
    }

    @Override
    public List<SimResult> getAllSimresult() {
            return simResultDao.findAll();
    }
}
