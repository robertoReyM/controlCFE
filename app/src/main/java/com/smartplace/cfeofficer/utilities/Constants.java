package com.smartplace.cfeofficer.utilities;

import android.os.Environment;

/**
 * Created by Roberto on 20/10/2014.
 */
public class Constants {

    public static final int REQUEST_DETAILED_REPORT =234;

    public static final String IMAGES_PATH = Environment.getExternalStorageDirectory().toString() + "/Android/data/com.smartplace.cfeofficer/Images";
    public static final String MAPS_STATIC_IMAGE_PREFIX = "http://maps.googleapis.com/maps/api/staticmap?size=500x300&zoom=14&markers=color:0x089e5f%7C";
    public static final int TYPE_COMMENT_PUBLIC = 1;
    public static final int TYPE_COMMENT_PRIVATE = 2;

    public static final int TYPE_FAIL = 1;
    public static final int TYPE_ISSUE = 2;

    //subtypes
    public static final int SUBTYPE_NO_LIGHT_HOME = 1;
    public static final int SUBTYPE_NO_LIGHT_NEIGHBORHOOD = 2;
    public static final int SUBTYPE_VOLTAGE_VARIATION_HOME = 3;
    public static final int SUBTYPE_VOLTAGE_VARIATION_NEIGHBORHOOD = 4;
    public static final int SUBTYPE_CFEMATIC_NOT_WORKING= 5;


    public static final int SUBTYPE_HIGH_CONSUMING= 1;
    public static final int SUBTYPE_BAD_ATTENTION= 2;
    public static final int SUBTYPE_EXTORSION = 3;

}
