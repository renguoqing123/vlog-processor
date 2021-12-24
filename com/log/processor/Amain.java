package com.log.processor;

public class Amain{
	@SuppressWarnings({"static-access" })
	public static void main(String[] args) throws Exception {
		args = new String[args.length + 3];
		args[0] = "-processor";
		args[1] = "com.log.processor.LogProcessor";
		args[2] = "com.log.processor.App";
		com.sun.tools.javac.Main m = new com.sun.tools.javac.Main();
		m.main(args);
	}
}
