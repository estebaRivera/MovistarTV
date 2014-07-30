package com.smartboxtv.movistartv.programation.delegates;

import com.smartboxtv.movistartv.data.models.CategorieChannel;
import com.smartboxtv.movistartv.data.modelssm.CategorieChannelSM;

/**
 * Created by Esteban- on 20-04-14.
 */
public abstract class CategoryDelegateGetCategory {

    public abstract void  getCategory(CategorieChannel cc);

    public abstract  void getCategory(CategorieChannelSM cc);
}
