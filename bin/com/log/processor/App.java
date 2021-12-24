package com.log.processor;

/**
 * Hello world!
 *
 */
@LogClass
public class App 
{
	private static final com.uhome.log.Log log = new com.uhome.log.LogAdapter();
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
