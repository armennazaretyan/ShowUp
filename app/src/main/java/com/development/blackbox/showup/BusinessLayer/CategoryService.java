package com.development.blackbox.showup.BusinessLayer;

import com.development.blackbox.showup.Models.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    public static List<CategoryModel> GetStoreCategories() {

        List<CategoryModel> retVal = new ArrayList<CategoryModel>();

        /*CategoryModel cm = new CategoryModel();
        cm.ID = 1;
        cm.Name = "Կուլոն";
        cm.ImageResource = R.drawable.c_kulon;
        retVal.add(cm);

        cm = new CategoryModel();
        cm.ID = 2;
        cm.Name = "Ականջող";
        cm.ImageResource = R.drawable.c_akanjox;
        retVal.add(cm);

        cm = new CategoryModel();
        cm.ID = 3;
        cm.Name = "Մատանի";
        cm.ImageResource = R.drawable.c_matani;
        retVal.add(cm);

        cm = new CategoryModel();
        cm.ID = 4;
        cm.Name = "Բրասլետ";
        cm.ImageResource = R.drawable.c_braslet;
        retVal.add(cm);

        cm = new CategoryModel();
        cm.ID = 5;
        cm.Name = "Կոմպլեկտ";
        cm.ImageResource = R.drawable.c_komplekt;
        retVal.add(cm);*/

        return retVal;
    }
}
