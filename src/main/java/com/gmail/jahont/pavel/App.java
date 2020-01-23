package com.gmail.jahont.pavel;

import com.gmail.jahont.pavel.controller.HomeWorkController;

public class App
{
    public static void main( String[] args )
    {
        HomeWorkController homeWorkController = HomeWorkController.getInstance();
        homeWorkController.runTask();
    }
}
