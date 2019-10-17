package com.linkallcloud.core.trans;

/**
 * 事务相关的用户代码类的顶层接口.注意, 虽然继承了Runnable,但并非与线程相关!!事务也不是跑着单独的线程.
 * <p/> <b>Runnable不等于会有新线程!!<b/>
 *
 */
public interface Atom extends Runnable {}
