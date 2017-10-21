package com.devseven.gympack.setlogger;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

/**
 * Created by Dickbutt on 09.06.2017.
 */

@Root
public class Programs
{
    @Attribute
    String tag;

    @ElementList
    ArrayList<Program> programsList = new ArrayList<Program>();

}