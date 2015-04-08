package com.log.slf4j

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Slf4JClient {
	static main(args) {
		Logger logger = LoggerFactory.getLogger(Slf4JClient.class)
		println "*************"
		logger.info("Hello World1{}, 2{}, 3{}", "a", "b", "c")
	}
}