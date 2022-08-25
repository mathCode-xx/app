package app.controller;

import app.entity.R;
import app.service.ILogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/log")
public class LogController {

    @Resource
    ILogService logService;

    @GetMapping
    public R findAllLogs() {
        return logService.findAllLogs();
    }
}
