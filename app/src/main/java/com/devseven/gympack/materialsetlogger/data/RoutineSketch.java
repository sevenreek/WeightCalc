package com.devseven.gympack.materialsetlogger.data;


import org.simpleframework.xml.Attribute;

// A class that contains basic routine data for faster xml deserialization:
 // - name
 // - day amount

 public class RoutineSketch {
     @Attribute
     String name;
     @Attribute(required = false)
     String description;
     @Attribute
     int dayCount;
     public RoutineSketch()
     {

     }
     public String getName() {
         return name;
     }

     public void setName(String name) {
         this.name = name;
     }

     public String getDescription() {
         return description;
     }

     public void setDescription(String description) {
         this.description = description;
     }

     public int getDayCount() {
         return dayCount;
     }

     public void setDayCount(int dayCount) {
         this.dayCount = dayCount;
     }

     public RoutineSketch(String name, String description, int dayCount) {
         this.name = name;
         this.description = description;
         this.dayCount = dayCount;
     }
 }