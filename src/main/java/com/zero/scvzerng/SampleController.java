package com.zero.scvzerng;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 *
 * Created by scvzerng on 2017/7/3.
 */
@RestController
@RequestMapping("/samples")
public class SampleController {
    @GetMapping
    public List<String> index(){
        return Arrays.asList("A","b");
    }
}
