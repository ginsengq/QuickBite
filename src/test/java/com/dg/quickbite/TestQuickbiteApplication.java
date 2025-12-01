package com.dg.quickbite;

import org.springframework.boot.SpringApplication;

public class TestQuickbiteApplication {

	public static void main(String[] args) {
		SpringApplication.from(QuickbiteApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
