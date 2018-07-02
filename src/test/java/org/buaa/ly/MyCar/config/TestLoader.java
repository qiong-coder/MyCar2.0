package org.buaa.ly.MyCar.config;

import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(name = "child", classes = {TestRootConfig.class, WebConfig.class, TestPayConfig.class})
@FixMethodOrder(MethodSorters.DEFAULT)
@Rollback
public class TestLoader {}
