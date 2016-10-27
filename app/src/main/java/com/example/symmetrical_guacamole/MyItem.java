package com.example.symmetrical_guacamole;

import java.io.Serializable;

/**
 * Created by stefanmay on 03/02/2016.
 */
public class MyItem implements Serializable{

    public static final String TITLE = "title";
    public static final String DESC = "desc";
    public static final String IMAGE = "image";

    public String title = null;
    public String desc = null;
    public String image = null;
}
